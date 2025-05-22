package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ArqContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ServicoContrato;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ArqContratoEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContratoEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ServicoContratoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.VigenciaContratoEntidadeDTO;

@Mapper(componentModel = "spring")
public interface ContratoEntidadeMapper {

    ContratoEntidadeMapper INSTANCE = Mappers.getMapper(ContratoEntidadeMapper.class);

    @Mapping(target = "idEntidade", source = "entidade.idEntidade")
    ContratoEntidadeDTO toDTO(ContratoEntidade entity);

    @Mapping(target = "entidade", source = "idEntidade", qualifiedByName = "idToEntidade")
    @Mapping(target = "dtCadastro", ignore = true)
    ContratoEntidade toEntity(ContratoEntidadeDTO dto);
    
    // Método para converter ID em Entidade
    @Named("idToEntidade")
    default Entidade idToEntidade(Long id) {
        if (id == null) {
            return null;
        }
        Entidade entidade = new Entidade();
        entidade.setIdEntidade(id);
        return entidade;
    }
    // Mapeamento para objetos aninhados
    ArqContratoEntidadeDTO arqContratoToArqContratoDTO(ArqContratoEntidade arq);
    @Mapping(target = "contratoEntidade", ignore = true)
    ArqContratoEntidade arqContratoDTOToArqContrato(ArqContratoEntidadeDTO dto);

    VigenciaContratoEntidadeDTO vigenciaToVigenciaDTO(VigenciaContratoEntidade vigencia);
    @Mapping(target = "contratoEntidade", ignore = true)
    VigenciaContratoEntidade vigenciaDTOToVigencia(VigenciaContratoEntidadeDTO dto);

    ServicoContratoDTO servicoToServicoDTO(ServicoContrato servico);
    @Mapping(target = "contratoEntidade", ignore = true)
    ServicoContrato servicoDTOToServico(ServicoContratoDTO dto);

    // Métodos para converter listas
    default List<ArqContratoEntidadeDTO> arqContratoListToArqContratoDTOList(List<ArqContratoEntidade> list) {
        if (list == null) return null;
        return list.stream().map(this::arqContratoToArqContratoDTO).collect(Collectors.toList());
    }

    default List<ArqContratoEntidade> arqContratoDTOListToArqContratoList(List<ArqContratoEntidadeDTO> list) {
        if (list == null) return null;
        return list.stream().map(this::arqContratoDTOToArqContrato).collect(Collectors.toList());
    }

    // Métodos similares para outras listas...

    @AfterMapping
    default void afterToEntity(@MappingTarget ContratoEntidade contrato, ContratoEntidadeDTO dto) {
        if (contrato.getDtCadastro() == null) {
            contrato.setDtCadastro(java.time.LocalDateTime.now());
        }

        if (dto.getIdEntidade() != null) {
            Entidade entidade = new Entidade();
            entidade.setIdEntidade(dto.getIdEntidade());
            contrato.setEntidade(entidade);
        }
    }

    @Mapping(target = "idContratoEntidade", ignore = true)
    @Mapping(target = "arquivos", ignore = true)
    @Mapping(target = "servicos", ignore = true)
    @Mapping(target = "vigencias", ignore = true)
    @Mapping(target = "dtCadastro", ignore = true)
    @Mapping(target = "entidade", ignore = true)
    void updateEntityFromDTO(ContratoEntidadeDTO dto, @MappingTarget ContratoEntidade entity);

    @AfterMapping
    default void afterUpdate(@MappingTarget ContratoEntidade entity, ContratoEntidadeDTO dto) {
        if (dto.getIdEntidade() != null) {
            Entidade entidade = new Entidade();
            entidade.setIdEntidade(dto.getIdEntidade());
            entity.setEntidade(entidade);
        }
    }
}