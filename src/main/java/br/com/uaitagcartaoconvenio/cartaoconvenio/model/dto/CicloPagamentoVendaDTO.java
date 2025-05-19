package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idCicloPagamentoVenda")
public class CicloPagamentoVendaDTO {
    private Long idCicloPagamentoVenda;
    private String anoMes;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtCriacao;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtAlteracao;
    private BigDecimal valorCiclo;
    private BigDecimal valorCalcTaxaConveniadoCiclo;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private Date dtPagamento;
    private String docAutenticacaoBanco;
    private String observacao;
    private String nomeArquivo;
    private String conteudoBase64;
    private Long tamanhoBytes;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dataUpload;
    private StatusCicloPgVenda descStatusPagamento;
    private ConveniadosResumoDTO conveniados = new ConveniadosResumoDTO();
    private TaxaConveniadosResumoDTO taxaConveniados = new TaxaConveniadosResumoDTO();
    private List<FechamentoConvItensVendasDTO> fechamentoConvItensVendas = new ArrayList<FechamentoConvItensVendasDTO>();
    private List<CicloTaxaExtraDTO> taxasExtras = new ArrayList<>(); // Novo campo
}
