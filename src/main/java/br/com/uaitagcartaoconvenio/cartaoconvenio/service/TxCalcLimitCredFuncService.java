package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaCalcLimiteCredFuncionaro;
import br.com.uaitagcartaoconvenio.cartaoconvenio.excecoes.BusinessException;
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

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @Transactional(readOnly = true)
    public List<TaxaCalcLimiteCreditoFunc> findAllByEntidadeId(Long idEntidade) {
        return txCalcLimitCredFuncRepositore.findAllByEntidadeId(idEntidade);
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    @Transactional(readOnly = true)
    public TaxaCalcLimiteCreditoFunc findAtualByEntidadeId(Long idEntidade) {
        return txCalcLimitCredFuncRepositore.findAtualByEntidadeId(idEntidade)
                .orElseThrow(() -> new BusinessException("Nenhuma taxa ATUAL encontrada para a entidade com ID: " + idEntidade));
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Transactional(readOnly = true)
    public List<TaxaCalcLimiteCreditoFunc> findByEntidadeIdAndStatus(Long idEntidade, StatusTaxaCalcLimiteCredFuncionaro status) {
        return txCalcLimitCredFuncRepositore.findByEntidadeIdAndStatus(idEntidade, status);
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Transactional
    public TaxaCalcLimiteCreditoFunc atualizarTaxaBase(Long idEntidade, BigDecimal novaTaxaBase) {
        // Busca a taxa atual
        TaxaCalcLimiteCreditoFunc taxaAtual = findAtualByEntidadeId(idEntidade);

        // Muda status da taxa atual para DESATUALIZADA
        taxaAtual.setStatusTaxaCalcLimiteCredFuncionaro(StatusTaxaCalcLimiteCredFuncionaro.DESATUALIZADA);
        txCalcLimitCredFuncRepositore.save(taxaAtual);

        // Cria nova taxa com status ATUAL
        TaxaCalcLimiteCreditoFunc novaTaxa = new TaxaCalcLimiteCreditoFunc();
        novaTaxa.setTaxaBase(novaTaxaBase);
        novaTaxa.setStatusTaxaCalcLimiteCredFuncionaro(StatusTaxaCalcLimiteCredFuncionaro.ATUAL);
        novaTaxa.setEntidade(taxaAtual.getEntidade());
        
        return txCalcLimitCredFuncRepositore.save(novaTaxa);
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
   @Transactional
    public TaxaCalcLimiteCreditoFunc atualizarStatusTaxa(Long idEntidade, StatusTaxaCalcLimiteCredFuncionaro novoStatus) {
        if (novoStatus != StatusTaxaCalcLimiteCredFuncionaro.CANCELA && 
            novoStatus != StatusTaxaCalcLimiteCredFuncionaro.AGUARDANDO_APROVACAO) {
            throw new BusinessException("Status permitidos para atualização: CANCELA ou AGUARDANDO_APROVACAO");
        }

        // Busca a última taxa (maior ID)
        List<TaxaCalcLimiteCreditoFunc> taxas = txCalcLimitCredFuncRepositore.findLastByEntidadeId(idEntidade);
        if (taxas.isEmpty()) {
            throw new BusinessException("Nenhuma taxa encontrada para a entidade com ID: " + idEntidade);
        }

        TaxaCalcLimiteCreditoFunc ultimaTaxa = taxas.get(0);
        ultimaTaxa.setStatusTaxaCalcLimiteCredFuncionaro(novoStatus);
        
        return txCalcLimitCredFuncRepositore.save(ultimaTaxa);
    }
	

}
