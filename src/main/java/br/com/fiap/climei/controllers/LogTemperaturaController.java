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
import br.com.fiap.climei.models.LogTemperatura;
import br.com.fiap.climei.repository.LogTemperaturaRepository;
import br.com.fiap.climei.repository.NivelTemperaturaRepository;
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
@Tag(name = "Log de Temperatura")
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/api/v1/logTemperatura")
public class LogTemperaturaController {

    @Autowired
    LogTemperaturaRepository logTemperaturaRepository;

    @Autowired
    UsuarioRepository logRepository;

    @Autowired
    NivelTemperaturaRepository nivelTemperaturaRepository;

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    /*
     * [GET] /api/v1/logTemperatura
     */
    @GetMapping
    @Operation(summary = "Lista os dados de todos os logs", description = "Lista os dados de todos os logs")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de dados de logs exibida com sucesso"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public PagedModel<EntityModel<Object>> index(@RequestParam(required = false) Integer log, @ParameterObject @PageableDefault(size = 5) Pageable pageable) {
        var logTemperatura = (log == null) ? 
            logTemperaturaRepository.findAll(pageable): 
            logTemperaturaRepository.findByUsuarioId(log, pageable);

        return assembler.toModel(logTemperatura.map(LogTemperatura::toEntityModel)); //HAL
    }

    /*
     * [GET] /api/v1/logTemperatura/{id}
     */
    @GetMapping("{id}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Log de Temperatura exibido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Log de Temperatura não encontrado"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    @Operation(summary = "Mostra os dados de um log", description = "Mostra os dados de um log")
    public EntityModel<LogTemperatura> show(@PathVariable Long id) {
        log.info("Mostrando logTemperatura {}", id);
        return getLogTemperatura(id).toEntityModel();
    }
    
    /*
     * [POST] /api/v1/logTemperatura
     */
    @PostMapping
    @Operation(summary = "Cria um novo log", description = "Cria um novo log")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Log de Temperatura criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public ResponseEntity<EntityModel<LogTemperatura>> create(@RequestBody @Valid LogTemperatura logTemperatura, BindingResult result){
        log.info("Criando logTemperatura {}", logTemperatura);

        logTemperaturaRepository.save(logTemperatura);
        logTemperatura.setUsuario(logRepository.findById(logTemperatura.getUsuario().getId()).get());
        logTemperatura.setNivelTemperatura(nivelTemperaturaRepository.findById(logTemperatura.getNivelTemperatura().getId()).get());
        return ResponseEntity
            .created(logTemperatura.toEntityModel().getRequiredLink("self").toUri())
            .body(logTemperatura.toEntityModel());
    }

    /*
     * [DELETE] /api/v1/logTemperatura/{id}
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Deleta os dados de um log", description = "Deleta os dados de um log")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Log de Temperatura deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Log de Temperatura não encontrado"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        log.info("Deletando logTemperatura {}", id);
        logTemperaturaRepository.delete(getLogTemperatura(id));
        return ResponseEntity.noContent().build();
    }

    /*
     * [PUT] /api/v1/logTemperatura/{id}
     */
    @PutMapping("{id}")
    @Operation(summary = "Atualiza os dados de um log", description = "Atualiza os dados de um log")
    public ResponseEntity<EntityModel<LogTemperatura>> update(@PathVariable Long id, @RequestBody @Valid LogTemperatura logTemperatura) {
        log.info("Atualizando logTemperatura {}", id);
        getLogTemperatura(id);
        logTemperatura.setId(id);
        logTemperaturaRepository.save(logTemperatura);
        return ResponseEntity.ok(logTemperatura.toEntityModel());
    }


    // Metodos
    private LogTemperatura getLogTemperatura(Long id) {
        return logTemperaturaRepository.findById(id).orElseThrow(() -> new RestNotFoundException("Dados de log não encontrado"));
    }
}
