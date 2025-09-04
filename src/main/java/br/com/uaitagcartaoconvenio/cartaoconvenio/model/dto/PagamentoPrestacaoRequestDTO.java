//PagamentoPrestacaoRequestDTO.java
package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoPrestacaoRequestDTO {
	
 @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
 private Date dtPagamento;
 private String observacao;
 
}
