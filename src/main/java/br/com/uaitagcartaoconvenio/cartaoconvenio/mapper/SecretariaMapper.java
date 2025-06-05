package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Secretaria;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.SecretariaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.SecretariaResumoDTO;

@Mapper(componentModel = "spring", 
uses = {EntidadeMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SecretariaMapper {
    //SecretariaMapper INSTANCE = Mappers.getMapper(SecretariaMapper.class);
/*
    SecretariaDTO toDto(Secretaria secretaria);
    Secretaria toEntity(SecretariaDTO secretariaDTO);
    @Named("secretariaMapper")
    SecretariaResumoDTO toResumoDto(Secretaria secretaria);
*/   
	
	SecretariaDTO toDto(Secretaria secretaria);
    List<SecretariaDTO> toDtoList(List<Secretaria> secretarias);

    Secretaria toEntity(SecretariaDTO secretariaDTO);
    List<Secretaria> toEntityList(List<SecretariaDTO> secretariaDTOs);

    @Named("secretariaMapper")
    SecretariaResumoDTO toResumoDto(Secretaria secretaria);

}
