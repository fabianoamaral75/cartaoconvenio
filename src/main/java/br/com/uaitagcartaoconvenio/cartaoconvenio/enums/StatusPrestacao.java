package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusPrestacao {
	
	PENDENTE   ( "Prestação Pendente"   ),
	PAGA       ( "Prestação Paga"       ),
	ATRASADA   ( "Prestação Atrasada"   ),
	CANCELADA  ( "Prestação Cancelada"  ),
	EM_COBRANCA( "Prestação Em Cobrança") ;	
	
	private String descStatusPrestacao;
	
	private StatusPrestacao(String desc) {
		this.descStatusPrestacao = desc;
	}
	
	public String getDescStatusPrestacao() {
		return descStatusPrestacao;
	}
	
	@Override
	public String toString() {
		return this.descStatusPrestacao;
	}

}
