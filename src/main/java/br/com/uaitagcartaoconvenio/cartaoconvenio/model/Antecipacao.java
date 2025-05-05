package br.com.uaitagcartaoconvenio.cartaoconvenio.model;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusAntecipacao;
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
import jakarta.persistence.PreUpdate;
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
@Table(name = "ANTECIPACAO")
public class Antecipacao {

    @Id
    @EqualsAndHashCode.Include
    @SequenceGenerator(name = "seq_id_antecipacao", sequenceName = "seq_id_antecipacao", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_antecipacao")
    @Column(name = "ID_ANTECIPACAO", nullable = false)
    private Long id;

    @CreationTimestamp
    @Column(name = "DT_SOLICITACAO", updatable = false, nullable = false)
    private LocalDateTime dataSolicitacao;

    @Column(name = "VLR_ANTECIPACAO", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorAntecipacao;

    @Column(name = "RESPONSAVEL_SOLICITACAO", length = 200, nullable = false)
    private String responsavelSolicitacao;
    
    @Column(name = "STATUS_ANTECIPACAO", length = 200, nullable = false)
    private StatusAntecipacao status;

    @Column(name = "NOME_AQR_DOC_BANCO", length = 200)
    private String nomeArquivoDocumentoBanco;

    @Column(name = "CONTEUDO_BASE64", columnDefinition = "TEXT")
    private String conteudoBase64;

    @Column(name = "TAMANHO_BYTES")
    private Integer tamanhoBytes;

    @Column(name = "DT_UPLOAD")
    private LocalDateTime dataUpload;

    @Column(name = "TAXA_NOMINAL", precision = 19, scale = 6)
    private BigDecimal taxaNominal;

    @Column(name = "TAXA_DIA", precision = 19, scale = 6)
    private BigDecimal taxaDia;

    @Column(name = "TAXA_PERIODO", precision = 19, scale = 6)
    private BigDecimal taxaPeriodo;

    @Column(name = "DT_CORTE")
    private LocalDate dataCorte;

    @Column(name = "DT_PAGAMENTO")
    private LocalDate dataPagamento;

    @Column(name = "DT_VENCIMENTO_ORIGINAL")
    private LocalDate dataVencimentoOriginal;

    @Column(name = "QTY_DIAS")
    private Long quantidadeDias;

    @Column(name = "VLR_TAXA", precision = 19, scale = 2)
    private BigDecimal valorTaxa;

    @Column(name = "VLR_ORIGINAL", precision = 19, scale = 2)
    private BigDecimal valorOriginal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CICLO_PAGAMENTO_VENDA", nullable = false, 
               foreignKey = @ForeignKey(name = "fk_antecipacao_ciclo_pg_venda"))
    private CicloPagamentoVenda cicloPagamentoVenda;

    @PrePersist
    @PreUpdate
    public void prePersistUpdate() {
        if (dataUpload == null && conteudoBase64 != null) {
            dataUpload = LocalDateTime.now();
        }
    }
}
