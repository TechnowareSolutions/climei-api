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
import br.com.fiap.climei.models.LogBatimentoOxigenacao;
import br.com.fiap.climei.repository.LogBatimentoOxigenacaoRepository;
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
@Tag(name = "Log de BatimentoOxigenacao")
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/api/v1/logBatimentoOxigenacao")
public class LogBatimentoOxigenacaoController {

    @Autowired
    LogBatimentoOxigenacaoRepository logBatimentoOxigenacaoRepository;

    @Autowired
    UsuarioRepository logRepository;

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    /*
     * [GET] /api/v1/logBatimentoOxigenacao
     */
    @GetMapping
    @Operation(summary = "Lista os dados de todos os logs", description = "Lista os dados de todos os logs")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de dados de logs exibida com sucesso"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public PagedModel<EntityModel<Object>> index(@RequestParam(required = false) Long usuario, @ParameterObject @PageableDefault(size = 5) Pageable pageable) {
        var logBatimentoOxigenacao = (usuario == null) ? 
            logBatimentoOxigenacaoRepository.findAll(pageable): 
            logBatimentoOxigenacaoRepository.findLastLogByUsuarioId(usuario, pageable);

        return assembler.toModel(logBatimentoOxigenacao.map(LogBatimentoOxigenacao::toEntityModel)); //HAL
    }

    /*
     * [GET] /api/v1/logBatimentoOxigenacao/{id}
     */
    @GetMapping("{id}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Log de BatimentoOxigenacao exibido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Log de BatimentoOxigenacao não encontrado"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    @Operation(summary = "Mostra os dados de um log", description = "Mostra os dados de um log")
    public EntityModel<LogBatimentoOxigenacao> show(@PathVariable Long id) {
        log.info("Mostrando logBatimentoOxigenacao {}", id);
        return getLogBatimentoOxigenacao(id).toEntityModel();
    }
    
    /*
     * [POST] /api/v1/logBatimentoOxigenacao
     */
    @PostMapping
    @Operation(summary = "Cria um novo log", description = "Cria um novo log")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Log de BatimentoOxigenacao criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public ResponseEntity<EntityModel<LogBatimentoOxigenacao>> create(@RequestBody @Valid LogBatimentoOxigenacao logBatimentoOxigenacao, BindingResult result){
        log.info("Criando logBatimentoOxigenacao {}", logBatimentoOxigenacao);

        logBatimentoOxigenacaoRepository.save(logBatimentoOxigenacao);
        logBatimentoOxigenacao.setUsuario(logRepository.findById(logBatimentoOxigenacao.getUsuario().getId()).get());
        return ResponseEntity
            .created(logBatimentoOxigenacao.toEntityModel().getRequiredLink("self").toUri())
            .body(logBatimentoOxigenacao.toEntityModel());
    }

    /*
     * [DELETE] /api/v1/logBatimentoOxigenacao/{id}
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Deleta os dados de um log", description = "Deleta os dados de um log")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Log de BatimentoOxigenacao deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Log de BatimentoOxigenacao não encontrado"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        log.info("Deletando logBatimentoOxigenacao {}", id);
        logBatimentoOxigenacaoRepository.delete(getLogBatimentoOxigenacao(id));
        return ResponseEntity.noContent().build();
    }

    /*
     * [PUT] /api/v1/logBatimentoOxigenacao/{id}
     */
    @PutMapping("{id}")
    @Operation(summary = "Atualiza os dados de um log", description = "Atualiza os dados de um log")
    public ResponseEntity<EntityModel<LogBatimentoOxigenacao>> update(@PathVariable Long id, @RequestBody @Valid LogBatimentoOxigenacao logBatimentoOxigenacao) {
        log.info("Atualizando logBatimentoOxigenacao {}", id);
        getLogBatimentoOxigenacao(id);
        logBatimentoOxigenacao.setId(id);
        logBatimentoOxigenacaoRepository.save(logBatimentoOxigenacao);
        return ResponseEntity.ok(logBatimentoOxigenacao.toEntityModel());
    }


    // Metodos
    private LogBatimentoOxigenacao getLogBatimentoOxigenacao(Long id) {
        return logBatimentoOxigenacaoRepository.findById(id).orElseThrow(() -> new RestNotFoundException("Dados de log não encontrado"));
    }
}
