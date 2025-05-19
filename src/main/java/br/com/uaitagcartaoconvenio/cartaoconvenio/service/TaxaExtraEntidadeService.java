package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.*;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.*;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.*;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaxaExtraEntidadeService {

    private final TaxaExtraEntidadeRepository repository;
    private final PeriodoCobrancaTaxaRepository periodoRepository;
    private final EntidadeRespository entidadeRepository;
    private final ItemTaxaExtraEntidadeRepository itemRepository;
    private final TaxaExtraEntidadeMapper mapper;
    private final ItemTaxaExtraEntidadeMapper itemMapper;

    @Transactional
    public TaxaExtraEntidadeDTO criarTaxaExtra(TaxaExtraEntidadeDTO dto) {
        TaxaExtraEntidade taxa = mapper.toEntity(dto);
        
        // Configurar relacionamentos obrigatórios
        taxa.setPeriodoCobrancaTaxa(periodoRepository.findById(dto.getPeriodoCobrancaTaxaId())
                .orElseThrow(() -> new EntityNotFoundException("Período de cobrança não encontrado")));
        
        taxa.setEntidade(entidadeRepository.findById(dto.getEntidadeId())
                .orElseThrow(() -> new EntityNotFoundException("Entidade não encontrada")));
        
        // Salvar a taxa primeiro
        TaxaExtraEntidade saved = repository.save(taxa);
        
        // Salvar os itens de relacionamento
        if (dto.getItensContasReceber() != null) {
            List<ItemTaxaExtraEntidade> itens = dto.getItensContasReceber().stream()
                    .map(itemDto -> {
                        ItemTaxaExtraEntidade item = itemMapper.toEntity(itemDto);
                        item.setTaxaExtraEntidade(saved);
                        return item;
                    })
                    .collect(Collectors.toList());
            
            itemRepository.saveAll(itens);
            saved.setItensContasReceber(itens);
        }
        
        return mapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public TaxaExtraEntidadeDTO buscarPorId(Long id) {
        TaxaExtraEntidade taxa = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Taxa extra não encontrada"));
        
        TaxaExtraEntidadeDTO dto = mapper.toDTO(taxa);
        
        // Carregar os itens associados
        List<ItemTaxaExtraEntidade> itens = itemRepository.findByTaxaExtraEntidadeId(id);
        dto.setItensContasReceber(itemMapper.toDTOList(itens));
        
        return dto;
    }

    @Transactional
    public void adicionarContaReceber(Long taxaExtraId, ItemTaxaExtraEntidadeDTO itemDto) {
        TaxaExtraEntidade taxa = repository.findById(taxaExtraId)
                .orElseThrow(() -> new EntityNotFoundException("Taxa extra não encontrada"));
        
        ItemTaxaExtraEntidade item = itemMapper.toEntity(itemDto);
        item.setTaxaExtraEntidade(taxa);
        
        itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<ItemTaxaExtraEntidadeDTO> listarContasReceberPorTaxa(Long taxaExtraId) {
        return itemMapper.toDTOList(itemRepository.findByTaxaExtraEntidadeId(taxaExtraId));
    }
}