package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Pessoa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ConveniadosDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ConveniadosResumoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PessoaResumoDTO;

@Mapper(componentModel = "spring", 
        uses = {
            NichoMapper.class, 
            RamoAtividadeMapper.class, 
            TaxaConveniadosMapper.class
       //     , PessoaMapper.class
        }, 
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConveniadosMapper {
    
    ConveniadosMapper INSTANCE = Mappers.getMapper(ConveniadosMapper.class);

    // @Mapping(target = "pessoa", ignore = true)
    ConveniadosDTO toDto(Conveniados conveniados);
    
    // @Mapping(target = "pessoa", ignore = true)
    List<ConveniadosDTO> toListDto(List<Conveniados> conveniados);
    
    // @Mapping(target = "pessoa", ignore = true)
    Conveniados toEntity(ConveniadosDTO conveniadosDTO);
    
    //@Mapping(target = "pessoa", ignore = true)
    ConveniadosResumoDTO toResumoDto(Conveniados conveniados);
    
    /**
     * Método para mapeamento completo incluindo a pessoa
     * @param dto DTO do Conveniado
     * @param pessoa Entidade Pessoa já mapeada
     * @return Conveniados com pessoa associada
     */
    @Named("mapWithPessoa")
    default Conveniados mapWithPessoa(ConveniadosDTO dto, Pessoa pessoa) {
        if (dto == null) {
            return null;
        }
        
        Conveniados conveniados = toEntity(dto);
        conveniados.setPessoa(pessoa);
        
        return conveniados;
    }
    
    /**
     * Método para mapeamento usando DTO da pessoa
     * @param dto DTO do Conveniado
     * @param pessoaDto DTO da Pessoa
     * @return Conveniados com pessoa associada
     */
    @Named("mapWithPessoaDto")
    default Conveniados mapWithPessoa(ConveniadosDTO dto, PessoaResumoDTO pessoaDto) {
        if (dto == null || pessoaDto == null) {
            return null;
        }
        
        Conveniados conveniados = toEntity(dto);
        
        // Cria uma instância básica de Pessoa apenas com o ID
        Pessoa pessoa = new Pessoa();
        pessoa.setIdPessoa(pessoaDto.getIdPessoa());
        pessoa.setNomePessoa(pessoaDto.getNomePessoa());
        conveniados.setPessoa(pessoa);
        
        return conveniados;
    }
    
    /**
     * Método para atualização de entidade existente
     * @param dto DTO com atualizações
     * @param conveniados Entidade existente
     * @return Conveniados atualizado
     */
    @Named("updateFromDto")
    default Conveniados updateFromDto(ConveniadosDTO dto, Conveniados conveniados) {
        if (dto == null) {
            return conveniados;
        }
        
        // Atualiza campos básicos
        for(int i = 0; i < dto.getCicloPagamentoVenda().size(); i++  ) {
        	CicloPagamentoVenda cpv = new CicloPagamentoVenda();
        	
        	cpv.setIdCicloPagamentoVenda( dto.getCicloPagamentoVenda().get(i).getIdCicloPagamentoVenda() );
        	cpv.setAnoMes( dto.getCicloPagamentoVenda().get(i).getAnoMes() );
            conveniados.getCicloPagamentoVenda().add(cpv);
        }
        
        // Pessoa não é atualizada aqui - usar mapWithPessoa separadamente
        return conveniados;
    }
    
    @Named("toDtoWithPessoa")
    default ConveniadosDTO toDtoWithPessoa(Conveniados conveniados) {
        if (conveniados == null) {
            return null;
        }
        
        ConveniadosDTO dto = toDto(conveniados);
        if (conveniados.getPessoa() != null) {
            PessoaResumoDTO pessoaDto = new PessoaResumoDTO();
            pessoaDto.setIdPessoa(conveniados.getPessoa().getIdPessoa());
            pessoaDto.setNomePessoa(conveniados.getPessoa().getNomePessoa());
            dto.setPessoa(pessoaDto);
        }
        
        return dto;
    }
    
    default Page<ConveniadosDTO> toPageDto(Page<Conveniados> page) {
        return page.map(this::toDto);
    }
}