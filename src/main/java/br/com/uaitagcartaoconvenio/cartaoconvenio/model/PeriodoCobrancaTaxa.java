package br.com.uaitagcartaoconvenio.cartaoconvenio.model;


import java.time.LocalDate;
import java.time.LocalDateTime;

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
@Table(name = "PERIODO_COBRANCA_TAXA")
public class PeriodoCobrancaTaxa {

    @Id
    @EqualsAndHashCode.Include
    @SequenceGenerator(name = "seq_id_periodo_cobranca", sequenceName = "seq_id_periodo_cobranca", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_periodo_cobranca")
    @Column(name = "ID_PERIODO_COBRANCA_TAXA", nullable = false)
    private Long id;

    @Column(name = "DESC_PERIODO_COBRANCA_TAXA", length = 200, nullable = false)
    private String descricao;

    @Column(name = "DT_INICIO", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "DT_FIM", nullable = false)
    private LocalDate dataFim;

    @Column(name = "OBSERVACAO", columnDefinition = "TEXT")
    private String observacao;

    @CreationTimestamp
    @Column(name = "DT_CRIACAO", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;
    
 // Relacionamento 1:1 com TaxaExtraConveniada
    @OneToOne(mappedBy = "periodoCobrancaTaxa", cascade = CascadeType.ALL, orphanRemoval = true)
    private TaxaExtraConveniada taxaExtraConveniada;
    
    // Relacionamento 1:1 com TaxaExtraEntidade
    @OneToOne(mappedBy = "periodoCobrancaTaxa", cascade = CascadeType.ALL, orphanRemoval = true)
    private TaxaExtraEntidade taxaExtraEntidade;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_PERIODO", nullable = false, foreignKey = @ForeignKey(name = "fk_periodo_cobranca_tipo"))
    private TipoPeriodo tipoPeriodo;

    @PrePersist
    public void validateDates() {
        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }
    }
    
    public void setTaxaExtraConveniada(TaxaExtraConveniada taxa) {
        this.taxaExtraConveniada = taxa;
        if (taxa != null && taxa.getPeriodoCobrancaTaxa() != this) {
            taxa.setPeriodoCobrancaTaxa(this);
        }
    }
}