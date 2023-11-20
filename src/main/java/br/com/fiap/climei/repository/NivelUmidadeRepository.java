package br.com.fiap.climei.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.climei.models.NivelUmidade;

public interface NivelUmidadeRepository extends JpaRepository<NivelUmidade, Long> {

}
