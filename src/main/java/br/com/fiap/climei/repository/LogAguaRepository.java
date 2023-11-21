package br.com.fiap.climei.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.climei.models.LogAgua;

public interface LogAguaRepository extends JpaRepository<LogAgua, Long> {
    Page<LogAgua> findByUsuarioId(Integer usuario, Pageable pageable);
}
