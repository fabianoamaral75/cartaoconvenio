package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

public class NichoDTO {
    private Long idNicho;
    private String descNicho;
    private Long idConveniados;

    // Getters e Setters
    public Long getIdNicho() {
        return idNicho;
    }

    public void setIdNicho(Long idNicho) {
        this.idNicho = idNicho;
    }

    public String getDescNicho() {
        return descNicho;
    }

    public void setDescNicho(String descNicho) {
        this.descNicho = descNicho;
    }

    public Long getIdConveniados() {
        return idConveniados;
    }

    public void setIdConveniados(Long idConveniados) {
        this.idConveniados = idConveniados;
    }
}
