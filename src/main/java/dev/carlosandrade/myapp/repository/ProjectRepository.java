package dev.carlosandrade.myapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.carlosandrade.myapp.entity.ProjectEntity;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long>
{
    List<ProjectEntity> findByIsActiveTrue();

    List<ProjectEntity> findByIsActiveFalse();
}
