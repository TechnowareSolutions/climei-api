package br.com.fiap.climei.models;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;

import br.com.fiap.climei.controllers.LogUmidadeController;
import br.com.fiap.climei.controllers.NivelUmidadeController;
import br.com.fiap.climei.controllers.UsuarioController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "T_CLM_LOG_UMIDADE")
public class LogUmidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "É obrigatório inserir um usuário") @ManyToOne
    private Usuario usuario;

    @NotNull(message = "É obrigatório inserir um nivel de umidade") @ManyToOne
    private NivelUmidade nivelUmidade;

    @NotNull(message = "É obrigatório inserir uma umidade")
    private Double umidade;

    @NotNull @PastOrPresent
    private LocalDate dataAvaliacao;

    public EntityModel<LogUmidade> toEntityModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(LogUmidadeController.class).show(id)).withSelfRel(),
            linkTo(methodOn(LogUmidadeController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(LogUmidadeController.class).index(null, Pageable.unpaged())).withRel("all"),
            linkTo(methodOn(NivelUmidadeController.class).show(this.getNivelUmidade().getId())).withRel("nivelumidade"),
            linkTo(methodOn(UsuarioController.class).show(this.getUsuario().getId())).withRel("usuario")
        );
    }
}
