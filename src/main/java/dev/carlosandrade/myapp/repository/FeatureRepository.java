package dev.carlosandrade.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.carlosandrade.myapp.entity.FeatureEntity;

public interface FeatureRepository extends JpaRepository<FeatureEntity, Long>
{

}
