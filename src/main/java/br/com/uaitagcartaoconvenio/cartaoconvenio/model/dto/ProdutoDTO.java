package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "idProduto")
public class ProdutoDTO {
    private Long idProduto;
    
    @NotNull(message = "O nome do produto é obrigatório")
    private String produto;
    
    @NotNull(message = "O valor do produto é obrigatório")
    private BigDecimal vlrProduto;
    
    private Date dtCadastro;
    private Long idConveniado;

    // Construtores
    public ProdutoDTO() {
    }

    public ProdutoDTO(Long idProduto, String produto, BigDecimal vlrProduto, Date dtCadastro, Long idConveniado) {
        this.idProduto = idProduto;
        this.produto = produto;
        this.vlrProduto = vlrProduto;
        this.dtCadastro = dtCadastro;
        this.idConveniado = idConveniado;
    }

    // Getters e Setters
    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public BigDecimal getVlrProduto() {
        return vlrProduto;
    }

    public void setVlrProduto(BigDecimal vlrProduto) {
        this.vlrProduto = vlrProduto;
    }

    public Date getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(Date dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public Long getIdConveniado() {
        return idConveniado;
    }

    public void setIdConveniado(Long idConveniado) {
        this.idConveniado = idConveniado;
    }
}
