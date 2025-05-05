package br.com.uaitagcartaoconvenio.cartaoconvenio.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "VIGENCIA_CONTRATO_CONVENIADA")
public class VigenciaContratoConveniada implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @SequenceGenerator( name = "seq_id_vigencia_contrato_conveniada",sequenceName = "seq_id_vigencia_contrato_conveniada", allocationSize = 1, initialValue = 1 )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_vigencia_contrato_conveniada")
    @Column(name = "ID_VIGENCIA_CONTRATO_CONVENIADA", nullable = false)
    private Long id;

    @Column(name = "DT_INICIO", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "DT_FINAL", nullable = false)
    private LocalDate dataFinal;

    @CreationTimestamp
    @Column(name = "DT_CRIACAO", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "DT_ALTERACAO", nullable = false)
    private LocalDateTime dataAlteracao;

    @Column(name = "DT_DESATIVACAO")
    private LocalDateTime dataDesativacao;

    @Column(name = "USER_DESATIVACAO", length = 100)
    private String usuarioDesativacao;

    @Column(name = "RENOVACAO", nullable = false)
    @Builder.Default
    private Boolean renovacao = false;

    @Column(name = "OBSERVACAO", columnDefinition = "TEXT")
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "ID_CONTRATO_CONVENIADO", nullable = false, foreignKey = @ForeignKey( name = "fk_vigencia_contrato_conveniado", value = ConstraintMode.CONSTRAINT ) )
    private ContratoConveniado contratoConveniado;
    
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.dataCriacao = now;
        this.dataAlteracao = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataAlteracao = LocalDateTime.now();
    }


}