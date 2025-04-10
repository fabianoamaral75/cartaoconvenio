package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Cartao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.CartaoDTO;

@Mapper(componentModel = "spring"/*,uses = {FuncionarioMapper.class}*/, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartaoMapper {
    CartaoMapper INSTANCE = Mappers.getMapper(CartaoMapper.class);

    CartaoDTO toDto(Cartao cartao);
    List<CartaoDTO> toListDto(List<Cartao> listCartao); 
    Cartao toEntity(CartaoDTO cartaoDTO);
}
