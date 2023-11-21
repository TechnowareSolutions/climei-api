package br.com.fiap.climei.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.climei.models.LogTemperatura;

public interface LogTemperaturaRepository extends JpaRepository<LogTemperatura, Long> {
    Page<LogTemperatura> findByUsuarioId(Long usuario, Pageable pageable);
}