package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusVendaPg {
	
	VENDA_PAGA            ("Pagamento realizado para o Conveniado"                           ),
	AGURARDANDO_PAGAMENTO ("Aguardando fechamento para pagamento ao Conveniado!"             ),
	VENDA_CANCELADA       ("Avenda foi cancelada, não irá gerar pagamento para a Conveniada!"),
	PAGAMENTO_REJEITADO   ("Apagamento rejeitado pela Administração!"                        );

	private String descStatusVendaPg;
	
	private StatusVendaPg(String desc) {
		this.descStatusVendaPg = desc;
	}

	public String getDescStatusVendaPg() {
		return descStatusVendaPg;
	}
	
	@Override
	public String toString() {
		return this.descStatusVendaPg;
	}

}
