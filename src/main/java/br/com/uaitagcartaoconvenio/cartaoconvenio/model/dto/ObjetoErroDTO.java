package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObjetoErroDTO implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	
	private String error;
	private String code;

}
