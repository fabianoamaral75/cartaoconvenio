package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.FechamentoConvItensVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ItemTaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.CicloPagamentoVendaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ConveniadosResumoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.FechamentoConvItensVendasDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ItemTaxaExtraConveniadaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PeriodoCobrancaTaxaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaConveniadosResumoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaExtraConveniadaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TipoPeriodoDTO;

@Mapper(componentModel = "spring", 
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CicloPagamentoVendaInterfaceMapper {
    
    @Mapping(target = "fechamentoConvItensVendas", expression = "java(mapFechamentos(source.getFechamentoConvItensVendas()))")
    @Mapping(target = "itemTaxaExtraConveniada", expression = "java(mapItensTaxa(source.getItemTaxaExtraConveniada()))")
    @Mapping(target = "conveniados", qualifiedByName = "mapConveniadosToResumo")
    @Mapping(target = "taxaConveniados", qualifiedByName = "mapTaxaConveniadosToResumo")
    @Mapping(target = "taxasFaixaVendas", ignore = true) // Se necessário, implemente este mapeamento
    CicloPagamentoVendaDTO toDto(CicloPagamentoVenda source);
    
    List<CicloPagamentoVendaDTO> toListDto(List<CicloPagamentoVenda> list);
    
    @Mapping(target = "conveniados", ignore = true)
    @Mapping(target = "taxaConveniados", ignore = true)
    @Mapping(target = "fechamentoConvItensVendas", ignore = true)
    @Mapping(target = "itemTaxaExtraConveniada", ignore = true)
    @Mapping(target = "taxasFaixaVendas", ignore = true)
    CicloPagamentoVenda toEntity(CicloPagamentoVendaDTO cicloPagamentoVendaDTO);

    @Named("mapConveniadosToResumo")
    default ConveniadosResumoDTO mapConveniadosToResumo(Conveniados conveniados) {
        if (conveniados == null) {
            return null;
        }
        
        ConveniadosResumoDTO dto = new ConveniadosResumoDTO();
        dto.setIdConveniados(conveniados.getIdConveniados());
        
        if (conveniados.getPessoa() != null && !conveniados.getPessoa().isEmpty()) {
            dto.setNome(conveniados.getPessoa().get(0).getNomePessoa());
        }
        
        return dto;
    }

    @Named("mapTaxaConveniadosToResumo")
    default TaxaConveniadosResumoDTO mapTaxaConveniadosToResumo(TaxaConveniados taxaConveniados) {
        if (taxaConveniados == null) {
            return null;
        }
        
        TaxaConveniadosResumoDTO dto = new TaxaConveniadosResumoDTO();
        dto.setIdTaxaConveniados(taxaConveniados.getIdTaxaConveniados());
        dto.setTaxa(taxaConveniados.getTaxa());
        // Adicione outros campos conforme necessário
        
        return dto;
    }
    
    default List<FechamentoConvItensVendasDTO> mapFechamentos(List<FechamentoConvItensVendas> fechamentos) {
        if (fechamentos == null) {
            return null;
        }
        return fechamentos.stream()
            .map(f -> {
                FechamentoConvItensVendasDTO dto = new FechamentoConvItensVendasDTO();
                dto.setId(f.getId());
                dto.setIdCicloPagamentoVenda(f.getCicloPagamentoVenda() != null ? 
                    f.getCicloPagamentoVenda().getIdCicloPagamentoVenda() : null);
                dto.setIdVenda(f.getVenda() != null ? f.getVenda().getIdVenda() : null);
                dto.setDtCriacao(f.getDtCriacao());
                return dto;
            })
            .collect(Collectors.toList());
    }
    
    default List<ItemTaxaExtraConveniadaDTO> mapItensTaxa(List<ItemTaxaExtraConveniada> itens) {
        if (itens == null) {
            return null;
        }
        return itens.stream()
            .map(i -> {
                ItemTaxaExtraConveniadaDTO dto = new ItemTaxaExtraConveniadaDTO();
                dto.setId(i.getId());
                dto.setValorTaxa(i.getValorTaxa());
                dto.setDataCadastro(i.getDataCadastro());
                dto.setCobrancaValorBruto(i.getCobrancaValorBruto());
                dto.setTipoCobrancaPercentual(i.getTipoCobrancaPercentual());
                
                if (i.getTaxaExtraConveniada() != null) {
                    TaxaExtraConveniadaDTO taxa = new TaxaExtraConveniadaDTO();
                    taxa.setId(i.getTaxaExtraConveniada().getId());
                    taxa.setDescricaoTaxa(i.getTaxaExtraConveniada().getDescricaoTaxa());
                    taxa.setDataCriacao(i.getTaxaExtraConveniada().getDataCriacao());
                    taxa.setValorTaxa(i.getTaxaExtraConveniada().getValorTaxa());
                    taxa.setStatusTaxa(i.getTaxaExtraConveniada().getStatusTaxa());
                    taxa.setTipoCobrancaPercentual(i.getTaxaExtraConveniada().getTipoCobrancaPercentual());
                    taxa.setCobrancaValorBruto(i.getTaxaExtraConveniada().getCobrancaValorBruto());

                    if (i.getTaxaExtraConveniada().getPeriodoCobrancaTaxa() != null) {
                        PeriodoCobrancaTaxaDTO periodoCobrancaTaxa = new PeriodoCobrancaTaxaDTO();
                        periodoCobrancaTaxa.setId(i.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getId());
                        periodoCobrancaTaxa.setDescricao(i.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDescricao());
                        periodoCobrancaTaxa.setDataInicio(i.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDataInicio());
                        periodoCobrancaTaxa.setDataFim(i.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDataFim());
                        periodoCobrancaTaxa.setObservacao(i.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getObservacao());
                        periodoCobrancaTaxa.setDataCriacao(i.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDataCriacao());
                        periodoCobrancaTaxa.setDtUltimaCobranca(i.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDtUltimaCobranca());
                        periodoCobrancaTaxa.setDtProximaCobranca(i.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getDtProximaCobranca());
                        periodoCobrancaTaxa.setQtyCobranca(i.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getQtyCobranca());
                        
                        if (i.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getTipoPeriodo() != null) {
                            TipoPeriodoDTO tipoPeriodo = new TipoPeriodoDTO();
                            tipoPeriodo.setId(i.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getTipoPeriodo().getId());
                            tipoPeriodo.setDescricao(i.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getTipoPeriodo().getDescricao());
                            tipoPeriodo.setDataCriacao(i.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getTipoPeriodo().getDataCriacao());
                            tipoPeriodo.setTipo(i.getTaxaExtraConveniada().getPeriodoCobrancaTaxa().getTipoPeriodo().getTipo());
                            periodoCobrancaTaxa.setTipoPeriodo(tipoPeriodo);
                        }
                        
                        taxa.setPeriodoCobrancaTaxa(periodoCobrancaTaxa);
                    }
                    
                    dto.setTaxaExtraConveniada(taxa);
                }
                
                return dto;
            })
            .collect(Collectors.toList());
    }
}