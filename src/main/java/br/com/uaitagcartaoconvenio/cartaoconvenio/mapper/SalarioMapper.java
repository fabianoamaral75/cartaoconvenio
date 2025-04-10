package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Salario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.SalarioDTO;

@Mapper(uses = {FuncionarioMapper.class})
public interface SalarioMapper {
    SalarioMapper INSTANCE = Mappers.getMapper(SalarioMapper.class);

    SalarioDTO toDto(Salario salario);
    Salario toEntity(SalarioDTO salarioDTO);
}

