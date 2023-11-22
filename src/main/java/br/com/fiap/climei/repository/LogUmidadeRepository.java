package br.com.fiap.climei.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.fiap.climei.models.LogUmidade;

public interface LogUmidadeRepository extends JpaRepository<LogUmidade, Long> {
    Page<LogUmidade> findByUsuarioId(Long usuario, Pageable pageable);

    @Query("SELECT l FROM LogUmidade l WHERE l.usuario.id = ?1 ORDER BY l.id DESC")
    Page<LogUmidade> findLastLogByUsuarioId(Long usuario, Pageable pageable);
}
