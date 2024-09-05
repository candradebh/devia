package routers

import (
	"my-beego-chat/controllers"
	"github.com/beego/beego/v2/server/web"
)

func init() {
	
	//chat
	web.Router("/", &controllers.ChatController{})
	web.Router("/send", &controllers.ChatController{}, "post:Send")

	//comandos
	web.Router("/execute", &controllers.CommandController{}, "post:Post")
	
}

