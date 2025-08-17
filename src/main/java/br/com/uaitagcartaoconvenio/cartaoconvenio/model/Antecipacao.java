package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusAntecipacao;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idAntecipacao")
@SequenceGenerator(name = "seq_id_antecipacao", sequenceName = "seq_id_antecipacao", allocationSize = 1, initialValue = 1)
@Table(name = "ANTECIPACAO")
public class Antecipacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_antecipacao")
    @Column(name = "ID_ANTECIPACAO")
    private Long idAntecipacao;
    
    @Column(name = "DT_CRIACAO", nullable = false, columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCriacao = Calendar.getInstance().getTime();

    @Column(name = "DT_ALTERACAO", nullable = false, columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAlteracao = Calendar.getInstance().getTime();

    @Column(name = "TAXA_MES", nullable = false, precision = 10, scale = 4)
    private BigDecimal taxaMes;

    @Column(name = "TAXA_DIA", nullable = false, precision = 10, scale = 4)
    private BigDecimal taxaDia;

    @Column(name = "TAXA_PERIODO", nullable = false, precision = 10, scale = 4)
    private BigDecimal taxaPeriodo;

    @Column(name = "DT_CORTE", nullable = false)
    private LocalDate dtCorte;

    @Column(name = "DT_PAGAMENTO", nullable = false)
    private LocalDate dtPagamento;

    @Column(name = "DT_VENCIMENTO", nullable = false)
    private LocalDate dtVencimento;

    @Column(name = "PERIODO_DIAS", nullable = false)
    private Integer periodoDias;

    @Column(name = "VALOR_DESCONTO", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorDesconto;

    @Column(name = "VALOR_NOMINAL", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorNominal;

    @Column(name = "VALOR_BASE", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorBase;

    @Column(name = "OBSERVACAO", length = 5000)
    private String observacao;

    @Column(name = "NOME_ARQUIVO_APROVACAO", length = 255)
    private String nomeArquivoAprovacao;

    @Column(name = "CONTEUDO_BASE64_APROVACAO", columnDefinition = "TEXT")
    private String conteudoBase64Aprovacao;

    @Column(name = "NOME_ARQUIVO_COMPROVANTE", length = 255)
    private String nomeArquivoComprovante;

    @Column(name = "CONTEUDO_BASE64_COMPROVANTE", columnDefinition = "TEXT")
    private String conteudoBase64Comprovante;

    @Column(name = "TAMANHO_BYTES_APROVACAO")
    private Long tamanhoBytesAprovacao;

    @Column(name = "TAMANHO_BYTES_COMPROVANTE")
    private Long tamanhoBytesComprovante;

    @Column(name = "LOGIN_USER", length = 100, nullable = false)
    private String loginUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private StatusAntecipacao descStatusAntecipacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONVENIADOS", nullable = false)
    private Conveniados conveniados;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CICLO_PAGAMENTO_VENDA")
    private CicloPagamentoVenda cicloPagamentoVenda;

    @OneToMany(mappedBy = "antecipacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AntecipacaoVenda> vendas;

    @PreUpdate
    public void preUpdate() {
        dtAlteracao = Calendar.getInstance().getTime();
    }

    @PrePersist
    public void prePersist() {
        dtCriacao = Calendar.getInstance().getTime();
        dtAlteracao = Calendar.getInstance().getTime();
    }
    
    
}