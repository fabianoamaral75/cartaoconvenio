package br.com.uaitagcartaoconvenio.cartaoconvenio.excecoes;


public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public BusinessException(String message) {
        super(message);
    }
}