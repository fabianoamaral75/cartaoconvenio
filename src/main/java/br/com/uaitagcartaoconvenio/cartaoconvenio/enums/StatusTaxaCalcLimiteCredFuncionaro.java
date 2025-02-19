package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusTaxaCalcLimiteCredFuncionaro {
	
	
	CANCELA             ("Taxa cancela!"                                                                ),
	ATUAL               ("Taxa de percentual de base de cáucula para analise de crédito do funcionário!"),
	DESATUALIZADA       ("Taxa desatualizada!"                                                          ),
	AGUARDANDO_APROVACAO("Taxa sob analise, aguradando aprovação!"                                      );

	
	private String descTaxaCalcLimiteCredFuncionaro;
	
	private StatusTaxaCalcLimiteCredFuncionaro( String desc ) {
		this.descTaxaCalcLimiteCredFuncionaro = desc;
	}
	
	public String getDescTaxaCalcLimiteCredFuncionaro() {
		return descTaxaCalcLimiteCredFuncionaro;
	}
	
	@Override
	public String toString() {
		return this.descTaxaCalcLimiteCredFuncionaro;
	}

}
