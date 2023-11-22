package br.com.fiap.climei.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.fiap.climei.models.LogTemperatura;

public interface LogTemperaturaRepository extends JpaRepository<LogTemperatura, Long> {
    Page<LogTemperatura> findByUsuarioId(Long usuario, Pageable pageable);

    @Query("SELECT l FROM LogTemperatura l WHERE l.usuario.id = ?1 ORDER BY l.id DESC")
    Page<LogTemperatura> findLastLogByUsuarioId(Long usuario, Pageable pageable);
}