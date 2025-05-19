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
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaConveniadosMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaConveniadosDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TaxaConveniadosService;

@Controller
public class TaxaConveniadosController {

	@Autowired
	private TaxaConveniadosService taxaConveniadosService;
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaTaxaConveniadosByStatusTaxaConv/{status}")
	public ResponseEntity<List<TaxaConveniadosDTO>> getListaTaxaConveniadosByStatusTaxaConv( @PathVariable("status") String status) throws ExceptionCustomizada{

		StatusTaxaConv statusTaxaCon = StatusTaxaConv.valueOf(status);
		
		List<TaxaConveniados> listaTaxaConveniados = taxaConveniadosService.getListaTaxaConveniadosByStatusTaxaConv( statusTaxaCon );
		
		if(listaTaxaConveniados == null) {
			throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para o Status: " + StatusCicloPgVenda.valueOf(status).getDescStatusReceber() );
		}
		
		List<TaxaConveniadosDTO> dto = TaxaConveniadosMapper.INSTANCE.toListDto(listaTaxaConveniados); 
		
		return new ResponseEntity<List<TaxaConveniadosDTO>>(dto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaTaxaConveniadosByStatusTaxaConv/{idConveniados}")
	public ResponseEntity<TaxaConveniadosDTO> getListaTaxaConveniadosByStatusTaxaConv( @PathVariable("idConveniados") Long idConveniados ) throws ExceptionCustomizada{

		TaxaConveniados taxaConveniados = taxaConveniadosService.getTaxaConveniadosByIdConveniados( idConveniados );
		
		if(taxaConveniados == null) {
			throw new ExceptionCustomizada("Não existe Ciclo de Pagamento para a ID da Conveniada: " + idConveniados );
		}
		
		TaxaConveniadosDTO dto = TaxaConveniadosMapper.INSTANCE.toDto(taxaConveniados);
		
		return new ResponseEntity<TaxaConveniadosDTO>(dto, HttpStatus.OK);		
	}

}
