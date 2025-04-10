package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.CicloPagamentoVendaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ConveniadosRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaConveiniadosRepository;

@Component
public class CicloPagamentoVendaMapper {
    @Autowired
    private ConveniadosRepository conveniadosRepository;
    
    @Autowired
    private TaxaConveiniadosRepository taxaConveiniadosRepository;

    // Converte DTO para Entidade
    public CicloPagamentoVenda toEntity(CicloPagamentoVendaDTO dto) {
    	CicloPagamentoVenda cicloPagamentoVenda = new CicloPagamentoVenda();
    	cicloPagamentoVenda.setIdCicloPagamentoVenda ( dto.getIdCicloPagamentoVenda() );
    	cicloPagamentoVenda.setAnoMes                ( dto.getAnoMes()                );    	
    	cicloPagamentoVenda.setDtCriacao             ( dto.getDtCriacao()             );
    	cicloPagamentoVenda.setDtAlteracao           ( dto.getDtAlteracao()           );
    	cicloPagamentoVenda.setValorCiclo            ( dto.getValorCiclo()            );    	
    	cicloPagamentoVenda.setDtPagamento           ( dto.getDtPagamento()           );    	
    	cicloPagamentoVenda.setNomeArquivo           ( dto.getNomeArquivo()           );    	
    	cicloPagamentoVenda.setConteudoBase64        ( dto.getConteudoBase64()        );
    	cicloPagamentoVenda.setTamanhoBytes          ( dto.getTamanhoBytes()          );    	
    	cicloPagamentoVenda.setDataUpload            ( dto.getDataUpload()            );
    	cicloPagamentoVenda.setDescStatusPagamento   ( dto.getDescStatusPagamento()   );    	
        
        if (dto.getConveniados().getIdConveniados() != null) {
        	cicloPagamentoVenda.setConveniados(conveniadosRepository.findById(dto.getConveniados().getIdConveniados()).orElse(null));
        }
        
        if (dto.getTaxaConveiniados().getIdTaxaConveiniados() != null) {
        	cicloPagamentoVenda.setTaxaConveiniados( taxaConveiniadosRepository.findById(dto.getTaxaConveiniados().getIdTaxaConveiniados() ).orElse(null)  );
        }
        
        return cicloPagamentoVenda;
    }

    // Converte Entidade para DTO
    public CicloPagamentoVendaDTO toDTO(CicloPagamentoVenda cicloPagamentoVenda) {
    	CicloPagamentoVendaDTO dto = new CicloPagamentoVendaDTO();
        dto.setIdCicloPagamentoVenda( cicloPagamentoVenda.getIdCicloPagamentoVenda() );
        dto.setAnoMes               ( cicloPagamentoVenda.getAnoMes()                );
        dto.setDtCriacao            ( cicloPagamentoVenda.getDtCriacao()             );
        dto.setDtAlteracao          ( cicloPagamentoVenda.getDtAlteracao()           );        
        dto.setValorCiclo           ( cicloPagamentoVenda.getValorCiclo()            );
        dto.setDtPagamento          ( cicloPagamentoVenda.getDtPagamento()           );
        dto.setNomeArquivo          ( cicloPagamentoVenda.getNomeArquivo()           );
        dto.setConteudoBase64       ( cicloPagamentoVenda.getConteudoBase64()        );        
        dto.setDataUpload           ( cicloPagamentoVenda.getDataUpload()            );
        dto.setDescStatusPagamento  ( cicloPagamentoVenda.getDescStatusPagamento()   );
        
        if (cicloPagamentoVenda.getConveniados() != null) {
            dto.getConveniados().setIdConveniados(cicloPagamentoVenda.getConveniados().getIdConveniados());
        }
        
        if (cicloPagamentoVenda.getTaxaConveiniados() != null) {
            dto.getTaxaConveiniados().setIdTaxaConveiniados(cicloPagamentoVenda.getTaxaConveiniados().getIdTaxaConveiniados());
        }
       
        return dto;
    }

}
