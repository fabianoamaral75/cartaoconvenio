package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;



import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Antecipacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.AntecipacaoDTO;

@Mapper(componentModel = "spring")
public interface AntecipacaoMapper {

    AntecipacaoMapper INSTANCE = Mappers.getMapper(AntecipacaoMapper.class);

    @Mapping(target = "cicloPagamentoVendaId", source = "cicloPagamentoVenda.idCicloPagamentoVenda")
    AntecipacaoDTO toDTO(Antecipacao entity);

    @Mapping(target = "cicloPagamentoVenda", ignore = true)
    Antecipacao toEntity(AntecipacaoDTO dto);
}
