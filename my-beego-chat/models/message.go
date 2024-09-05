package models

import (
	"time"
	"github.com/beego/beego/v2/client/orm"
)

type Message struct {
	Id          int
	UserMessage string    `orm:"type(text)"`
	BotResponse string    `orm:"type(text)"`
	Timestamp   time.Time `orm:"auto_now_add;type(datetime)"`
}

func init() {
	orm.RegisterModel(new(Message))
}
