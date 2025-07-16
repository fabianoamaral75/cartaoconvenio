package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusContrato {
	
	PRELIMINAR ("Indica que o contrato ainda está em fase de elaboração ou que é uma versão preliminar."),
	PENDENTE   ("Significa que o contrato não pode ser ativado ou executado devido a pendências, como falta de informações, documentos ou pagamentos. "),
	VIGENTE    ("Indica que o contrato está ativo e válido, com as partes obrigadas a cumprir seus termos."),
	NAO_VIGENTE("O contrato não está mais em vigor, seja por término do prazo, rescisão ou outro motivo."),
	CANCELADO  ("O contrato foi cancelado por alguma razão, geralmente antes de entrar em vigor ou durante sua execução."),
	ARQUIVADO  ("O contrato foi encerrado e armazenado, geralmente após sua execução total ou cancelamento.");	

	private String descStatusContrato;
	
	private StatusContrato( String desc ) {
		this.descStatusContrato = desc;
	}
	
	public String getDescStatusCartao() {
		return descStatusContrato;
	}
	
	@Override
	public String toString() {
		return this.descStatusContrato;
	}

}
