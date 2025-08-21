
package br.com.uaitagcartaoconvenio.cartaoconvenio.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.CicloPagamentoVendaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ConveniadosResumoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaConveniadosResumoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxasFaixaVendasDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ConveniadosRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaConveniadosRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxasFaixaVendasRepository;

@Component
public class CicloPagamentoVendaMapper {

    @Autowired
    private ConveniadosRepository conveniadosRepository;
    
    @Autowired
    private TaxaConveniadosRepository taxaConveniadosRepository;
    
    @Autowired
    private TaxasFaixaVendasRepository taxasFaixaVendasRepository;
    
    @Autowired
    private TaxasFaixaVendasMapper taxasFaixaVendasMapper;
    
    @Autowired
    private FechamentoConvItensVendasMapper fechamentoMapper;
    
    @Autowired
    private ItemTaxaExtraConveniadaMapper itemTaxaExtraMapper;

    public CicloPagamentoVenda toEntity(CicloPagamentoVendaDTO dto) {
        if (dto == null) {
            return null;
        }

        CicloPagamentoVenda entity = new CicloPagamentoVenda();
        entity.setIdCicloPagamentoVenda(dto.getIdCicloPagamentoVenda());
        entity.setAnoMes(dto.getAnoMes());
        entity.setDtCriacao(dto.getDtCriacao());
        entity.setDtAlteracao(dto.getDtAlteracao());
        
        // Mapeamento de valores monetários
        entity.setVlrCicloBruto(dto.getVlrCicloBruto());
        entity.setVlrTaxaSecundaria(dto.getVlrTaxaSecundaria());
        entity.setVlrLiquido(dto.getVlrLiquido());
        entity.setVlrTaxaExtraPercentual(dto.getVlrTaxaExtraPercentual());
        entity.setVlrTaxaExtraValor(dto.getVlrTaxaExtraValor());
        entity.setVlrLiquidoPagamento(dto.getVlrLiquidoPagamento());
        entity.setVlrTaxasFaixaVendas(dto.getVlrTaxasFaixaVendas());
        
        // Mapeamento de informações de pagamento
        entity.setDtPagamento(dto.getDtPagamento());
        entity.setDocAutenticacaoBanco(dto.getDocAutenticacaoBanco());
        entity.setObservacao(dto.getObservacao());
        
        // Mapeamento de arquivos
        entity.setNomeArquivo(dto.getNomeArquivo());
        entity.setConteudoBase64(dto.getConteudoBase64());
        entity.setTamanhoBytes(dto.getTamanhoBytes());
        entity.setDataUpload(dto.getDataUpload());
        
        // Mapeamento de status e relacionamentos
        entity.setDescStatusPagamento(dto.getDescStatusPagamento());
        entity.setIdTaxaConveniadosEntidate(dto.getIdTaxaConveniadosEntidate());

        // Mapeamento de entidades relacionadas
        mapRelatedEntities(dto, entity);
        
        return entity;
    }

    private void mapRelatedEntities(CicloPagamentoVendaDTO dto, CicloPagamentoVenda entity) {
        // Mapeamento de Conveniados
        if (dto.getConveniados() != null && dto.getConveniados().getIdConveniados() != null) {
            entity.setConveniados(conveniadosRepository.findById(dto.getConveniados().getIdConveniados()).orElse(null));
        }

        // Mapeamento de TaxaConveniados
        if (dto.getTaxaConveniados() != null && dto.getTaxaConveniados().getIdTaxaConveniados() != null) {
            entity.setTaxaConveniados(taxaConveniadosRepository.findById(dto.getTaxaConveniados().getIdTaxaConveniados()).orElse(null));
        }

        // Mapeamento de TaxasFaixaVendas (com tratamento para evitar circularidade)
        if (dto.getTaxasFaixaVendas() != null && dto.getTaxasFaixaVendas().getId() != null) {
            entity.setTaxasFaixaVendas(taxasFaixaVendasRepository.findById(dto.getTaxasFaixaVendas().getId()).orElse(null));
        }
    }

    public CicloPagamentoVendaDTO toDTO(CicloPagamentoVenda entity) {
        if (entity == null) {
            return null;
        }

        CicloPagamentoVendaDTO dto = new CicloPagamentoVendaDTO();
        dto.setIdCicloPagamentoVenda(entity.getIdCicloPagamentoVenda());
        dto.setAnoMes(entity.getAnoMes());
        dto.setDtCriacao(entity.getDtCriacao());
        dto.setDtAlteracao(entity.getDtAlteracao());
        
        // Mapeamento de valores monetários
        dto.setVlrCicloBruto(entity.getVlrCicloBruto());
        dto.setVlrTaxaSecundaria(entity.getVlrTaxaSecundaria());
        dto.setVlrLiquido(entity.getVlrLiquido());
        dto.setVlrTaxaExtraPercentual(entity.getVlrTaxaExtraPercentual());
        dto.setVlrTaxaExtraValor(entity.getVlrTaxaExtraValor());
        dto.setVlrLiquidoPagamento(entity.getVlrLiquidoPagamento());
        dto.setVlrTaxasFaixaVendas(entity.getVlrTaxasFaixaVendas());
        
        // Mapeamento de informações de pagamento
        dto.setDtPagamento(entity.getDtPagamento());
        dto.setDocAutenticacaoBanco(entity.getDocAutenticacaoBanco());
        dto.setObservacao(entity.getObservacao());
        
        // Mapeamento de arquivos
        dto.setNomeArquivo(entity.getNomeArquivo());
        dto.setConteudoBase64(entity.getConteudoBase64());
        dto.setTamanhoBytes(entity.getTamanhoBytes());
        dto.setDataUpload(entity.getDataUpload());
        
        // Mapeamento de status e relacionamentos
        dto.setDescStatusPagamento(entity.getDescStatusPagamento());
        dto.setIdTaxaConveniadosEntidate(entity.getIdTaxaConveniadosEntidate());

        // Mapeamento de DTOs relacionados
        mapRelatedDTOs(entity, dto);
        
        return dto;
    }

    private void mapRelatedDTOs(CicloPagamentoVenda entity, CicloPagamentoVendaDTO dto) {
        // Mapeamento de ConveniadosResumoDTO
        if (entity.getConveniados() != null) {
            ConveniadosResumoDTO conveniadosDTO = new ConveniadosResumoDTO();
            conveniadosDTO.setIdConveniados(entity.getConveniados().getIdConveniados());
            conveniadosDTO.setNome(entity.getConveniados().getPessoa().getNomePessoa());
            dto.setConveniados(conveniadosDTO);
        }

        // Mapeamento de TaxaConveniadosResumoDTO
        if (entity.getTaxaConveniados() != null) {
            TaxaConveniadosResumoDTO taxaDTO = new TaxaConveniadosResumoDTO();
            taxaDTO.setIdTaxaConveniados(entity.getTaxaConveniados().getIdTaxaConveniados());
            dto.setTaxaConveniados(taxaDTO);
        }

        // Mapeamento de TaxasFaixaVendasDTO (com tratamento para evitar circularidade)
        if (entity.getTaxasFaixaVendas() != null) {
            TaxasFaixaVendasDTO taxasDTO = taxasFaixaVendasMapper.toDto(entity.getTaxasFaixaVendas());
          //  taxasDTO.setCiclosPagamento(null); // Quebra a referência circular
            dto.setTaxasFaixaVendas(taxasDTO);
        }

     // Mapeamento de FechamentoConvItensVendasDTO
        if (entity.getFechamentoConvItensVendas() != null) {
            dto.setFechamentoConvItensVendas(
                entity.getFechamentoConvItensVendas().stream()
                    .map(fechamentoMapper::toDto) // Alterado para toDto (d minúsculo)
                    .collect(Collectors.toList())
            );
        }

        // Mapeamento de ItemTaxaExtraConveniadaDTO
        if (entity.getItemTaxaExtraConveniada() != null) {
            dto.setItemTaxaExtraConveniada(
                entity.getItemTaxaExtraConveniada().stream()
                    .map(itemTaxaExtraMapper::toDTO)
                    .collect(Collectors.toList())
            );
        }
    }

    // Métodos para conversão de listas
    public List<CicloPagamentoVendaDTO> toDTOList(List<CicloPagamentoVenda> entities) {
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<CicloPagamentoVenda> toEntityList(List<CicloPagamentoVendaDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    // Método para mapeamento simplificado (sem relações profundas)
    public CicloPagamentoVendaDTO toSimpleDTO(CicloPagamentoVenda entity) {
        if (entity == null) {
            return null;
        }

        CicloPagamentoVendaDTO dto = new CicloPagamentoVendaDTO();
        dto.setIdCicloPagamentoVenda(entity.getIdCicloPagamentoVenda());
        dto.setAnoMes(entity.getAnoMes());
        dto.setDescStatusPagamento(entity.getDescStatusPagamento());
        dto.setVlrLiquidoPagamento(entity.getVlrLiquidoPagamento());
        dto.setDtPagamento(entity.getDtPagamento());
        
        return dto;
    }
}

