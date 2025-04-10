package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
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
@SequenceGenerator(name = "seq_id_fech_item_conv", sequenceName = "seq_id_fech_item_conv", allocationSize = 1, initialValue = 1)
@Table(name = "FECHAMENTO_CONV_ITENS_VENDAS")
public class FechamentoConvItensVendas implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_fech_item_conv")
	@Column(name = "ID_FECHAMENTO_CONV_ITENS_VENDAS")
	private Long id;
	
	@ManyToOne(targetEntity = CicloPagamentoVenda.class)
	@JoinColumn(name = "ID_CICLO_PAGAMENTO_VENDA", referencedColumnName = "ID_CICLO_PAGAMENTO_VENDA", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_FECHA_CONV_ITEM_CICLO"))	
	private CicloPagamentoVenda cicloPagamentoVenda; 
	
	@ManyToOne(targetEntity = Venda.class)
	@JoinColumn(name = "ID_VENDA", referencedColumnName = "ID_VENDA", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_FECHA_CONV_ITEM_VENDA"))	
	private Venda venda; 
	
	@Column(name = "DT_CRIACAO", nullable = false )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao = Calendar.getInstance().getTime();
	
}
