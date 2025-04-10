package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusRestabeleceLimiteCredito {

	AGUARDANDO_PAGAMENTO("Venda realizada, aguardando funcionário realizar o pagamento!"), 
	VENDA_REALIZADA     ("Pagamento confirmado pelo funcioário!"                        ), 
	LIMITE_RESTABELECIDO("O limite referente a esta venda, foi restabelecido"           ), 
	VENDA_CANCELADA     ("Venda cancelada e valor, caso valor tenha sido restabelecido, voltar a ser incrementado novamente no limite do funcionário!");	
	
	private String descRestLimiteCredito;

	private StatusRestabeleceLimiteCredito( String desc ) {
		this.descRestLimiteCredito = desc;
	}
	
    public String getDescStatusReceber() {
		return descRestLimiteCredito;
	}
    
	@Override
	public String toString() {
		return this.descRestLimiteCredito;
	}

}
