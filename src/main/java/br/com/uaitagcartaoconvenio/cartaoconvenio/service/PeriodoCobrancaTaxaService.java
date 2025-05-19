package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.PeriodoCobrancaTaxaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PeriodoCobrancaTaxa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PeriodoCobrancaTaxaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.PeriodoCobrancaTaxaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaExtraConveniadaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TipoPeriodoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PeriodoCobrancaTaxaService {

    private final PeriodoCobrancaTaxaRepository repository;
    private final TaxaExtraConveniadaRepository taxaExtraRepository;
    private final TipoPeriodoRepository tipoPeriodoRepository;
    private final PeriodoCobrancaTaxaMapper mapper;

    public PeriodoCobrancaTaxaDTO criarPeriodoCobranca(PeriodoCobrancaTaxaDTO dto) {
        PeriodoCobrancaTaxa periodo = mapper.toEntity(dto);
        
        // Configurar relacionamentos
        periodo.setTipoPeriodo(tipoPeriodoRepository.findById(dto.getTipoPeriodoId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de período não encontrado")));
                
        periodo.setTaxaExtraConveniada(taxaExtraRepository.findById(dto.getTaxaExtraConveniadaId())
                .orElseThrow(() -> new EntityNotFoundException("Taxa extra não encontrada")));
        
        PeriodoCobrancaTaxa saved = repository.save(periodo);
        return mapper.toDTO(saved);
    }
}