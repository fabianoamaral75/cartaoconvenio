package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TipoPeriodoMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TipoPeriodo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TipoPeriodoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TipoPeriodoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoPeriodoService {

    private final TipoPeriodoRepository tipoPeriodoRepository;
    private final TipoPeriodoMapper tipoPeriodoMapper;

    @Transactional(readOnly = true)
    public List<TipoPeriodoDTO> findAll() {
        return tipoPeriodoRepository.findAll().stream()
                .map(tipoPeriodoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TipoPeriodoDTO findById(Long id) {
        TipoPeriodo tipoPeriodo = tipoPeriodoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de período não encontrado com id: " + id));
        return tipoPeriodoMapper.toDTO(tipoPeriodo);
    }

    @Transactional
    public TipoPeriodoDTO create(TipoPeriodoDTO tipoPeriodoDTO) {
        if (tipoPeriodoRepository.existsByDescricao(tipoPeriodoDTO.getDescricao())) {
            throw new IllegalArgumentException("Já existe um tipo de período com esta descrição");
        }
        
        TipoPeriodo tipoPeriodo = tipoPeriodoMapper.toEntity(tipoPeriodoDTO);
        TipoPeriodo savedTipoPeriodo = tipoPeriodoRepository.save(tipoPeriodo);
        return tipoPeriodoMapper.toDTO(savedTipoPeriodo);
    }

    @Transactional
    public TipoPeriodoDTO update(Long id, TipoPeriodoDTO tipoPeriodoDTO) {
        TipoPeriodo existingTipoPeriodo = tipoPeriodoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de período não encontrado com id: " + id));

        if (!existingTipoPeriodo.getDescricao().equals(tipoPeriodoDTO.getDescricao()) && 
            tipoPeriodoRepository.existsByDescricao(tipoPeriodoDTO.getDescricao())) {
            throw new IllegalArgumentException("Já existe um tipo de período com esta descrição");
        }

        existingTipoPeriodo.setDescricao(tipoPeriodoDTO.getDescricao());
        TipoPeriodo updatedTipoPeriodo = tipoPeriodoRepository.save(existingTipoPeriodo);
        return tipoPeriodoMapper.toDTO(updatedTipoPeriodo);
    }

    @Transactional
    public void delete(Long id) {
        if (!tipoPeriodoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de período não encontrado com id: " + id);
        }
        tipoPeriodoRepository.deleteById(id);
    }
}