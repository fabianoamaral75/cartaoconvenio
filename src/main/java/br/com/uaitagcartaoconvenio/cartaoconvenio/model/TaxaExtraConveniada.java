package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "TAXAS_EXTRA_CONVENIADA")
public class TaxaExtraConveniada {

    @Id
    @EqualsAndHashCode.Include
    @SequenceGenerator(name = "seq_id_tx_conveniada", sequenceName = "seq_id_tx_conveniada", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_tx_conveniada")
    @Column(name = "ID_TAXAS_EXTRA_CONVENIADA", nullable = false)
    private Long id;

    @Column(name = "DESC_TAXA", length = 200, nullable = false)
    private String descricaoTaxa;

    @CreationTimestamp
    @Column(name = "DT_CRIACAO", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "VLR_TAXA", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTaxa;

    @Column(name = "STATUS_TAXA", length = 200, nullable = false)
    private String statusTaxa;

    @ManyToOne(targetEntity = Conveniados.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONVENIADOS", nullable = false, foreignKey = @ForeignKey(name = "fk_taxa_extra_conveniados"))
    private Conveniados conveniados;
    
    // Adicione este campo à classe existente
    @OneToMany(mappedBy = "taxaExtraConveniada", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CicloTaxaExtra> ciclosPagamento = new ArrayList<CicloTaxaExtra>();
    
    // Relacionamento 1:1 com PeriodoCobrancaTaxa (lado proprietário)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_PERIODO_COBRANCA_TAXA", nullable = false, foreignKey = @ForeignKey(name = "fk_taxa_extra_periodo"))
    private PeriodoCobrancaTaxa periodoCobrancaTaxa;
    
    @PrePersist
    public void prePersist() {
        if (statusTaxa == null || statusTaxa.isEmpty()) {
            statusTaxa = "ATIVA"; // Valor padrão
        }
    }
    
    public void setPeriodoCobrancaTaxa(PeriodoCobrancaTaxa periodo) {
        this.periodoCobrancaTaxa = periodo;
        if (periodo != null) {
            periodo.setTaxaExtraConveniada(this);
        }
    }
}