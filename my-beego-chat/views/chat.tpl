<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Beego Chat</title>
</head>
<body>
    <h1>Chat Simples com Beego</h1>

    <div id="chat-box">
        {{range .Messages}}
        <div class="message">
            <strong>Você:</strong> {{.UserMessage}} <br>
            <strong>Bot:</strong> {{.BotResponse}} <br>
            <small>{{.Timestamp}}</small>
        </div>
        {{end}}
    </div>

    <form action="/send" method="POST">
        <textarea name="message" placeholder="Digite sua mensagem..."></textarea>
        <br>
        <button type="submit">Enviar</button>
    </form>
</body>
</html>
