package br.com.uaitagcartaoconvenio.cartaoconvenio.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaConveniadaEntidadeRepository;
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
	
	@Autowired
	private  TaxaConveniadaEntidadeRepository taxaConveniadaEntidadeRepository;

    // Função principal para converter a lista de DTOs
    public List<Map<String, String>> convertCPVListToMapList( List<CicloPagamentoVenda> objs ) {
        
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
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance( new Locale("pt", "BR") );
        
        // Popula o Map com os valores formatados
        row.put("nome"          , getNameConveniados(obj.getConveniados().getIdConveniados()) ); 
        row.put("periodo"       , obj.getAnoMes()                                             );
        row.put("valor"         , currencyFormat.format( obj.getVlrCicloBruto()     )         );
        row.put("taxaValor"     , currencyFormat.format( obj.getVlrTaxaSecundaria() )         );
        row.put("taxaPercentual", validaTaxaPercentual ( obj                        )         );
        
        return row;
    }
    
    
    private String validaTaxaPercentual(CicloPagamentoVenda obj) {
        if (obj == null) {
            return formatPercent(BigDecimal.ZERO, "DF");
        }

        BigDecimal taxa = null;
        String origin = "Taxa Conveniados"; // TaxaConveniados

        if (obj.getTaxaConveniados() != null) {
            taxa = obj.getTaxaConveniados().getTaxa();
        } else if (obj.getIdTaxaConveniadosEntidate() != null) {
            taxa = taxaConveniadaEntidadeRepository.getTaxaConvEnti(obj.getIdTaxaConveniadosEntidate());
            origin = "Taxa Conveniada especifica para a Entidade"; // TaxaConveniadaEntidade
        }

        return formatPercent(taxa != null ? taxa : BigDecimal.ZERO, origin);
    }

    private String formatPercent(BigDecimal percentual, String originMarker) {
        // Padrão: até 3 dígitos antes da vírgula e exatamente 2 após
        DecimalFormat df = new DecimalFormat("###0.00%");
        df.setRoundingMode(RoundingMode.HALF_UP);
        
        // Divide por 100 se o valor for > 1 (assumindo que valores > 1 são percentuais "crus")
        BigDecimal valorFormatado = percentual.compareTo(BigDecimal.ONE) > 0 
            ? percentual.divide(new BigDecimal("100")) 
            : percentual;
        
        String formattedValue = df.format(valorFormatado);
        
        return String.format("%s [%s]", formattedValue.replace(".", ","), originMarker);
    }  
/*    
    // Método para obter o nome da entidade (você precisará implementar)
    private String getConveniadosName(Long idConveniados) {
    	Conveniados conveniado = conveniadosService.getConveniadoId(idConveniados);
    	if(conveniado.getPessoa().getNomePessoa() != null)
           return conveniado.getPessoa().getNomePessoa();
    	else return "Conveniada não encontrada";
    }
*/ 
    private String getNameConveniados(Long idConveniados) {
    	String conveniado = conveniadosService.getNomeConveniada(idConveniados);
    	return conveniado;
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
 /*   
    private String formatPercent(BigDecimal percentual) {
        if (percentual == null) {
            percentual = BigDecimal.ZERO;
        }
        
        DecimalFormat df = new DecimalFormat("#,##0.00%");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(percentual.movePointRight(2));
    }
 */   
    private String formatPercent(BigDecimal percentual ) {
        // Padrão: até 3 dígitos antes da vírgula e exatamente 2 após
        DecimalFormat df = new DecimalFormat("###0.00%");
        df.setRoundingMode(RoundingMode.HALF_UP);
        
        // Divide por 100 se o valor for > 1 (assumindo que valores > 1 são percentuais "crus")
        BigDecimal valorFormatado = percentual.compareTo(BigDecimal.ONE) > 0 
            ? percentual.divide(new BigDecimal("100")) 
            : percentual;
        
        String formattedValue = df.format(valorFormatado);
        
        return String.format("%s", formattedValue.replace(".", ","));
    }
}
