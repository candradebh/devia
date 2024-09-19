package dev.carlosandrade.myapp.controller;

import java.util.List;
import java.util.logging.Logger;

import dev.carlosandrade.myapp.entity.ProjectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.carlosandrade.myapp.dto.MessageRequestDTO;
import dev.carlosandrade.myapp.entity.LLMModelEntity;
import dev.carlosandrade.myapp.entity.MessageEntity;
import dev.carlosandrade.myapp.repository.LLMModelRepository;
import dev.carlosandrade.myapp.services.LLMModelService;

@RestController
@RequestMapping("/chat")
public class LlmController
{

    private static final Logger logger = Logger.getLogger(LlmController.class.getName());

    @Autowired
    private LLMModelRepository llmModelRepository;

    @Autowired
    private LLMModelService llmService;

    @PostMapping("/message")
    public ResponseEntity<String> processMessage(@RequestBody MessageRequestDTO messageRequest)
    {
        String response = llmService.processMessage(messageRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/history")
    public ResponseEntity<List<MessageEntity>> getProjectHistory(@RequestBody MessageRequestDTO messageRequest)
    {
        List<MessageEntity> history = llmService.getHistoryChat(messageRequest);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/models")
    public ResponseEntity<List<LLMModelEntity>> getModels()
    {
        List<LLMModelEntity> models = llmModelRepository.findAll();
        return ResponseEntity.ok(models);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LLMModelEntity> getModelById(@PathVariable Long id)
    {
        return llmModelRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LLMModelEntity> create(@RequestBody LLMModelEntity entity)
    {
        return ResponseEntity.ok(llmModelRepository.save(entity));
    }

}
