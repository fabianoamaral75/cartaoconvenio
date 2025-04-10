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
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaEntidadeMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TaxaEntidadeService;

@Controller
public class TaxaEntidadeController {

	@Autowired
	private TaxaEntidadeService taxaEntidadeService;

	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaTaxaEntidadeByStatusTaxaEntidade/{status}")
	public ResponseEntity<List<TaxaEntidadeDTO>> getListaTaxaEntidadeByStatusTaxaEntidade( @PathVariable("status") String status) throws ExceptionCustomizada{

		StatusTaxaEntidade statusTaxaEntidade = StatusTaxaEntidade.valueOf(status);
		
		List<TaxaEntidade> listTaxaEntidade = taxaEntidadeService.getListaTaxaEntidadeByStatusTaxaEntidade( statusTaxaEntidade );
		
		if(listTaxaEntidade == null) {
			throw new ExceptionCustomizada("Não existe Taxa para a Entidade para o Status: " + StatusTaxaEntidade.valueOf(status).getDescStatusTaxaEntidade() );
		}
		
		List<TaxaEntidadeDTO> dto = TaxaEntidadeMapper.INSTANCE.toListDto(listTaxaEntidade); 
		
		return new ResponseEntity<List<TaxaEntidadeDTO>>(dto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getTaxaEntidadeByIdEntidade/{idEntidade}")
	public ResponseEntity<List<TaxaEntidadeDTO>> getTaxaEntidadeByIdEntidade( @PathVariable("idEntidade") Long idEntidade ) throws ExceptionCustomizada{

		List<TaxaEntidade> listTaxaEntidade = taxaEntidadeService.getTaxaEntidadeByIdEntidade( idEntidade );
		
		if(listTaxaEntidade == null) {
			throw new ExceptionCustomizada("Não existe Taxa para a Entidade com o ID dasta Entidade: " + idEntidade );
		}
		
		List<TaxaEntidadeDTO> dto = TaxaEntidadeMapper.INSTANCE.toListDto(listTaxaEntidade); 
		
		return new ResponseEntity<List<TaxaEntidadeDTO>>( dto, HttpStatus.OK);		
	}

}
