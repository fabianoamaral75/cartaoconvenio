package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;


public class FechamentoConvItensVendasCreateDTO {
    private Long idCicloPagamentoVenda;
    private Long idVenda;

    // Getters e Setters
    public Long getIdCicloPagamentoVenda() {
        return idCicloPagamentoVenda;
    }

    public void setIdCicloPagamentoVenda(Long idCicloPagamentoVenda) {
        this.idCicloPagamentoVenda = idCicloPagamentoVenda;
    }

    public Long getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Long idVenda) {
        this.idVenda = idVenda;
    }
}
