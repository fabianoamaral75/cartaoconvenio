package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idCicloPagamentoVenda")
@SequenceGenerator(name = "seq_id_ciclo_pagamento_venda", sequenceName = "seq_id_ciclo_pagamento_venda", allocationSize = 1, initialValue = 1)
@Table(name = "CICLO_PAGAMENTO_VENDA")
public class CicloPagamentoVenda implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_ciclo_pagamento_venda")
	@Column(name = "ID_CICLO_PAGAMENTO_VENDA")
	private Long idCicloPagamentoVenda;
		
	@NotNull(message = "O Ano e Mês referência do mês de fechamento do ciclo deverá ser informado!")
	@Column(name = "ANO_MES", length = 6, nullable = false)
	private String anoMes;

	@Column(name = "DT_CRIACAO", nullable = false, insertable=false, updatable=false )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao = Calendar.getInstance().getTime();

	@Column(name = "DT_ALTERACAO", nullable = false )
	private Date dtAlteracao = Calendar.getInstance().getTime();
	
	@NotNull(message = "O Valor referência do fechamento do ciclo deverá ser informado")
	@Column(name = "VALOR_CICLO", nullable = false)
	private BigDecimal valorCiclo; 

	@Column(name = "DT_PAGAMENTO", columnDefinition = "DATE")
	private Date dtPagamento;

	@NotNull(message = "Status da Conta a Receber da Entidade deverá ser informada!")
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusCicloPgVenda descStatusPagamento;
	
	@ManyToOne(targetEntity = Conveniados.class)
	@JoinColumn(name = "ID_CONVENIADOS", nullable = true, referencedColumnName = "ID_CONVENIADOS", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_CICLO_PG_VENDA_CONV"))
	private Conveniados conveniados;
	
	@NotNull(message = "O Tipo da mudança deve(m) ser informado!")
	@ManyToOne(targetEntity = TaxaConveiniados.class)
	@JoinColumn(name = "ID_TAXA_CONVEINIADOS", referencedColumnName = "ID_TAXA_CONVEINIADOS", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_CICLO_PG_VENDA_TX_CONVEINIADOS"))
	private TaxaConveiniados taxaConveiniados;
	
	@PreUpdate
    public void preUpdate() {
		dtAlteracao = Calendar.getInstance().getTime(); 
    }
	

}
