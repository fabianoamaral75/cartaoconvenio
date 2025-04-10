package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaCalcLimiteCredFuncionaro;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaCalcLimiteCreditoFuncMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaCalcLimiteCreditoFunc;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaCalcLimiteCreditoFuncDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TxCalcLimitCredFuncService;

@RestController
public class TxCalcLimitCredFuncController {

	@Autowired
	private TxCalcLimitCredFuncService txCalcLimitCredFuncService;
	
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaTaxaCalcLimiteCreditoFuncByDescStatusPagamento/{status}")
	public ResponseEntity<List<TaxaCalcLimiteCreditoFuncDTO>> getListaTaxaCalcLimiteCreditoFuncByDescStatusPagamento( @PathVariable("status") String status) throws ExceptionCustomizada{

		StatusTaxaCalcLimiteCredFuncionaro statusTaxaCalcLimite = StatusTaxaCalcLimiteCredFuncionaro.valueOf(status);
		
		List<TaxaCalcLimiteCreditoFunc> listTaxaCalcLimiteCreditoFunc = txCalcLimitCredFuncService.getListaTaxaCalcLimiteCreditoFuncByDescStatusPagamento( statusTaxaCalcLimite );
		
		if(listTaxaCalcLimiteCreditoFunc == null) {
			throw new ExceptionCustomizada("Não existe Taxa para o Status: " + StatusTaxaCalcLimiteCredFuncionaro.valueOf(status).getDescTaxaCalcLimiteCredFuncionaro() );
		}
		
		List<TaxaCalcLimiteCreditoFuncDTO> dto = TaxaCalcLimiteCreditoFuncMapper.INSTANCE.toListDto(listTaxaCalcLimiteCreditoFunc);
		
		return new ResponseEntity<List<TaxaCalcLimiteCreditoFuncDTO>>(dto, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getCicloPagamentoVendaByNomeConveniado/{nomeFuncionario}")
	public ResponseEntity<List<TaxaCalcLimiteCreditoFuncDTO>> getCicloPagamentoVendaByNomeConveniado( @PathVariable("nomeFuncionario") String nomeFuncionario) throws ExceptionCustomizada{

		List<TaxaCalcLimiteCreditoFunc> listTaxaCalcLimiteCreditoFunc = txCalcLimitCredFuncService.getlistaTaxaCalcLimiteCreditoFuncByNomeFuncionario( nomeFuncionario );
		
		if(listTaxaCalcLimiteCreditoFunc == null) {
			throw new ExceptionCustomizada("Não existe Taxa para o Funcionarário: " + nomeFuncionario );
		}
		
		List<TaxaCalcLimiteCreditoFuncDTO> dto = TaxaCalcLimiteCreditoFuncMapper.INSTANCE.toListDto(listTaxaCalcLimiteCreditoFunc);
		
		return new ResponseEntity<List<TaxaCalcLimiteCreditoFuncDTO>>(dto, HttpStatus.OK);		
	}

}
