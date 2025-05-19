package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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
    
    @Column(name = "VLR_PERIODO_APROVADO", precision = 19, scale = 2)
    private BigDecimal vlrPeriodoAprovado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONTRATO_ENTIDADE", nullable = false)
    private ContratoEntidade contratoEntidade;
    
    @PrePersist
    protected void onCreate() {
        dtCadastro = LocalDateTime.now();
    }
}
