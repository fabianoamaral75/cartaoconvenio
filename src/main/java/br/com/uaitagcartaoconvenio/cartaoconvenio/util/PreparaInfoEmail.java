package br.com.uaitagcartaoconvenio.cartaoconvenio.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaEntidadeRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.ConveniadosService;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.EntidadeService;

@Component
public class PreparaInfoEmail {
	
	@Autowired
	private ConveniadosService conveniadosService;
	
	@Autowired
	private  EntidadeService entidadeService;
	
	@Autowired
	private  TaxaEntidadeRepository taxaEntidadeRepository;

    // Função principal para converter a lista de DTOs
    public List<Map<String, String>> convertCPVListToMapList(
            List<CicloPagamentoVenda> objs) {
        
        List<Map<String, String>> result = new ArrayList<>();
        
        for (CicloPagamentoVenda obj : objs) {
            result.add(createRowMap(obj));
        }        
        return result;
    }

    // Cria um Map representando uma linha da tabela
    private Map<String, String> createRowMap(CicloPagamentoVenda obj) {
        Map<String, String> row = new HashMap<>();
        
        // Formatação de valores monetários
        @SuppressWarnings("deprecation")
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        
        // Popula o Map com os valores formatados
        row.put("nome"     , getConveniadosName(obj.getConveniados().getIdConveniados())); 
        row.put("periodo"  , obj.getAnoMes());
        row.put("valor"    , currencyFormat.format( obj.getValorCiclo()                   ) );
        row.put("taxaValor", currencyFormat.format( obj.getValorCalcTaxaConveniadoCiclo() ) );
        row.put("taxaPercentual", formatPercent(obj.getTaxaConveniados().getTaxa()));
        
        return row;
    }
    
    // Método auxiliar para formatar percentual
    private String formatPercent(BigDecimal percentual) {
        return String.format("%.2f%%", percentual);
    }
    
    // Método para obter o nome da entidade (você precisará implementar)
    private String getConveniadosName(Long idConveniados) {
    	Conveniados conveniado = conveniadosService.getConveniadoId(idConveniados);
        return conveniado.getPessoa().getNomePessoa(); 
    }
 
    // Método para obter o nome da entidade (você precisará implementar)
    private Entidade getEntidade(Long idEntidade) {
    	Entidade entidade = entidadeService.getEntidadesId(idEntidade);
        return entidade; 
    }
    
    // Método para obter o nome da entidade (você precisará implementar)
    private TaxaEntidade getTaxaEntidade(Long idTaxa) {
    	TaxaEntidade taxaEntidade = taxaEntidadeRepository.findById(idTaxa).orElse(null);
        return taxaEntidade; 
    }
  
    // Função principal para converter a lista de DTOs
    public List<Map<String, String>> convertCRListToMapList(
            List<ContasReceber> objs) {
        
        List<Map<String, String>> result = new ArrayList<>();
        
        for (ContasReceber obj : objs) {
            result.add(createRowMapCR(obj));
        }        
        return result;
    }
   
    // Cria um Map representando uma linha da tabela
    private Map<String, String> createRowMapCR(ContasReceber obj) {
        Map<String, String> row = new HashMap<>();
        
        // Formatação de valores monetários
        @SuppressWarnings("deprecation")
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        Entidade entidade = getEntidade( obj.getEntidade().getIdEntidade() );
        TaxaEntidade taxaEntidade = getTaxaEntidade(obj.getTaxaEntidade().getIdTaxaEntidade());
        
        // Popula o Map com os valores formatados
        row.put("nome"     , entidade.getNomeEntidade()); 
        row.put("periodo"  , obj.getAnoMes());
        row.put("valor"    , currencyFormat.format( obj.getValorReceber()                   ) );
        row.put("taxaValor", currencyFormat.format( obj.getValorCalcTaxaEntidadeCiclo() ) );
        row.put("taxaPercentual", formatPercent( taxaEntidade.getTaxaEntidade() ) );
        
        return row;
    }
}
