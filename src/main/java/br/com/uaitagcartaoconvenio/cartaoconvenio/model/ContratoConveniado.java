package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@EqualsAndHashCode(of ="idContratoConveniado")
@SequenceGenerator(name = "seq_id_contrato_conveniado", sequenceName = "seq_id_contrato_conveniado", allocationSize = 1, initialValue = 1)
@Table(name = "CONTRATO_CONVENIADO")
public class ContratoConveniado implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_contrato_conveniado")
	@Column(name = "ID_CONTRATO_CONVENIADO")
	private Long idContratoConveniado;	
	
	@Column(name = "DT_CRIACAO", nullable = false, columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtCriacao = Calendar.getInstance().getTime();

    @Column(name = "ARQ_CONTRATO", length = 100, nullable = true)
    private String arqContrato;

    @Column(name = "CONTEUDO_BASE64", columnDefinition = "TEXT", nullable = true)
    private String conteudoBase64;

    @Column(name = "TAMANHO_BYTES")
    private Long tamanhoBytes;

    @Column(name = "DATA_UPLOAD", columnDefinition = "TIMESTAMP")
    private Date dataUpload;
    
    @Column(name = "OBSERVACAO", columnDefinition = "TEXT", nullable = true)
    private String observacao;
    
	@ManyToOne(targetEntity = Conveniados.class)
	@JoinColumn(name = "ID_CONVENIADOS", nullable = true, referencedColumnName = "ID_CONVENIADOS", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_CONVENIADO_CONTRATO"))
	private Conveniados conveniados;
	
	@OneToMany(mappedBy = "contratoConveniado", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VigenciaContratoConveniada> vigencias = new ArrayList<VigenciaContratoConveniada>();

    // Método auxiliar para adicionar vigências mantendo a consistência
    public void adicionarVigencia(VigenciaContratoConveniada vigencia) {
        vigencia.setContratoConveniado(this);
        this.vigencias.add(vigencia);
    }
    
    // Método auxiliar para remover vigências
    public void removerVigencia(VigenciaContratoConveniada vigencia) {
        this.vigencias.remove(vigencia);
        vigencia.setContratoConveniado(null);
    }
}
