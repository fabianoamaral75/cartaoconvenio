package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VigenciaContratoConveniadaDTO {
    private Long id;
    private LocalDate dataInicio;
    private LocalDate dataFinal;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;
    private LocalDateTime dataDesativacao;
    private String usuarioDesativacao;
    private Boolean renovacao;
    private String observacao;
    private Long contratoConveniadoId; // Apenas o ID para evitar complexidade
}