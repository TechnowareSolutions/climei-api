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
import br.com.fiap.climei.models.LogAgua;
import br.com.fiap.climei.repository.LogAguaRepository;
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
@Tag(name = "Log de Agua")
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/api/v1/logAgua")
public class LogAguaController {

    @Autowired
    LogAguaRepository logAguaRepository;

    @Autowired
    UsuarioRepository logRepository;

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    /*
     * [GET] /api/v1/logAgua
     */
    @GetMapping
    @Operation(summary = "Lista os dados de todos os logs", description = "Lista os dados de todos os logs")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de dados de logs exibida com sucesso"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public PagedModel<EntityModel<Object>> index(@RequestParam(required = false) Long usuario, @ParameterObject @PageableDefault(size = 5) Pageable pageable) {
        var logAgua = (usuario == null) ? 
            logAguaRepository.findAll(pageable): 
            logAguaRepository.findByUsuarioId(usuario, pageable);

        return assembler.toModel(logAgua.map(LogAgua::toEntityModel)); //HAL
    }

    /*
     * [GET] /api/v1/logAgua/{id}
     */
    @GetMapping("{id}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Log de Agua exibido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Log de Agua não encontrado"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    @Operation(summary = "Mostra os dados de um log", description = "Mostra os dados de um log")
    public EntityModel<LogAgua> show(@PathVariable Long id) {
        log.info("Mostrando logAgua {}", id);
        return getLogAgua(id).toEntityModel();
    }
    
    /*
     * [POST] /api/v1/logAgua
     */
    @PostMapping
    @Operation(summary = "Cria um novo log", description = "Cria um novo log")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Log de Agua criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public ResponseEntity<EntityModel<LogAgua>> create(@RequestBody @Valid LogAgua logAgua, BindingResult result){
        log.info("Criando logAgua {}", logAgua);

        logAguaRepository.save(logAgua);
        logAgua.setUsuario(logRepository.findById(logAgua.getUsuario().getId()).get());
        return ResponseEntity
            .created(logAgua.toEntityModel().getRequiredLink("self").toUri())
            .body(logAgua.toEntityModel());
    }

    /*
     * [DELETE] /api/v1/logAgua/{id}
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Deleta os dados de um log", description = "Deleta os dados de um log")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Log de Agua deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Log de Agua não encontrado"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        log.info("Deletando logAgua {}", id);
        logAguaRepository.delete(getLogAgua(id));
        return ResponseEntity.noContent().build();
    }

    /*
     * [PUT] /api/v1/logAgua/{id}
     */
    @PutMapping("{id}")
    @Operation(summary = "Atualiza os dados de um log", description = "Atualiza os dados de um log")
    public ResponseEntity<EntityModel<LogAgua>> update(@PathVariable Long id, @RequestBody @Valid LogAgua logAgua) {
        log.info("Atualizando logAgua {}", id);
        getLogAgua(id);
        logAgua.setId(id);
        logAguaRepository.save(logAgua);
        return ResponseEntity.ok(logAgua.toEntityModel());
    }


    // Metodos
    private LogAgua getLogAgua(Long id) {
        return logAguaRepository.findById(id).orElseThrow(() -> new RestNotFoundException("Dados de log não encontrado"));
    }
}
