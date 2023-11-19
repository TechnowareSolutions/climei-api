package br.com.fiap.climei.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.climei.models.DadosUsuario;

public interface DadosUsuarioRepository extends JpaRepository<DadosUsuario, Long> {
    Page<DadosUsuario> findByUsuarioId(Integer usuario, Pageable pageable);
}
