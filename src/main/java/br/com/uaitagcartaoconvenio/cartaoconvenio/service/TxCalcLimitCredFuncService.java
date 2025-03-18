package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaCalcLimiteCredFuncionaro;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaCalcLimiteCreditoFunc;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TxCalcLimitCredFuncRepositore;

@Service
public class TxCalcLimitCredFuncService {

	@Autowired
    private TxCalcLimitCredFuncRepositore txCalcLimitCredFuncRepositore;
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public BigDecimal getCalculoLimiteCredito(Long idEntidade, BigDecimal salarioBase) {
		Double vlrCalc = ( salarioBase.doubleValue() * txCalcLimitCredFuncRepositore.getTxCalcLimitCredFunc(idEntidade).doubleValue()) / 100 ;
		
		return new BigDecimal(vlrCalc);
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<TaxaCalcLimiteCreditoFunc> getListaTaxaCalcLimiteCreditoFuncByDescStatusPagamento( StatusTaxaCalcLimiteCredFuncionaro statusTaxaCalcLimiteCredFuncionaro )  {
		
		List<TaxaCalcLimiteCreditoFunc> listaTaxaCalcLimiteCreditoFunc = txCalcLimitCredFuncRepositore.listaTaxaCalcLimiteCreditoFuncByDescStatusPagamento( statusTaxaCalcLimiteCredFuncionaro );
		
		return listaTaxaCalcLimiteCreditoFunc;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<TaxaCalcLimiteCreditoFunc> getlistaTaxaCalcLimiteCreditoFuncByNomeFuncionario( String nomeFuncionario )  {
		
		List<TaxaCalcLimiteCreditoFunc> listaCalcLimiteCreditoFunc = txCalcLimitCredFuncRepositore.listaTaxaCalcLimiteCreditoFuncByNomeFuncionario( nomeFuncionario );
		
		return listaCalcLimiteCreditoFunc;
		
	}


}
