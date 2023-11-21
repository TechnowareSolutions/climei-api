package br.com.fiap.climei.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.climei.models.LogBatimentoOxigenacao;

public interface LogBatimentoOxigenacaoRepository extends JpaRepository<LogBatimentoOxigenacao, Long> {
    Page<LogBatimentoOxigenacao> findByUsuarioId(Integer usuario, Pageable pageable);
}
