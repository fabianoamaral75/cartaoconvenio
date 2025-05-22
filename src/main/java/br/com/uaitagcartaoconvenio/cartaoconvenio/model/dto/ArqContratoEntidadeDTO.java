package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class ArqContratoEntidadeDTO {
    private Long idArqContratoEntidade;
    private String caminhoArquivo;
    private String arqContrato;
 //   private String conteudoBase64;
    private Integer tamanhoBytes;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dtUpload;

}
