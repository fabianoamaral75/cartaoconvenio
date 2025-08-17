package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AntecipacaoVendaDTO {

    private Long idAntecipacaoVenda;
    private Long idAntecipacao;
    private Long idVenda;
}