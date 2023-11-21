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
import br.com.fiap.climei.models.LogUmidade;
import br.com.fiap.climei.repository.LogUmidadeRepository;
import br.com.fiap.climei.repository.NivelUmidadeRepository;
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
@Tag(name = "Log de Umidade")
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/api/v1/logUmidade")
public class LogUmidadeController {

    @Autowired
    LogUmidadeRepository logUmidadeRepository;

    @Autowired
    UsuarioRepository logRepository;

    @Autowired
    NivelUmidadeRepository nivelUmidadeRepository;

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    /*
     * [GET] /api/v1/logUmidade
     */
    @GetMapping
    @Operation(summary = "Lista os dados de todos os logs", description = "Lista os dados de todos os logs")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de dados de logs exibida com sucesso"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public PagedModel<EntityModel<Object>> index(@RequestParam(required = false) Integer log, @ParameterObject @PageableDefault(size = 5) Pageable pageable) {
        var logUmidade = (log == null) ? 
            logUmidadeRepository.findAll(pageable): 
            logUmidadeRepository.findByUsuarioId(log, pageable);

        return assembler.toModel(logUmidade.map(LogUmidade::toEntityModel)); //HAL
    }

    /*
     * [GET] /api/v1/logUmidade/{id}
     */
    @GetMapping("{id}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Log de Umidade exibido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Log de Umidade não encontrado"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    @Operation(summary = "Mostra os dados de um log", description = "Mostra os dados de um log")
    public EntityModel<LogUmidade> show(@PathVariable Long id) {
        log.info("Mostrando logUmidade {}", id);
        return getLogUmidade(id).toEntityModel();
    }
    
    /*
     * [POST] /api/v1/logUmidade
     */
    @PostMapping
    @Operation(summary = "Cria um novo log", description = "Cria um novo log")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Log de Umidade criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public ResponseEntity<EntityModel<LogUmidade>> create(@RequestBody @Valid LogUmidade logUmidade, BindingResult result){
        log.info("Criando logUmidade {}", logUmidade);

        logUmidadeRepository.save(logUmidade);
        logUmidade.setUsuario(logRepository.findById(logUmidade.getUsuario().getId()).get());
        logUmidade.setNivelUmidade(nivelUmidadeRepository.findById(logUmidade.getNivelUmidade().getId()).get());
        return ResponseEntity
            .created(logUmidade.toEntityModel().getRequiredLink("self").toUri())
            .body(logUmidade.toEntityModel());
    }

    /*
     * [DELETE] /api/v1/logUmidade/{id}
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Deleta os dados de um log", description = "Deleta os dados de um log")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Log de Umidade deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Log de Umidade não encontrado"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        log.info("Deletando logUmidade {}", id);
        logUmidadeRepository.delete(getLogUmidade(id));
        return ResponseEntity.noContent().build();
    }

    /*
     * [PUT] /api/v1/logUmidade/{id}
     */
    @PutMapping("{id}")
    @Operation(summary = "Atualiza os dados de um log", description = "Atualiza os dados de um log")
    public ResponseEntity<EntityModel<LogUmidade>> update(@PathVariable Long id, @RequestBody @Valid LogUmidade logUmidade) {
        log.info("Atualizando logUmidade {}", id);
        getLogUmidade(id);
        logUmidade.setId(id);
        logUmidadeRepository.save(logUmidade);
        return ResponseEntity.ok(logUmidade.toEntityModel());
    }


    // Metodos
    private LogUmidade getLogUmidade(Long id) {
        return logUmidadeRepository.findById(id).orElseThrow(() -> new RestNotFoundException("Dados de log não encontrado"));
    }
}
