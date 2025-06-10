package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemTaxaExtraEntidadeDTO {
    private Long id;
    private Long taxaExtraEntidadeId;
    private Long contasReceberId;
    private BigDecimal valorTaxa;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dataCadastro;
}