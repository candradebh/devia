package dev.carlosandrade.myapp.controller;

import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.carlosandrade.myapp.dto.MessageRequestDTO;
import dev.carlosandrade.myapp.entity.MessageEntity;
import dev.carlosandrade.myapp.services.LLMModelService;

@RestController
@RequestMapping("/chat")
public class LlmController
{

    private static final Logger logger = Logger.getLogger(LlmController.class.getName());

    @Autowired
    private LLMModelService llmService;

    @PostMapping("/message")
    public ResponseEntity<String> processMessage(@RequestBody MessageRequestDTO messageRequest)
    {
        Long projectId = messageRequest.getProjectId();
        String message = messageRequest.getMessage();

        String response = llmService.processMessage(projectId, message);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/{projectId}")
    public ResponseEntity<List<MessageEntity>> getProjectHistory(@PathVariable Long projectId)
    {
        List<MessageEntity> history = llmService.getProjectHistory(projectId);
        return ResponseEntity.ok(history);
    }

}
