package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxasExtraordinarias;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxasExtraordinariasDTO;

@Mapper
public interface TaxasExtraordinariasMapper {
    
    TaxasExtraordinariasMapper INSTANCE = Mappers.getMapper(TaxasExtraordinariasMapper.class);
    
    TaxasExtraordinariasDTO toDTO(TaxasExtraordinarias entity);
    
    TaxasExtraordinarias toEntity(TaxasExtraordinariasDTO dto);
}
