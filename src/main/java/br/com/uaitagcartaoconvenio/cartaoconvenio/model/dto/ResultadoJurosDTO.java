package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResultadoJurosDTO {
    private final BigDecimal taxaMensal;
    private final BigDecimal taxaDiaria;
    private final BigDecimal taxaPeriodo;
    private final LocalDate dataVencimento;
    private final LocalDate dataPagamento;
    private final long periodoDias;
    private final BigDecimal valorJuros;
    private final BigDecimal valorComDesconto;
    
    @Override
    public String toString() {
        return String.format(
            "ResultadoJuros[\n" +
            "  Taxa (%% m) = %s\n" +
            "  Taxa (%% d) = %s\n" +
            "  Taxa (%% p) = %s\n" +
            "  Data Vencimento = %s\n" +
            "  Data Pagamento = %s\n" +
            "  Período (dias) = %d\n" +
            "  Vlr Desc ESTAB (R$) = %s\n" +
            "  Vlr Nom c/ Desc (R$) = %s\n" +
            "]",
            taxaMensal.setScale(4, RoundingMode.HALF_UP),
            taxaDiaria.setScale(6, RoundingMode.HALF_UP),
            taxaPeriodo.setScale(6, RoundingMode.HALF_UP),
            dataVencimento,
            dataPagamento,
            periodoDias,
            valorJuros.setScale(2, RoundingMode.HALF_UP),
            valorComDesconto.setScale(2, RoundingMode.HALF_UP)
        );
    }    
}
