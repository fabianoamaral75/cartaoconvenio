
package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContasReceberDTO;

@Mapper(componentModel = "spring", 
     uses = {TaxaEntidadeMapper.class, EntidadeMapper.class}, // Remova FechamentoEntContasReceberMapper
     unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContasReceberMapper {
 
 @Mapping(target = "fechamentoEntContasReceber", ignore = true) // Ignora a lista de fechamentos
 ContasReceberDTO toDto(ContasReceber contasReceber);
 
 List<ContasReceberDTO> toListDto(List<ContasReceber> listContasReceber);
 
 @Mapping(target = "fechamentoEntContasReceber", ignore = true)
 ContasReceber toEntity(ContasReceberDTO contasReceberDTO);
 
 /**
  * Método especial para mapeamento completo quando necessário
  */
 @Named("mapWithFechamentos")
 default ContasReceberDTO mapWithFechamentos(ContasReceber contasReceber, FechamentoEntContasReceberMapper fechamentoMapper) {
     ContasReceberDTO dto = toDto(contasReceber);
     if(contasReceber.getFechamentoEntContasReceber() != null) {
         dto.setFechamentoEntContasReceber(fechamentoMapper.toDtoList(contasReceber.getFechamentoEntContasReceber()));
     }
     return dto;
 }
}
