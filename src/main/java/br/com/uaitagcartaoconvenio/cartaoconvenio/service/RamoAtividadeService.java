package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.RamoAtividadeMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.RamoAtividade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RamoAtividadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.RamoAtividadeRepository;

@Service
public class RamoAtividadeService {

    @Autowired
    private RamoAtividadeRepository ramoAtividadeRepository;

    @Transactional
    public RamoAtividadeDTO updateRamoAtividade(Long id, RamoAtividade ramoAtividadeDetails) throws ExceptionCustomizada {
        RamoAtividade ramoAtividade = ramoAtividadeRepository.findById(id)
                .orElseThrow(() -> new ExceptionCustomizada("Ramo de Atividade não encontrado com id: " + id));

        ramoAtividade.setDescRamoAtividade(ramoAtividadeDetails.getDescRamoAtividade());
        ramoAtividade.setConveniados(ramoAtividadeDetails.getConveniados());

        RamoAtividade updatedRamoAtividade = ramoAtividadeRepository.save(ramoAtividade);
        return RamoAtividadeMapper.INSTANCE.toDto(updatedRamoAtividade);
    }
}