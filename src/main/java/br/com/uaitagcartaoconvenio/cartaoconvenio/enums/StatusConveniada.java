package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusConveniada {
	
	ATIVA                  ("Conveniada Ativa!"                          ),
	AGUARDANDO_CONFIRMACAO ("Conveniada Aguardando Aprovação para Ativação!"      ),
	BLOQUEIO               ("Conveniada Bloqueada!"                      ),
	BLOQUEIO_PAGAMENTO     ("Conveniada Bloqueada por falta de pabamento"), 
	DESATIVADA             ("Conveniada Desativada!"                     ),
	DESATIVADA_FIM_CONTRATO("Conveniada Desativada POR FIM DE CONTRATO!" );	
	
	private String descStatusConveniada;
	
	private StatusConveniada(String desc) {
		this.descStatusConveniada = desc;
	}
	
	public String getDescStatusEmtidade() {
		return descStatusConveniada;
	}
	
	@Override
	public String toString() {
		return this.descStatusConveniada;
	}

}
