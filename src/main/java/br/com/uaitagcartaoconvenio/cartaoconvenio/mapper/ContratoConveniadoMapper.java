
/*
package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;


//   Modelo de Mapper para todo o projeto.
 
import java.time.LocalDateTime;
import java.util.Date;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContratoConveniado;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContratoConveniadoDTO;

@Mapper(componentModel = "spring")
public interface ContratoConveniadoMapper {

    ContratoConveniadoMapper INSTANCE = Mappers.getMapper(ContratoConveniadoMapper.class);

    @Mapping(target = "dtCriacao", source = "dtCriacao", qualifiedByName = "dateToLocalDateTime")
    @Mapping(target = "dataUpload", source = "dataUpload", qualifiedByName = "dateToLocalDateTime")
    @Mapping(target = "conteudoBase64", source = "conteudoBase64")
    @Mapping(target = "idConveniados", source = "conveniados.idConveniados")
    ContratoConveniadoDTO toDTO(ContratoConveniado contratoConveniado);

    @Mapping(target = "dtCriacao", source = "dtCriacao", qualifiedByName = "localDateTimeToDate")
    @Mapping(target = "dataUpload", source = "dataUpload", qualifiedByName = "localDateTimeToDate")
    @Mapping(target = "conveniados", ignore = true) // Será tratado no serviço
    ContratoConveniado toEntity(ContratoConveniadoDTO dto); 

    @Named("dateToLocalDateTime")
    default LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) return null;
        return new java.sql.Timestamp(date.getTime()).toLocalDateTime();
    }

    @Named("localDateTimeToDate")
    default Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return java.sql.Timestamp.valueOf(localDateTime);
    }
}
*/

package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContratoConveniado;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContratoConveniadoDTO;

@Mapper(componentModel = "spring")
public interface ContratoConveniadoMapper {

    ContratoConveniadoMapper INSTANCE = Mappers.getMapper(ContratoConveniadoMapper.class);

    @Mapping(target = "dtCriacao", source = "dtCriacao", qualifiedByName = "dateToLocalDateTime")
    @Mapping(target = "dataUpload", source = "dataUpload", qualifiedByName = "dateToLocalDateTime")
    @Mapping(target = "conteudoBase64", source = "conteudoBase64")
    @Mapping(target = "idConveniados", source = "conveniados.idConveniados")
    ContratoConveniadoDTO toDTO(ContratoConveniado contratoConveniado);

    @Mapping(target = "dtCriacao", source = "dtCriacao", qualifiedByName = "localDateTimeToDate")
    @Mapping(target = "dataUpload", source = "dataUpload", qualifiedByName = "localDateTimeToDate")
    @Mapping(target = "conveniados", ignore = true) // Será tratado no serviço
    ContratoConveniado toEntity(ContratoConveniadoDTO dto);

    /**
     * Converte lista de entidades para lista de DTOs
     */
    default List<ContratoConveniadoDTO> toListDTO(List<ContratoConveniado> contratos) {
        if (contratos == null) {
            return Collections.emptyList();
        }
        return contratos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converte lista de DTOs para lista de entidades
     */
    default List<ContratoConveniado> toListEntity(List<ContratoConveniadoDTO> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Named("dateToLocalDateTime")
    default LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) return null;
        return new java.sql.Timestamp(date.getTime()).toLocalDateTime();
    }

    @Named("localDateTimeToDate")
    default Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return java.sql.Timestamp.valueOf(localDateTime);
    }
}