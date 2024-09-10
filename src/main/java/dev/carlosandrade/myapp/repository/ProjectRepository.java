package dev.carlosandrade.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.carlosandrade.myapp.entity.ProjectEntity;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long>
{

}
