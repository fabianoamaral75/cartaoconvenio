package br.com.uaitagcartaoconvenio.cartaoconvenio.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ItemTaxaExtraConveniadaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PeriodoCobrancaTaxaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaExtraConveniadaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TipoPeriodoDTO;

@Component
public class ItemTaxaExtraConveniadaMapper {


    public ItemTaxaExtraConveniadaDTO toDTO(ItemTaxaExtraConveniada entity) {
        if (entity == null) {
            return null;
        }

        ItemTaxaExtraConveniadaDTO dto = new ItemTaxaExtraConveniadaDTO();
        dto.setId                    ( entity.getId()                     );
        dto.setValorTaxa             ( entity.getValorTaxa()              );
        dto.setDataCadastro          ( entity.getDataCadastro()           );
        dto.setTipoCobrancaPercentual( entity.getTipoCobrancaPercentual() );
        dto.setCobrancaValorBruto    ( entity.getCobrancaValorBruto()     );

        // Mapeamento de entidades relacionadas
        if (entity.getTaxaExtraConveniada() != null) {
 
            TaxaExtraConveniadaDTO taxa = new TaxaExtraConveniadaDTO();
            taxa.setId(entity.getTaxaExtraConveniada().getId());
            taxa.setDescricaoTaxa(entity.getTaxaExtraConveniada().getDescricaoTaxa());
            taxa.setDataCriacao(entity.getTaxaExtraConveniada().getDataCriacao());
            taxa.setValorTaxa(entity.getTaxaExtraConveniada().getValorTaxa());
            taxa.setStatusTaxa(entity.getTaxaExtraConveniada().getStatusTaxa());
            taxa.setTipoCobrancaPercentual(entity.getTaxaExtraConveniada().getTipoCobrancaPercentual());
            taxa.setCobrancaValorBruto(entity.getTaxaExtraConveniada().getCobrancaValorBruto());

            PeriodoCobrancaTaxaDTO periodoCobrancaTaxa = new PeriodoCobrancaTaxaDTO();
            periodoCobrancaTaxa.setId               ( entity.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getId()                );
            periodoCobrancaTaxa.setDescricao        ( entity.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDescricao()         );
            periodoCobrancaTaxa.setDataInicio       ( entity.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDataInicio()        );
            periodoCobrancaTaxa.setDataFim          ( entity.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDataFim()           );
            periodoCobrancaTaxa.setObservacao       ( entity.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getObservacao()        );
            periodoCobrancaTaxa.setDataCriacao      ( entity.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDataCriacao()       );
            periodoCobrancaTaxa.setDtUltimaCobranca ( entity.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDtUltimaCobranca()  );
            periodoCobrancaTaxa.setDtProximaCobranca( entity.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDtProximaCobranca() );
            periodoCobrancaTaxa.setQtyCobranca      ( entity.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getQtyCobranca()       );
            
            TipoPeriodoDTO tipoPeriodo = new TipoPeriodoDTO();
            tipoPeriodo.setId         ( entity.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getTipoPeriodo().getId()          );
            tipoPeriodo.setDescricao  ( entity.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getTipoPeriodo().getDescricao()   );
            tipoPeriodo.setDataCriacao( entity.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getTipoPeriodo().getDataCriacao() );
            tipoPeriodo.setTipo       ( entity.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getTipoPeriodo().getTipo()        );
            
            periodoCobrancaTaxa.setTipoPeriodo(tipoPeriodo);
            
            dto.setTaxaExtraConveniada(taxa);
    
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
        	
            TaxaExtraConveniada taxa = new TaxaExtraConveniada();
            taxa.setId(dto.getTaxaExtraConveniada().getId());
            taxa.setDescricaoTaxa(dto.getTaxaExtraConveniada().getDescricaoTaxa());
            taxa.setDataCriacao(dto.getTaxaExtraConveniada().getDataCriacao());
            taxa.setValorTaxa(dto.getTaxaExtraConveniada().getValorTaxa());
            taxa.setStatusTaxa(dto.getTaxaExtraConveniada().getStatusTaxa());
            taxa.setTipoCobrancaPercentual(dto.getTaxaExtraConveniada().getTipoCobrancaPercentual());
            taxa.setCobrancaValorBruto(dto.getTaxaExtraConveniada().getCobrancaValorBruto());

            PeriodoCobrancaTaxa periodoCobrancaTaxa = new PeriodoCobrancaTaxa();
            periodoCobrancaTaxa.setId               ( dto.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getId()                );
            periodoCobrancaTaxa.setDescricao        ( dto.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDescricao()         );
            periodoCobrancaTaxa.setDataInicio       ( dto.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDataInicio()        );
            periodoCobrancaTaxa.setDataFim          ( dto.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDataFim()           );
            periodoCobrancaTaxa.setObservacao       ( dto.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getObservacao()        );
            periodoCobrancaTaxa.setDataCriacao      ( dto.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDataCriacao()       );
            periodoCobrancaTaxa.setDtUltimaCobranca ( dto.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDtUltimaCobranca()  );
            periodoCobrancaTaxa.setDtProximaCobranca( dto.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDtProximaCobranca() );
            periodoCobrancaTaxa.setQtyCobranca      ( dto.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getQtyCobranca()       );
            
            TipoPeriodo tipoPeriodo = new TipoPeriodo();
            tipoPeriodo.setId         ( dto.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getTipoPeriodo().getId()          );
            tipoPeriodo.setDescricao  ( dto.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getTipoPeriodo().getDescricao()   );
            tipoPeriodo.setDataCriacao( dto.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getTipoPeriodo().getDataCriacao() );
            tipoPeriodo.setTipo       ( dto.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getTipoPeriodo().getTipo()        );
            
            periodoCobrancaTaxa.setTipoPeriodo(tipoPeriodo);
            
            entity.setTaxaExtraConveniada(taxa);
      	
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