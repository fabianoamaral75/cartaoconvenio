package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.RamoAtividade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RamoAtividadeDTO;

@Mapper(componentModel = "spring", uses = {ConveniadosMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE )
public interface RamoAtividadeMapper {
    RamoAtividadeMapper INSTANCE = Mappers.getMapper(RamoAtividadeMapper.class);

    RamoAtividadeDTO toDto(RamoAtividade ramoAtividade);
    List<RamoAtividadeDTO> toListDto(List<RamoAtividade> listRamoAtividade);
    RamoAtividade toEntity(RamoAtividadeDTO ramoAtividadeDTO);
}
