package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoPeriodoDTO {

    private Long id;
    private String descricao;
    private LocalDateTime dataCriacao;
    private List<Long> periodosCobrancaIds; // Apenas os IDs
}
