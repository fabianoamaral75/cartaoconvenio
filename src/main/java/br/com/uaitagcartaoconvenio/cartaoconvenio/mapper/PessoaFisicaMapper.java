package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PessoaFisica;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PessoaFisicaDTO;

@Mapper( componentModel = "spring", uses = {PessoaMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PessoaFisicaMapper {
    PessoaFisicaMapper INSTANCE = Mappers.getMapper(PessoaFisicaMapper.class);

    PessoaFisicaDTO toDto(PessoaFisica pessoaFisica);
    PessoaFisica toEntity(PessoaFisicaDTO pessoaFisicaDTO);
}
