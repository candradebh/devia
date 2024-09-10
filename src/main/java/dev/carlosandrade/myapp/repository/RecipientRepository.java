package dev.carlosandrade.myapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.carlosandrade.myapp.entity.RecipientEntity;

public interface RecipientRepository extends JpaRepository<RecipientEntity, Long>
{
    List<RecipientEntity> findByIsActiveTrue();

}
