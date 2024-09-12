package dev.carlosandrade.myapp.repository;

import dev.carlosandrade.myapp.enums.TaskStatus;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.carlosandrade.myapp.entity.TaskEntity;
import dev.carlosandrade.myapp.entity.ProjectEntity;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByStatus(TaskStatus status);

    List<TaskEntity> findByProject(ProjectEntity project);

    // Buscar tasks que não foram deletadas
    List<TaskEntity> findByDeletedFalse();

    // Buscar tasks por status e que não foram deletadas
    List<TaskEntity> findByStatusAndDeletedFalse(TaskStatus status);
}

