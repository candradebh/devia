package dev.carlosandrade.myapp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.carlosandrade.myapp.entity.ScheduledTaskEntity;

public interface ScheduledTaskRepository extends JpaRepository<ScheduledTaskEntity, Long>
{
    Optional<ScheduledTaskEntity> findByServiceName(String serviceName);

    List<ScheduledTaskEntity> findByIsActiveTrue();
}
