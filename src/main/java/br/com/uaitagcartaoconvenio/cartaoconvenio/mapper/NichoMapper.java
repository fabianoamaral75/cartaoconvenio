package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Nicho;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.NichoDTO;

@Mapper(componentModel = "spring", uses = {ConveniadosMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NichoMapper {
    NichoMapper INSTANCE = Mappers.getMapper(NichoMapper.class);

    NichoDTO toDto(Nicho nicho);
    List<NichoDTO> toListDto(List<Nicho> funcionario); 
    Nicho toEntity(NichoDTO nichoDTO);
}