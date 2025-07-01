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
@Table(name = "TAXAS_EXTRA_ENTIDADE")
public class TaxaExtraEntidade {

    @Id
    @EqualsAndHashCode.Include
    @SequenceGenerator(name = "seq_id_taxa_extra_entidade", sequenceName = "seq_id_taxa_extra_entidade", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_taxa_extra_entidade")
    @Column(name = "ID_TAXAS_EXTRA_ENTIDADE", nullable = false)
    private Long id;

    @Column(name = "DESC_TAXA", length = 200, nullable = false)
    private String descricaoTaxa;
      
    @Column(name = "VLR_TAXA", nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    @Column(name = "STATUS", length = 50, nullable = false)
    private String status;

    @CreationTimestamp
    @Column(name = "DT_CRIACAO", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    // Relacionamento 1:1 com PeriodoCobrancaTaxa (lado proprietário)
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_PERIODO_COBRANCA_TAXA", nullable = false, foreignKey = @ForeignKey(name = "fk_taxa_extra_entidade_periodo"))
    private PeriodoCobrancaTaxa periodoCobrancaTaxa;

    @ManyToOne(targetEntity = Entidade.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ENTIDADE", nullable = false, foreignKey = @ForeignKey(name = "fk_taxa_extra_entidade"))
    private Entidade entidade;

    // Adicionar relacionamento com a tabela de junção
    @OneToMany(mappedBy = "taxaExtraEntidade", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemTaxaExtraEntidade> itensTaxaExtraEntidade = new ArrayList<ItemTaxaExtraEntidade>();

    public void setPeriodoCobrancaTaxa(PeriodoCobrancaTaxa periodo) {
        this.periodoCobrancaTaxa = periodo;
        if (periodo != null) {
            periodo.setTaxaExtraEntidade(this);
        }
    }

    
    @PrePersist
    public void setDefaultStatus() {
        if (status == null || status.isEmpty()) {
            status = "ATIVA";
        }
    }
}