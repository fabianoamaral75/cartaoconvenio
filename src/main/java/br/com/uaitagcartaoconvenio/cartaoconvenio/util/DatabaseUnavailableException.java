package br.com.uaitagcartaoconvenio.cartaoconvenio.util;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando ocorrem problemas de conexão com o banco de dados.
 */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE) // Retorna HTTP 503 (Service Unavailable)
public class DatabaseUnavailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public DatabaseUnavailableException(String message) {
        super(message);
    }

    public DatabaseUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}