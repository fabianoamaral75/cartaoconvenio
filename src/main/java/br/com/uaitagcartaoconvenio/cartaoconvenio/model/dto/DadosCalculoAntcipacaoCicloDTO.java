package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    private String loginUser;     // Login do usuário que esta criando e solicitando a antecipação.

    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dataCorte;           // Data Corte
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dataPagamento;       // Pagamento em 5 dias
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dataVencimento;      // Vencimento em 1 mês        
    private BigDecimal taxaNominalMensal;  // 2% ao mês (configurável)

}
