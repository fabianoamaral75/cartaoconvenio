package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.excecoes.BusinessException;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaEntidadeRepository;



@Service
public class TaxaEntidadeService {

	@Autowired
	private TaxaEntidadeRepository taxaEntidadeRepository;
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<TaxaEntidade> getListaTaxaEntidadeByStatusTaxaEntidade( StatusTaxaEntidade statusTaxaEntidade )  {
		
		List<TaxaEntidade> listaTaxaEntidade = taxaEntidadeRepository.listaTaxaEntidadeByStatusTaxaEntidade( statusTaxaEntidade );
		
		return listaTaxaEntidade;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<TaxaEntidade> getTaxaEntidadeByIdEntidade( Long idEntidade )  {
		
		List<TaxaEntidade> listaTaxaEntidade = taxaEntidadeRepository.taxaEntidadeByIdEntidade( idEntidade );
		
		return listaTaxaEntidade;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public TaxaEntidade getTaxaEntidadeAtualByIdEntidade( Long idEntidade )  {
		
		TaxaEntidade taxaEntidade = taxaEntidadeRepository.taxaEntidadeAtualByIdEntidade( idEntidade );
		
		return taxaEntidade;
		
	}

    @Transactional(readOnly = true)
    public List<TaxaEntidade> findByEntidadeIdAndStatus(Long idEntidade, StatusTaxaEntidade status) {
        return taxaEntidadeRepository.findByEntidadeIdAndStatus(idEntidade, status);
    }

    @Transactional
    public TaxaEntidade atualizarTaxa(Long idEntidade, BigDecimal novaTaxa) {
        // Busca a taxa atual
        TaxaEntidade taxaAtual = taxaEntidadeRepository.taxaEntidadeAtualByIdEntidade(idEntidade);
        
        if (taxaAtual == null) {
            throw new BusinessException("Nenhuma taxa ATUAL encontrada para a entidade com ID: " + idEntidade);
        }

        // Muda status da taxa atual para DESATUALIZADA
        taxaAtual.setStatusTaxaEntidade(StatusTaxaEntidade.DESATUALIZADA);
        taxaEntidadeRepository.save(taxaAtual);

        // Cria nova taxa com status ATUAL
        TaxaEntidade novaTaxaEntidade = new TaxaEntidade();
        novaTaxaEntidade.setTaxaEntidade(novaTaxa);
        novaTaxaEntidade.setStatusTaxaEntidade(StatusTaxaEntidade.ATUAL);
        novaTaxaEntidade.setEntidade(taxaAtual.getEntidade());
        
        return taxaEntidadeRepository.save(novaTaxaEntidade);
    }

    @Transactional
    public TaxaEntidade atualizarStatusTaxa(Long idEntidade, StatusTaxaEntidade novoStatus) {
        if (novoStatus != StatusTaxaEntidade.BLOQUEADA && novoStatus != StatusTaxaEntidade.AGUARDANDO_APROVACAO) {
            throw new BusinessException("Status permitidos para atualização: BLOQUEADA ou AGUARDANDO_APROVACAO");
        }

        // Busca a última taxa (maior ID)
        List<TaxaEntidade> taxas = taxaEntidadeRepository.findLastByEntidadeId(idEntidade);
        if (taxas.isEmpty()) {
            throw new BusinessException("Nenhuma taxa encontrada para a entidade com ID: " + idEntidade);
        }

        TaxaEntidade ultimaTaxa = taxas.get(0);
        ultimaTaxa.setStatusTaxaEntidade(novoStatus);
        
        return taxaEntidadeRepository.save(ultimaTaxa);
    }
}
