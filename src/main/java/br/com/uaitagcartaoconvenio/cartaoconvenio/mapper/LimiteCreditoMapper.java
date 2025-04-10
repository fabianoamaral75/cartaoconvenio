package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.LimiteCredito;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.LimiteCreditoDTO;

@Mapper(uses = {FuncionarioMapper.class})
public interface LimiteCreditoMapper {
    LimiteCreditoMapper INSTANCE = Mappers.getMapper(LimiteCreditoMapper.class);

    LimiteCreditoDTO toDto(LimiteCredito limiteCredito);
    LimiteCredito toEntity(LimiteCreditoDTO limiteCreditoDTO);
}