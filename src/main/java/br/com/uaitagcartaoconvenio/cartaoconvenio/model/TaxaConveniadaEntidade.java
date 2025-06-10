package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
@EqualsAndHashCode(of = "id")
@SequenceGenerator(name = "seq_id_taxa_conv_enti", sequenceName = "seq_id_taxa_conv_enti", allocationSize = 1, initialValue = 1)
@Table(name = "TAXA_CONVENIADA_ENTIDADE")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TaxaConveniadaEntidade implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_taxa_conv_enti")
    @Column(name = "ID_TAXA_CONVENIADA_ENTIDADE")
    private Long id;
    
    @Column(name = "DT_CRIACAO", columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCriacao = Calendar.getInstance().getTime(); 

    @Column(name = "DT_ALTERACAO", nullable = false, columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAlteracao = Calendar.getInstance().getTime(); 
    
    @NotNull(message = "Descrição da Taxa entre Conveniada e Entidade!")
    @Column(name = "DESC_TAXA", nullable = false)
    private String descTaxa;    
    
    @NotNull(message = "Valor da Taxa entre Conveniada deverá ser informado")
    @Column(name = "VLR_TAXA", nullable = false)
    private BigDecimal vlrTaxa;
    
    @NotNull(message = "Status da taxa da Conveniada deverá ser informada!")
    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusTaxaConv statusTaxaConEnt;
    
    // Relacionamento N:1 com Conveniados
    @NotNull(message = "Conveniado associado à taxa deve ser informado!")
    @ManyToOne
    @JoinColumn(name = "ID_CONVENIADOS", referencedColumnName = "ID_CONVENIADOS", nullable = false, foreignKey = @ForeignKey(name = "fk_taxa_conv_enti_conveniados"))
    private Conveniados conveniados;
    
    // Relacionamento N:1 com Entidade
    @NotNull(message = "Entidade associada à taxa deve ser informada!")
    @ManyToOne
    @JoinColumn( name = "ID_ENTIDADE", referencedColumnName = "ID_ENTIDADE", nullable = false, foreignKey = @ForeignKey(name = "fk_taxa_conv_enti_entidade"))
    private Entidade entidade;
    
    @PreUpdate
    public void preUpdate() {
        dtAlteracao = Calendar.getInstance().getTime(); 
    }
    
    @PrePersist
    protected void onCreate() {
        dtCriacao = Calendar.getInstance().getTime();
        dtAlteracao = Calendar.getInstance().getTime(); 
    }
}