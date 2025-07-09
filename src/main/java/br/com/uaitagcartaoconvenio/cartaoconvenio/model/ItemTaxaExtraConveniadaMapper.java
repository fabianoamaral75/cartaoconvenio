package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ItemTaxaExtraConveniadaDTO;

@Component
public class ItemTaxaExtraConveniadaMapper {


    public ItemTaxaExtraConveniadaDTO toDTO(ItemTaxaExtraConveniada entity) {
        if (entity == null) {
            return null;
        }

        ItemTaxaExtraConveniadaDTO dto = new ItemTaxaExtraConveniadaDTO();
        dto.setId(entity.getId());
        dto.setValorTaxa(entity.getValorTaxa());
        dto.setDataCadastro(entity.getDataCadastro());
        dto.setTipoCobrancaPercentual(entity.getTipoCobrancaPercentual());
        dto.setCobrancaValorBruto(entity.getCobrancaValorBruto());

        // Mapeamento de entidades relacionadas
        if (entity.getTaxaExtraConveniada() != null) {
            dto.setTaxaExtraConveniada(entity.getTaxaExtraConveniada());
        }
        
        if (entity.getCicloPagamentoVenda() != null) {
            dto.setCicloPagamentoVenda(entity.getCicloPagamentoVenda());
        }
        

        return dto;
    }

    public ItemTaxaExtraConveniada toEntity(ItemTaxaExtraConveniadaDTO dto) {
        if (dto == null) {
            return null;
        }

        ItemTaxaExtraConveniada entity = new ItemTaxaExtraConveniada();
        entity.setId(dto.getId());
        entity.setValorTaxa(dto.getValorTaxa());
        entity.setDataCadastro(dto.getDataCadastro());
        entity.setTipoCobrancaPercentual(dto.getTipoCobrancaPercentual());
        entity.setCobrancaValorBruto(dto.getCobrancaValorBruto());

        // Mapeamento de entidades relacionadas
        if (dto.getTaxaExtraConveniada() != null) {
            entity.setTaxaExtraConveniada(dto.getTaxaExtraConveniada());
        }
        
        if (dto.getCicloPagamentoVenda() != null) {
            entity.setCicloPagamentoVenda(dto.getCicloPagamentoVenda());
        }
        

        return entity;
    }

    public List<ItemTaxaExtraConveniadaDTO> toDTOList(List<ItemTaxaExtraConveniada> entities) {
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ItemTaxaExtraConveniada> toEntityList(List<ItemTaxaExtraConveniadaDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    // Método para mapeamento simplificado
    public ItemTaxaExtraConveniadaDTO toSimpleDTO(ItemTaxaExtraConveniada entity) {
        if (entity == null) {
            return null;
        }

        ItemTaxaExtraConveniadaDTO dto = new ItemTaxaExtraConveniadaDTO();
        dto.setId(entity.getId());
        dto.setValorTaxa(entity.getValorTaxa());
        dto.setTipoCobrancaPercentual(entity.getTipoCobrancaPercentual());
        
        return dto;
    }
}