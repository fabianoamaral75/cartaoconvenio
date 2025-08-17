package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
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
@EqualsAndHashCode(of = "idAntecipacaoVenda")
@SequenceGenerator(name = "seq_id_antecipacao_venda", sequenceName = "seq_id_antecipacao_venda", allocationSize = 1, initialValue = 1)
@Table(name = "ANTECIPACAO_VENDA")
public class AntecipacaoVenda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_antecipacao_venda")
    @Column(name = "ID_ANTECIPACAO_VENDA")
    private Long idAntecipacaoVenda;

    @ManyToOne
    @JoinColumn(name = "ID_ANTECIPACAO", nullable = false)
    private Antecipacao antecipacao;

    @ManyToOne
    @JoinColumn(name = "ID_VENDA", nullable = false)
    private Venda venda;
    
 // Adicionar na classe AntecipacaoVenda
    public Long getIdVenda() {
        return this.venda != null ? this.venda.getIdVenda() : null;
    }
}