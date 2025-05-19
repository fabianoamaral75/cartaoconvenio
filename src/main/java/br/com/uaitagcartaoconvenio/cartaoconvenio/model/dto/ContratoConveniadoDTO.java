package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

/**
 *   Modelo de DTO para todo o projeto.
 */
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO para transferência de dados de Contrato Conveniado")
public class ContratoConveniadoDTO {

    @Schema(description = "ID do contrato conveniado", example = "1")
    private Long idContratoConveniado;

    @Schema(description = "Data de criação do contrato", example = "2023-05-15T10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dtCriacao;

    @Schema(description = "Nome do arquivo do contrato", example = "contrato_123.pdf")
    private String arqContrato;
    
    @Schema(description = "Arquivo convertido para Base64", example = "Informação codificada...")
    private String conteudoBase64;

    @Schema(description = "Tamanho do arquivo em bytes", example = "1024")
    private Long tamanhoBytes;

    @Schema(description = "Data de upload do contrato", example = "2023-05-15T10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataUpload;

    @Schema(description = "Observações sobre o contrato", example = "Contrato com cláusula especial")
    private String observacao;

    @Schema(description = "ID do conveniado associado", example = "1")
    private Long idConveniados;
    
    private List<VigenciaContratoConveniadaDTO> vigencias;
    

}
