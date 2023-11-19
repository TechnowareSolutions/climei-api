package br.com.fiap.climei.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.climei.config.AuthorizationFilter;
import br.com.fiap.climei.exception.RestNotFoundException;
import br.com.fiap.climei.models.Credencial;
import br.com.fiap.climei.models.Usuario;
import br.com.fiap.climei.repository.UsuarioRepository;
import br.com.fiap.climei.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(name = "Usuario")
@RequestMapping("/api/v1/usuario")
public class UsuarioController {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    AuthenticationManager manager;

    @Autowired
    AuthorizationFilter authorizationFilter;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    TokenService tokenService;

    @GetMapping
    @Operation(summary = "Retorna o usuário pelo token", description = "Retorna o usuário pelo token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário retornado com sucesso"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação"),
    })
    public ResponseEntity<Usuario> show(HttpServletRequest request){
        Usuario usuario = getUsuario(request);
        log.info("Usuario: {}", usuario);
        log.info("Token: {}", request.getHeader("Authorization"));
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("{id}")
    @Operation(summary = "Retorna o usuário pelo id", description = "Retorna o usuário pelo id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário retornado com sucesso"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Usuario> show(@PathVariable Long id){
        var usuario = getUsuarioById(id);
        return ResponseEntity.ok(usuario);
    }

    // Delete
    @DeleteMapping
    @Operation(summary = "Deleta o usuário", description = "Deleta o usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Usuario> delete(HttpServletRequest request){
        var usuarioOptional = getUsuario(request);
        repository.delete(usuarioOptional);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Put
    @PutMapping
    @Operation(summary = "Atualiza o usuário", description = "Atualiza o usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação"),
        @ApiResponse(responseCode = "403", description = "É necessário estar autenticado para realizar esta operação"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Usuario> update(HttpServletRequest request, @RequestBody @Valid Usuario usuario, BindingResult result){
        Usuario usuarioToken = getUsuario(request);
        log.info("Usuario atualizado com sucesso! " + usuario);
        usuario.setId(usuarioToken.getId());
        repository.save(usuario);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("signup")
    @Operation(summary = "Registra um novo usuário", description = "Registra um novo usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<Usuario> registrar(@RequestBody @Valid Usuario usuario){
        log.info("Registrando usuário {}", usuario);
        usuario.setSenha(encoder.encode(usuario.getSenha()));
        repository.save(usuario);

        return ResponseEntity.ok(usuario);
    }

    @PostMapping("login")
    @Operation(summary = "Autentica um usuário", description = "Autentica um usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<Object> login(@RequestBody Credencial credencial){
        log.info("Autenticando usuário {}", credencial);
        manager.authenticate(credencial.toAuthentication());
        var token = tokenService.generateToken(credencial);
        return ResponseEntity.ok(token);
    }

    private Usuario getUsuario(HttpServletRequest request) {
        var token = authorizationFilter.getToken(request);
        Usuario usuario = tokenService.getUserByToken(token);
        return usuario;
    }

    private Usuario getUsuarioById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RestNotFoundException("Usuario não encontrado!"));
    }
    
}
