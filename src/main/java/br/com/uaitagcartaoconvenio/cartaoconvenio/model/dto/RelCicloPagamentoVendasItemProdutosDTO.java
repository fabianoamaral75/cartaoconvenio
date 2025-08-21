package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;

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
public class RelCicloPagamentoVendasItemProdutosDTO {

    private Long idItensVenda;
    private Integer qtyItem;
    private BigDecimal vlrUnitario;
    private BigDecimal vlrTotalItem;
    private String produto;
    private BigDecimal vlrProduto;
    private Long idVenda;
}
