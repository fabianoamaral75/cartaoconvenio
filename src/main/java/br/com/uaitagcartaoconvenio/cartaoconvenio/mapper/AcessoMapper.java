package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Acesso;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.AcessoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.AcessoResumoDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AcessoMapper {
    AcessoMapper INSTANCE = Mappers.getMapper(AcessoMapper.class);

    AcessoDTO toDto(Acesso acesso);
    Acesso toEntity(AcessoDTO acessoDTO);
    AcessoResumoDTO toResumoDto(Acesso acesso);
}