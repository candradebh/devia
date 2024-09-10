package dev.carlosandrade.myapp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.carlosandrade.myapp.entity.ParametersEntity;

public interface ParameterRepository extends JpaRepository<ParametersEntity, Long>
{
    Optional<ParametersEntity> findByName(String name);
}
