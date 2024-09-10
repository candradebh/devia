package dev.carlosandrade.myapp.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.carlosandrade.myapp.utils.GitUtils;

@RestController
@RequestMapping("/chat")
public class LlmController
{

    private static final Logger logger = Logger.getLogger(LlmController.class.getName());

    private Process ollamaProcess;

    private BufferedWriter writer;

    private BufferedReader reader;

    @PostMapping("/start")
    public ResponseEntity<String> executeCommand(@RequestBody String command)
    {
        try
        {
            GitUtils.createDirectory("C:/projetos/teste/projeto1");
            GitUtils.initGitRepository("C:/projetos/teste/projeto1");

            return ResponseEntity.ok("Diretorio Criado com sucesso!");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao iniciar Ollama: " + e.getMessage());
        }
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendCommand(@RequestBody String userInput)
    {
        try
        {
            // Iniciar o processo Ollama sem usar bash
            ProcessBuilder processBuilder = new ProcessBuilder("ollama", "run", "mistral-nemo");
            Process ollamaProcess = processBuilder.start();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ollamaProcess.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(ollamaProcess.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(ollamaProcess.getErrorStream()));

            // Enviar comando
            writer.write("quem é voce ?");
            writer.newLine();
            writer.flush();

            // Ler a resposta
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                output.append(line).append("\n");
            }

            // Ler possíveis erros
            StringBuilder errors = new StringBuilder();
            while ((line = errorReader.readLine()) != null)
            {
                errors.append(line).append("\n");
            }

            ollamaProcess.waitFor(); // Espera o processo terminar

            if (errors.length() > 0)
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + errors.toString());
            }

            return ResponseEntity.ok("Resposta do Ollama: " + output.toString());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao enviar comando para Ollama: " + e.getMessage());
        }

    }

}
