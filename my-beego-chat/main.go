package main

import (
	_ "my-beego-chat/routers"
	"os/exec"
	"github.com/beego/beego/v2/server/web"
	"github.com/beego/beego/v2/client/orm"
	_ "github.com/lib/pq"
	"log"
)

func init() {
	orm.RegisterDriver("postgres", orm.DRPostgres)
	orm.RegisterDataBase("default", "postgres", "user=postgres password=sa dbname=chat_db host=localhost port=5434 sslmode=disable")
	orm.RunSyncdb("default", false, true)
}

func main() {
	log.Println("Iniciando o servidor Beego...")

	//executando comando
	cmd := exec.Command("cmd", "/C", "go version")
    out, err := cmd.CombinedOutput()
    if err != nil {
        log.Println("Erro ao executar go version:", err)
    }
    log.Println("Saída do comando go version:", string(out))

    web.Run()
}
