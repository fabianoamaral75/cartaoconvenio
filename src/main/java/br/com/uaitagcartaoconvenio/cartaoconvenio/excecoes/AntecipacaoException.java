package br.com.uaitagcartaoconvenio.cartaoconvenio.excecoes;

public class AntecipacaoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public AntecipacaoException(String message) {
        super(message);
    }
    
    public AntecipacaoException(String message, Throwable cause) {
        super(message, cause);
    }
}

