package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.UsuarioAcesso;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.UsuarioAcessoDTO;

@Mapper(componentModel = "spring", uses = {AcessoMapper.class, UsuarioMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioAcessoMapper {
    UsuarioAcessoMapper INSTANCE = Mappers.getMapper(UsuarioAcessoMapper.class);

    UsuarioAcessoDTO toDto(UsuarioAcesso usuarioAcesso);
    UsuarioAcesso toEntity(UsuarioAcessoDTO usuarioAcessoDTO);
}
