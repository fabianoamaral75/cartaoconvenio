package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.ContratoEntidadeMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContratoEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ContratoEntidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContratoEntidadeService {

    private final ContratoEntidadeRepository repository;
    private final ContratoEntidadeMapper mapper;
    private final EntidadeService entidadeService;

    @Transactional
    public ContratoEntidadeDTO create(ContratoEntidadeDTO dto) {
        ContratoEntidade entity = mapper.toEntity(dto);
        entity.setEntidade(entidadeService.findByIdEntity(dto.getIdEntidade()));
        return mapper.toDTO(repository.save(entity));
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
}