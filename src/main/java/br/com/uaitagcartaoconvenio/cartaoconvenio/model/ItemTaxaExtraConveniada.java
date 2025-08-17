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
@Table(name = "ITENS_TAXAS_EXTRA_CONVENIADA")
public class ItemTaxaExtraConveniada {

    @Id
    @SequenceGenerator(name = "seq_item_taxa_extra_conveniada", sequenceName = "seq_item_taxa_extra_conveniada", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_item_taxa_extra_conveniada")
    @Column(name = "ID_ITEM_TAXA_EXTRA_CONVENIADA")
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_TAXAS_EXTRA_CONVENIADA", nullable = false, referencedColumnName = "ID_TAXAS_EXTRA_CONVENIADA", foreignKey = @ForeignKey(name = "fk_item_taxa_extra_conv"))
    private TaxaExtraConveniada taxaExtraConveniada;
	
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CICLO_PAGAMENTO_VENDA", nullable = false, referencedColumnName = "ID_CICLO_PAGAMENTO_VENDA", foreignKey = @ForeignKey(name = "fk_item_conta_pagar"))
    private CicloPagamentoVenda cicloPagamentoVenda;
    
    @Column(name = "VLR_TAXA", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTaxa;

    @CreationTimestamp
    @Column(name = "DT_CADASTRO", updatable = false, nullable = false)
    private LocalDateTime dataCadastro;
    
    @Column(name = "TIPO_COBRANCA_PERCENTUAL", nullable = false, columnDefinition = "boolean default false")
    private Boolean tipoCobrancaPercentual;

    @Column(name = "COBRANCA_VALOR_BRUTO", nullable = false, columnDefinition = "boolean default false")
    private Boolean cobrancaValorBruto;

    @PrePersist
    public void setDataCadastro() {
        this.dataCadastro = LocalDateTime.now();
    }


}
