package br.com.uaitagcartaoconvenio.cartaoconvenio;

public class ExceptionCustomizada extends Exception{
	private static final long serialVersionUID = 1L;

	public ExceptionCustomizada(String msgErro) {
		super(msgErro);
	}

}
