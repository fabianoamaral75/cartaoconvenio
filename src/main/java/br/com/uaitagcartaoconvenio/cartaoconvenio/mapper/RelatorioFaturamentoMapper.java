package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.RelatorioFaturamentoConveniado;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RelatorioFaturamentoResponseDTO;

@Mapper(componentModel = "spring")
public interface RelatorioFaturamentoMapper {

    RelatorioFaturamentoMapper INSTANCE = Mappers.getMapper(RelatorioFaturamentoMapper.class);

    @Mapping(source = "cicloPagamentoVenda.idCicloPagamentoVenda", target = "idCicloPagamentoVenda")
    RelatorioFaturamentoResponseDTO toDTO(RelatorioFaturamentoConveniado relatorio);

    @Mapping(source = "idCicloPagamentoVenda", target = "cicloPagamentoVenda.idCicloPagamentoVenda")
    RelatorioFaturamentoConveniado toEntity(RelatorioFaturamentoResponseDTO dto);
}