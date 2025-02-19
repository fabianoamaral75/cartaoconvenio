package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusVendaReceb {

	VENDA_RECEBIDA          ("Venda Recebida da Entidade!"                        ),
	AGURARDANDO_RECEBIMENTO ("Aguardando fechamento para recebimento da Entidade!"),
	AGURARDANDO_CANCELADA   ("Venda cancelada, não irá gerar pagamento para entidade!"),
	RECEBIMENTO_REJEITADO   ("Recebimento Rejeitado pela Entidade!"               );
	
	private String descStatusVendaReceb;
	
	private StatusVendaReceb( String desc ) {
		this.descStatusVendaReceb = desc;
	}
	
	public String getDescStatusVendaReceb() {
		return descStatusVendaReceb;
	}
	
	@Override
	public String toString() {
		return this.descStatusVendaReceb;
	}

}
