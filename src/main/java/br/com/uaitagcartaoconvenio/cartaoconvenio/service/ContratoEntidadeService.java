package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.ContratoEntidadeMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ArqContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ServicoContrato;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContratoEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ContratoEntidadeRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.EntidadeRespository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContratoEntidadeService {

	@Autowired           
    private ContratoEntidadeRepository repository;
	
    @Autowired           
    private ContratoEntidadeMapper mapper;
        
    @Autowired           
    private EntidadeRespository entidadeRepository;

    public List<ContratoEntidade> findAllByEntidadeId(Long idEntidade) {
        return repository.findByEntidadeIdEntidade(idEntidade);
    }

    public List<ContratoEntidade> findByEntidadeIdAndStatus(Long idEntidade, Boolean status) {
        return repository.findByEntidadeIdEntidadeAndStatus(idEntidade, status);
    }

    public ContratoEntidade findByIdAndEntidadeId(Long idContrato, Long idEntidade) throws ExceptionCustomizada {
        return repository.findByIdContratoEntidadeAndEntidadeIdEntidade(idContrato, idEntidade)
                .orElseThrow(() -> new ExceptionCustomizada("Contrato não encontrado para a entidade informada"));
    }
/*
    @Transactional
    public ContratoEntidadeDTO create(ContratoEntidadeDTO dto) {
        ContratoEntidade entity = mapper.toEntity(dto);
        entity.setStatus(true); 
        entity.setEntidade(entidadeService.findByIdEntity(dto.getIdEntidade()));
        return mapper.toDTO(repository.save(entity));
    }
*/    

    @Transactional
    public ContratoEntidadeDTO create(ContratoEntidadeDTO dto) {
        // Busca a entidade
        Entidade entidade = entidadeRepository.findById(dto.getIdEntidade())
                .orElseThrow(() -> new EntityNotFoundException("Entidade não encontrada"));

        // Converte DTO para Entity
        ContratoEntidade contrato = mapper.toEntity(dto);
        contrato.setEntidade(entidade);

        // Adiciona serviços se existirem
        if (dto.getServicos() != null) {
        	for(int i = 0; i < dto.getServicos().size(); i++ ) {
        		ServicoContrato servico = mapper.servicoDTOToServico( dto.getServicos().get(i) );
        		servico.setContratoEntidade(contrato);
        		contrato.getServicos().get(i).setContratoEntidade(contrato);
        	}
        }

        // Adiciona vigencias se existirem
        if (dto.getVigencias() != null) {        	
        	for(int i = 0; i < dto.getVigencias().size(); i++ ) {
        		VigenciaContratoEntidade vigencia = mapper.vigenciaDTOToVigencia( dto.getVigencias().get(i) );
        		vigencia.setContratoEntidade(contrato);
        		contrato.getVigencias().get(i).setContratoEntidade(contrato);
        	}
        }

        // Adiciona arquivos se existirem
        if (dto.getArquivos() != null) {
        	for(int i = 0; i < dto.getArquivos().size(); i++ ) {
        		ArqContratoEntidade arquivo = mapper.arqContratoDTOToArqContrato(dto.getArquivos().get(i));
        		arquivo.setContratoEntidade(contrato);
        		contrato.getArquivos().get(i).setContratoEntidade(contrato);
        	}
        }

        // Salva o contrato (cascata salvará serviços, vigencias e arquivos)
        ContratoEntidade savedContrato = repository.save(contrato);

        return mapper.toDTO(savedContrato);
    }


    @Transactional(readOnly = true)
    public ContratoEntidadeDTO findById(Long id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrato não encontrado")));
    }

    @Transactional(readOnly = true)
    public List<ContratoEntidadeDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ContratoEntidadeDTO update(Long id, ContratoEntidadeDTO dto) {
    	
    	if (dto.getVlrMensal() != null && dto.getVlrMensal().compareTo(BigDecimal.ZERO) < 0) {
    	    throw new IllegalArgumentException("Valor mensal inválido");
    	}
    	
        ContratoEntidade entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrato não encontrado"));
        mapper.updateEntityFromDTO(dto, entity);
        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
    
    @Transactional
    public void delete(Long idContrato, Long idEntidade) throws ExceptionCustomizada {
        ContratoEntidade contrato = findByIdAndEntidadeId(idContrato, idEntidade);
        repository.delete(contrato);
    }

    @Transactional
    public ContratoEntidade renovarContrato(Long idContrato, Long idEntidade, ContratoEntidade novoContrato) throws ExceptionCustomizada {
        ContratoEntidade contratoAtual = findByIdAndEntidadeId(idContrato, idEntidade);
        contratoAtual.setStatus(false); // Contrato antigo é desativado
        repository.save(contratoAtual);
        
        novoContrato.setStatus(true); // Novo contrato inicia como inativo
        return repository.save(novoContrato);
    }
    
}






