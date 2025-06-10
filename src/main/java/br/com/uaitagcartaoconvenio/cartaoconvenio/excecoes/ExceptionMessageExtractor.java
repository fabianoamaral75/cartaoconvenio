package br.com.uaitagcartaoconvenio.cartaoconvenio.excecoes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.converter.HttpMessageNotReadableException;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;

public class ExceptionMessageExtractor {

    public static String extractDetailedErrorMessage(Throwable ex) {
        if (ex instanceof InvalidDefinitionException) {
            return extractInvalidDefinitionError((InvalidDefinitionException) ex);
        } else if (ex instanceof MismatchedInputException) {
            return extractMismatchedInputError((MismatchedInputException) ex);
        } else if (ex instanceof HttpMessageNotReadableException) {
            return extractHttpMessageNotReadableError((HttpMessageNotReadableException) ex);
        } else if (ex instanceof ExceptionCustomizada) {
            return ex.getMessage();
        } else {
            return "Erro no processamento da requisição: " + ex.getMessage();
        }
    }

    private static String extractInvalidDefinitionError(InvalidDefinitionException ex) {
        // Tratamento específico para "Invalid Object Id definition"
        if (ex.getMessage().contains("Invalid Object Id definition")) {
            Pattern pattern = Pattern.compile(
                "Invalid Object Id definition for `(.+?)`: cannot find property with name '(.+?)'"
            );
            
            Matcher matcher = pattern.matcher(ex.getMessage());
            if (matcher.find()) {
                String className = matcher.group(1);
                String propertyName = matcher.group(2);
                return String.format(
                    "Propriedade '%s' não encontrada na classe %s - verifique se o campo existe no JSON enviado",
                    propertyName, className
                );
            }
        }
        
        // Fallback para outros tipos de InvalidDefinitionException
        return "Erro no formato dos dados: " + ex.getOriginalMessage();
    }

    private static String extractMismatchedInputError(MismatchedInputException ex) {
        try {
            if (!ex.getPath().isEmpty()) {
                String fieldName = ex.getPath().get(0).getFieldName();
                return String.format(
                    "Tipo de dado inválido para o campo '%s' - valor recebido não é compatível com o tipo esperado",
                    fieldName
                );
            }
        } catch (Exception e) {
            // Ignora erros na extração
        }
        return "Tipo de dado inválido em um dos campos do JSON";
    }

    private static String extractHttpMessageNotReadableError(HttpMessageNotReadableException ex) {
        if (ex.getCause() != null) {
            String causeMessage = extractDetailedErrorMessage(ex.getCause());
            if (!causeMessage.startsWith("Erro no processamento da requisição")) {
                return causeMessage;
            }
        }
        return "Requisição malformada ou corpo da requisição inválido";
    }
}