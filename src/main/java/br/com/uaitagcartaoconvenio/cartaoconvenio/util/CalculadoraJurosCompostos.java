package br.com.uaitagcartaoconvenio.cartaoconvenio.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class CalculadoraJurosCompostos {

    private static final MathContext MATH_CTX = new MathContext(10, RoundingMode.HALF_UP);
    private static final BigDecimal DIAS_NO_MES = new BigDecimal("30");
    private static final BigDecimal CEM = new BigDecimal("100");
    private static final BigDecimal UM = BigDecimal.ONE;

    public static ResultadoJuros calcularJuros(BigDecimal taxaNominalMensal,
                                             LocalDate dataCorte,
                                             LocalDate dataAdiantamento,
                                             LocalDate dataVencimento,
                                             BigDecimal valorBase) {

        // 1. Calcular período em dias entre adiantamento e vencimento
        long periodoDias = ChronoUnit.DAYS.between(dataAdiantamento, dataVencimento);
        BigDecimal periodoBigD = new BigDecimal(periodoDias);

        // 2. Converter taxa mensal para diária (juros compostos)
        BigDecimal taxaMensalDecimal = taxaNominalMensal.divide(CEM, MATH_CTX);
        
        // Cálculo: (1 + taxaMensal)^(1/30) - 1
        BigDecimal baseDiaria = UM.add(taxaMensalDecimal);
        BigDecimal expoenteDiario = UM.divide(DIAS_NO_MES, MATH_CTX);
        BigDecimal taxaDiariaDecimal = pow(baseDiaria, expoenteDiario).subtract(UM);

        // 3. Calcular taxa total do período
        // (1 + taxaDiaria)^dias - 1
        BigDecimal basePeriodo = UM.add(taxaDiariaDecimal);
        BigDecimal taxaPeriodoDecimal = pow(basePeriodo, periodoBigD).subtract(UM);

        // 4. Calcular valor dos juros
        BigDecimal valorJuros = valorBase.multiply(taxaPeriodoDecimal, MATH_CTX);

        // 5. Calcular valor com desconto
        BigDecimal valorComDesconto = valorBase.subtract(valorJuros, MATH_CTX);

        // Converter taxas para porcentagem
        BigDecimal taxaDiariaPercent = taxaDiariaDecimal.multiply(CEM);
        BigDecimal taxaPeriodoPercent = taxaPeriodoDecimal.multiply(CEM);

        return new ResultadoJuros(
            taxaNominalMensal,
            taxaDiariaPercent,
            taxaPeriodoPercent,
            dataCorte,
            dataAdiantamento,
            dataVencimento,
            periodoDias,
            valorJuros,
            valorComDesconto,
            valorBase
        );
    }

    // Método auxiliar para cálculo de potência com BigDecimal
    private static BigDecimal pow(BigDecimal base, BigDecimal exponent) {
        // Para cálculos financeiros simples, usamos aproximação
        return new BigDecimal(Math.pow(base.doubleValue(), exponent.doubleValue()), MATH_CTX);
    }

    public static class ResultadoJuros {
        private final BigDecimal taxaMensal;
        private final BigDecimal taxaDiaria;
        private final BigDecimal taxaPeriodo;
        private final LocalDate dataCorte;
        private final LocalDate dataAdiantamento;
        private final LocalDate dataVencimento;
        private final long periodoDias;
        private final BigDecimal valorJuros;
        private final BigDecimal valorComDesconto;
        private final BigDecimal valorBase;
        
        @SuppressWarnings("deprecation")
		NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        public ResultadoJuros(BigDecimal taxaMensal, BigDecimal taxaDiaria, BigDecimal taxaPeriodo,
                            LocalDate dataCorte, LocalDate dataAdiantamento, LocalDate dataVencimento,
                            long periodoDias, BigDecimal valorJuros, BigDecimal valorComDesconto, BigDecimal valorBase) {
            this.taxaMensal = taxaMensal;
            this.taxaDiaria = taxaDiaria;
            this.taxaPeriodo = taxaPeriodo;
            this.dataCorte = dataCorte;
            this.dataAdiantamento = dataAdiantamento;
            this.dataVencimento = dataVencimento;
            this.periodoDias = periodoDias;
            this.valorJuros = valorJuros;
            this.valorComDesconto = valorComDesconto;
            this.valorBase = valorBase;
        }

        // Getters
        public BigDecimal getTaxaMensal() { return taxaMensal.setScale(2, RoundingMode.HALF_UP); }
        public BigDecimal getTaxaDiaria() { return taxaDiaria.setScale(4, RoundingMode.HALF_UP); }
        public BigDecimal getTaxaPeriodo() { return taxaPeriodo.setScale(4, RoundingMode.HALF_UP); }
        public LocalDate getDataCorte() { return dataCorte; }
        public LocalDate getDataAdiantamento() { return dataAdiantamento; }
        public LocalDate getDataVencimento() { return dataVencimento; }
        public long getPeriodoDias() { return periodoDias; }
        public BigDecimal getValorJuros() { return valorJuros.setScale(2, RoundingMode.HALF_UP); }
        public BigDecimal getValorComDesconto() { return valorComDesconto.setScale(2, RoundingMode.HALF_UP); }
        public BigDecimal getValorBase() { return valorBase.setScale(2, RoundingMode.HALF_UP); }

        @Override
        public String toString() {
            return String.format(
                "Taxa (%% m)\tTaxa (%% d)\tTaxa (%% p)\tDt Corte\tDt Pgto\t\tDta Ven\t\tPeríodo (dias)\tVlr Desc ESTAB (R$)\tVlr Nom c/ Desc (R$)\tVlr Base (R$)%n" +
                "%s%%\t\t%s%%\t\t%s%%\t\t%s\t%s\t%s\t%d\t\t%s\t\t%s\t\t%s",
                getTaxaMensal(),
                getTaxaDiaria(),
                getTaxaPeriodo(),
                dataCorte.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                dataAdiantamento.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                dataVencimento.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                periodoDias,
                formato.format(getValorJuros()),
                formato.format(getValorComDesconto()),
                formato.format(getValorBase())
            );
        }
    }

    public static void main(String[] args) {
        // Exemplo com os valores fornecidos
        BigDecimal taxaNominal = new BigDecimal("2");
        LocalDate dataCorte = LocalDate.of(2023, 12, 27);
        LocalDate dataAdiantamento = LocalDate.of(2024, 1, 16);
        LocalDate dataVencimento = LocalDate.of(2024, 2, 27);
        BigDecimal valorBase = new BigDecimal("3627.36");

        ResultadoJuros resultado = calcularJuros(
            taxaNominal,
            dataCorte,
            dataAdiantamento,
            dataVencimento,
            valorBase
        );

        System.out.println(resultado);
    }
}
