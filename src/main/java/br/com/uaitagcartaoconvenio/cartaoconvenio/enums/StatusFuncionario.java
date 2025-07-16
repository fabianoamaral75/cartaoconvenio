package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusFuncionario {
	
	ATIVO("Funcionário ativo no sistema"),
    INATIVO("Funcionário inativo no sistema"),
    FERIAS("Funcionário em período de férias"),
    LICENCA("Funcionário em licença"),
    DESLIGADO("Funcionário desligado");
	
	private String descStatusFuncionario;
	
	private StatusFuncionario(String desc) {
		this.descStatusFuncionario = desc;
	}

	public String getdescStatusTipoFuncionario() {
		return descStatusFuncionario;
	}
	
	@Override
	public String toString() {
		return this.descStatusFuncionario;
	}

}
