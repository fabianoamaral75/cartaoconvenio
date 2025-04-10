package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;


import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.RoleAcesso;

public class AcessoDTO {
    private Long idAcesso;
    private RoleAcesso descAcesso;

    // Getters e Setters
    public Long getIdAcesso() {
        return idAcesso;
    }

    public void setIdAcesso(Long idAcesso) {
        this.idAcesso = idAcesso;
    }

    public RoleAcesso getDescAcesso() {
        return descAcesso;
    }

    public void setDescAcesso(RoleAcesso descAcesso) {
        this.descAcesso = descAcesso;
    }
}