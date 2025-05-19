package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaExtraConveniadaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaExtraConveniadaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.PeriodoCobrancaTaxaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaExtraConveniadaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaxaExtraConveniadaService {

    private final TaxaExtraConveniadaRepository repository;
    private final PeriodoCobrancaTaxaRepository periodoRepository;
    private final TaxaExtraConveniadaMapper mapper;

    public TaxaExtraConveniada save(TaxaExtraConveniada entity) {
        return repository.save(entity);
    }

    public TaxaExtraConveniada findById(Long id) {
        return repository.findById(id)
               .orElseThrow(() -> new EntityNotFoundException("Taxa extra conveniada não encontrada"));
    }

    public TaxaExtraConveniadaDTO criarTaxaExtra(TaxaExtraConveniadaDTO dto) {
        TaxaExtraConveniada taxa = mapper.toEntity(dto);
        
        taxa.setPeriodoCobrancaTaxa(periodoRepository.findById(dto.getPeriodoCobrancaTaxaId())
                .orElseThrow(() -> new EntityNotFoundException("Período de cobrança não encontrado")));
        
        TaxaExtraConveniada saved = repository.save(taxa);
        return mapper.toDTO(saved);
    }
}