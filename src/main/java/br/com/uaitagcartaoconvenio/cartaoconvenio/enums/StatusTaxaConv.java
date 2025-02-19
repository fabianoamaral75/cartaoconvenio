package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusTaxaConv {

	DESATUALIZADA       ("Taxa descontinuada!"                             ),
	ATUAL               ("Venda realizada com sucesso"                      ),
	BLOQUEADA           ("Taxa Bloqueada!"                                 ),
	AGUARDANDO_APROVACAO("Aguardando aprovação da Taxa pela Administração!");
	
	private String descStatusTaxaCon;
	
	private StatusTaxaConv(String desc) {
		this.descStatusTaxaCon = desc;
	}
	
	public String getDescStatusTaxaCon() {
		return descStatusTaxaCon;
	}
	
	@Override
	public String toString() {
		return this.descStatusTaxaCon;
	}

}
