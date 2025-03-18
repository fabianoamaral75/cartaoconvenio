package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusTaxaEntidade {

	DESATUALIZADA       ("Taxa descontinuada!"                             ),
	ATUAL               ("Taxa Atual"                      ),
	BLOQUEADA           ("Taxa Bloqueada!"                                 ),
	AGUARDANDO_APROVACAO("Aguardando aprovação da Taxa pela Administração!");

	
	private String descStatusTaxaEntidade;
	
	private StatusTaxaEntidade( String desc ) {
		this.descStatusTaxaEntidade = desc;
	}
	
	public String getDescStatusTaxaEntidade() {
		return descStatusTaxaEntidade;
	}
}
