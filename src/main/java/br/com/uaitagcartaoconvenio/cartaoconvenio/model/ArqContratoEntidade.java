package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

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
@Table(name = "ARQ_CONTRATO_ENTIDADE")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idArqContratoEntidade")
public class ArqContratoEntidade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ARQ_CONTRATO_ENTIDADE")
    private Long idArqContratoEntidade;
 
    @Column(name = "CAMINHO_ARQUIVO", length = 200)
    private String caminhoArquivo;

    @Column(name = "ARQ_CONTRATO", length = 200)
    private String arqContrato;
    
    @Column(name = "CONTEUDO_BASE64", columnDefinition = "TEXT")
    private String conteudoBase64;
    
    @Column(name = "TAMANHO_BYTES")
    private Integer tamanhoBytes;
    
    @Column(name = "DT_UPLOAD", nullable = false)
    private LocalDateTime dtUpload;
    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ContratoEntidade.class)
    @JoinColumn(name = "ID_CONTRATO_ENTIDADE", nullable = true, referencedColumnName = "ID_CONTRATO_ENTIDADE", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_ARQ_CONTRATO_ENTIDADE"))
    private ContratoEntidade contratoEntidade;
    
    @PrePersist
    protected void onCreate() {
        dtUpload = LocalDateTime.now();
    }
}
