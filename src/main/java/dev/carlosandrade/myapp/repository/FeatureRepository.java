package dev.carlosandrade.myapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.carlosandrade.myapp.entity.FeatureEntity;

public interface FeatureRepository extends JpaRepository<FeatureEntity, Long>
{

    List<FeatureEntity> findByProjectId(Long projectId);

}
