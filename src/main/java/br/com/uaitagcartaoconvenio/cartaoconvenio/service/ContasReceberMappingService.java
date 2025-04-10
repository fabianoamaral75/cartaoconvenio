package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.ContasReceberMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.FechamentoEntContasReceberMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContasReceberDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContasReceberMappingService {
    
    private final ContasReceberMapper contasReceberMapper;
    private final FechamentoEntContasReceberMapper fechamentoMapper;
    
    public List<ContasReceberDTO> mapCompleteList(List<ContasReceber> contasReceberList) {
        return contasReceberList.stream()
            .map(this::mapComplete)
            .collect(Collectors.toList());
    }
    
    public ContasReceberDTO mapComplete(ContasReceber contasReceber) {
        ContasReceberDTO dto = contasReceberMapper.toDto(contasReceber);
        
        if(contasReceber.getFechamentoEntContasReceber() != null) {
            dto.setFechamentoEntContasReceber(fechamentoMapper.toDtoList(contasReceber.getFechamentoEntContasReceber()));
        }
        
        return dto;
    }
}
