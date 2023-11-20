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
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.climei.exception.RestNotFoundException;
import br.com.fiap.climei.models.NivelUmidade;
import br.com.fiap.climei.repository.NivelUmidadeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(name = "Nivel de Umidade")
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/api/v1/nivelUmidade")
public class NivelUmidadeController {

    @Autowired
    NivelUmidadeRepository nivelUmidadeRepository;

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    /*
     * [GET] /api/v1/nivelUmidade
     */
    @GetMapping
    @Operation(summary = "Lista de Nivel de Umidade", description = "Lista de Nivel de Umidade")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de Nivel de Umidade exibida com sucesso"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public PagedModel<EntityModel<Object>> index(@ParameterObject @PageableDefault(size = 5) Pageable pageable) {
        var nivelUmidade = nivelUmidadeRepository.findAll(pageable);

        return assembler.toModel(nivelUmidade.map(NivelUmidade::toEntityModel)); //HAL
    }

    /*
     * [GET] /api/v1/nivelUmidade/{id}
     */
    @GetMapping("{id}")
    @Operation(summary = "Mostrar Nivel de Umidade", description = "Mostrar Nivel de Umidade")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nivel de Umidade exibido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nivel de Umidade não encontrado"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    
    public EntityModel<NivelUmidade> show(@PathVariable Long id) {
        log.info("Mostrando nivelUmidade {}", id);
        return getNivelUmidade(id).toEntityModel();
    }
    
    /*
     * [POST] /api/v1/nivelUmidade
     */
    @PostMapping
    @Operation(summary = "Criar Nivel de Umidade", description = "Criar Nivel de Umidade")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Nivel de Umidade criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public ResponseEntity<EntityModel<NivelUmidade>> create(@RequestBody @Valid NivelUmidade nivelUmidade, BindingResult result){
        log.info("Criando nivelUmidade {}", nivelUmidade);

        nivelUmidadeRepository.save(nivelUmidade);
        return ResponseEntity
            .created(nivelUmidade.toEntityModel().getRequiredLink("self").toUri())
            .body(nivelUmidade.toEntityModel());
    }

    /*
     * [DELETE] /api/v1/nivelUmidade/{id}
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Deletar Nivel de Umidade", description = "Deletar Nivel de Umidade")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Nivel de Umidade deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nivel de Umidade não encontrado"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        log.info("Deletando nivelUmidade {}", id);
        nivelUmidadeRepository.delete(getNivelUmidade(id));
        return ResponseEntity.noContent().build();
    }

    /*
     * [PUT] /api/v1/nivelUmidade/{id}
     */
    @PutMapping("{id}")
    @Operation(summary = "Atualizar Nivel de Umidade", description = "Atualizar Nivel de Umidade")
    public ResponseEntity<EntityModel<NivelUmidade>> update(@PathVariable Long id, @RequestBody @Valid NivelUmidade nivelUmidade) {
        log.info("Atualizando nivelUmidade {}", id);
        getNivelUmidade(id);
        nivelUmidade.setId(id);
        nivelUmidadeRepository.save(nivelUmidade);
        return ResponseEntity.ok(nivelUmidade.toEntityModel());
    }


    // Metodos
    private NivelUmidade getNivelUmidade(Long id) {
        return nivelUmidadeRepository.findById(id).orElseThrow(() -> new RestNotFoundException("Nivel de Umidade não encontrado"));
    }
}
