

package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.EntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.EntidadeResumoDTO;

@Mapper(componentModel = "spring", uses = {
		                                  // SecretariaMapper.class, 
		                                   TaxaEntidadeMapper.class, 
		                               //    FuncionarioMapper.class, 
		                                   TaxaCalcLimiteCreditoFuncMapper.class
		                                   }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EntidadeMapper {
    EntidadeMapper INSTANCE = Mappers.getMapper(EntidadeMapper.class);

    EntidadeDTO toDto(Entidade entidade);
    List<EntidadeDTO> toListDto(List<Entidade> listEntidade); 
    Entidade toEntity(EntidadeDTO entidadeDTO);
    EntidadeResumoDTO toResumoDto(Entidade entidade);
}
