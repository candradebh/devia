package controllers

import (
    "os/exec"
    "github.com/beego/beego/v2/server/web"
    "log"
    "encoding/json"
)

// CommandController para lidar com requisições de execução de comandos
type CommandController struct {
    web.Controller
}

// Estrutura para capturar o JSON
type CommandInput struct {
    Command string `json:"command"`
}

// Post recebe o comando via POST e executa no terminal do Windows
func (c *CommandController) Post() {
    log.Println("Entrando na função Post")

    // Lê o corpo da requisição
    var input CommandInput
    if err := json.Unmarshal(c.Ctx.Input.RequestBody, &input); err != nil {
        log.Println("Erro ao decodificar JSON:", err)
        c.Data["json"] = map[string]string{"error": "Comando inválido"}
        c.ServeJSON()
        return
    }

    log.Println("Comando recebido:", input.Command)

    // Executa o comando no terminal do Windows
    cmd := exec.Command("cmd", "/C", input.Command)
    log.Println("Executando comando:", input.Command)

    // Captura a saída e possíveis erros
    out, err := cmd.CombinedOutput()
    if err != nil {
        log.Println("Erro ao executar o comando:", err)
        log.Println("Saída do comando:", string(out))
        c.Data["json"] = map[string]string{
            "error":   err.Error(),
            "output":  string(out),
        }
        c.ServeJSON()
        return
    }

    log.Println("Saída do comando:", string(out))
    c.Data["json"] = map[string]string{"output": string(out)}
    c.ServeJSON()
}
