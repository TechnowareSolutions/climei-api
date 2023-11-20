package br.com.fiap.climei.controllers;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.climei.exception.RestNotFoundException;
import br.com.fiap.climei.models.DadosUsuario;
import br.com.fiap.climei.repository.DadosUsuarioRepository;
import br.com.fiap.climei.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(name = "Dados de Usuario")
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/api/v1/dadosUsuario")
public class DadosUsuarioController {

    @Autowired
    DadosUsuarioRepository dadosUsuarioRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    /*
     * [GET] /api/v1/dadosUsuario
     */
    @GetMapping
    @Operation(summary = "Lista os dados de todos os usuarios", description = "Lista os dados de todos os usuarios")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de dados de usuarios exibida com sucesso"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public PagedModel<EntityModel<Object>> index(@RequestParam(required = false) Integer usuario, @ParameterObject @PageableDefault(size = 5) Pageable pageable) {
        var dadosUsuario = (usuario == null) ? 
            dadosUsuarioRepository.findAll(pageable): 
            dadosUsuarioRepository.findByUsuarioId(usuario, pageable);

        return assembler.toModel(dadosUsuario.map(DadosUsuario::toEntityModel)); //HAL
    }

    /*
     * [GET] /api/v1/dadosUsuario/{id}
     */
    @GetMapping("{id}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dados de Usuario exibido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Dados de Usuario não encontrado"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    @Operation(summary = "Mostra os dados de um usuario", description = "Mostra os dados de um usuario")
    public EntityModel<DadosUsuario> show(@PathVariable Long id) {
        log.info("Mostrando dadosUsuario {}", id);
        return getDadosUsuario(id).toEntityModel();
    }
    
    /*
     * [POST] /api/v1/dadosUsuario
     */
    @PostMapping
    @Operation(summary = "Cria um novo usuario", description = "Cria um novo usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Dados de Usuario criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public ResponseEntity<EntityModel<DadosUsuario>> create(@RequestBody @Valid DadosUsuario dadosUsuario, BindingResult result){
        log.info("Criando dadosUsuario {}", dadosUsuario);

        dadosUsuarioRepository.save(dadosUsuario);
        dadosUsuario.setUsuario(usuarioRepository.findById(dadosUsuario.getUsuario().getId()).get());
        return ResponseEntity
            .created(dadosUsuario.toEntityModel().getRequiredLink("self").toUri())
            .body(dadosUsuario.toEntityModel());
    }

    /*
     * [DELETE] /api/v1/dadosUsuario/{id}
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Deleta os dados de um usuario", description = "Deleta os dados de um usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Dados de Usuario deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Dados de Usuario não encontrado"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        log.info("Deletando dadosUsuario {}", id);
        dadosUsuarioRepository.delete(getDadosUsuario(id));
        return ResponseEntity.noContent().build();
    }

    /*
     * [PUT] /api/v1/dadosUsuario/{id}
     */
    @PutMapping("{id}")
    @Operation(summary = "Atualiza os dados de um usuario", description = "Atualiza os dados de um usuario")
    public ResponseEntity<EntityModel<DadosUsuario>> update(@PathVariable Long id, @RequestBody @Valid DadosUsuario dadosUsuario) {
        log.info("Atualizando dadosUsuario {}", id);
        getDadosUsuario(id);
        dadosUsuario.setId(id);
        dadosUsuarioRepository.save(dadosUsuario);
        return ResponseEntity.ok(dadosUsuario.toEntityModel());
    }


    // Metodos
    private DadosUsuario getDadosUsuario(Long id) {
        return dadosUsuarioRepository.findById(id).orElseThrow(() -> new RestNotFoundException("dados de usuario não encontrado"));
    }
}
