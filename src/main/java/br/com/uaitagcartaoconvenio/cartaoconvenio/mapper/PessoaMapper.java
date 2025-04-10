package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Pessoa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PessoaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PessoaResumoDTO;

@Mapper( componentModel = "spring", uses = {
		                                     PessoaJuridicaMapper.class, 
		                                    // FuncionarioMapper.class, 
		                                     UsuarioMapper.class, 
		                                     ConveniadosMapper.class
		                                     }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PessoaMapper {
    PessoaMapper INSTANCE = Mappers.getMapper(PessoaMapper.class);

    PessoaDTO toDto(Pessoa pessoa);
    List<PessoaDTO> toListDto(List<Pessoa> pessoa); 
    Pessoa toEntity(PessoaDTO pessoaDTO);
    PessoaResumoDTO toResumoDto(Pessoa pessoa);
}