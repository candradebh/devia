package dev.carlosandrade.myapp.services;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import dev.carlosandrade.myapp.dto.LLMModelResponse;
import dev.carlosandrade.myapp.repository.LLMModelRepository;

@Service
public class LLMModelService
{

    @Value("${llm.endpoint}")
    private String llmEndpoint;

    private final LLMModelRepository llmModelRepository;

    private final RestTemplate restTemplate;

    public LLMModelService(LLMModelRepository llmModelRepository, RestTemplate restTemplate)
    {
        this.llmModelRepository = llmModelRepository;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void fetchAndStoreModels()
    {
        String url = llmEndpoint + "/api/tags";
        try
        {
            LLMModelResponse response = restTemplate.getForObject(url, LLMModelResponse.class);
            if (response != null && response.getModels() != null)
            {
                llmModelRepository.saveAll(response.getModels()); // Salva ou atualiza todos os modelos
            }
        }
        catch (Exception e)
        {
            System.err.println("Erro ao buscar modelos do endpoint: " + e.getMessage());
        }
    }
}
