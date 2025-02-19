package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;



public enum StatusVendas {
	
	ABERTA                ("Venda esta em abeto!"                   ),
	APROVADA              ("Venda realizada com sucesso"            ),
	CANCELADA             ("Venda Cancelada!"                       ),
	REJEITADA             ("Venda Rejeitada por Saldo insuficiente!"),
	AGUARDANDO_PAGAMENTO  ("Venda aguardando pagamento"             ), 
	PAGAMENTO_NAO_APROVADO("Pagamento não aprovado"                 ), 
	PAGAMENTO_AUTORIZADO  ("Pagamento autorizado"                   ), 
	PAGAMENTO_APROVADO    ("Pagamento aprovado"                     ),
	BLOQUEADA             ("Venda bloqueada"                        );
	
	private String descStatusVendas;

	StatusVendas(String desc) {
		this.descStatusVendas = desc;
	}

	public String getDescStatusVendas() {
		return descStatusVendas;
	}
	
	@Override
	public String toString() {
		return this.descStatusVendas;
	}

}
