package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusWorkflowInformativo {
	
	ATIVA    ("Workflow Informativo Satatus Ativa!"     ),
	BLOQUEIO ("Workflow Informativo Satatus Bloqueada!" ),
	CANCELADO("Workflow Informativo Satatus Cancelado!" );	
	
	private String descStatusWorkflow;
	
	private StatusWorkflowInformativo(String desc) {
		this.descStatusWorkflow = desc;
	}
	
	public String getDescStatusEmtidade() {
		return descStatusWorkflow;
	}
	
	@Override
	public String toString() {
		return this.descStatusWorkflow;
	}

}
