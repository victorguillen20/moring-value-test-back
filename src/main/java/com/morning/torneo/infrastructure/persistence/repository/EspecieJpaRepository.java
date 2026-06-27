package com.morning.torneo.infrastructure.persistence.repository;

import com.morning.torneo.infrastructure.persistence.entity.EspecieEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecieJpaRepository extends JpaRepository<EspecieEntity, Long> {

    Optional<EspecieEntity> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
