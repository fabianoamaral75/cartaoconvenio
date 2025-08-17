package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxasExtraordinariasMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxasExtraordinarias;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxasExtraordinariasDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxasExtraordinariasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaxasExtraordinariasService {

    private final TaxasExtraordinariasRepository repository;
    private final TaxasExtraordinariasMapper mapper;

    @Transactional(readOnly = true)
    public Page<TaxasExtraordinariasDTO> findAllPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataCriacao").descending());
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<TaxasExtraordinariasDTO> findByStatus(String status) {
        return repository.findByStatusTaxa(status)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaxasExtraordinariasDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ExceptionCustomizada("Taxa Extraordinária não encontrada com ID: " + id));
    }

    @Transactional
    public TaxasExtraordinariasDTO create(TaxasExtraordinariasDTO dto) {
        TaxasExtraordinarias entity = mapper.toEntity(dto);
        entity.setIdTaxasExtraordinarias(null); // Garante que será criado novo registro
        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public TaxasExtraordinariasDTO update(Long id, TaxasExtraordinariasDTO dto) {
        TaxasExtraordinarias existingEntity = repository.findById(id)
                .orElseThrow(() -> new ExceptionCustomizada("Taxa Extraordinária não encontrada com ID: " + id));
        
        // Atualiza apenas os campos permitidos
        existingEntity.setDescricaoTaxaExtraordinarias(dto.getDescricaoTaxaExtraordinarias());
        existingEntity.setValorTaxaExtraordinarias(dto.getValorTaxaExtraordinarias());
        existingEntity.setStatusTaxa(dto.getStatusTaxa());
        
        return mapper.toDTO(repository.save(existingEntity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ExceptionCustomizada("Taxa Extraordinária não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}