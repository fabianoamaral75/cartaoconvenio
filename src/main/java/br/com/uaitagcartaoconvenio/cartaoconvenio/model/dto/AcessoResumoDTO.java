package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.RoleAcesso;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idAcesso")
public class AcessoResumoDTO {
    private Long idAcesso;
    private RoleAcesso descAcesso;

    // Getters e Setters
}
