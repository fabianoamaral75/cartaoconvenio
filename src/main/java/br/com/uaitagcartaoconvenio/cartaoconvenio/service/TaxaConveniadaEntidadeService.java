package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaConveniadaEntidadeMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveniadaEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaConveniadaEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ConveniadosRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.EntidadeRespository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxaConveniadaEntidadeRepository;
import jakarta.transaction.Transactional;

@Service
public class TaxaConveniadaEntidadeService {

    @Autowired
    private TaxaConveniadaEntidadeRepository repository;
    
    @Autowired
    private ConveniadosRepository conveniadosRepository;
    
    @Autowired
    private EntidadeRespository entidadeRespository;

    public List<TaxaConveniadaEntidade> findAllByEntidade(Long idEntidade) {
        return repository.findByEntidadeIdEntidade(idEntidade);
    }

    public List<TaxaConveniadaEntidade> findAllByConveniados(Long idConveniados) {
        return repository.findByConveniadosIdConveniados(idConveniados);
    }

    public List<TaxaConveniadaEntidade> findAllByEntidadeAndConveniados(Long idEntidade, Long idConveniados) {
        return repository.findByEntidadeIdEntidadeAndConveniadosIdConveniados(idEntidade, idConveniados);
    }

    public TaxaConveniadaEntidade findByIdAndEntidadeAndConveniados(Long idTaxa, Long idEntidade, Long idConveniados) 
        throws ExceptionCustomizada {
        
        Optional<TaxaConveniadaEntidade> optional = repository
            .findByIdAndEntidadeIdEntidadeAndConveniadosIdConveniados(idTaxa, idEntidade, idConveniados);
        
        if (!optional.isPresent()) {
            throw new ExceptionCustomizada("Taxa não encontrada para os IDs fornecidos");
        }
        
        return optional.get();
    }
    

    @Transactional
    public void updateStatusWithValidation(
            StatusTaxaConv newStatus,
            
            Long idEntidade,
            Long idConveniados,
            StatusTaxaConv currentStatus
    ) {
        // Atualização direta no banco
        int updated = repository.updateStatusByIdEntConv(
                newStatus,
                idEntidade,
                idConveniados
        );
        
        if (updated == 0) {
            throw new IllegalArgumentException(
                    "Nenhuma taxa encontrada para os IDs fornecidos."
            );
        }
    }    
    
    @Transactional
    public void updateStatusWithValidation(
            StatusTaxaConv newStatus,
            Long idTaxa,
            Long idEntidade,
            Long idConveniados
    ) {
        // Busca as taxas que correspondem aos IDs E ao status atual
        List<TaxaConveniadaEntidade> taxas = repository
                .findByEntidadeIdEntidadeAndConveniadosIdConveniados(
                        idEntidade,
                        idConveniados
                );

        if (taxas.isEmpty()) {
            throw new IllegalArgumentException(
                    "Nenhuma taxa encontrada para os IDs e status fornecidos."
            );
        }


        // Atualiza o status e salva (o @PreUpdate ajusta dtAlteracao)
        taxas.forEach(t -> {
            t.setStatusTaxaConEnt(newStatus);
            repository.save(t);
        });

    }
    
    @Transactional
    public void updateStatus(
            StatusTaxaConv newStatus,
            Long idTaxa
    ) {
    	TaxaConveniadaEntidade taxa = repository.findById(idTaxa).orElseThrow(() -> new UsernameNotFoundException("Não foi possivel encontrar a Taxa para o ID: " + idTaxa));
    	taxa.setStatusTaxaConEnt(newStatus);
    	repository.save(taxa);
    }


    public TaxaConveniadaEntidade save(TaxaConveniadaEntidadeDTO dto) {
 
    	Entidade entidade = entidadeRespository.findById(dto.getIdEntidade()).orElseThrow(() -> new UsernameNotFoundException("Entidade não encontrado: " + dto.getIdEntidade()));
    	
    	Conveniados conveniada = conveniadosRepository.findById(dto.getIdConveniados()).orElseThrow(() -> new UsernameNotFoundException("Conveniada não encontrado: " + dto.getIdConveniados()));
    	
    	//
        updateStatusWithValidation( StatusTaxaConv.DESATUALIZADA, dto.getIdEntidade(), dto.getIdConveniados(), StatusTaxaConv.ATUAL );
 
        TaxaConveniadaEntidade taxa = TaxaConveniadaEntidadeMapper.INSTANCE.toEntity(dto);
        
        taxa.setEntidade(entidade);
        taxa.setConveniados(conveniada);

        return repository.save(taxa);
    }

    public void delete(Long idTaxa, Long idEntidade, Long idConveniados) throws ExceptionCustomizada {
        TaxaConveniadaEntidade taxa = findByIdAndEntidadeAndConveniados(idTaxa, idEntidade, idConveniados);
        repository.delete(taxa);
    }

    public TaxaConveniadaEntidade update(Long idTaxa, TaxaConveniadaEntidadeDTO dto, Long idEntidade, Long idConveniados) 
        throws ExceptionCustomizada {
        
        TaxaConveniadaEntidade existing = findByIdAndEntidadeAndConveniados(idTaxa, idEntidade, idConveniados);
        
        TaxaConveniadaEntidade updated = TaxaConveniadaEntidadeMapper.INSTANCE.toEntity(dto);
        
    	Entidade entidade = entidadeRespository.findById(dto.getIdEntidade()).orElseThrow(() -> new UsernameNotFoundException("Entidade não encontrado: " + dto.getIdEntidade()));
    	Conveniados conveniada = conveniadosRepository.findById(dto.getIdConveniados()).orElseThrow(() -> new UsernameNotFoundException("Conveniada não encontrado: " + dto.getIdConveniados()));

        updated.setId(existing.getId());
        updated.setDtCriacao(existing.getDtCriacao());
        updated.setEntidade(entidade);
        updated.setConveniados(conveniada);
       
        return repository.save(updated);
    }
}
