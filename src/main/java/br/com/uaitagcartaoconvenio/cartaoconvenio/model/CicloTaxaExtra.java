package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "CICLO_TAXA_EXTRA")
public class CicloTaxaExtra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @SequenceGenerator(name = "seq_id_ciclo_taxa_extra", sequenceName = "seq_id_ciclo_taxa_extra", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_ciclo_taxa_extra")
    @Column(name = "ID_CICLO_TAXA_EXTRA")
    private Long id;

    @CreationTimestamp
    @Column(name = "DT_CADASTRO", updatable = false, nullable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "VLR_TAXA_EXTRA", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTaxaExtra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CICLO_PAGAMENTO_VENDA", nullable = false, foreignKey = @ForeignKey(name = "fk_ciclo_taxa_extra_ciclo"))
    private CicloPagamentoVenda cicloPagamentoVenda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TAXAS_EXTRA_CONVENIADA", nullable = false, foreignKey = @ForeignKey(name = "fk_ciclo_taxa_extra_taxa"))
    private TaxaExtraConveniada taxaExtraConveniada;

    @PrePersist
    public void prePersist() {
        if (dataCadastro == null) {
            dataCadastro = LocalDateTime.now();
        }
    }
}