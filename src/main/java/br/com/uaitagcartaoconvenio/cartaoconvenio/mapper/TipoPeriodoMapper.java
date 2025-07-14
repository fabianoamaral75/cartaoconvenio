package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PeriodoCobrancaTaxa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TipoPeriodo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TipoPeriodoDTO;

@Mapper(componentModel = "spring")
public interface TipoPeriodoMapper {

//    @Mapping(target = "periodosCobrancaIds", source = "periodosCobranca", qualifiedByName = "mapPeriodosCobrancaToIds")
    TipoPeriodoDTO toDTO(TipoPeriodo tipoPeriodo);

    @Mapping(target = "periodosCobranca", ignore = true)
    TipoPeriodo toEntity(TipoPeriodoDTO tipoPeriodoDTO);

    @Named("mapPeriodosCobrancaToIds")
    default List<Long> mapPeriodosCobrancaToIds(List<PeriodoCobrancaTaxa> periodosCobranca) {
        if (periodosCobranca == null) {
            return null;
        }
        return periodosCobranca.stream()
                .map(PeriodoCobrancaTaxa::getId)
                .collect(Collectors.toList());
    }
}