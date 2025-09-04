package br.com.uaitagcartaoconvenio.cartaoconvenio.enums;

public enum StatusEmprestimo {
	
	SOLICITADO  ("Emprestimo Solicitado"  ),
	APROVADO    ("Emprestimo Aprovado"    ),
	REPROVADO   ("Emprestimo Reprovado"   ),
	EM_ANDAMENTO("Emprestimo Em Andamento"), 
	QUITADO     ("Emprestimo Quitado"     ),
	CANCELADO   ("Emprestimo Cancelado"   );	
	
	private String descStatusEmprestimo;
	
	private StatusEmprestimo(String desc) {
		this.descStatusEmprestimo = desc;
	}
	
	public String getDescStatusEmprestimo() {
		return descStatusEmprestimo;
	}
	
	@Override
	public String toString() {
		return this.descStatusEmprestimo;
	}

}
