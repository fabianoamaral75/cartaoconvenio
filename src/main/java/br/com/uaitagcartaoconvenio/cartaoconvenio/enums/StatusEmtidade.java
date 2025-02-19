package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusEmtidade {

	ATIVA                  ("Entidade Ativa!"                          ),
	AGUARDANDO_CONFIRMACAO ("Aguardando Aprovação para Ativação!"      ),
	BLOQUEIO               ("Entidade Bloqueada!"                      ),
	BLOQUEIO_PAGAMENTO     ("Entidade Bloqueada por falta de pabamento"), 
	DESATIVADA             ("Entidade Desativada!"                     ),
	DESATIVADA_FIM_CONTRATO("Entidade Desativada POR FIM DE CONTRATO!" );	
	
	private String descStatusEmtidade;
	
	private StatusEmtidade(String desc) {
		this.descStatusEmtidade = desc;
	}
	
	public String getDescStatusEmtidade() {
		return descStatusEmtidade;
	}
	
	@Override
	public String toString() {
		return this.descStatusEmtidade;
	}

}
