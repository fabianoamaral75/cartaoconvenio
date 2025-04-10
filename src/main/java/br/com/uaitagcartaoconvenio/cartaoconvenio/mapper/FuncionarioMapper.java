package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Funcionario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.FuncionarioDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.FuncionarioResumoDTO;
@Mapper(componentModel = "spring", uses = { 
		                                    LimiteCreditoMapper.class, 
		                                    SalarioMapper.class, 
		                                    CartaoMapper.class, 
		                                    PessoaMapper.class, 
		                                    SecretariaMapper.class, 
		                                    EntidadeMapper.class
		                                   }, 
                                    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FuncionarioMapper {
    FuncionarioMapper INSTANCE = Mappers.getMapper(FuncionarioMapper.class);

    FuncionarioDTO toDto(Funcionario funcionario);
    List<FuncionarioDTO> toListDto(List<Funcionario> funcionario); 
    Funcionario toEntity(FuncionarioDTO funcionarioDTO);
    FuncionarioResumoDTO toResumoDto(Funcionario funcionario);
}

