package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Nicho;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.NichoRepository;

@Service
public class NichoService {

    @Autowired
    private NichoRepository nichoRepository;

    @Transactional
    public Nicho atualizarNichoCompleto(Nicho nichoAtualizado) {
        Nicho nichoExistente = nichoRepository.findById(nichoAtualizado.getIdNicho())
            .orElseThrow(() -> new IllegalArgumentException("Nicho não encontrado"));
        
        nichoExistente.setDescNicho(nichoAtualizado.getDescNicho());
        nichoExistente.setConveniados(nichoAtualizado.getConveniados());
        
        return nichoRepository.save(nichoExistente);
    }

    @Transactional
    public void atualizarDescricao(Long idNicho, String novaDescricao) {
        if (novaDescricao == null || novaDescricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição do nicho não pode ser vazia");
        }
        nichoRepository.atualizarDescricaoNicho(idNicho, novaDescricao);
    }

    @Transactional
    public void atualizarConveniado(Long idNicho, Long idConveniados) {
        nichoRepository.atualizarConveniadoNicho(idNicho, idConveniados);
    }
}

