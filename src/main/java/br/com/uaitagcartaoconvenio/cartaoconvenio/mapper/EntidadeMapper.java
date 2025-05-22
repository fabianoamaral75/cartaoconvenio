package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Funcionario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Secretaria;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaCalcLimiteCreditoFunc;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.EntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.FuncionarioResumoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.SecretariaResumoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaCalcLimiteCreditoFuncResumoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaEntidadeResumoDTO;

@Mapper(componentModel = "spring", uses = {ContratoEntidadeMapper.class})
public interface EntidadeMapper {

    EntidadeMapper INSTANCE = Mappers.getMapper(EntidadeMapper.class);

    // Adicione estas anotações para evitar referências circulares
     @Mapping(target = "secretaria.funcionario", ignore = true)
     @Mapping(target = "listaFuncionario.secretaria", ignore = true)
     EntidadeDTO toDTO(Entidade entidade);
     
     List<EntidadeDTO> toDTO(List<Entidade> entidades);

    // Mapeamento principal de DTO para Entidade
     Entidade toEntity(EntidadeDTO dto);

    // Mapeamentos para objetos aninhados
 //   @Mapping(target = "entidade", ignore = true)
    @Mapping(target = "funcionario", ignore = true)
    SecretariaResumoDTO secretariaToSecretariaResumoDTO(Secretaria secretaria);
    @Mapping(target = "entidade", ignore = true)
    Secretaria secretariaResumoDTOToSecretaria(SecretariaResumoDTO dto);

    TaxaEntidadeResumoDTO taxaEntidadeToTaxaEntidadeResumoDTO(TaxaEntidade taxaEntidade);
    TaxaEntidade taxaEntidadeResumoDTOToTaxaEntidade(TaxaEntidadeResumoDTO dto);

 //   @Mapping(target = "secretaria", ignore = true)
//    @Mapping(target = "entidade", ignore = true)
    FuncionarioResumoDTO funcionarioToFuncionarioResumoDTO(Funcionario funcionario);
    Funcionario funcionarioResumoDTOToFuncionario(FuncionarioResumoDTO dto);

    TaxaCalcLimiteCreditoFuncResumoDTO taxaLimiteToTaxaLimiteResumoDTO(TaxaCalcLimiteCreditoFunc taxa);
    TaxaCalcLimiteCreditoFunc taxaCalcLimiteResumoDTOToTaxaCalcLimite(TaxaCalcLimiteCreditoFuncResumoDTO dto);

    // Métodos para lidar com listas
    default List<SecretariaResumoDTO> secretariaListToSecretariaResumoDTOList(List<Secretaria> list) {
        if (list == null) return null;
        return list.stream().map(this::secretariaToSecretariaResumoDTO).collect(Collectors.toList());
    }

    default List<Secretaria> secretariaResumoDTOListToSecretariaList(List<SecretariaResumoDTO> list) {
        if (list == null) return null;
        return list.stream().map(this::secretariaResumoDTOToSecretaria).collect(Collectors.toList());
    }

    // Métodos similares para outras listas...

    @AfterMapping
    default void afterToEntity(@MappingTarget Entidade entidade, EntidadeDTO dto) {
        // Configurações pós-mapeamento podem ser adicionadas aqui
        if (entidade.getDtCriacao() == null) {
            entidade.setDtCriacao(new Date());
        }
        entidade.setDtAlteracao(new Date());
    }

    @AfterMapping
    default void afterToDTO(@MappingTarget EntidadeDTO dto, Entidade entidade) {
        // Configurações pós-mapeamento podem ser adicionadas aqui
    }
    
    default List<EntidadeDTO> toDTOList(List<Entidade> entidades) {
        if (entidades == null) {
            return null;
        }
        return entidades.stream()
                       .map(this::toDTO)
                       .collect(Collectors.toList());
    }
}