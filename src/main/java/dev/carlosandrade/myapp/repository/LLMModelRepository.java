package dev.carlosandrade.myapp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.carlosandrade.myapp.entity.LLMModelEntity;

public interface LLMModelRepository extends JpaRepository<LLMModelEntity, Long>
{

    Optional<LLMModelEntity> findByName(String name);
}
