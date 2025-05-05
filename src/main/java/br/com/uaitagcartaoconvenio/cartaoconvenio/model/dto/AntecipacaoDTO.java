package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusAntecipacao;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AntecipacaoDTO {

    private Long id;
    private LocalDateTime dataSolicitacao;
    private BigDecimal valorAntecipacao;
    private String responsavelSolicitacao;
    private String nomeArquivoDocumentoBanco;
    private StatusAntecipacao status;
    private String conteudoBase64;
    private Integer tamanhoBytes;
    private LocalDateTime dataUpload;
    private BigDecimal taxaNominal;
    private BigDecimal taxaDia;
    private BigDecimal taxaPeriodo;
    private LocalDate dataCorte;
    private LocalDate dataPagamento;
    private LocalDate dataVencimentoOriginal;
    private Long quantidadeDias;
    private BigDecimal valorTaxa;
    private BigDecimal valorOriginal;
    private Long cicloPagamentoVendaId; // Apenas o ID para evitar complexidade
}
