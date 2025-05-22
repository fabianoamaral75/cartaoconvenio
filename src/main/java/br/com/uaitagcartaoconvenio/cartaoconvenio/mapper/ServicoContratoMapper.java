package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ServicoContrato;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ServicoContratoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ServicoContratoMapper {

    ServicoContratoMapper INSTANCE = Mappers.getMapper(ServicoContratoMapper.class);

 //   @Mapping(target = "tipoCobrancaTaxaExtraEnt", source = "tipoCobranca")
     ServicoContratoDTO toDTO(ServicoContrato entity);

 //   @Mapping(target = "tipoCobranca", source = "tipoCobrancaTaxaExtraEnt")
    @Mapping(target = "contratoEntidade", ignore = true)
    ServicoContrato toEntity(ServicoContratoDTO dto);
}