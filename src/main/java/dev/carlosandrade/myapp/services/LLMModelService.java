package dev.carlosandrade.myapp.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import dev.carlosandrade.myapp.dto.LLMModelResponse;
import dev.carlosandrade.myapp.entity.LLMModelEntity;
import dev.carlosandrade.myapp.entity.MessageEntity;
import dev.carlosandrade.myapp.entity.ProjectEntity;
import dev.carlosandrade.myapp.repository.LLMModelRepository;
import dev.carlosandrade.myapp.repository.MessageRepository;
import dev.carlosandrade.myapp.repository.ProjectRepository;

@Service
public class LLMModelService
{

    @Value("${llm.endpoint}")
    private String llmEndpoint;

    @Autowired
    private LLMModelRepository llmModelRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    public void fetchAndStoreModels()
    {
        String url = llmEndpoint + "/api/tags";
        try
        {
            LLMModelResponse response = restTemplate.getForObject(url, LLMModelResponse.class);
            if (response != null && response.getModels() != null)
            {
                List<LLMModelEntity> v_listModels = llmModelRepository.findAll();
                llmModelRepository.saveAll(response.getModels()); // Salva ou atualiza todos os modelos
            }
        }
        catch (Exception e)
        {
            System.err.println("Erro ao buscar modelos do endpoint: " + e.getMessage());
        }
    }

    public List<MessageEntity> getProjectHistory(Long projectId)
    {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(new Supplier<RuntimeException>()
        {
            @Override
            public RuntimeException get()
            {
                return new RuntimeException("Projeto não encontrado");
            }
        });
        return messageRepository.findByProject(project);
    }

    public String processMessage(Long projectId, String userMessage)
    {
        // Busca o projeto
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(new Supplier<RuntimeException>()
        {
            @Override
            public RuntimeException get()
            {
                return new RuntimeException("Projeto não encontrado");
            }
        });

        // Salva a mensagem do usuário no banco
        MessageEntity userMessageEntity = new MessageEntity();
        userMessageEntity.setProject(project);
        userMessageEntity.setMessage(userMessage);
        userMessageEntity.setSender("user");
        userMessageEntity.setTimestamp(LocalDateTime.now());
        messageRepository.save(userMessageEntity);

        // Faz a requisição para o modelo de LLM
        String botResponse = this.sendToLlmApi(userMessage);

        // Salva a resposta da IA
        MessageEntity botMessageEntity = new MessageEntity();
        botMessageEntity.setProject(project);
        botMessageEntity.setMessage(botResponse);
        botMessageEntity.setSender("bot");
        botMessageEntity.setTimestamp(LocalDateTime.now());
        messageRepository.save(botMessageEntity);

        return botResponse;
    }

    // Método que envia a requisição HTTP para o LLM
    private String sendToLlmApi(String userMessage)
    {
        // Define a URL da API de LLM;
        String apiUrl = llmEndpoint + "/api/chat"; // Ajuste para a URL correta

        // Define o corpo da requisição
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "mistral-nemo");
        requestBody.put("stream", false);

        // Cria a lista de mensagens manualmente
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMessageMap = new HashMap<>();
        userMessageMap.put("role", "user");
        userMessageMap.put("content", userMessage);
        messages.add(userMessageMap);
        requestBody.put("messages", messages);

        // Configura os headers (caso seja necessário enviar content-type como JSON)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Cria a entidade com o corpo e os headers
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // Faz a requisição POST
        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);

        // Verifica se a resposta é bem-sucedida e retorna o conteúdo
        if (response.getStatusCode() == HttpStatus.OK)
        {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("message"))
            {
                Map<String, String> messageMap = (Map<String, String>) responseBody.get("message");
                return messageMap.get("content"); // Retorna o conteúdo da mensagem

            }
        }
        return "Erro ao processar a mensagem";
    }
}
