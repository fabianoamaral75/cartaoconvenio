package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Nicho;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Pessoa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.RamoAtividade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveiniados;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idConveniados")
public class ConveniadosDTO {
	private Long idConveniados;	
	private Date dtCriacao = Calendar.getInstance().getTime();
	private Date  dtAlteracao = Calendar.getInstance().getTime();
	private String site;
	private String obs;	
	private StatusConveniada descStatusConveniada;
	private Nicho nicho;
	private RamoAtividade ramoAtividade;
	private List<CicloPagamentoVenda> CicloPagamentoVenda = new ArrayList<CicloPagamentoVenda>();
	private List<TaxaConveiniados> taxaConveiniados = new ArrayList<TaxaConveiniados>();
	private Pessoa pessoa = new Pessoa();

}
