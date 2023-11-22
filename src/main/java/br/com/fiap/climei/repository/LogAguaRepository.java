package br.com.fiap.climei.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.fiap.climei.models.LogAgua;

public interface LogAguaRepository extends JpaRepository<LogAgua, Long> {
    Page<LogAgua> findByUsuarioId(Long usuario, Pageable pageable);

    @Query("SELECT l FROM LogAgua l WHERE l.usuario.id = ?1 ORDER BY l.id DESC")
    Page<LogAgua> findLastLogByUsuarioId(Long usuario, Pageable pageable);
}
