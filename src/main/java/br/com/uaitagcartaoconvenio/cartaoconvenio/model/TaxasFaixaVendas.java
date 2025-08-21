package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TAXAS_FAIXA_VENDAS")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class TaxasFaixaVendas {

    @Id
    @EqualsAndHashCode.Include
    @SequenceGenerator(name = "seq_id_tx_faixa_vendas", sequenceName = "seq_id_tx_faixa_vendas", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_tx_faixa_vendas")
    @Column(name = "ID_TAXAS_FAIXA_VENDAS", nullable = false)
    private Long id;
   
    @Column(name = "DESC_TAXA", length = 200, nullable = false)
    private String descricaoTaxa;

    @CreationTimestamp
    @Column(name = "DT_CRIACAO", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    @CreationTimestamp
    @Column(name = "DT_ALTERACAO", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime dtAlteracao;

    @Column(name = "TIPO_COBRANCA_PERCENTUAL", nullable = false, columnDefinition = "boolean default false")
    private Boolean tipoCobrancaPercentual;
    
    @Column(name = "VLR_TAXA", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorTaxa;

    @Column(name = "VALOR_TAXA_PERCENTUAL", nullable = false, precision = 19, scale = 2, columnDefinition = "NUMERIC(19,2) DEFAULT 0.00")
    private BigDecimal valorTaxaPercentual;

    @Column(name = "VLR_FAIXA_TAXA_MAX", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorFaixaTaxaMax;
  
    @Column(name = "VLR_FAIXA_TAXA_MIN", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorFaixaTaxaMin;

    @Column(name = "STATUS_TAXA", length = 200, nullable = false)
    private String statusTaxa;
    
    @JsonManagedReference("taxaFaixaVendas-ciclos") // Adicione esta anotação
    @OneToMany(mappedBy = "taxasFaixaVendas", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CicloPagamentoVenda> ciclosPagamento = new ArrayList<CicloPagamentoVenda>();

    @PreUpdate
    public void preUpdate() {
        dtAlteracao = LocalDateTime.now();
    }
    
    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }

	@Override
	public String toString() {
		return "TaxasFaixaVendas [id=" + id + ", descricaoTaxa=" + descricaoTaxa + ", dataCriacao=" + dataCriacao
				+ ", dtAlteracao=" + dtAlteracao + ", tipoCobrancaPercentual=" + tipoCobrancaPercentual + ", valorTaxa="
				+ valorTaxa + ", valorTaxaPercentual=" + valorTaxaPercentual + ", valorFaixaTaxaMax="
				+ valorFaixaTaxaMax + ", valorFaixaTaxaMin=" + valorFaixaTaxaMin + ", statusTaxa=" + statusTaxa
				+ ", ciclosPagamento=" + ciclosPagamento + "]";
	}
    
    
}