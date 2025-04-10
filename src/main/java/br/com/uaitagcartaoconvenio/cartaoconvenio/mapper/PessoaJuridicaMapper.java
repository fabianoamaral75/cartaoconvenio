package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PessoaJuridica;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PessoaJuridicaDTO;

@Mapper(componentModel = "spring", uses = {PessoaMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PessoaJuridicaMapper {
    PessoaJuridicaMapper INSTANCE = Mappers.getMapper(PessoaJuridicaMapper.class);

    PessoaJuridicaDTO toDto(PessoaJuridica pessoaJuridica);
    PessoaJuridica toEntity(PessoaJuridicaDTO pessoaJuridicaDTO);
}
