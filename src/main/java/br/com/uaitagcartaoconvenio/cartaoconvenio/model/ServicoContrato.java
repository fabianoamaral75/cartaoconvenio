package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.TipoCobrancaTaxaExtraEnt;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "SERVICO_CONTRATO")
public class ServicoContrato {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SERVICO_CONTRATO")
    private Long idServicoContrato;
    
    @Column(name = "DT_CADASTRO", nullable = false)
    private LocalDateTime dtCadastro;
    
    @Column(name = "DES_SERVICO", length = 300, nullable = false)
    private String desServico;
    
    @Column(name = "VLR_SERVICO", precision = 19, scale = 2)
    private BigDecimal vlrServico;
    
    @NotNull(message = "Tipo de cobrança da taxa extra a entidade!")
	@Column(name = "TIPO_COBRANCA_TAXA_EXTRA", nullable = false)
	@Enumerated(EnumType.STRING)
    private TipoCobrancaTaxaExtraEnt tipoCobrancaTaxaExtraEnt;
    
	@Column(name = "DATA_ULTIMA_COBRANCA", columnDefinition = "DATE")
	private Date dtUltimaCobranca;
    
    @Column(name = "QTY_MESES_COBRANCA", columnDefinition = "bigint default 0")
    private Integer qtyMesesCobranca;    

    @Column(name = "QTY_MESES_COBRANCA_REALIZADAS", columnDefinition = "bigint default 0")
    private Integer qtyMesesCobrancaRealizadas;    

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ContratoEntidade.class)
    @JoinColumn(name = "ID_CONTRATO_ENTIDADE", nullable = false, referencedColumnName = "ID_CONTRATO_ENTIDADE", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_SERVICO_CONTRATO_ENTIDADE"))
    private ContratoEntidade contratoEntidade;

    @PrePersist
    protected void onCreate() {
        dtCadastro = LocalDateTime.now();
    }
}
