package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusAntecipacao {
	
	APROVADO            ("Antecipação aprovada e realizada!"                       ),
	AGUARDANDO_APROVACAO("Antecipação aguardando aprovação da Conveniada!"         ),
	REPROVADO           ("Antecipação reprovada pela Conveniada!"                  ),
	CANCELADO           ("Antecipação cancelada!"                                  ),
	EXPIRADO            ("Antecipação expirada por falta de retorno da Conveniada!");	

	private String descStatusAntecipacao;
	
	private StatusAntecipacao( String desc ) {
		this.descStatusAntecipacao = desc;
	}
	
	public String getDescStatusCartao() {
		return descStatusAntecipacao;
	}
	
	@Override
	public String toString() {
		return this.descStatusAntecipacao;
	}

}
