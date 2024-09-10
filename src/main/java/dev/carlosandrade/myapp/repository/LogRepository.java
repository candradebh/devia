package dev.carlosandrade.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.carlosandrade.myapp.entity.LogEntity;

public interface LogRepository extends JpaRepository<LogEntity, Long>
{

}
