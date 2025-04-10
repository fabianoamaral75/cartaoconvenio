package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;


public class RamoAtividadeDTO {
    private Long idRamoAtividade;
    private String descRamoAtividade;
    private Long idConveniados;

    // Getters e Setters
    public Long getIdRamoAtividade() {
        return idRamoAtividade;
    }

    public void setIdRamoAtividade(Long idRamoAtividade) {
        this.idRamoAtividade = idRamoAtividade;
    }

    public String getDescRamoAtividade() {
        return descRamoAtividade;
    }

    public void setDescRamoAtividade(String descRamoAtividade) {
        this.descRamoAtividade = descRamoAtividade;
    }

    public Long getIdConveniados() {
        return idConveniados;
    }

    public void setIdConveniados(Long idConveniados) {
        this.idConveniados = idConveniados;
    }
}
