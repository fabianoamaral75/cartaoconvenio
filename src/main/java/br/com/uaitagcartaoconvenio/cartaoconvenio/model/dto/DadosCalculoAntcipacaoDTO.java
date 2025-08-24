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
public class DadosCalculoAntcipacaoDTO {
	
    private Long idConveniados;   // Lista de conveniada para selecionar a que solicitou a antecipação.
    private List<Long> idsVendas; // Lista de ID das vendas que serão antecipadas
    private String loginUser;     // Login do usuário que esta criando e solicitando a antecipação.

    private LocalDate dataCorte;           // Ex.: Data Corte
    private LocalDate dataPagamento;       // Ex.: Pagamento em 5 dias
    private LocalDate dataVencimento;      // Ex.: Vencimento em 1 mês        
    private BigDecimal taxaNominalMensal;  // Ex.: 2% ao mês (configurável)

}
