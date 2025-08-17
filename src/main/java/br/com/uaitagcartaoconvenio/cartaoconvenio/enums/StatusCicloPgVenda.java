package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusCicloPgVenda {
	
	AGUARDANDO_PAGAMENTO("Aguardando o Pagamento para a conveniada!"), 
	PAGAMENTO           ("Pagamento Realizado!"                     ), 
	PAGAMENTO_BLOQUEADO ("Pagamento Bloqueado!"                     ), 
	PAGAMENTO_REJEITADO ("Pagamento Rejeitado!"                     ),
	PAGAMENTO_CANCELADO ("Pagamento Cancelado!"                     ),
	PAGAMENTO_APROVADO  ("Pagamento Aprovado!"                      ),
	AGUARDANDO_UPLOAD_NF("Aguardando Upload NF da Conveniada!"      ),
	PAGAMENTO_ANTECIPADO("Pagamento feito por Antecipação!"         );	
	
	private String descStatusPagamento;

	private StatusCicloPgVenda( String desc ) {
		this.descStatusPagamento = desc;
	}
	
    public String getDescStatusReceber() {
		return descStatusPagamento;
	}
    
	@Override
	public String toString() {
		return this.descStatusPagamento;
	}

}
