package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum TipoAntecipacao {
	MES_CORRENTE    ("Workflow Informativo Satatus Ativa!"     ),
	CICLO_ANTERIOR ("Workflow Informativo Satatus Bloqueada!" );	
	
	private String descTipoAntecipacao;
	
	private TipoAntecipacao(String desc) {
		this.descTipoAntecipacao = desc;
	}
	
	public String getTipoAntecipacao() {
		return descTipoAntecipacao;
	}
	
	@Override
	public String toString() {
		return this.descTipoAntecipacao;
	}

}
