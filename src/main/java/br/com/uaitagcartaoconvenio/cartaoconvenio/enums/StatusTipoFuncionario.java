package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusTipoFuncionario {
	
	EFETIVO      ("Funcionários admitidos por concurso público."                               ),
	TEMPORARIO   ("Contratados por tempo determinado, geralmente para atender necessidades temporárias da administração pública."                               ),
	COMISSIONADO ("Ocupam cargos de direção, chefia ou assessoramento, nomeados e exonerados livremente pela administração."                 ),
	ESTAGIARIO   ("Alunos de instituições de ensino que realizam atividades práticas na prefeitura, com o objetivo de complementar a formação. " );

	private String descStatusTipoFuncionario;
	
	private StatusTipoFuncionario(String desc) {
		this.descStatusTipoFuncionario = desc;
	}

	public String getdescStatusTipoFuncionario() {
		return descStatusTipoFuncionario;
	}
	
	@Override
	public String toString() {
		return this.descStatusTipoFuncionario;
	}

}
