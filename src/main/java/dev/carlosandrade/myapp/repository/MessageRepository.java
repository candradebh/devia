package dev.carlosandrade.myapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.carlosandrade.myapp.entity.MessageEntity;
import dev.carlosandrade.myapp.entity.ProjectEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long>
{
    List<MessageEntity> findByProject(ProjectEntity project);

    List<MessageEntity> findByProjectIsNull();
}
