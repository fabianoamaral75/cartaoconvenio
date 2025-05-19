package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaxaExtraEntidadeDTO {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private String status;
    private String observacao;
    private LocalDateTime dataCriacao;
    private Long periodoCobrancaTaxaId;
    private Long entidadeId;
    private List<ItemTaxaExtraEntidadeDTO> itensContasReceber;
}