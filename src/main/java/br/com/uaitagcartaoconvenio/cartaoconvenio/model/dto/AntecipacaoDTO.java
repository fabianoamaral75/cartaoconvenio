package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusAntecipacao;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AntecipacaoDTO {

    private Long id;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dataSolicitacao;
    private BigDecimal valorAntecipacao;
    private String responsavelSolicitacao;
    private String nomeArquivoDocumentoBanco;
    private StatusAntecipacao status;
    private String conteudoBase64;
    private Integer tamanhoBytes;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dataUpload;
    private BigDecimal taxaNominal;
    private BigDecimal taxaDia;
    private BigDecimal taxaPeriodo;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dataCorte;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dataPagamento;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
    private LocalDate dataVencimentoOriginal;
    private Long quantidadeDias;
    private BigDecimal valorTaxa;
    private BigDecimal valorOriginal;
    private Long cicloPagamentoVendaId; // Apenas o ID para evitar complexidade
}
