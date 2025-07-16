package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import br.com.uaitagcartaoconvenio.cartaoconvenio.excecoes.BusinessException;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaConveniadosRepository;

@Service
public class TaxaConveniadosService {

	
	@Autowired
	private TaxaConveniadosRepository taxaConveniadosRepository;
	
	public TaxaConveniados salvarTaxaConveniados(TaxaConveniados txConveiniados) {
		
		taxaConveniadosRepository.saveAndFlush(txConveiniados);

		return txConveiniados;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public TaxaConveniados getTaxaConveniadosByIdConveniados( Long idConveniados )  {
		
		TaxaConveniados taxaConveniados = taxaConveniadosRepository.taxaConveniadosByIdConveniados( idConveniados );
		
		return taxaConveniados;
		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public List<TaxaConveniados> getListaTaxaConveniadosByStatusTaxaConv( StatusTaxaConv descStatusTaxaCon )  {
		
		List<TaxaConveniados> listaTaxaConveniados = taxaConveniadosRepository.listaTaxaConveniadosByStatusTaxaConv( descStatusTaxaCon );
		
		return listaTaxaConveniados;
		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public TaxaConveniados getTaxaConveniadosAtualByIdConveniados( Long idConveniados )  {
		
		TaxaConveniados taxaConveniados = taxaConveniadosRepository.taxaConveniadosAtualByIdConveniados( idConveniados );
		
		return taxaConveniados;
		
	}

	
    @Transactional(readOnly = true)
    public List<TaxaConveniados> findByConveniadosIdAndStatus(Long idConveniados, StatusTaxaConv status) {
        return taxaConveniadosRepository.findByConveniadosIdAndStatus(idConveniados, status);
    }

    @Transactional
    public TaxaConveniados atualizarTaxa(Long idConveniados, BigDecimal novaTaxa) {
        // Busca a taxa atual
        TaxaConveniados taxaAtual = taxaConveniadosRepository.taxaConveniadosAtualByIdConveniados(idConveniados);
        
        if (taxaAtual == null) {
            throw new BusinessException("Nenhuma taxa ATUAL encontrada para a conveniada com ID: " + idConveniados);
        }

        // Muda status da taxa atual para DESATUALIZADA
        taxaAtual.setDescStatusTaxaCon(StatusTaxaConv.DESATUALIZADA);
        taxaConveniadosRepository.save(taxaAtual);

        // Cria nova taxa com status ATUAL
        TaxaConveniados novaTaxaConveniados = new TaxaConveniados();
        novaTaxaConveniados.setTaxa(novaTaxa);
        novaTaxaConveniados.setDescStatusTaxaCon(StatusTaxaConv.ATUAL);
        novaTaxaConveniados.setConveniados(taxaAtual.getConveniados());
        
        return taxaConveniadosRepository.save(novaTaxaConveniados);
    }

    @Transactional
    public TaxaConveniados atualizarStatusTaxa(Long idConveniados, StatusTaxaConv novoStatus) {
        if (novoStatus != StatusTaxaConv.BLOQUEADA && novoStatus != StatusTaxaConv.AGUARDANDO_APROVACAO) {
            throw new BusinessException("Status permitidos para atualização: BLOQUEADA ou AGUARDANDO_APROVACAO");
        }

        // Busca a última taxa (maior ID)
        List<TaxaConveniados> taxas = taxaConveniadosRepository.findLastByConveniadosId(idConveniados);
        if (taxas.isEmpty()) {
            throw new BusinessException("Nenhuma taxa encontrada para a conveniada com ID: " + idConveniados);
        }

        TaxaConveniados ultimaTaxa = taxas.get(0);
        ultimaTaxa.setDescStatusTaxaCon(novoStatus);
        
        return taxaConveniadosRepository.save(ultimaTaxa);
    }

}
