package br.com.fiap.climei.models;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;

import br.com.fiap.climei.controllers.LogAguaController;
import br.com.fiap.climei.controllers.UsuarioController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
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
@Table(name = "T_CLM_LOG_AGUA")
public class LogAgua {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "É obrigatório inserir um usuário") @ManyToOne
    private Usuario usuario;

    @NotNull(message = "É obrigatório inserir uma quantidade")
    @Min(value = 1, message = "A quantidade deve ser maior que 0")
    private Double quantidade;

    @NotNull @PastOrPresent
    private LocalDate dataAvaliacao;

    public EntityModel<LogAgua> toEntityModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(LogAguaController.class).show(id)).withSelfRel(),
            linkTo(methodOn(LogAguaController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(LogAguaController.class).index(null, Pageable.unpaged())).withRel("all"),
            linkTo(methodOn(UsuarioController.class).show(this.getUsuario().getId())).withRel("usuario")
        );
    }
}
