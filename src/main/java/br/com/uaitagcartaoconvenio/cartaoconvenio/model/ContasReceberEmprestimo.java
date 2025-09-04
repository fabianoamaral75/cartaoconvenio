// ContasReceberEmprestimo.java
package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "CONTAS_RECEBER_EMPRESTIMO")
@SequenceGenerator(name = "seq_contas_receber_emprestimo", sequenceName = "seq_contas_receber_emprestimo", allocationSize = 1, initialValue = 1)
public class ContasReceberEmprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_contas_receber_emprestimo")
    @Column(name = "ID_CONTAS_RECEBER_EMPRESTIMO")
    private Long idContasReceberEmprestimo;

    @Column(name = "DT_CRIACAO", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCriacao = Calendar.getInstance().getTime();

    @Column(name = "VALOR_PARCELA", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorParcela;

    @Column(name = "NUMERO_PARCELA", nullable = false)
    private Integer numeroParcela;

    @Column(name = "ANO_MES_REFERENCIA", length = 6, nullable = false)
    private String anoMesReferencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EMPRESTIMO", nullable = false)
    private Emprestimo emprestimo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONTAS_RECEBER", nullable = false)
    private ContasReceber contasReceber;

    @PrePersist
    protected void onCreate() {
        dtCriacao = Calendar.getInstance().getTime();
    }
}
