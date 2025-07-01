package br.com.uaitagcartaoconvenio.cartaoconvenio.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FuncoesUteis {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyyMM");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Retorna uma data no formato PostgreSQL (YYYY-MM-DD) com base no dia do mês
     * e no incremento de meses a partir da data atual.
     * 
     * @param dia O dia do mês que será usado na data retornada
     * @param mesesIncremento Número de meses a incrementar a partir da data atual
     * @return String com a data no formato PostgreSQL
     * @throws IllegalArgumentException Se o dia for inválido (menor que 1 ou maior que 31)
     */
    public static String getDataPostgres(int dia, int mesesIncremento) {
        // Validar o dia
        if (dia < 1 || dia > 31) {
            throw new IllegalArgumentException("Dia inválido. Deve ser entre 1 e 31.");
        }
        
        // Obter a data atual e adicionar os meses
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataCalculada = dataAtual.plusMonths(mesesIncremento);
        
        // Ajustar para o dia especificado, tratando casos onde o dia não existe no mês
        int ultimoDiaMes = dataCalculada.lengthOfMonth();
        int diaAjustado = Math.min(dia, ultimoDiaMes);
        
        LocalDate dataFinal = LocalDate.of(
            dataCalculada.getYear(),
            dataCalculada.getMonth(),
            diaAjustado
        );
        
        // Formatar para o padrão PostgreSQL
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dataFinal.format(formatter);
    }
/*    
    // Exemplo de uso
    public static void main(String[] args) {
        // Exemplo 1: Dia 15 do mês atual
        System.out.println(getDataPostgres(26, 3)); // Saída: 2023-11-15 (supondo que hoje é nov/2023)
        
        // Exemplo 2: Dia 31 com incremento de 1 mês (mes que vem)
        System.out.println(getDataPostgres(28, 2)); // Saída: 2023-12-31
        
    }
*/
	public static Map<String, String> getFirstAndLastDayOfMonthSafe(String yyyyMM) {
	    try {
	        if (yyyyMM == null || !yyyyMM.matches("\\d{6}")) {
	            throw new IllegalArgumentException("Formato deve ser yyyyMM com 6 dígitos");
	        }
	        
	        YearMonth yearMonth = YearMonth.parse(yyyyMM, INPUT_FORMAT);
	        LocalDate now = LocalDate.now();
	        
	        // Valida se o mês não é no futuro
	        if (yearMonth.isAfter(YearMonth.from(now))) {
	            throw new IllegalArgumentException("Mês não pode ser no futuro");
	        }
	        
	        Map<String, String> result = new HashMap<>();
	        result.put("primeiroDia", yearMonth.atDay(1).format(OUTPUT_FORMAT));
	        result.put("ultimoDia", yearMonth.atEndOfMonth().format(OUTPUT_FORMAT));
	        
	        return result;
	    } catch (Exception e) {
	        // Retorna valores padrão em caso de erro
	        Map<String, String> fallback = new HashMap<>();
	        fallback.put("primeiroDia", "01/01/1970");
	        fallback.put("ultimoDia", "01/01/1970");
	        return fallback;
	    }
	}
    /**
     * Retorna a data e hora atuais no formato dd/MM/yyyy HH:mm:ss
     * @return String com a data formatada
     */
    public static String getCurrentDateTimeBrasil() {
        // Define o formato desejado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        // Obtém a data/hora atual e formata
        return LocalDateTime.now().format(formatter);
    }
    
    public static boolean isValidYearMonth(String dateStr) {
        if (dateStr == null || dateStr.length() != 6) {
            return false;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");

        try {
            YearMonth.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static String removerCaracteresNaoNumericos(String input) {
        // Substitui todos os caracteres não numéricos por uma string vazia
        return input.replaceAll("[^0-9]", "");
    }
    
    /**
     * Valida e converte uma data no formato "dd/MM/yyyy HH:mm:ss" para "yyyy-MM-dd 00:00:00".
     *
     * @param dataEntrada Data no formato "dd/MM/yyyy HH:mm:ss".
     * @return Data formatada como "yyyy-MM-dd 00:00:00" ou null se a entrada for inválida.
     */
    public static String validarEConverterData(String dataEntrada, String formatHora) {
        // Define o formato de entrada
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        formatoEntrada.setLenient(false); // Não permite datas inválidas (ex: 31/02/2023)
        // Define o formato de saída
        SimpleDateFormat formatoSaida = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss"  );

        try {
        	String dataFormatada = converterParaFormatoDDMMYYYY(dataEntrada);
        	
            // Faz o parsing da data de entrada
            Date data = formatoEntrada.parse(dataFormatada + " " + formatHora);

            // Formata a data para o formato de saída
            return formatoSaida.format(data);
        } catch (ParseException e) {
            // Se a data for inválida, retorna null ou lança uma exceção
            System.err.println("Data inválida: " + dataEntrada);
            return null;
        }
    }
    
    /**
     * Converte uma string no formato "yyyy-MM-dd HH:mm:ss" para java.sql.Timestamp.
     *
     * @param dataString String no formato "yyyy-MM-dd HH:mm:ss".
     * @return Objeto java.sql.Timestamp.
     * @throws DateTimeParseException Se a string não estiver no formato esperado.
     */
    public static Timestamp converterParaTimestamp(String dataString) {
        // Define o formato de entrada
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Faz o parsing da string para um objeto LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.parse(dataString, formatoEntrada);

        // Converte o LocalDateTime para Timestamp
        return Timestamp.valueOf(localDateTime);
    }

    
    /**
     * Converte uma data no formato "ddmmyyyy" para uma string no formato "dd/MM/yyyy".
     *
     * @param dataNoFormatoDDMMYYYY String no formato "ddmmyyyy".
     * @return String no formato "dd/MM/yyyy".
     * @throws ParseException Se a string não estiver no formato esperado.
     */
    public static String converterParaFormatoDDMMYYYY(String dataNoFormatoDDMMYYYY) throws ParseException {
        // Verifica se a string tem o tamanho correto
        if (dataNoFormatoDDMMYYYY.length() != 8) {
            throw new IllegalArgumentException("A data deve estar no formato ddmmyyyy (8 dígitos).");
        }

        // Define o formato de entrada (ddmmyyyy)
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("ddMMyyyy");
        formatoEntrada.setLenient(false); // Não permite datas inválidas (ex: 31/02/2023)

        // Define o formato de saída (dd/MM/yyyy)
        SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy");

        // Faz o parsing da data de entrada
        Date data = formatoEntrada.parse(dataNoFormatoDDMMYYYY);

        // Formata a data para o formato de saída
        return formatoSaida.format(data);
    }

    /**
     * Retorna o mês anterior ao atual no formato "yyyymm"
     * @return String no formato "yyyymm" representando o mês anterior
     */
    public static String getPreviousMonthFormatted() {
        // Obtém a data atual e subtrai 1 mês
        LocalDate previousMonthDate = LocalDate.now().minusMonths(1);
        
        // Define o formato desejado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        
        // Retorna a data formatada
        return previousMonthDate.format(formatter);
    }

    // Versão alternativa que recebe uma data específica como parâmetro
    public static String getPreviousMonthFormatted(LocalDate referenceDate) {
        return referenceDate.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
    }

    /**
     * Retorna a data atual no formato "yyyymm".
     *
     * @return String com a data no formato "yyyymm".
     */
    public static String getDataAtualFormatoYYYMM() {
        // Obtém a data atual
        Date dataAtual = new Date();

        // Define o formato desejado
        SimpleDateFormat formato = new SimpleDateFormat("yyyyMM");

        // Formata a data e retorna como string
        return formato.format(dataAtual);
    }
    
    /**
     * Formata um valor numérico como uma string no formato de moeda brasileira (Real).
     *
     * @param valor O valor numérico a ser formatado.
     * @return String formatada no formato de moeda brasileira (R$).
     */
    public static String formatarParaReal(double valor) {
        // Cria um formatador de moeda para o local brasileiro
        @SuppressWarnings("deprecation")
		NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        // Formata o valor e retorna como string
        return formatador.format(valor);
    }
    
    /**
     * Converte um valor monetário em formato de string para um BigDecimal.
     *
     * @param valorMoeda String no formato de moeda (ex: "R$ 12.123,98" ou "12.123,98").
     * @return BigDecimal representando o valor numérico.
     * @throws ParseException Se a string não estiver em um formato válido.
     */
    public static BigDecimal converterParaBigDecimal(String valorMoeda) throws ParseException {
        // Remove o símbolo da moeda (R$) e espaços em branco
        String valorLimpo = valorMoeda.replace("R$", "").trim();

        // Cria um formatador de número para o local brasileiro
        @SuppressWarnings("deprecation")
		NumberFormat formatador = NumberFormat.getNumberInstance(new Locale("pt", "BR"));

        // Faz o parsing da string para um Number e converte para BigDecimal
        return new BigDecimal(formatador.parse(valorLimpo).toString());
    }

    /**
     * Converte um valor monetário em formato de string para um valor numérico.
     *
     * @param valorMoeda String no formato de moeda (ex: "R$ 12.123,98" ou "12.123,98").
     * @return Valor numérico (ex: 12123.98).
     * @throws ParseException Se a string não estiver em um formato válido.
     */
    public static double converterParaDouble(String valorMoeda) throws ParseException {
        // Remove o símbolo da moeda (R$) e espaços em branco
        String valorLimpo = valorMoeda.replace("R$", "").trim();

        // Cria um formatador de número para o local brasileiro
        @SuppressWarnings("deprecation")
		NumberFormat formatador = NumberFormat.getNumberInstance(new Locale("pt", "BR"));

        // Faz o parsing da string para um número
        return formatador.parse(valorLimpo).doubleValue();
    }
    
    /**
     * Trunca um valor double em duas casas decimais.
     *
     * @param valor O valor double a ser truncado.
     * @return Valor truncado com duas casas decimais.
     */
    public static double truncar(double valor) {
        // Converte o valor double para BigDecimal
        BigDecimal bd = new BigDecimal(valor);

        // Define o modo de arredondamento para truncar (descarta as casas decimais excedentes)
        bd = bd.setScale(2, RoundingMode.DOWN);

        // Retorna o valor truncado como double
        return bd.doubleValue();
    }

    /**
     * Soma dias à data atual e retorna a nova data
     * @param dias Quantidade de dias a adicionar (pode ser negativo para subtrair)
     * @return Nova data com os dias adicionados
     */
    public static Date somarDiasDataAtual(int dias) {
        // Obtém a instância do Calendar
        Calendar calendar = Calendar.getInstance();
        
        // Adiciona a quantidade de dias
        calendar.add(Calendar.DAY_OF_MONTH, dias);
        
        // Retorna como java.util.Date
        return calendar.getTime();
    }

    // Versão alternativa usando Java 8+ (mais moderna)
    public static Date somarDiasDataAtualJava8(int dias) {
        // Converte para java.time (mais moderno) e depois volta para java.util.Date
        java.time.LocalDate localDate = java.time.LocalDate.now().plusDays(dias);
        return java.util.Date.from(localDate.atStartOfDay()
                .atZone(java.time.ZoneId.systemDefault())
                .toInstant());
    }
    
    /**
     * Retorna a data atual formatada como string no padrão "yyyy-MM-dd HH:mm:ss"
     * @return Data formatada como String
     */
    public static String getCurrentDateTimeFormatted() {
        // Cria um objeto Date com a data/hora atual
        Date currentDate = new Date();
        
        // Cria o formatador com o padrão desejado
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        // Formata a data
        return sdf.format(currentDate);
    }

    // Versão que retorna tanto a String formatada quanto o objeto Date
    public static class DateTimeResult {
        public final Date date;
        public final String formatted;

        public DateTimeResult(Date date, String formatted) {
            this.date = date;
            this.formatted = formatted;
        }
    }

    public static DateTimeResult getCurrentDateTime() {
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new DateTimeResult(currentDate, sdf.format(currentDate));
    }

    /**
     * Converte Date para String no formato dd/MM/yyyy HH:mm:ss (Java 8+)
     * @param data - Objeto Date a ser formatado
     * @return String com a data formatada
     */
    public static String dateToString(Date data) {
        if (data == null) {
            return null;
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return data.toInstant()
                  .atZone(ZoneId.systemDefault())
                  .toLocalDateTime()
                  .format(formatter);
    }
    
    /**
     * Soma um número de anos à data atual e retorna como java.util.Date
     * @param anos Número de anos a serem somados (pode ser negativo para subtrair)
     * @return Data atual com os anos adicionados
     */
    public static Date somarAnosADataAtual(int anos) {
        // Obtém a data atual no fuso horário do sistema
        LocalDate dataAtual = LocalDate.now();
        
        // Adiciona os anos (funciona com valores negativos para subtrair)
        LocalDate dataResultante = dataAtual.plusYears(anos);
        
        // Converte LocalDate para java.util.Date
        return Date.from(
            dataResultante.atStartOfDay()       // Converte para LocalDateTime (meia-noite)
            .atZone(ZoneId.systemDefault())    // Adiciona fuso horário
            .toInstant()                        // Converte para Instant
        );
    }

    /**
     * Retorna uma data no mês subsequente ao atual, com o dia especificado.
     * 
     * Esta função calcula a data correspondente ao dia fornecido (parâmetro 'day')
     * no próximo mês. Caso o dia especificado seja inválido para o mês seguinte
     * (ex: 31 de abril), a função automaticamente ajusta para o último dia válido
     * desse mês.
     * 
     * @param day O dia do mês desejado (1-31). Se for maior que os dias do mês,
     *            será ajustado para o último dia do mês.
     * @return Um objeto java.util.Date representando a data calculada no próximo mês,
     *         com o dia ajustado conforme necessário.
     */
    public static Date getDateNextMonth(int day) {
        // Obter a data atual
        LocalDate today = LocalDate.now();
        
        // Adicionar 1 mês
        LocalDate nextMonth = today.plusMonths(1);
        
        // Ajustar o dia, usando o menor valor entre o dia solicitado e o último dia do mês
        nextMonth = nextMonth.withDayOfMonth(Math.min(day, nextMonth.lengthOfMonth()));
        
        // Converter LocalDate para Date
        return Date.from(nextMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    
    /**
     * Verifica se:
     * 1. A data de comparação está entre as datas de início e fim (inclusive), OU
     * 2. A data de comparação é maior que dataFim E qtyCobranca é igual a 11
     *
     * @param dataInicio Data de início do período (inclusive)
     * @param dataFim Data de fim do período (inclusive)
     * @param dataComparacao Data a ser comparada
     * @param qtyCobranca Quantidade de cobrança
     * @return true se atender a uma das condições acima
     */
    public static boolean verificarDataECobranca(LocalDate dataInicio, 
                                               LocalDate dataFim, 
                                               LocalDate dataComparacao, 
                                               Long qtyCobranca) {
        
        // Verifica se a data está dentro do intervalo (primeira condição)
        boolean dentroDoIntervalo = !dataComparacao.isBefore(dataInicio) && 
                                  !dataComparacao.isAfter(dataFim);
        
        // Verifica a segunda condição (data após dataFim E qtyCobranca == 11)
        boolean depoisComCobranca = dataComparacao.isAfter(dataFim) && 
                                   qtyCobranca != null && 
                                   qtyCobranca == 11L;
        
        return dentroDoIntervalo || depoisComCobranca;
    }
    
    
    
    /**
     * Verifica se:
     * 1. A data de comparação está entre as datas de início e fim (inclusive)
     *
     * @param dataInicio Data de início do período (inclusive)
     * @param dataFim Data de fim do período (inclusive)
     * @param dataComparacao Data a ser comparada
     * @return true se atender a uma das condições acima
     */
    public static boolean verificarDataCobrancaMensal(LocalDate dataInicio, 
                                                      LocalDate dataFim, 
                                                      LocalDate dataComparacao) {
        
        // Verifica se a data está dentro do intervalo (primeira condição)
        boolean dentroDoIntervalo = !dataComparacao.isBefore(dataInicio) && 
                                  !dataComparacao.isAfter(dataFim);

        return dentroDoIntervalo;
    }

}
