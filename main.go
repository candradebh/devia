package main

import (
	"bytes"
	"database/sql"
	"log"
	"net/http"
	"os/exec"
	"regexp"
	"strings"

	"github.com/gin-gonic/gin"
	_ "github.com/lib/pq"
)

var db *sql.DB

func initDB() {
	var err error
	// Configure sua string de conexão com o Postgres
	db, err = sql.Open("postgres", "user=postgres password=sa dbname=chat_db host=localhost port=5434 sslmode=disable")
	if err != nil {
		log.Fatalf("Erro ao conectar ao banco de dados: %v", err)
	}

	err = db.Ping()
	if err != nil {
		log.Fatalf("Erro ao pingar o banco de dados: %v", err)
	}

	// Crie a tabela de mensagens, se não existir
	_, err = db.Exec(`CREATE TABLE IF NOT EXISTS messages (
		id SERIAL PRIMARY KEY,
		user_message TEXT,
		bot_response TEXT,
		timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	)`)
	if err != nil {
		log.Fatalf("Erro ao criar a tabela de mensagens: %v", err)
	}
}

func main() {
	// Inicializa o banco de dados
	initDB()
	defer db.Close() // Fechar a conexão com o banco de dados ao sair da aplicação

	router := gin.Default()

	router.LoadHTMLGlob("templates/*.html")
	router.Static("/static", "./static")

	// Exibir o formulário e o histórico de mensagens
	router.GET("/", func(c *gin.Context) {
		var messages []map[string]interface{}
		rows, err := db.Query("SELECT user_message, bot_response, timestamp FROM messages ORDER BY timestamp DESC")
		if err != nil {
			log.Printf("Erro ao buscar mensagens no banco de dados: %v", err)
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Erro ao buscar mensagens"})
			return
		}
		defer rows.Close()

		for rows.Next() {
			var userMessage, botResponse, timestamp string
			if err := rows.Scan(&userMessage, &botResponse, &timestamp); err != nil {
				log.Printf("Erro ao escanear mensagens: %v", err)
				c.JSON(http.StatusInternalServerError, gin.H{"error": "Erro ao processar mensagens"})
				return
			}
			messages = append(messages, gin.H{
				"user_message": userMessage,
				"bot_response": botResponse,
				"timestamp":    timestamp,
			})
		}

		c.HTML(http.StatusOK, "chat.html", gin.H{
			"messages": messages,
		})
	})

	// Processar o texto enviado e gravar no banco
	router.POST("/send", func(c *gin.Context) {
		userMessage := c.PostForm("message")

		// Verifica se a mensagem foi recebida corretamente
		log.Println("Mensagem recebida:", userMessage)

		// Executa o comando 'ollama run mistral-nemo' com a mensagem do usuário como argumento
		cmd := exec.Command("ollama", "run", "mistral-nemo")

		// Envia a mensagem do usuário para o stdin do comando
		cmd.Stdin = bytes.NewBufferString(userMessage)

		// Captura a saída do comando
		out, err := cmd.CombinedOutput()
		if err != nil {
			log.Printf("Erro ao executar o comando Ollama: %v", err)
			c.JSON(http.StatusInternalServerError, gin.H{
				"error":  "Erro ao processar o comando",
				"output": string(out),
			})
			return
		}

		// Remove caracteres de controle ANSI e spinners da saída
		botResponse := cleanSpinnerAndAnsi(string(out))
		log.Println("Saída do Ollama limpa:", botResponse)

		// Insere no banco de dados
		_, err = db.Exec("INSERT INTO messages (user_message, bot_response) VALUES ($1, $2)", userMessage, botResponse)
		if err != nil {
			log.Printf("Erro ao inserir mensagem no banco de dados: %v", err)
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Erro ao salvar mensagem"})
			return
		}

		// Redireciona de volta para a página principal
		c.Redirect(http.StatusFound, "/")
	})

	// Inicia o servidor na porta 8080
	router.Run(":8080")
}

// Função para remover caracteres de controle ANSI e spinners
func cleanSpinnerAndAnsi(input string) string {
	// Remove sequências ANSI
	reAnsi := regexp.MustCompile(`\x1b\[[0-9;]*[a-zA-Z]`)
	cleaned := reAnsi.ReplaceAllString(input, "")

	// Remove spinners (⠋, ⠙, ⠹, etc.)
	spinnerChars := "⠋⠙⠹⠸⠼⠴⠦⠧⠇⠏"
	for _, char := range spinnerChars {
		cleaned = strings.ReplaceAll(cleaned, string(char), "")
	}

	// Retorna a saída limpa
	return strings.TrimSpace(cleaned)
}
