package br.com.fiap.climei.models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "T_CLM_LOG_TEMP")
public class LogTemperatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "É obrigatório inserir um usuário") @ManyToOne
    private Usuario usuario;

    @NotNull(message = "É obrigatório inserir um nivel de temperatura") @ManyToOne
    private NivelTemperatura nivelTemperatura;

    @NotNull(message = "É obrigatório inserir uma temperatura")
    private Double temperatura;

    @NotNull @PastOrPresent
    private LocalDate dataAvaliacao;
}
