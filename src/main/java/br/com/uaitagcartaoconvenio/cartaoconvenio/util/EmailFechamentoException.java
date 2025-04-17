package br.com.uaitagcartaoconvenio.cartaoconvenio.util;

public class EmailFechamentoException extends Exception {
    private static final long serialVersionUID = 1L;
	private final String tipoErro;
    private final String detalhes;

    public EmailFechamentoException(String tipoErro, String detalhes, Throwable causa) {
        super(String.format("Erro no envio de e-mail de fechamento [%s]: %s", tipoErro, detalhes), causa);
        this.tipoErro = tipoErro;
        this.detalhes = detalhes;
    }

    // Getters
    public String getTipoErro() { return tipoErro; }
    public String getDetalhes() { return detalhes; }
}