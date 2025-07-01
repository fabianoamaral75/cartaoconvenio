package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.CicloPagamentoVendaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ConveniadosResumoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaConveniadosResumoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ConveniadosRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaConveniadosRepository;

@Component
public class CicloPagamentoVendaMapper {
    @Autowired
    private ConveniadosRepository conveniadosRepository;
    
    @Autowired
    private TaxaConveniadosRepository taxaConveniadosRepository;

    // Converte DTO para Entidade
    public CicloPagamentoVenda toEntity(CicloPagamentoVendaDTO dto) {
    	CicloPagamentoVenda cicloPagamentoVenda = new CicloPagamentoVenda();
    	cicloPagamentoVenda.setIdCicloPagamentoVenda    ( dto.getIdCicloPagamentoVenda()     );
    	cicloPagamentoVenda.setAnoMes                   ( dto.getAnoMes()                    );    	
    	cicloPagamentoVenda.setDtCriacao                ( dto.getDtCriacao()                 );
    	cicloPagamentoVenda.setDtAlteracao              ( dto.getDtAlteracao()               );

    	cicloPagamentoVenda.setVlrCicloBruto            ( dto.getVlrCicloBruto()             );
    	cicloPagamentoVenda.setVlrTaxaSecundaria        ( dto.getVlrTaxaSecundaria()         );
    	cicloPagamentoVenda.setVlrLiquido               ( dto.getVlrLiquido()                );
    	cicloPagamentoVenda.setVlrTaxaExtraPercentual   ( dto.getVlrTaxaExtraPercentual()    );
    	cicloPagamentoVenda.setVlrTaxaExtraValor        ( dto.getVlrTaxaExtraValor()         );

    	cicloPagamentoVenda.setDtPagamento              ( dto.getDtPagamento()               );    	
    	cicloPagamentoVenda.setNomeArquivo              ( dto.getNomeArquivo()               );    	
    	cicloPagamentoVenda.setConteudoBase64           ( dto.getConteudoBase64()            );
    	cicloPagamentoVenda.setTamanhoBytes             ( dto.getTamanhoBytes()              );    	
    	cicloPagamentoVenda.setDataUpload               ( dto.getDataUpload()                );
    	cicloPagamentoVenda.setDescStatusPagamento      ( dto.getDescStatusPagamento()       );   
    	cicloPagamentoVenda.setIdTaxaConveniadosEntidate( dto.getIdTaxaConveniadosEntidate() );
        
        if (dto.getConveniados().getIdConveniados() != null) {
        	cicloPagamentoVenda.setConveniados(conveniadosRepository.findById(dto.getConveniados().getIdConveniados()).orElse(null));
        }
        
        if (dto.getTaxaConveniados().getIdTaxaConveniados() != null) {
        	cicloPagamentoVenda.setTaxaConveniados( taxaConveniadosRepository.findById(dto.getTaxaConveniados().getIdTaxaConveniados() ).orElse(null)  );
        }
        
        return cicloPagamentoVenda;
    }

    // Converte Entidade para DTO
    public CicloPagamentoVendaDTO toDTO(CicloPagamentoVenda cicloPagamentoVenda) {
    	CicloPagamentoVendaDTO dto = new CicloPagamentoVendaDTO();
        dto.setIdCicloPagamentoVenda    ( cicloPagamentoVenda.getIdCicloPagamentoVenda()     );
        dto.setAnoMes                   ( cicloPagamentoVenda.getAnoMes()                    );
        dto.setDtCriacao                ( cicloPagamentoVenda.getDtCriacao()                 );
        dto.setDtAlteracao              ( cicloPagamentoVenda.getDtAlteracao()               );        

        dto.setVlrCicloBruto            ( cicloPagamentoVenda.getVlrCicloBruto()             );
        dto.setVlrTaxaSecundaria        ( cicloPagamentoVenda.getVlrTaxaSecundaria()         );
        dto.setVlrLiquido               ( cicloPagamentoVenda.getVlrLiquido()                );
        dto.setVlrTaxaExtraPercentual   ( cicloPagamentoVenda.getVlrTaxaExtraPercentual()    );
        dto.setVlrTaxaExtraValor        ( cicloPagamentoVenda.getVlrTaxaExtraValor()         );

        dto.setDtPagamento              ( cicloPagamentoVenda.getDtPagamento()               );
        dto.setNomeArquivo              ( cicloPagamentoVenda.getNomeArquivo()               );
        dto.setConteudoBase64           ( cicloPagamentoVenda.getConteudoBase64()            );        
        dto.setDataUpload               ( cicloPagamentoVenda.getDataUpload()                );
        dto.setDescStatusPagamento      ( cicloPagamentoVenda.getDescStatusPagamento()       );
        dto.setIdTaxaConveniadosEntidate( cicloPagamentoVenda.getIdTaxaConveniadosEntidate() );
        
         // Inicializa ConveniadosResumoDTO se existir na entidade
        if (cicloPagamentoVenda.getConveniados() != null) {
            ConveniadosResumoDTO conveniadosDTO = new ConveniadosResumoDTO();
            conveniadosDTO.setIdConveniados(cicloPagamentoVenda.getConveniados().getIdConveniados());
            conveniadosDTO.setNome(cicloPagamentoVenda.getConveniados().getPessoa().getNomePessoa()); // Assumindo que Conveniados tem um "nome"
            dto.setConveniados(conveniadosDTO);
        }
        
        // Inicializa TaxaConveniadosResumoDTO se existir na entidade
        if (cicloPagamentoVenda.getTaxaConveniados() != null) {
            TaxaConveniadosResumoDTO taxaDTO = new TaxaConveniadosResumoDTO();
            taxaDTO.setIdTaxaConveniados(cicloPagamentoVenda.getTaxaConveniados().getIdTaxaConveniados());
            dto.setTaxaConveniados(taxaDTO);
        }
       
        return dto;
    }

}
