package dev.carlosandrade.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.carlosandrade.myapp.entity.TaskStatusEntity;

public interface TaskStatusRepository extends JpaRepository<TaskStatusEntity, Long>
{
}
