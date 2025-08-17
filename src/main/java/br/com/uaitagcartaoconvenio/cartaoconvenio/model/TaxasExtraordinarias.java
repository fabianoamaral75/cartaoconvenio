package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
@Table(name = "TAXAS_EXTRAORDINARIAS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TaxasExtraordinarias {

    @Id
    @EqualsAndHashCode.Include
    @SequenceGenerator(name = "seq_id_taxas_extraordinarias", sequenceName = "seq_id_taxas_extraordinarias", 
                      allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_taxas_extraordinarias")
    @Column(name = "ID_TAXAS_EXTRAORDINARIAS", nullable = false)
    private Long idTaxasExtraordinarias;

    @Column(name = "DESC_TAXA_EXTRAORDINARIAS", length = 400, nullable = false)
    private String descricaoTaxaExtraordinarias;

    @CreationTimestamp
    @Column(name = "DT_CRIACAO", updatable = false, nullable = false, columnDefinition = "TIMESTAMP")
    private Date dataCriacao;
    
    @Column(name = "DT_ALTERACAO", nullable = false, columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAlteracao;

    @Column(name = "VLR_TAXA_EXTRAORDINARIAS", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTaxaExtraordinarias;

    @Column(name = "STATUS_TAXA_EXTRAORDINARIAS", length = 200, nullable = false)
    private String statusTaxa;
    
    @PreUpdate
    public void preUpdate() {
    	dataCriacao = Calendar.getInstance().getTime();
    }  
    
    @PrePersist
    public void prePersist() {
    	dataCriacao = Calendar.getInstance().getTime();
        dtAlteracao = Calendar.getInstance().getTime();
    }
        
    
}