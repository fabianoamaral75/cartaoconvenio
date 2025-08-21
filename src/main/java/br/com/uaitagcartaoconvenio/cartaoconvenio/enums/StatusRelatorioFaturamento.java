package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusRelatorioFaturamento {
	ATUAL            ("Relatório Atual"        ), 
	REL_DESATUALIZADO("Relatório Desatualizado"), 
	PROCESSANDO      ("Relatório Processado"   ), 
	ERRO             ("Erro ao gerar Relatório");	
	
	private String descStatusRelatorioFaturamento;

	private StatusRelatorioFaturamento( String desc ) {
		this.descStatusRelatorioFaturamento = desc;
	}
	
    public String getStatusRelatorioFaturamento() {
		return descStatusRelatorioFaturamento;
	}
    
	@Override
	public String toString() {
		return this.descStatusRelatorioFaturamento;
	}
}
