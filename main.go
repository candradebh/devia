package main

import (
    "database/sql"
    "log"
    "net/http"
    "github.com/gin-gonic/gin"
    _ "github.com/lib/pq"
)

var db *sql.DB

func initDB() {
    var err error
    // Configure sua string de conexão com o Postgres
    db, err = sql.Open("postgres", "user=postgres password=sa dbname=chat_db host=localhost port=5434 sslmode=disable")
    if err != nil {
        log.Fatal(err)
    }

    err = db.Ping()
    if err != nil {
        log.Fatal(err)
    }

    // Crie as tabelas de mensagens
    _, err = db.Exec(`CREATE TABLE IF NOT EXISTS messages (
        id SERIAL PRIMARY KEY,
        user_message TEXT,
        bot_response TEXT,
        timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    )`)
    if err != nil {
        log.Fatal(err)
    }
}

func main() {
    initDB()
    router := gin.Default()

    router.LoadHTMLGlob("templates/*.html")  // Carrega os arquivos de template HTML
    router.Static("/static", "./static")     // Carrega arquivos estáticos

    // Exibir o formulário e o histórico
    router.GET("/", func(c *gin.Context) {
        var messages []map[string]interface{}
        rows, err := db.Query("SELECT user_message, bot_response, timestamp FROM messages ORDER BY timestamp DESC")
        if err != nil {
            log.Fatal(err)
        }
        defer rows.Close()

        for rows.Next() {
            var userMessage, botResponse string
            var timestamp string
            if err := rows.Scan(&userMessage, &botResponse, &timestamp); err != nil {
                log.Fatal(err)
            }
            messages = append(messages, gin.H{"user_message": userMessage, "bot_response": botResponse, "timestamp": timestamp})
        }

        c.HTML(http.StatusOK, "chat.html", gin.H{
            "messages": messages,
        })
    })

    // Processar o texto enviado e gravar no banco
    router.POST("/send", func(c *gin.Context) {
        userMessage := c.PostForm("message")
        botResponse := processText(userMessage)

        // Insere no banco de dados
        _, err := db.Exec("INSERT INTO messages (user_message, bot_response) VALUES ($1, $2)", userMessage, botResponse)
        if err != nil {
            log.Fatal(err)
        }

        c.Redirect(http.StatusFound, "/")
    })

    router.Run(":8080")
}

// Função simulada que processa o texto do usuário e retorna uma resposta
func processText(input string) string {
    return "Resposta: " + input
}
