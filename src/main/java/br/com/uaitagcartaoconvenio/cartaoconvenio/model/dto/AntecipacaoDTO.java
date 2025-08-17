package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusAntecipacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AntecipacaoDTO {

    private Long idAntecipacao;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtCriacao;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtAlteracao;
    private BigDecimal taxaMes;
    private BigDecimal taxaDia;
    private BigDecimal taxaPeriodo;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dtCorte;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dtPagamento;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dtVencimento;
    private Integer periodoDias;
    private BigDecimal valorDesconto;
    private BigDecimal valorNominal;
    private BigDecimal valorBase;
    private String observacao;
    private String nomeArquivoAprovacao;
    private String conteudoBase64Aprovacao;
    private String nomeArquivoComprovante;
    private String conteudoBase64Comprovante;
    private Long tamanhoBytesAprovacao;
    private Long tamanhoBytesComprovante;
    private String loginUser;
    private StatusAntecipacao descStatusAntecipacao;
    private Long idConveniados;
    private Long idCicloPagamentoVenda;
    private List<Long> idsVendas;
    
}
