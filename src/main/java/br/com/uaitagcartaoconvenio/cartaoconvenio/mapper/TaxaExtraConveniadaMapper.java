package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;


import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PeriodoCobrancaTaxa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaExtraConveniadaDTO;

@Mapper(componentModel = "spring", uses = PeriodoCobrancaTaxaMapper.class)
public interface TaxaExtraConveniadaMapper {
 //   TaxaExtraConveniadaMapper INSTANCE = Mappers.getMapper(TaxaExtraConveniadaMapper.class);

    @Mapping(target = "conveniadosId", source = "conveniados.idConveniados")
//    @Mapping(target = "periodoCobrancaTaxa", source = "periodoCobrancaTaxa")
    TaxaExtraConveniadaDTO toDTO(TaxaExtraConveniada entity);

    @Mapping(target = "conveniados", ignore = true)
 //   @Mapping(target = "periodoCobrancaTaxa", ignore = true)
    TaxaExtraConveniada toEntity(TaxaExtraConveniadaDTO dto);

    List<TaxaExtraConveniadaDTO> toListDTO(List<TaxaExtraConveniada> taxas);
    
    @AfterMapping
    default void afterToEntity(@MappingTarget TaxaExtraConveniada entity, TaxaExtraConveniadaDTO dto) {
    	System.out.println("Executando afterToEntity...");
        System.out.println("DTO recebido no afterMapping: " + dto);
        // Validação dos campos obrigatórios
        if (dto.getConveniadosId() == null) {
            throw new IllegalArgumentException("ID do Conveniado é obrigatório");
        }
        
        if (dto.getPeriodoCobrancaTaxa() == null) {
            throw new IllegalArgumentException("Período de Cobrança é obrigatório");
        }

        // Mapeamento do Conveniados
        Conveniados conveniado = new Conveniados();
        conveniado.setIdConveniados(dto.getConveniadosId()); // Verifique se é setId ou setIdConveniados na sua entidade
        entity.setConveniados(conveniado);

        // Mapeamento do PeriodoCobrancaTaxa usando o mapper específico
        PeriodoCobrancaTaxa periodo = PeriodoCobrancaTaxaMapper.INSTANCE.toEntity(dto.getPeriodoCobrancaTaxa());
        entity.setPeriodoCobrancaTaxa(periodo);
        
        // Configura o relacionamento bidirecional
        if (periodo != null) {
            periodo.setTaxaExtraConveniada(entity);
        }
    }

}
