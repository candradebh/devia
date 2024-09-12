package dev.carlosandrade.myapp.controller;

import dev.carlosandrade.myapp.entity.ProjectEntity;
import dev.carlosandrade.myapp.repository.ProjectRepository;
import dev.carlosandrade.myapp.repository.TaskRepository;
import dev.carlosandrade.myapp.entity.TaskEntity;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping
    public List<TaskEntity> getAllTasks() {
        return taskRepository.findByDeletedFalse();
    }

    @PostMapping
    public TaskEntity createTask(@RequestBody TaskEntity task) {
        // Certifique-se de que o projeto está associado
        ProjectEntity project = projectRepository.findById(task.getProject().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + task.getProject().getId()));
        task.setProject(project);

        return taskRepository.save(task);
    }


    @PutMapping("/{id}")
    public ResponseEntity<TaskEntity> updateTask(@PathVariable Long id, @RequestBody TaskEntity taskDetails) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());

        // Atualizar o projeto associado, se necessário
        if(taskDetails.getProject()!=null && taskDetails.getProject().getId()!=null){
            ProjectEntity project = projectRepository.findById(taskDetails.getProject().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + taskDetails.getProject().getId()));
            task.setProject(project);
        }

        final TaskEntity updatedTask = taskRepository.save(task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDeleteTask(@PathVariable Long id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));

        // Marcar a task como deletada (soft delete)
        task.setDeleted(true);
        taskRepository.save(task);

        return ResponseEntity.ok().build();
    }
}

