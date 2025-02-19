package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusCartao {
	
	ATIVO    ("Cartão Ativo!"    ),
	BLOQUEADA("Cartão Bloqueado!"),
	VENCIDO  ("Cartão Vencido!"  ),
	CANCELADO("Cartão Cancelado!");	

	private String descStatusCartao;
	
	private StatusCartao( String desc ) {
		this.descStatusCartao = desc;
	}
	
	public String getDescStatusCartao() {
		return descStatusCartao;
	}
	
	@Override
	public String toString() {
		return this.descStatusCartao;
	}

}
