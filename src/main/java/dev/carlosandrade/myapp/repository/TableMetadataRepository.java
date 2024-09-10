package dev.carlosandrade.myapp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.carlosandrade.myapp.entity.TableMetadataEntity;

public interface TableMetadataRepository extends JpaRepository<TableMetadataEntity, Long>
{

    Optional<TableMetadataEntity> findByTableName(String tableName);
}
