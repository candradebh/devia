package dev.carlosandrade.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.carlosandrade.myapp.entity.LLMModelEntity;

public interface LLMModelRepository extends JpaRepository<LLMModelEntity, Long> {
}

