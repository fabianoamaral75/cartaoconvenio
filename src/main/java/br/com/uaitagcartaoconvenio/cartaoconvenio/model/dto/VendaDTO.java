package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusRestabeleceLimiteCredito;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaPg;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaReceb;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendas;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idVenda")
public class VendaDTO {

    private Long idVenda;
    private BigDecimal valorVenda;
    private BigDecimal valorCalcTaxaConveniado;
    private BigDecimal valorCalcTaxaEntidade;
    private String anoMes;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtVenda;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtAlteracao;
    private String loginUser;
    private StatusVendaReceb descStatusVendaReceb;
    private StatusVendaPg descStatusVendaPg;
    private StatusVendas descStatusVendas;
    private StatusRestabeleceLimiteCredito descRestLimiteCredito;
    private ConveniadosResumoDTO conveniados = new ConveniadosResumoDTO();
    private TaxaConveiniadosResumoDTO taxaConveiniados = new TaxaConveiniadosResumoDTO();
    private TaxaEntidadeResumoDTO taxaEntidade = new TaxaEntidadeResumoDTO();
    private CartaoResumoDTO cartao = new CartaoResumoDTO();
    private List<ItensVendaDTO> itensVenda = new ArrayList<ItensVendaDTO>();

}
