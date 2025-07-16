package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCartao;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusCartaoDTO {
    private StatusCartao novoStatus;
}