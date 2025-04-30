package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

public class UsuarioAcessoDTO {
    private Long idUsuarioAcesso;
    private AcessoResumoDTO acesso   = new AcessoResumoDTO();
    private UsuarioResumoDTO usuario = new UsuarioResumoDTO();

    // Getters e Setters
    public Long getIdUsuarioAcesso() {
        return idUsuarioAcesso;
    }

    public void setIdUsuarioAcesso(Long idUsuarioAcesso) {
        this.idUsuarioAcesso = idUsuarioAcesso;
    }

    public AcessoResumoDTO getAcesso() {
        return acesso;
    }

    public void setAcesso(AcessoResumoDTO acesso) {
        this.acesso = acesso;
    }

    public UsuarioResumoDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioResumoDTO usuario) {
        this.usuario = usuario;
    }
}