package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaExtraConveniadaMapper;
// import br.com.uaitagcartaoconvenio.cartaoconvenio.dto.TaxaExtraConveniadaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PeriodoCobrancaTaxa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TipoPeriodo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaExtraConveniadaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ConveniadosRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.PeriodoCobrancaTaxaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaExtraConveniadaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TipoPeriodoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;


@Service
public class TaxaExtraConveniadaService {

    @Autowired
    private TaxaExtraConveniadaRepository taxaExtraConveniadaRepository;

    @Autowired
    private ConveniadosRepository conveniadosRepository;

    @Autowired
    private TipoPeriodoRepository tipoPeriodoRepository;
    
    @Autowired
    private PeriodoCobrancaTaxaRepository periodoRepository;
    
    @Autowired
    private TaxaExtraConveniadaMapper mapper;
    
    public TaxaExtraConveniada save(TaxaExtraConveniada entity) {
        return taxaExtraConveniadaRepository.save(entity);
    }

    public TaxaExtraConveniada findById(Long id) {
        return taxaExtraConveniadaRepository.findById(id)
               .orElseThrow(() -> new EntityNotFoundException("Taxa extra conveniada não encontrada"));
    }

    public TaxaExtraConveniadaDTO criarTaxaExtra(TaxaExtraConveniadaDTO dto) {
        TaxaExtraConveniada taxa = mapper.toEntity(dto);
        
        taxa.setPeriodoCobrancaTaxa(periodoRepository.findById(dto.getPeriodoCobrancaTaxa().getId())
                .orElseThrow(() -> new EntityNotFoundException("Período de cobrança não encontrado")));
        
        TaxaExtraConveniada saved = taxaExtraConveniadaRepository.save(taxa);
        return mapper.toDTO(saved);
    }
    

    public List<TaxaExtraConveniada> findAllByConveniadoId(Long idConveniado) {
        return taxaExtraConveniadaRepository.findByConveniadoId(idConveniado);
    }

    public TaxaExtraConveniada findById(Long idConveniado, Long idTaxa) {
        return taxaExtraConveniadaRepository.findByConveniadoIdAndTaxaId(idConveniado, idTaxa)
                .orElseThrow(() -> new EntityNotFoundException("Taxa não encontrada para o conveniado informado"));
    }

    public List<TaxaExtraConveniada> findByConveniadoIdAndStatus(Long idConveniado, String status) {
        if (!isStatusValido(status)) {
            throw new IllegalArgumentException("Status inválido. Valores permitidos: ATIVA, DESATIVA, SUSPENSO");
        }
        return taxaExtraConveniadaRepository.findByConveniadoIdAndStatus(idConveniado, status);
    }

    @Transactional
    public TaxaExtraConveniada create(Long idConveniado, TaxaExtraConveniadaDTO dto) {
        Conveniados conveniado = conveniadosRepository.findById(idConveniado)
                .orElseThrow(() -> new EntityNotFoundException("Conveniado não encontrado"));

        TipoPeriodo tipoPeriodo = tipoPeriodoRepository.findById(dto.getPeriodoCobrancaTaxa().getTipoPeriodoId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de período não encontrado"));

        // Cria período de cobrança
        PeriodoCobrancaTaxa periodo = new PeriodoCobrancaTaxa();
        periodo.setDescricao  ( dto.getPeriodoCobrancaTaxa().getDescricao()  );
        periodo.setDataInicio ( dto.getPeriodoCobrancaTaxa().getDataInicio() );
        periodo.setDataFim    ( dto.getPeriodoCobrancaTaxa().getDataFim()    );
        periodo.setObservacao ( dto.getPeriodoCobrancaTaxa().getObservacao() );
        periodo.setTipoPeriodo( tipoPeriodo                                    );

        // Cria taxa
        TaxaExtraConveniada taxa = new TaxaExtraConveniada();
        taxa.setDescricaoTaxa      ( dto.getDescricaoTaxa() );
        taxa.setValorTaxa          ( dto.getValorTaxa()     );
        taxa.setStatusTaxa         ( dto.getStatusTaxa()    );
        taxa.setConveniados        ( conveniado             );
        taxa.setPeriodoCobrancaTaxa( periodo                );

        return taxaExtraConveniadaRepository.save(taxa);
    }

    @Transactional
    public TaxaExtraConveniada update(Long idConveniado, Long idTaxa, TaxaExtraConveniadaDTO dto) {
        TaxaExtraConveniada taxa = findById(idConveniado, idTaxa);

        if (dto.getDescricaoTaxa() != null) {
            taxa.setDescricaoTaxa(dto.getDescricaoTaxa());
        }
        
        if (dto.getValorTaxa() != null) {
            taxa.setValorTaxa(dto.getValorTaxa());
        }
        
        if (dto.getStatusTaxa() != null) {
            if (!isStatusValido(dto.getStatusTaxa())) {
                throw new IllegalArgumentException("Status inválido. Valores permitidos: ATIVA, DESATIVA, SUSPENSO");
            }
            taxa.setStatusTaxa(dto.getStatusTaxa());
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

        return taxaExtraConveniadaRepository.save(taxa);
    }

    @Transactional
    public void delete(Long idConveniado, Long idTaxa) {
        TaxaExtraConveniada taxa = findById(idConveniado, idTaxa);
        taxaExtraConveniadaRepository.delete(taxa);
    }

    @Transactional
    public TaxaExtraConveniada updateStatus(Long idConveniado, Long idTaxa, String novoStatus) {
        if (!isStatusValido(novoStatus)) {
            throw new IllegalArgumentException("Status inválido. Valores permitidos: ATIVA, DESATIVA, SUSPENSO");
        }

        TaxaExtraConveniada taxa = findById(idConveniado, idTaxa);
        taxa.setStatusTaxa(novoStatus);
        return taxaExtraConveniadaRepository.save(taxa);
    }

    private boolean isStatusValido(String status) {
        return status != null && 
               (status.equals("ATIVA") || status.equals("DESATIVA") || status.equals("SUSPENSO"));
    }
}