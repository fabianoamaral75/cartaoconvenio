package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CicloTaxaExtraDTO {
    private Long id;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dataCadastro;
    private BigDecimal valorTaxaExtra;
    private Long cicloPagamentoVendaId;
    private Long taxaExtraConveniadaId;
}
