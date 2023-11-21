package br.com.fiap.climei.models;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import br.com.fiap.climei.controllers.DadosUsuarioController;
import br.com.fiap.climei.controllers.UsuarioController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "T_CLM_DADOS_USUARIO")
public class DadosUsuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "É obrigatório inserir um usuário") @ManyToOne
    @JoinColumn(unique = true)
    private Usuario usuario;

    @NotNull(message = "É obrigatório inserir uma altura")
    private Double altura;

    @NotNull(message = "É obrigatório inserir um peso")
    private Double peso;

    public EntityModel<DadosUsuario> toEntityModel() {
        return EntityModel.of(
            this,
            linkTo(methodOn(DadosUsuarioController.class).show(id)).withSelfRel(),
            linkTo(methodOn(DadosUsuarioController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(DadosUsuarioController.class).index(null, Pageable.unpaged())).withRel("all"),
            linkTo(methodOn(UsuarioController.class).show(this.getUsuario().getId())).withRel("usuario")
        );
    }
}
