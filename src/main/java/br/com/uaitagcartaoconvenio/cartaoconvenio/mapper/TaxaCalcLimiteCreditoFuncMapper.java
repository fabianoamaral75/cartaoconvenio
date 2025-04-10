package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaCalcLimiteCreditoFunc;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaCalcLimiteCreditoFuncDTO;

@Mapper(componentModel = "spring", uses = {EntidadeMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE )
public interface TaxaCalcLimiteCreditoFuncMapper {
    TaxaCalcLimiteCreditoFuncMapper INSTANCE = Mappers.getMapper(TaxaCalcLimiteCreditoFuncMapper.class);

    TaxaCalcLimiteCreditoFuncDTO toDto(TaxaCalcLimiteCreditoFunc taxaCalcLimiteCreditoFunc);
    List<TaxaCalcLimiteCreditoFuncDTO> toListDto(List<TaxaCalcLimiteCreditoFunc> listTaxaCalcLimiteCreditoFunc);
    TaxaCalcLimiteCreditoFunc toEntity(TaxaCalcLimiteCreditoFuncDTO taxaCalcLimiteCreditoFuncDTO);
}
