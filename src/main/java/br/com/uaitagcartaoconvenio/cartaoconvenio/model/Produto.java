package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@EqualsAndHashCode(of = "idProduto")
@SequenceGenerator(name = "seq_id_produto", sequenceName = "seq_id_produto", allocationSize = 1, initialValue = 1)
@Table(name = "PRODUTO")
public class Produto implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_produto")
	@Column(name = "ID_PRODUTO")
	private Long idProduto;

	@NotNull(message = "A Razao Social da Conveniada deverá ser informado!")
	@Column(name = "PRODUTO", length = 50, nullable = false)
	private String produto;
	
	@NotNull(message = "O Valor do Produto deverá ser informado")
	@Column(name = "VLR_PRODUTO", nullable = false)
	private BigDecimal vlrProduto; 

	@Column(name = "DT_CADASTRO", nullable = false/*, insertable=false, updatable=false*/ )
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCadastro = Calendar.getInstance().getTime(); 
	
	@JsonIgnore
	@ManyToOne(targetEntity = Conveniados.class)
	@JoinColumn(name = "ID_CONVENIADOS", nullable = true, referencedColumnName = "ID_CONVENIADOS", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_PRODUTO_CONVENIADO"))
	private Conveniados conveniados;

}
