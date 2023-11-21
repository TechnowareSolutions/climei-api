package br.com.fiap.climei.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.climei.models.LogUmidade;

public interface LogUmidadeRepository extends JpaRepository<LogUmidade, Long> {
    Page<LogUmidade> findByUsuarioId(Long usuario, Pageable pageable);
}
