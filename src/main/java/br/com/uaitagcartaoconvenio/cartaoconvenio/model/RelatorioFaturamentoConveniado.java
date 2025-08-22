package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;
import java.util.Date;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusRelatorioFaturamento;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor@EqualsAndHashCode(of = "idRelatorioFaturamentoConveniado")
@SequenceGenerator(name = "seq_id_rel_fat_conveniado", sequenceName = "seq_id_rel_fat_conveniado", allocationSize = 1, initialValue = 1)
@Table(name = "RELATORIO_FATURAMENTO")
public class RelatorioFaturamentoConveniado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_rel_fat_conveniado")
    @Column(name = "ID_RELATORIO_FATURAMENTO_CONVENIADO")
    private Long idRelatorioFaturamentoConveniado;

    @Column(name = "ID_CONVENIADOS", nullable = false)
    private Long idConveniados;

    @Column(name = "ANO_MES", length = 6, nullable = false)
    private String anoMes;

    @Column(name = "NOME_ARQUIVO", length = 200, nullable = false)
    private String nomeArquivo;
    
    @Column(name = "ARQUIVO_TIPO", length = 200, nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'application/pdf'")
    private String ArquivoTipo;

    @Column(name = "CONTEUDO_BASE64", columnDefinition = "TEXT", nullable = false)
    private String conteudoBase64;

    @Column(name = "TAMANHO_BYTES", nullable = false)
    private Long tamanhoBytes;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private StatusRelatorioFaturamento status;

    @Column(name = "DT_CRIACAO", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCriacao;

    @Column(name = "OBSERVACAO", columnDefinition = "TEXT")
    private String observacao;

    @ManyToOne(targetEntity = CicloPagamentoVenda.class)
	@JoinColumn(name = "ID_CICLO_PAGAMENTO_VENDA", nullable = true, referencedColumnName = "ID_CICLO_PAGAMENTO_VENDA", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_REL_FATURAMENTO_CICLO_PG"))
    private CicloPagamentoVenda cicloPagamentoVenda;
    
    
    
    
    @PrePersist
    protected void onCreate() {
        dtCriacao = new Date();
    }
}

