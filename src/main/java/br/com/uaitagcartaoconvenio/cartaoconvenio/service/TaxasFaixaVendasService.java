package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxasFaixaVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxasFaixaVendasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxasFaixaVendasService {

    private final TaxasFaixaVendasRepository repository;

    @Transactional(readOnly = true)
    public TaxasFaixaVendas findById(Long id) throws ExceptionCustomizada {
        return repository.findById(id)
                .orElseThrow(() -> new ExceptionCustomizada("Taxa não encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<TaxasFaixaVendas> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<TaxasFaixaVendas> findByDescricao(String descricao) throws ExceptionCustomizada {
        List<TaxasFaixaVendas> result = repository.findByDescricaoTaxaContainingIgnoreCase(descricao);
        if (result.isEmpty()) {
            throw new ExceptionCustomizada("Nenhuma taxa encontrada com a descrição: " + descricao);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<TaxasFaixaVendas> findByStatus(String status) throws ExceptionCustomizada {
        List<TaxasFaixaVendas> result = repository.findByStatusTaxa(status);
        if (result.isEmpty()) {
            throw new ExceptionCustomizada("Nenhuma taxa encontrada com o status: " + status);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<TaxasFaixaVendas> findAtivasOrderByFaixasAsc( ) throws ExceptionCustomizada {
    	
        List<TaxasFaixaVendas> result = repository.findAtivasOrderByFaixasAsc();
        if (result.isEmpty()) {
            throw new ExceptionCustomizada("Não existe taxa cadastrada!");
        }
        return result;
    }

    @Transactional
    public TaxasFaixaVendas save(TaxasFaixaVendas taxa) {
        return repository.save(taxa);
    }

    @Transactional
    public void delete(Long id) throws ExceptionCustomizada {
        if (!repository.existsById(id)) {
            throw new ExceptionCustomizada("Taxa não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}