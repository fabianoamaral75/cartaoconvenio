// PrestacaoEmprestimo.java
package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusPrestacao;
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
import jakarta.persistence.PrePersist;
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
@Table(name = "PRESTACAO_EMPRESTIMO")
@SequenceGenerator(name = "seq_prestacao_emprestimo", sequenceName = "seq_prestacao_emprestimo", allocationSize = 1, initialValue = 1)
public class PrestacaoEmprestimo {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_prestacao_emprestimo")
    @Column(name = "ID_PRESTACAO")
    private Long idPrestacao;

    @Column(name = "NUMERO_PARCELA", nullable = false)
    private Integer numeroParcela;

    @Column(name = "VALOR_PARCELA", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorParcela;

    @Column(name = "VALOR_JUROS", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorJuros;

    @Column(name = "VALOR_AMORTIZACAO", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorAmortizacao;

    @Column(name = "SALDO_DEVEDOR", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoDevedor;

    @Column(name = "DT_VENCIMENTO", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dtVencimento;

    @Column(name = "DT_PAGAMENTO")
    @Temporal(TemporalType.DATE)
    private Date dtPagamento;
    
    @Column(name = "ANO_MES_REFERENCIA", length = 6)
    private String anoMesReferencia;

    @Column(name = "DT_ENVIO_COBRANCA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtEnvioCobranca;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private StatusPrestacao status = StatusPrestacao.PENDENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EMPRESTIMO", nullable = false)
    private Emprestimo emprestimo;

    @PrePersist
    protected void onCreate() {
        if (dtVencimento == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, numeroParcela);
            dtVencimento = calendar.getTime();
        }
    }
}