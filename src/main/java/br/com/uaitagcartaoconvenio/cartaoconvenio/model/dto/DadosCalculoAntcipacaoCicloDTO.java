package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosCalculoAntcipacaoCicloDTO {
	
    private Long idCicloPagamentoVenda;
    private List<Long> idsVendas;
    private String loginUser;

    private LocalDate dataCorte;           // Data Corte
    private LocalDate dataPagamento;       // Pagamento em 5 dias
    private LocalDate dataVencimento;      // Vencimento em 1 mês        
    private BigDecimal taxaNominalMensal;  // 2% ao mês (configurável)

}
