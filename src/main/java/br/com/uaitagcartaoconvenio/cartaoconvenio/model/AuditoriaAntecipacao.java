package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusAntecipacao;
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
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "auditoria_antecipacao", indexes = {
	    @Index(name = "idx_auditoria_antecipacao", columnList = "id_antecipacao"),
	    @Index(name = "idx_auditoria_data"       , columnList = "data_auditoria"),
	    @Index(name = "idx_auditoria_usuario"    , columnList = "usuario"       )
	})
public class AuditoriaAntecipacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Antecipacao.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "ID_ANTECIPACAO", nullable = false)
    @JoinColumn(name = "ID_ANTECIPACAO", referencedColumnName = "ID_ANTECIPACAO", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_AUDIT_ANTECIPACAO_ANTECIP"))
    private Antecipacao antecipacao;       

    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior", length = 20)
    private StatusAntecipacao statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", length = 20)
    private StatusAntecipacao statusNovo;

    @Column(name = "motivo", columnDefinition = "TEXT")
    private String motivo;

    @Column(name = "ip_origem", length = 45)
    private String ipOrigem;

    @Column(name = "usuario", length = 100)
    private String usuario;

    @CreationTimestamp
    @Column(name = "data_auditoria", updatable = false)
    private LocalDateTime dataAuditoria;

    @Column(name = "token_utilizado", length = 800)
    private String tokenUtilizado;

    @Column(name = "dispositivo", length = 800)
    private String dispositivo;

    @Column(name = "user_agent", length = 800)
    private String userAgent;
}