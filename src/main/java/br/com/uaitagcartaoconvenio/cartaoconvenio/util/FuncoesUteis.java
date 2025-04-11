package br.com.uaitagcartaoconvenio.cartaoconvenio.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;
import java.time.YearMonth;

public class FuncoesUteis {

	
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

    
}
