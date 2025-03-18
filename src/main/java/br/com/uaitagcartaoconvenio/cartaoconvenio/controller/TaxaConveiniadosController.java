package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveiniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TaxaConveiniadosService;

@Controller
public class TaxaConveiniadosController {

	@Autowired
	private TaxaConveiniadosService taxaConveiniadosService;
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaTaxaConveiniadosByStatusTaxaConv/{status}")
	public ResponseEntity<List<TaxaConveiniados>> getListaTaxaConveiniadosByStatusTaxaConv( @PathVariable("status") String status) throws ExceptionCustomizada{

		StatusTaxaConv statusTaxaCon = StatusTaxaConv.valueOf(status);
		
		List<TaxaConveiniados> listaTaxaConveiniados = taxaConveiniadosService.getListaTaxaConveiniadosByStatusTaxaConv( statusTaxaCon );
		
		if(listaTaxaConveiniados == null) {
			throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para o Status: " + StatusCicloPgVenda.valueOf(status).getDescStatusReceber() );
		}
		return new ResponseEntity<List<TaxaConveiniados>>(listaTaxaConveiniados, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaTaxaConveiniadosByStatusTaxaConv/{idConveniados}")
	public ResponseEntity<TaxaConveiniados> getListaTaxaConveiniadosByStatusTaxaConv( @PathVariable("idConveniados") Long idConveniados ) throws ExceptionCustomizada{

		TaxaConveiniados taxaConveiniados = taxaConveiniadosService.getTaxaConveiniadosByIdConveniados( idConveniados );
		
		if(taxaConveiniados == null) {
			throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para a ID da Conveniada: " + idConveniados );
		}
		return new ResponseEntity<TaxaConveiniados>(taxaConveiniados, HttpStatus.OK);		
	}

}
