package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "TIPO_PERIODO")
public class TipoPeriodo {

    @Id
    @EqualsAndHashCode.Include
    @SequenceGenerator(name = "seq_id_tipo_periodo", sequenceName = "seq_id_tipo_periodo", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_tipo_periodo")
    @Column(name = "ID_TIPO_PERIODO", nullable = false)
    private Long id;

    @Column(name = "DESC_TIPO_PERIODO", length = 200, nullable = false)
    private String descricao;
    
    /*
     	P --> Cobrança Periodica
		U --> Cobrança Unica
		M --> Cobrança Mensal
		A --> Cobrança Anaul
		D --> Cobrança Adintamento
		F --> Cobrança por Fechamnto
     */
    @Column(name = "TIPO", columnDefinition = "char(1) default 'U' check (TIPO in ('P', 'U', 'M', 'A', 'D', 'F'))", nullable = false)
    private String tipo;

    @CreationTimestamp
    @Column(name = "DT_CRIACAO", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "tipoPeriodo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PeriodoCobrancaTaxa> periodosCobranca = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
    	dataCriacao = LocalDateTime.now();
    }
}