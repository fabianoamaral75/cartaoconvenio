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
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContratoEntidadeDTO;

@Mapper(componentModel = "spring") // Usando "spring" para injeção de dependência (opcional)
public interface ContratoEntidadeMapper {

    ContratoEntidadeMapper INSTANCE = Mappers.getMapper(ContratoEntidadeMapper.class);

    // Mapeamento básico (campos simples)
    @Mapping(target = "arquivos", ignore = true) // Será tratado no @AfterMapping
    @Mapping(target = "servicos", ignore = true)
    @Mapping(target = "vigencias", ignore = true)
    @Mapping(target = "entidade", ignore = true)
    @Mapping(target = "dtCadastro", ignore = true) // Será definido no @AfterMapping
    ContratoEntidade toEntity(ContratoEntidadeDTO dto);

    // Mapeamento básico (campos simples + conversão de IDs)
    @Mapping(source = "entidade.idEntidade", target = "idEntidade")
    @Mapping(source = "arquivos", target = "idArquivos", qualifiedByName = "mapArquivosToIds")
    @Mapping(source = "servicos", target = "idServicos", qualifiedByName = "mapServicosToIds")
    @Mapping(source = "vigencias", target = "idVigencias", qualifiedByName = "mapVigenciasToIds")
    ContratoEntidadeDTO toDTO(ContratoEntidade entity);

    // ===== Métodos Auxiliares para Conversão de Listas =====
    @Named("mapArquivosToIds")
    default List<Long> mapArquivosToIds(List<ArqContratoEntidade> arquivos) {
        if (arquivos == null) return null;
        return arquivos.stream().map(ArqContratoEntidade::getIdArqContratoEntidade).collect(Collectors.toList());
    }

    @Named("mapServicosToIds")
    default List<Long> mapServicosToIds(List<ServicoContrato> servicos) {
        if (servicos == null) return null;
        return servicos.stream().map(ServicoContrato::getIdServicoContrato).collect(Collectors.toList());
    }

    @Named("mapVigenciasToIds")
    default List<Long> mapVigenciasToIds(List<VigenciaContratoEntidade> vigencias) {
        if (vigencias == null) return null;
        return vigencias.stream().map(VigenciaContratoEntidade::getIdVigenciaContratoEntidade).collect(Collectors.toList());
    }

    // ===== @AfterMapping para lógica customizada =====
    @AfterMapping
    default void afterToEntity(@MappingTarget ContratoEntidade contrato, ContratoEntidadeDTO dto) {
        // Define a data de cadastro
        contrato.setDtCadastro(java.time.LocalDateTime.now());

        // Converte o ID da entidade em um objeto Entidade (simplificado)
        if (dto.getIdEntidade() != null) {
            Entidade entidade = new Entidade();
            entidade.setIdEntidade(dto.getIdEntidade());
            contrato.setEntidade(entidade);
        }

        // Obs.: Se arquivos, serviços e vigências precisarem ser carregados de um repositório,
        // você pode injetar um Service aqui e buscar as listas correspondentes.
        // Exemplo (se necessário):
        // contrato.setArquivos(arquivoService.findByIdIn(dto.getIdArquivos()));
    }
    
    /**
     * Atualiza uma entidade existente com os dados do DTO
     * @param dto DTO com os novos valores
     * @param entity Entidade a ser atualizada
     */
    @Mapping(target = "idContratoEntidade", ignore = true) // Não atualiza o ID
    @Mapping(target = "arquivos", ignore = true) // Mantém a coleção original
    @Mapping(target = "servicos", ignore = true)
    @Mapping(target = "vigencias", ignore = true)
    @Mapping(target = "dtCadastro", ignore = true) // Mantém a data original
    @Mapping(target = "entidade", ignore = true) // Será tratado no @AfterMapping
 //   @Mapping(target = "observacao", expression = "java(dto.getObservacao() != null ? dto.getObservacao() : entity.getObservacao())") // Para campos que devem ser atualizados condicionalmente
    void updateEntityFromDTO(ContratoEntidadeDTO dto, @MappingTarget ContratoEntidade entity);

    // Adicione este método para tratar a entidade após o mapeamento
    @AfterMapping
    default void afterUpdate(@MappingTarget ContratoEntidade entity, ContratoEntidadeDTO dto) {
        if (dto.getIdEntidade() != null) {
            Entidade entidade = new Entidade();
            entidade.setIdEntidade(dto.getIdEntidade());
            entity.setEntidade(entidade);
        }
    }
}