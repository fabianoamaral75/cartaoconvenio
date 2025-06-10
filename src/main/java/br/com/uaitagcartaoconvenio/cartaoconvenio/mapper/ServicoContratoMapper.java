
package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContratoEntidadeDTO;

@Mapper(componentModel = "spring", uses = {ArqContratoMapper.class, VigenciaContratoMapper.class/*, ServicoContratoMapper.class*/})
public interface ServicoContratoMapper {
    ContratoEntidadeMapper INSTANCE = Mappers.getMapper(ContratoEntidadeMapper.class);

    @Mapping(target = "idEntidade", source = "entidade.idEntidade")
    @Mapping(target = "status", defaultValue = "false")
    ContratoEntidadeDTO toDto(ContratoEntidade entity);

    @Mapping(target = "entidade", ignore = true)
    @Mapping(target = "arquivos", ignore = true)
    @Mapping(target = "vigencias", ignore = true)
    @Mapping(target = "servicos", ignore = true)
    ContratoEntidade toEntity(ContratoEntidadeDTO dto);

    List<ContratoEntidadeDTO> toListDto(List<ContratoEntidade> entities);
}
