package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.ItemTaxaExtraEntidadeMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaExtraEntidadeMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ItemTaxaExtraEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PeriodoCobrancaTaxa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TipoPeriodo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ItemTaxaExtraEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaExtraEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.EntidadeRespository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ItemTaxaExtraEntidadeRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.PeriodoCobrancaTaxaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaExtraEntidadeRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TipoPeriodoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaxaExtraEntidadeService {

	@Autowired
    private TaxaExtraEntidadeRepository taxaExtraEntidadeRepository;
	
    private final PeriodoCobrancaTaxaRepository periodoRepository;
    
    @Autowired
    private EntidadeRespository entidadeRepository;
    
    @Autowired
    private TipoPeriodoRepository tipoPeriodoRepository;

    @Autowired
    private TaxaExtraEntidadeMapper taxaExtraEntidadeMapper;
    
    private final ItemTaxaExtraEntidadeRepository itemRepository;
    private final ItemTaxaExtraEntidadeMapper itemMapper;

    @Transactional
    public TaxaExtraEntidadeDTO criarTaxaExtra(TaxaExtraEntidadeDTO dto) {
        TaxaExtraEntidade taxa = taxaExtraEntidadeMapper.toEntity(dto);
        
        // Configurar relacionamentos obrigatórios
        taxa.setPeriodoCobrancaTaxa(periodoRepository.findById(dto.getPeriodoCobrancaTaxa().getId())
                .orElseThrow(() -> new EntityNotFoundException("Período de cobrança não encontrado")));
        
        taxa.setEntidade(entidadeRepository.findById(dto.getEntidadeId())
                .orElseThrow(() -> new EntityNotFoundException("Entidade não encontrada")));
        
        // Salvar a taxa primeiro
        TaxaExtraEntidade saved = taxaExtraEntidadeRepository.save(taxa);
        
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
        
        return taxaExtraEntidadeMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public TaxaExtraEntidadeDTO buscarPorId(Long id) {
        TaxaExtraEntidade taxa = taxaExtraEntidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Taxa extra não encontrada"));
        
        TaxaExtraEntidadeDTO dto = taxaExtraEntidadeMapper.toDto(taxa);
        
        // Carregar os itens associados
        List<ItemTaxaExtraEntidade> itens = itemRepository.findByTaxaExtraEntidadeId(id);
        dto.setItensContasReceber(itemMapper.toDTOList(itens));
        
        return dto;
    }

    @Transactional
    public void adicionarContaReceber(Long taxaExtraId, ItemTaxaExtraEntidadeDTO itemDto) {
        TaxaExtraEntidade taxa = taxaExtraEntidadeRepository.findById(taxaExtraId)
                .orElseThrow(() -> new EntityNotFoundException("Taxa extra não encontrada"));
        
        ItemTaxaExtraEntidade item = itemMapper.toEntity(itemDto);
        item.setTaxaExtraEntidade(taxa);
        
        itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<ItemTaxaExtraEntidadeDTO> listarContasReceberPorTaxa(Long taxaExtraId) {
        return itemMapper.toDTOList(itemRepository.findByTaxaExtraEntidadeId(taxaExtraId));
    }
        
    public List<TaxaExtraEntidade> findAllByEntidadeId(Long idEntidade) throws ExceptionCustomizada {
        List<TaxaExtraEntidade> taxas = taxaExtraEntidadeRepository.findByEntidadeId(idEntidade);
        if (taxas.isEmpty()) {
            throw new ExceptionCustomizada("Não existem taxas para a entidade com ID: " + idEntidade);
        }
        return taxas;
    }

    public TaxaExtraEntidade findById(Long idEntidade, Long idTaxa) throws ExceptionCustomizada {
        return taxaExtraEntidadeRepository.findByEntidadeIdAndTaxaId(idEntidade, idTaxa)
                .orElseThrow(() -> new ExceptionCustomizada("Taxa não encontrada para a entidade informada"));
    }

    public List<TaxaExtraEntidade> findByEntidadeIdAndStatus(Long idEntidade, String status) throws ExceptionCustomizada {
        if (!isStatusValido(status)) {
            throw new ExceptionCustomizada("Status inválido. Valores permitidos: ATIVA, DESATIVA, SUSPENSO");
        }
        List<TaxaExtraEntidade> taxas = taxaExtraEntidadeRepository.findByEntidadeIdAndStatus(idEntidade, status);
        if (taxas.isEmpty()) {
            throw new ExceptionCustomizada("Não existem taxas com status " + status + " para a entidade com ID: " + idEntidade);
        }
        return taxas;
    }

    @Transactional
    public TaxaExtraEntidade create(Long idEntidade, TaxaExtraEntidadeDTO dto) throws ExceptionCustomizada {
        Entidade entidade = entidadeRepository.findById(idEntidade)
                .orElseThrow(() -> new ExceptionCustomizada("Entidade não encontrada"));

        TipoPeriodo tipoPeriodo = tipoPeriodoRepository.findById(dto.getPeriodoCobrancaTaxa().getTipoPeriodoId())
                .orElseThrow(() -> new ExceptionCustomizada("Tipo de período não encontrado"));

        // Valida status
        if (dto.getStatus() != null && !isStatusValido(dto.getStatus())) {
            throw new ExceptionCustomizada("Status inválido. Valores permitidos: ATIVA, DESATIVA, SUSPENSO");
        }

        // Cria período de cobrança
        PeriodoCobrancaTaxa periodo = new PeriodoCobrancaTaxa();
        periodo.setDescricao  ( dto.getPeriodoCobrancaTaxa().getDescricao()  );
        periodo.setDataInicio ( dto.getPeriodoCobrancaTaxa().getDataInicio() );
        periodo.setDataFim    ( dto.getPeriodoCobrancaTaxa().getDataFim()    );
        periodo.setObservacao ( dto.getPeriodoCobrancaTaxa().getObservacao() );
        periodo.setTipoPeriodo(tipoPeriodo);

        // Cria taxa
        TaxaExtraEntidade taxa = taxaExtraEntidadeMapper.toEntity(dto);
        taxa.setEntidade(entidade);
        taxa.setPeriodoCobrancaTaxa(periodo);

        return taxaExtraEntidadeRepository.save(taxa);
    }

    @Transactional
    public TaxaExtraEntidade update(Long idEntidade, Long idTaxa, TaxaExtraEntidadeDTO dto) throws ExceptionCustomizada {
        TaxaExtraEntidade taxa = findById(idEntidade, idTaxa);

        if (dto.getDescricao() != null) {
            taxa.setDescricao(dto.getDescricao());
        }
        
        if (dto.getValor() != null) {
            taxa.setValor(dto.getValor());
        }
        
        if (dto.getStatus() != null) {
            if (!isStatusValido(dto.getStatus())) {
                throw new ExceptionCustomizada("Status inválido. Valores permitidos: ATIVA, DESATIVA, SUSPENSO");
            }
            taxa.setStatus(dto.getStatus());
        }

        if (dto.getObservacao() != null) {
            taxa.setObservacao(dto.getObservacao());
        }

        // Atualiza período de cobrança se necessário
        if (dto.getPeriodoCobrancaTaxa().getDataInicio() != null || dto.getPeriodoCobrancaTaxa().getDataFim()    != null || 
            dto.getPeriodoCobrancaTaxa().getDescricao()  != null || dto.getPeriodoCobrancaTaxa().getObservacao() != null) {
            
            PeriodoCobrancaTaxa periodo = taxa.getPeriodoCobrancaTaxa();
            if (dto.getPeriodoCobrancaTaxa().getDataInicio() != null) periodo.setDataInicio( dto.getPeriodoCobrancaTaxa().getDataInicio() );
            if (dto.getPeriodoCobrancaTaxa().getDataFim()    != null) periodo.setDataFim   ( dto.getPeriodoCobrancaTaxa().getDataFim()    );
            if (dto.getPeriodoCobrancaTaxa().getDescricao()  != null) periodo.setDescricao ( dto.getPeriodoCobrancaTaxa().getDescricao()  );
            if (dto.getPeriodoCobrancaTaxa().getObservacao() != null) periodo.setObservacao( dto.getPeriodoCobrancaTaxa().getObservacao() );
        }

        return taxaExtraEntidadeRepository.save(taxa);
    }

    @Transactional
    public void delete(Long idEntidade, Long idTaxa) throws ExceptionCustomizada {
        TaxaExtraEntidade taxa = findById(idEntidade, idTaxa);
        taxaExtraEntidadeRepository.delete(taxa);
    }

    @Transactional
    public TaxaExtraEntidade updateStatus(Long idEntidade, Long idTaxa, String novoStatus) throws ExceptionCustomizada {
        if (!isStatusValido(novoStatus)) {
            throw new ExceptionCustomizada("Status inválido. Valores permitidos: ATIVA, DESATIVA, SUSPENSO");
        }

        TaxaExtraEntidade taxa = findById(idEntidade, idTaxa);
        taxa.setStatus(novoStatus);
        return taxaExtraEntidadeRepository.save(taxa);
    }

    private boolean isStatusValido(String status) {
        return status != null && 
               (status.equals("ATIVA") || status.equals("DESATIVA") || status.equals("SUSPENSO"));
    }
    
}