package com.morning.torneo.infrastructure.persistence.repository;

import com.morning.torneo.infrastructure.persistence.entity.CombateEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CombateJpaRepository extends JpaRepository<CombateEntity, Long> {

    List<CombateEntity> findAllByOrderByFechaDesc();
}
