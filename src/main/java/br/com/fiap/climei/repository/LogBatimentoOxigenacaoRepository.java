package br.com.fiap.climei.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.fiap.climei.models.LogBatimentoOxigenacao;

public interface LogBatimentoOxigenacaoRepository extends JpaRepository<LogBatimentoOxigenacao, Long> {
    Page<LogBatimentoOxigenacao> findByUsuarioId(Long usuario, Pageable pageable);

    // GET LAST LOG (using dataAvaliacao) FROM A USER ID USE @Query JPQL -- SELECT ONLY ONE
    @Query("SELECT l FROM LogBatimentoOxigenacao l WHERE l.usuario.id = ?1 ORDER BY l.id DESC")
    Page<LogBatimentoOxigenacao> findLastLogByUsuarioId(Long usuario, Pageable pageable);
    
}
