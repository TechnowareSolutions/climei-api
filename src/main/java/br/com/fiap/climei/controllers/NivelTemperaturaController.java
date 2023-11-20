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
import br.com.fiap.climei.models.NivelTemperatura;
import br.com.fiap.climei.repository.NivelTemperaturaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(name = "Nivel de Temperatura")
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/api/v1/nivelTemperatura")
public class NivelTemperaturaController {

    @Autowired
    NivelTemperaturaRepository nivelTemperaturaRepository;

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    /*
     * [GET] /api/v1/nivelTemperatura
     */
    @GetMapping
    @Operation(summary = "Lista de Nivel de Temperatura", description = "Lista de Nivel de Temperatura")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de Nivel de Temperatura exibida com sucesso"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public PagedModel<EntityModel<Object>> index(@ParameterObject @PageableDefault(size = 5) Pageable pageable) {
        var nivelTemperatura = nivelTemperaturaRepository.findAll(pageable);

        return assembler.toModel(nivelTemperatura.map(NivelTemperatura::toEntityModel)); //HAL
    }

    /*
     * [GET] /api/v1/nivelTemperatura/{id}
     */
    @GetMapping("{id}")
    @Operation(summary = "Mostrar Nivel de Temperatura", description = "Mostrar Nivel de Temperatura")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nivel de Temperatura exibido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nivel de Temperatura não encontrado"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public EntityModel<NivelTemperatura> show(@PathVariable Long id) {
        log.info("Mostrando nivelTemperatura {}", id);
        return getNivelTemperatura(id).toEntityModel();
    }
    
    /*
     * [POST] /api/v1/nivelTemperatura
     */
    @PostMapping
    @Operation(summary = "Criar Nivel de Temperatura", description = "Criar Nivel de Temperatura")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Nivel de Temperatura criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public ResponseEntity<EntityModel<NivelTemperatura>> create(@RequestBody @Valid NivelTemperatura nivelTemperatura, BindingResult result){
        log.info("Criando nivelTemperatura {}", nivelTemperatura);

        nivelTemperaturaRepository.save(nivelTemperatura);
        return ResponseEntity
            .created(nivelTemperatura.toEntityModel().getRequiredLink("self").toUri())
            .body(nivelTemperatura.toEntityModel());
    }

    /*
     * [DELETE] /api/v1/nivelTemperatura/{id}
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Deletar Nivel de Temperatura", description = "Deletar Nivel de Temperatura")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Nivel de Temperatura deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nivel de Temperatura não encontrado"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        log.info("Deletando nivelTemperatura {}", id);
        nivelTemperaturaRepository.delete(getNivelTemperatura(id));
        return ResponseEntity.noContent().build();
    }

    /*
     * [PUT] /api/v1/nivelTemperatura/{id}
     */
    @PutMapping("{id}")
    @Operation(summary = "Atualizar Nivel de Temperatura", description = "Atualizar Nivel de Temperatura")
    public ResponseEntity<EntityModel<NivelTemperatura>> update(@PathVariable Long id, @RequestBody @Valid NivelTemperatura nivelTemperatura) {
        log.info("Atualizando nivelTemperatura {}", id);
        getNivelTemperatura(id);
        nivelTemperatura.setId(id);
        nivelTemperaturaRepository.save(nivelTemperatura);
        return ResponseEntity.ok(nivelTemperatura.toEntityModel());
    }


    // Metodos
    private NivelTemperatura getNivelTemperatura(Long id) {
        return nivelTemperaturaRepository.findById(id).orElseThrow(() -> new RestNotFoundException("Nivel de Temperatura não encontrado"));
    }
}
