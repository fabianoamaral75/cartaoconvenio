package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum RoleAcesso {
	
	ADMIN                   ("Acesso total ao sistema, acesso de ADIM!"                                         ),
	USER                    ("Acesso de leitura e relatório  ao sistema, acesso de ADIM!"                       ),
	ADMIN_FINANCEIRO        ("Acesso de Relatório as informações financeira do sistema!"                        ),
	USER_FINANCEIRO         ("Acesso total de geração de fechamento de ciclo e relatório financeiro do sistema!"),
	ADMIN_ENTIDADE          ("Acesso total de cadastro e relatório da visão de Entidade!"                       ),
	USER_ENTIDADE           ("Acesso com a visão de relatório para a Entidade!"                                 ),
	ADMIN_CONVENIADA        ("Acesso com a visão total para a empresa conveniada!"                              ),
	USER_CONVENIADA         ("Acesso com a visão vendedor e relatório!"                                         ),
	USER_CONVENIADA_VENDEDOR("Acesso com permissão apenas de vendedor!"                                         );

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
