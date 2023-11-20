package br.com.fiap.climei.models;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import br.com.fiap.climei.controllers.NivelUmidadeController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "T_CLM_NVL_UMIDADE")
public class NivelUmidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "É obrigatório inserir uma faixa")
    private String faixa;

    @NotNull @PastOrPresent
    private LocalDate dataAvaliacao;

    public EntityModel<NivelUmidade> toEntityModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(NivelUmidadeController.class).show(id)).withSelfRel(),
            linkTo(methodOn(NivelUmidadeController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(NivelUmidadeController.class).index(Pageable.unpaged())).withRel("all")
        );
    }
}