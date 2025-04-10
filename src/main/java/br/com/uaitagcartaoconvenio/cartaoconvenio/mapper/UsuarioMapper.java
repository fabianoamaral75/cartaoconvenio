package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Usuario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.UsuarioDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.UsuarioResumoDTO;

@Mapper( componentModel = "spring", uses = {
		                                    // UsuarioAcessoMapper.class
		                                   //  , PessoaMapper.class
		                                   }, unmappedTargetPolicy = ReportingPolicy.IGNORE )
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioDTO toDto(Usuario usuario);
    List<UsuarioDTO> toListDto(List<Usuario> listUsuario);
    Usuario toEntity(UsuarioDTO usuarioDTO);
    UsuarioResumoDTO toResumoDto(Usuario usuario);
}