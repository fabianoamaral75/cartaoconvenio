package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;



public enum StatusVendas {
	
	ABERTA                      ( "Venda esta em abeto!"                                    ),
	APROVADA                    ( "Venda realizada com sucesso!"                            ),
	CANCELADA                   ( "Venda Cancelada!!"                                       ),
	REJEITADA                   ( "Venda Rejeitada por Saldo insuficiente!!"                ),
	AGUARDANDO_PAGAMENTO        ( "Venda aguardando pagamento!"                             ), 
	PAGAMENTO_NAO_APROVADO      ( "Pagamento não aprovado!"                                 ), 
	PAGAMENTO_AUTORIZADO        ( "Pagamento autorizado!"                                   ), 
	PAGAMENTO_APROVADO          ( "Pagamento aprovado!"                                     ),
	BLOQUEADA                   ( "Venda bloqueada!"                                        ),
	FECHAMENTO_CONCLUIDO        ( "Fechamento concluído para a Venda!"                      ),
	FECHAMENTO_CANCELADO        ( "Fechamento cancelado para a Venda!"                      ),
	AGUARDANDO_FECHAMENTO       ( "Aguardando Fechamento para a Venda!"                     ),
	FECHAMENTO_MANUAL           ( "Fechamento Manual para a Venda!"                         ),
	FECHAMENTO_CANC_INDIVIDUAL  ( "Fechamento cancelado Manualmente!"                       ),
	FECHAMENTO_MANUAL_CONCLUIDO ( "Fechamento Manualmente concluído para a Venda!"          ),
	AGUARDANDO_FECHAMENTO_MANUAL( "Aguardando Fechamento Manualmente para a Venda!"         ),
	PRE_ANTECIPACAO             ( "Aguardando Fechamento de uma Venda com Pré-Antecipação!" ),
	ANTECIPACAO_CONCLUIDA       ( "Fechamento realizado para uma Venda por Antecipação!"    );
	
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
