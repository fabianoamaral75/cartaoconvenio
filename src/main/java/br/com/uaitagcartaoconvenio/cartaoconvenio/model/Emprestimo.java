// Emprestimo.java
package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusEmprestimo;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EMPRESTIMO")
@SequenceGenerator(name = "seq_emprestimo", sequenceName = "seq_emprestimo", allocationSize = 1, initialValue = 1)
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_emprestimo")
    @Column(name = "ID_EMPRESTIMO")
    private Long idEmprestimo;

    @Column(name = "VALOR_SOLICITADO", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorSolicitado;

    @Column(name = "TAXA_JUROS", nullable = false, precision = 5, scale = 4)
    private BigDecimal taxaJuros;

    @Column(name = "QUANTIDADE_PARCELAS", nullable = false)
    private Integer quantidadeParcelas;

    @Column(name = "VALOR_TOTAL", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "VALOR_PARCELA", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorParcela;

    @Column(name = "DT_SOLICITACAO", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtSolicitacao;

    @Column(name = "DT_APROVACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAprovacao;

    @Column(name = "DT_QUITACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtQuitacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private StatusEmprestimo status = StatusEmprestimo.SOLICITADO;

    @Column(name = "ANO_MES_REFERENCIA", length = 6)
    private String anoMesReferencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_FUNCIONARIO", nullable = false)
    private Funcionario funcionario;

    @OneToMany(mappedBy = "emprestimo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PrestacaoEmprestimo> prestacoes;

    @OneToMany(mappedBy = "emprestimo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContasReceberEmprestimo> contasReceber;

    @Column(name = "OBSERVACAO", length = 500)
    private String observacao;

    @PrePersist
    protected void onCreate() {
        dtSolicitacao = Calendar.getInstance().getTime();
    }

    @PreUpdate
    protected void onUpdate() {
        if (status == StatusEmprestimo.QUITADO && dtQuitacao == null) {
            dtQuitacao = Calendar.getInstance().getTime();
        }
    }
}