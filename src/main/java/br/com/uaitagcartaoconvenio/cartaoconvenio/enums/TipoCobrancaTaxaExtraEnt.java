package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum TipoCobrancaTaxaExtraEnt {
	
	CICLO   ("Cobrança da taxa em todo fechamento de ciclo"  ),
	UNICA   ("Cobrança unica, será cobrado somente uma vez." ),
	PERIODO ("Cobrança por um período determinado!"          );	
	
	private String descTipoTaxa;
	
	private TipoCobrancaTaxaExtraEnt(String desc) {
		this.descTipoTaxa = desc;
	}
	
	public String getDescTipoTaxa() {
		return descTipoTaxa;
	}
	
	@Override
	public String toString() {
		return this.descTipoTaxa;
	}

}
