package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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
@EqualsAndHashCode(of = "idContratoEntidade")
@Table(name = "CONTRATO_ENTIDADE")
public class ContratoEntidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CONTRATO_ENTIDADE")
    private Long idContratoEntidade;

    @Column(name = "DT_CADASTRO", nullable = false)
    private LocalDateTime dtCadastro;

    @Column(name = "VLR_MENSAL", precision = 19, scale = 2)
    private BigDecimal vlrMensal;

    @Column(name = "VLR_CONTRATO", precision = 19, scale = 2)
    private BigDecimal vlrContrato;

    @Column(name = "STATUS", nullable = false)
    private Boolean status;

    @Column(name = "OBSERVACAO", columnDefinition = "TEXT")
    private String observacao;

	@ManyToOne(targetEntity = Entidade.class)
	@JoinColumn(name = "ID_ENTIDADE", nullable = true, referencedColumnName = "ID_ENTIDADE", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_CONTRATO_ENT_ENTIDADE"))
    private Entidade entidade;

    @OneToMany(mappedBy = "contratoEntidade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArqContratoEntidade> arquivos = new ArrayList<ArqContratoEntidade>();

    @OneToMany(mappedBy = "contratoEntidade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VigenciaContratoEntidade> vigencias = new ArrayList<VigenciaContratoEntidade>();

    @OneToMany(mappedBy = "contratoEntidade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServicoContrato> servicos = new ArrayList<ServicoContrato>();

    @PrePersist
    protected void onCreate() {
        dtCadastro = LocalDateTime.now();
    }
}