package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
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
@Table(name = "VIGENCIA_CONTRATO_ENTIDADE")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idVigenciaContratoEntidade")
public class VigenciaContratoEntidade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VIGENCIA_CONTRATO_ENTIDADE")
    private Long idVigenciaContratoEntidade;
    
    @Column(name = "DT_INICIO", nullable = false)
    private LocalDate dtInicio;
    
    @Column(name = "DT_FINAL", nullable = false)
    private LocalDate dtFinal;
    
    @Column(name = "DT_CRIACAO", nullable = false)
    private LocalDateTime dtCriacao;
    
    @Column(name = "DT_DESATIVACAO")
    private LocalDateTime dtDesativacao;
    
    @Column(name = "USER_DESATIVACAO", length = 200)
    private String userDesativacao;
    
    @Column(name = "RENOVACAO", nullable = false)
    private Boolean renovacao;
    
    @Column(name = "OBSERVACAO", columnDefinition = "TEXT")
    private String observacao;
    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ContratoEntidade.class)
    @JoinColumn(name = "ID_CONTRATO_ENTIDADE", nullable = false, referencedColumnName = "ID_CONTRATO_ENTIDADE", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_VIGENCIA_CONTRATO_ENTIDADE"))
    private ContratoEntidade contratoEntidade;

    @PrePersist
    protected void onCreate() {
        dtCriacao = LocalDateTime.now();
    }
}
