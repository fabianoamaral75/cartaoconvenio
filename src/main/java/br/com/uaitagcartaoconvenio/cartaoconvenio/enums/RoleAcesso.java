package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum RoleAcesso {
	
	ADMIN              ("Acesso total ao sistema, acesso de ADIM!"                                         ),
	FINANCEIRO         ("Acesso de Relatório as informações financeira do sistema!"                        ),
	FINANCEIRO_ADMI    ("Acesso total de geração de fechamento de ciclo e relatório financeiro do sistema!"),
	ENTIDADE_ADMI      ("Acesso total de cadastro e relatório da visão de Entidade!"                       ),	
	ENTIDADE           ("Acesso com a visão de relatório para a Entidade!"                                 ),
	CONVENIADA_ADMI    ("Acesso com a visão total para a empresa conveniada!"                              ),
	CONVENIADA_VENDEDOR("Acesso com permissão de venda para a empresa conveniada!"                         );

	private String descRoleAcesso;
	
	private RoleAcesso( String desc ) {
		this.descRoleAcesso = desc;
	}
	
	public String getDescStatusCartao() {
		return descRoleAcesso;
	}
	
	@Override
	public String toString() {
		return this.descRoleAcesso;
	}

}
