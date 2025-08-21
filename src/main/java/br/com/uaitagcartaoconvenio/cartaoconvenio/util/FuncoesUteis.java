package br.com.uaitagcartaoconvenio.cartaoconvenio.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
    

  
    /**
     * Calcula o valor correspondente a uma porcentagem de um valor em real.
     *
     * @param valorReal O valor base em reais (BigDecimal).
     * @param porcentagem A porcentagem a ser calculada (BigDecimal). Ex: 10.0 para 10%.
     * @return O valor da porcentagem aplicada ao valorReal, com escala de 2 casas decimais.
     * @throws IllegalArgumentException Se valorReal ou porcentagem forem nulos.
     */
    public static BigDecimal calcularPorcentagem(BigDecimal valorReal, BigDecimal porcentagem) {
        // Validação dos parâmetros para evitar NullPointerException
        if (valorReal == null || porcentagem == null) {
            throw new IllegalArgumentException("Os parâmetros não podem ser nulos");
        }

        // Converte a porcentagem para sua forma decimal (ex: 10% -> 0.10)
        // Usamos 10 casas decimais e arredondamento HALF_UP para garantir precisão
        BigDecimal porcentagemDecimal = porcentagem.divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);

        // Calcula o valor da porcentagem (valorReal * porcentagemDecimal)
        BigDecimal resultado = valorReal.multiply(porcentagemDecimal);

        // Ajusta o resultado para 2 casas decimais (formato monetário padrão)
        resultado = resultado.setScale(2, RoundingMode.HALF_UP);

        return resultado;
    }
/*
    public static void main(String[] args) {
        // Exemplo de uso da função
        BigDecimal valor = new BigDecimal("1500.00");
        BigDecimal porcentagem = new BigDecimal("10.0"); // 10%

        BigDecimal resultado = calcularPorcentagem(valor, porcentagem);
        System.out.println("10% de R$" + valor + " = R$" + resultado);
    }
*/ 
    

    /**
     * Realiza o cálculo que soma os dois últimos valores e subtrai do primeiro valor,
     * garantindo que o resultado nunca seja negativo.
     * 
     * @param primeiroValor O valor base que será subtraído (BigDecimal)
     * @param segundoValor O primeiro valor a ser somado (BigDecimal)
     * @param terceiroValor O segundo valor a ser somado (BigDecimal)
     * @return Resultado da operação (primeiroValor - (segundoValor + terceiroValor)),
     *         ou ZERO caso o resultado seja negativo
     * @throws IllegalArgumentException Se qualquer parâmetro for nulo
     */
    public static BigDecimal calcularSubtracaoLimitada(BigDecimal primeiroValor, 
                                                     BigDecimal segundoValor, 
                                                     BigDecimal terceiroValor) {
        
        // Validação dos parâmetros de entrada
        if (primeiroValor == null || segundoValor == null || terceiroValor == null) {
            throw new IllegalArgumentException("Nenhum dos parâmetros pode ser nulo");
        }

        // Soma os dois últimos valores (segundoValor + terceiroValor)
        BigDecimal somaValores = segundoValor.add(terceiroValor);

        // Subtrai a soma do primeiro valor (primeiroValor - somaValores)
        BigDecimal resultado = primeiroValor.subtract(somaValores);

        // Verifica se o resultado é negativo
        if (resultado.compareTo(BigDecimal.ZERO) < 0) {
            // Retorna 0 se o resultado for negativo
            return BigDecimal.ZERO;
        }

        // Retorna o resultado da operação se for positivo ou zero
        return resultado;
    }
    

    /**
     * Realiza o cálculo que soma os três últimos valores e subtrai do primeiro valor.
     * 
     * @param primeiroValor O valor base que será subtraído (BigDecimal)
     * @param segundoValor O primeiro valor a ser somado (BigDecimal)
     * @param terceiroValor O segundo valor a ser somado (BigDecimal)
     * @param quartoValor O terceiro valor a ser somado (BigDecimal)
     * @return Resultado da operação (primeiroValor - (segundoValor + terceiroValor + quartoValor))
     *         Pode retornar valor negativo, positivo ou zero
     * @throws IllegalArgumentException Se qualquer parâmetro for nulo
     */
    public static BigDecimal calcularSubtracao(BigDecimal primeiroValor , 
                                               BigDecimal segundoValor  , 
                                               BigDecimal terceiroValor ,
                                               BigDecimal quartoValor   ) {
        
        // Validação dos parâmetros de entrada
        if (primeiroValor == null || segundoValor == null || 
            terceiroValor == null || quartoValor == null) {
            throw new IllegalArgumentException("Nenhum dos parâmetros pode ser nulo");
        }

        // Soma os três últimos valores (segundoValor + terceiroValor + quartoValor)
        BigDecimal somaValores = segundoValor.add(terceiroValor).add(quartoValor);

        // Subtrai a soma do primeiro valor (primeiroValor - somaValores)
        return primeiroValor.subtract(somaValores);
    }    
    
    
    /**
     * Converte uma data no formato yyyy-MM-dd (LocalDate) para uma String no formato yyyymm.
     * 
     * @param data - Objeto LocalDate contendo a data a ser formatada.
     * @return String - Data formatada no padrão yyyymm (ano e mês, sem separadores).
     */
    public static String formataAnoMes(LocalDate data) {
        // Extrai o ano e formata com 4 dígitos
        int ano = data.getYear();
        
        // Extrai o mês e formata com 2 dígitos (1-12)
        int mes = data.getMonthValue();
        
        // Formata a String no padrão yyyymm (concatena ano e mês)
        // Usa String.format para garantir que o mês tenha 2 dígitos (ex: 01, 02, ..., 12)
        String anoMesFormatado = String.format("%04d%02d", ano, mes);
        
        return anoMesFormatado;
    }  
    
    /**
     * Retorna o primeiro dia do mês no formato "dd/MM/yyyy"
     * @param yyyymm String no formato "YYYYMM" (ex: "202408")
     * @return String no formato "dd/MM/yyyy" (ex: "01/08/2024")
     * @throws IllegalArgumentException se o parâmetro for inválido
     */
    public static String getPrimeiroDiaMes(String yyyymm) {
        // Validação básica do parâmetro
        if (yyyymm == null || yyyymm.length() != 6) {
            throw new IllegalArgumentException("Parâmetro deve ter 6 dígitos no formato YYYYMM");
        }
        
        try {
            // Extrair ano e mês da string
            int ano = Integer.parseInt(yyyymm.substring(0, 4));
            int mes = Integer.parseInt(yyyymm.substring(4, 6));
            
            // Validar se o mês está entre 1 e 12
            if (mes < 1 || mes > 12) {
                throw new IllegalArgumentException("Mês deve estar entre 01 e 12");
            }
            
            // Criar data do primeiro dia do mês
            LocalDate primeiroDia = LocalDate.of(ano, mes, 1);
            
            // Formatar para o padrão desejado
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            return primeiroDia.format(formatter);
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Parâmetro deve conter apenas números", e);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data inválida", e);
        }
    }

    /**
     * Retorna o último dia do mês no formato "dd/MM/yyyy"
     * @param yyyymm String no formato "YYYYMM" (ex: "202408")
     * @return String no formato "dd/MM/yyyy" (ex: "31/08/2024")
     * @throws IllegalArgumentException se o parâmetro for inválido
     */
    public static String getUltimoDiaMes(String yyyymm) {
        // Validação básica do parâmetro
        if (yyyymm == null || yyyymm.length() != 6) {
            throw new IllegalArgumentException("Parâmetro deve ter 6 dígitos no formato YYYYMM");
        }
        
        try {
            // Extrair ano e mês da string
            int ano = Integer.parseInt(yyyymm.substring(0, 4));
            int mes = Integer.parseInt(yyyymm.substring(4, 6));
            
            // Validar se o mês está entre 1 e 12
            if (mes < 1 || mes > 12) {
                throw new IllegalArgumentException("Mês deve estar entre 01 e 12");
            }
            
            // Criar data do primeiro dia do mês e ir para o último dia
            LocalDate primeiroDia = LocalDate.of(ano, mes, 1);
            LocalDate ultimoDia = primeiroDia.withDayOfMonth(primeiroDia.lengthOfMonth());
            
            // Formatar para o padrão desejado
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            return ultimoDia.format(formatter);
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Parâmetro deve conter apenas números", e);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data inválida", e);
        }
    }
    
    /**
     * Formata um BigDecimal como percentual no formato brasileiro
     * @param valor BigDecimal com o valor (ex: 5.00)
     * @return String formatada (ex: "5,00 %")
     */
    public static String formatarPercentual(BigDecimal valor) {
        if (valor == null) {
            return "0,00 %";
        }
        
        // CORREÇÃO: Usando Locale.of() em vez do construtor depreciado
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.of("pt", "BR"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        
        DecimalFormat percentFormat = new DecimalFormat("#,##0.00", symbols);
        
        return percentFormat.format(valor) + " %";
    }
       
/*
    public static void main(String[] args) {
        // Exemplo 1: Resultado positivo
        BigDecimal valor1 = new BigDecimal("100.50");
        BigDecimal valor2 = new BigDecimal("30.25");
        BigDecimal valor3 = new BigDecimal("20.25");
        BigDecimal resultado1 = calcularSubtracaoLimitada(valor1, valor2, valor3);
        System.out.println("Resultado 1: " + resultado1);  // Deve imprimir 50.00

        // Exemplo 2: Resultado negativo (retorna 0)
        BigDecimal valor4 = new BigDecimal("50.00");
        BigDecimal valor5 = new BigDecimal("30.00");
        BigDecimal valor6 = new BigDecimal("40.00");
        BigDecimal resultado2 = calcularSubtracaoLimitada(valor4, valor5, valor6);
        System.out.println("Resultado 2: " + resultado2);  // Deve imprimir 0

        // Exemplo 3: Valores decimais
        BigDecimal valor7 = new BigDecimal("75.30");
        BigDecimal valor8 = new BigDecimal("25.10");
        BigDecimal valor9 = new BigDecimal("25.20");
        BigDecimal resultado3 = calcularSubtracaoLimitada(valor7, valor8, valor9);
        System.out.println("Resultado 3: " + resultado3);  // Deve imprimir 25.00
    }
*/    

}
