package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusReceber {
	
	AGUARDANDO_RECEBIMENTO("Aguardando recebimento da Entidade!"), 
	RECEBIDO              ("Pagamento Recebido!"                ), 
	RECEBIMENTO_BLOQUEADO ("Pagamento Bloqueado pela Entididade"), 
	RECEBIMENTO_REJEITADO ("Pagamento Rejeitado pela Entididade"),
	RECEBIMENTO_CANCELADO ("Pagamento Cancelado!"               ),
	RECEBIMENTO_APROVADO  ("Pagamento Aprovado pela Entididade" );	
	
	private String descStatusReceber;

	private StatusReceber( String desc ) {
		this.descStatusReceber = desc;
	}
	
    public String getDescStatusReceber() {
		return descStatusReceber;
	}
    
	@Override
	public String toString() {
		return this.descStatusReceber;
	}

}
