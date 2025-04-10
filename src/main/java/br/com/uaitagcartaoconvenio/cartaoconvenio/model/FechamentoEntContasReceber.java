package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.util.Calendar;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@EqualsAndHashCode(of ="id")
@SequenceGenerator(name = "seq_id_ent_item_conv", sequenceName = "seq_id_ent_item_conv", allocationSize = 1, initialValue = 1)
@Table(name = "FECHAMENTO_ENT_CONTAS_RECEBER")
public class FechamentoEntContasReceber {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_ent_item_conv")
	@Column(name = "ID_FECHAMENTO_ENT_CONTAS_RECEBER")
	private Long id;
	
	@ManyToOne(targetEntity = ContasReceber.class)
	@JoinColumn(name = "ID_CONTAS_RECEBER", referencedColumnName = "ID_CONTAS_RECEBER", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_FECHA_ENTIDADE_ITEM_CICLO"))	
	private ContasReceber contasReceber; 
	
	@ManyToOne(targetEntity = Venda.class)
	@JoinColumn(name = "ID_VENDA", referencedColumnName = "ID_VENDA", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_FECHA_ENTIDADE_ITEM_VENDA"))	
	private Venda venda; 
	
	@Column(name = "DT_CRIACAO", nullable = false )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao = Calendar.getInstance().getTime();

}
