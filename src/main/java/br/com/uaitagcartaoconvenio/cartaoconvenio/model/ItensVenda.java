package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.math.BigDecimal;

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
import jakarta.validation.constraints.NotNull;
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
@EqualsAndHashCode(of = "idItensVenda")
@SequenceGenerator(name = "seq_id_itens_venda", sequenceName = "seq_id_itens_venda", allocationSize = 1, initialValue = 1)
@Table(name = "ITENS_VENDA")
public class ItensVenda implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_itens_venda")
	@Column(name = "ID_ITENS_VENDA")
	private Long idItensVenda;

	@NotNull(message = "A quantidade de itens deverá ser informado!")
	@Column(name = "QTY_ITEM", length = 50, nullable = false)
	private Integer qtyItem;
	
	@NotNull(message = "O Valor do Produto deverá ser informado")
	@Column(name = "VLR_UNITARIO", nullable = false)
	private BigDecimal vlrProduto; 

	@NotNull(message = "O Valor toral referente a quantidade de Produto deverá ser informado")
	@Column(name = "VLR_TOTAL_ITEM", nullable = false)
	private BigDecimal vlrTotalItem; 

	@ManyToOne(targetEntity = Venda.class)
	@JoinColumn(name = "ID_VENDA", referencedColumnName = "ID_VENDA", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_ITENS_VENDA_VENDA"))	
	private Venda venda;
	
	@ManyToOne(targetEntity = Produto.class)
	@JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID_PRODUTO", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_ITENS_VENDA_PRODUTO"))	
	private Produto produto;

}
