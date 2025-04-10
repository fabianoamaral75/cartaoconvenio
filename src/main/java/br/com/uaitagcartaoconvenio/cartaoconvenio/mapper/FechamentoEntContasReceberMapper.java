package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.FechamentoEntContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.FechamentoEntContasReceberDTO;

@Mapper(componentModel = "spring", uses = {
		                                    ContasReceberMapper.class, 
		                                    VendaMapper.class
		                                    }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FechamentoEntContasReceberMapper {
    FechamentoEntContasReceberMapper INSTANCE = Mappers.getMapper(FechamentoEntContasReceberMapper.class);
/*
    FechamentoEntContasReceberDTO toDto(FechamentoEntContasReceber fechamentoEntContasReceber);
    List<FechamentoEntContasReceberDTO> toListDto(List<FechamentoEntContasReceber> listFechamentoEntContasReceber); 
    FechamentoEntContasReceber toEntity(FechamentoEntContasReceberDTO fechamentoEntContasReceberDTO);
*/    
    @Mapping(target = "contasReceber", ignore = true) // Ignora a referência circular
    FechamentoEntContasReceberDTO toDto(FechamentoEntContasReceber fechamento);
    
    List<FechamentoEntContasReceberDTO> toDtoList(List<FechamentoEntContasReceber> fechamentos);
    
    @Mapping(target = "contasReceber", ignore = true)
    FechamentoEntContasReceber toEntity(FechamentoEntContasReceberDTO dto);

}
