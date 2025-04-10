package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idConveniados")
public class ConveniadosResumoDTO {
    private Long idConveniados;
    private String nome; // Assumindo que Conveniados tem um nome via Pessoa

}
