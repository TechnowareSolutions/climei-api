package br.com.fiap.climei.config;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.fiap.climei.models.DadosUsuario;
import br.com.fiap.climei.models.LogBatimentoOxigenacao;
import br.com.fiap.climei.models.Usuario;
import br.com.fiap.climei.repository.DadosUsuarioRepository;
import br.com.fiap.climei.repository.LogBatimentoOxigenacaoRepository;
import br.com.fiap.climei.repository.UsuarioRepository;


@Configuration
@Profile("dev")
public class DatabaseSeeder implements CommandLineRunner {

    /*
     * Repositories
     */
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    DadosUsuarioRepository dadosUsuarioRepository;

    @Autowired
    LogBatimentoOxigenacaoRepository logBatimentoOxigenacaoRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {

        /*
         * Usuários
         */
        usuarioRepository.saveAll(List.of(
            Usuario.builder().nome("Caio Gallo Barreira").email("caiogallobarreira@gmail.com").senha(encoder.encode("123456")).idade(20).sexo("M").build(),
            Usuario.builder().nome("Testes FIAP").email("testesfiap@gmail.com").senha(encoder.encode("123456")).idade(20).sexo("M").build()
        ));

        /*
         * Dados dos Usuários
         */
        dadosUsuarioRepository.saveAll(List.of(
            DadosUsuario.builder().usuario(usuarioRepository.findById(1L).get()).altura(1.80).peso(80.0).build(),
            DadosUsuario.builder().usuario(usuarioRepository.findById(2L).get()).altura(1.80).peso(80.0).build()
        ));

        /*
         * Log de Batimento Cardíaco
         */

        logBatimentoOxigenacaoRepository.saveAll(List.of(
            LogBatimentoOxigenacao.builder().usuario(usuarioRepository.findById(1L).get()).quantidade(80.0).dataAvaliacao(LocalDate.now()).build(),
            LogBatimentoOxigenacao.builder().usuario(usuarioRepository.findById(1L).get()).quantidade(32.0).dataAvaliacao(LocalDate.now()).build(),
            LogBatimentoOxigenacao.builder().usuario(usuarioRepository.findById(1L).get()).quantidade(54.0).dataAvaliacao(LocalDate.now()).build(),
            LogBatimentoOxigenacao.builder().usuario(usuarioRepository.findById(1L).get()).quantidade(76.0).dataAvaliacao(LocalDate.now()).build()
        ));

    }
    
}
