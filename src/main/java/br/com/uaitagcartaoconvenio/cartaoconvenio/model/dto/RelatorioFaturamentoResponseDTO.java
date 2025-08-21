package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.util.Date;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusRelatorioFaturamento;
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
public class RelatorioFaturamentoResponseDTO {
    private Long idRelatorioFaturamentoConveniado;
    private Long idCicloPagamentoVenda;
    private Long idConveniados;
    private String anoMes;
    private String nomeArquivo;
    private Long tamanhoBytes;
    private StatusRelatorioFaturamento status;
    private Date dtCriacao;
    private String observacao;
    private String downloadUrl;
}