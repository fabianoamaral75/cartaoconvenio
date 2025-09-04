package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dataCorte;           // Ex.: Data Corte
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dataPagamento;       // Ex.: Pagamento em 5 dias
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dataVencimento;      // Ex.: Vencimento em 1 mês        
    private BigDecimal taxaNominalMensal;  // Ex.: 2% ao mês (configurável)

}
