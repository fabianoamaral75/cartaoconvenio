package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ITENS_TAXAS_EXTRA_ENTIDADE")
public class ItemTaxaExtraEntidade {

    @Id
    @SequenceGenerator(name = "seq_item_taxa_extra", sequenceName = "seq_item_taxa_extra", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_item_taxa_extra")
    @Column(name = "ID_ITEM_TAXA_EXTRA")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TAXAS_EXTRA_ENTIDADE", nullable = false, 
               foreignKey = @ForeignKey(name = "fk_item_taxa_extra"))
    private TaxaExtraEntidade taxaExtraEntidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONTAS_RECEBER", nullable = false,
               foreignKey = @ForeignKey(name = "fk_item_conta_receber"))
    private ContasReceber contasReceber;

    @Column(name = "VLR_TAXA", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTaxa;

    @CreationTimestamp
    @Column(name = "DT_CADASTRO", updatable = false, nullable = false)
    private LocalDateTime dataCadastro;

    @PrePersist
    public void setDataCadastro() {
        this.dataCadastro = LocalDateTime.now();
    }
}