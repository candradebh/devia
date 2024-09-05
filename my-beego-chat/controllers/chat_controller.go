package controllers

import (
	"my-beego-chat/models"
	"github.com/beego/beego/v2/server/web"
	"github.com/beego/beego/v2/client/orm"
)

type ChatController struct {
	web.Controller
}

func (c *ChatController) Get() {
	o := orm.NewOrm()
	var messages []models.Message
	_, err := o.QueryTable("message").OrderBy("-Timestamp").All(&messages)
	if err != nil {
		c.Ctx.WriteString("Erro ao carregar mensagens")
		return
	}
	c.Data["Messages"] = messages
	c.TplName = "chat.tpl"
}

func (c *ChatController) Send() {
	userMessage := c.GetString("message")
	botResponse := processText(userMessage)

	message := models.Message{
		UserMessage: userMessage,
		BotResponse: botResponse,
	}

	o := orm.NewOrm()
	_, err := o.Insert(&message)
	if err != nil {
		c.Ctx.WriteString("Erro ao salvar mensagem")
		return
	}

	c.Redirect("/", 302)
}

func processText(input string) string {
	return "Resposta: " + input
}
