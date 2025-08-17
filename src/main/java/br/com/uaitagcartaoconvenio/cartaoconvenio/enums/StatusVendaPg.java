package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusVendaPg {
	
	VENDA_PAGA            ("Pagamento realizado para o Conveniado"                               ),
	AGUARDANDO_PAGAMENTO  ("Aguardando o pagamento ao Conveniado!"                               ),
	AGUARDANDO_FECHAMENTO ("Aguardando fechamento para pagamento ao Conveniado!"                 ),
	FECHAMENTO_CONCLUIDO  ("Fechamento concluido com sucesso, pagamento realizado a Conveniada!" ),
	VENDA_CANCELADA       ("Avenda foi cancelada, não irá gerar pagamento para a Conveniada!"    ),
	PAGAMENTO_REJEITADO   ("Apagamento rejeitado pela Administração!"                            ),
	PRE_ANTECIPACAO       ( "Aguardando Fechamento de uma Venda com Pré-Antecipação!"            ),
	ANTECIPACAO_CONCLUIDA ( "Fechamento realizado para uma Venda por Antecipação!"               );

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
