package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusContrato;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.VigenciaContratoConveniadaRepository;

@Service
public class VigenciaContratoConveniadaService {
	
	@Autowired
	private VigenciaContratoConveniadaRepository repository;
	
	
	public VigenciaContratoConveniada save( VigenciaContratoConveniada savedEntity ) {
		savedEntity = repository.save(savedEntity);
		return savedEntity;
	}

	
	public VigenciaContratoConveniada findById( Long id ) {
				
		return repository.findById(id).orElse(null);

	}

    public List<VigenciaContratoConveniada> findByConveniadaId(Long idConveniada) {
        return repository.findByConveniadaId(idConveniada);
    }

    public List<VigenciaContratoConveniada> findByConveniadaIdAndStatus(Long idConveniada, StatusContrato status) {
        return repository.findByConveniadaIdAndStatus(idConveniada, status);
    }

    public VigenciaContratoConveniada findCurrentVigencia(Long idConveniada) {
        LocalDate currentDate = LocalDate.now();
        List<VigenciaContratoConveniada> currentVigencias = repository.findCurrentVigencia(idConveniada, currentDate);
        
        if (!currentVigencias.isEmpty()) {
            return currentVigencias.get(0);
        }
        
        // If no current vigencia found, return the latest one
        return repository.findLatestVigencia(idConveniada);
    }

    @Transactional
    public VigenciaContratoConveniada renovarVigencia(Long idConveniada, VigenciaContratoConveniada novaVigencia) {
        VigenciaContratoConveniada vigenciaAtual = findCurrentVigencia(idConveniada);
        
        if (vigenciaAtual != null) {
            vigenciaAtual.setDescStatusContrato(StatusContrato.NAO_VIGENTE);
            //vigenciaAtual.setDataFinal(LocalDate.now());
            repository.save(vigenciaAtual);
        }
        
        novaVigencia.setDescStatusContrato(StatusContrato.VIGENTE);
        return repository.save(novaVigencia);
    }

    @Transactional
    public VigenciaContratoConveniada updateStatus(Long idVigencia, StatusContrato newStatus) {
        VigenciaContratoConveniada vigencia = repository.findById(idVigencia)
            .orElseThrow(() -> new RuntimeException("Vigência não encontrada"));
        
        if (isValidStatusTransition(vigencia.getDescStatusContrato(), newStatus)) {
            vigencia.setDescStatusContrato(newStatus);
            return repository.save(vigencia);
        } else {
            throw new RuntimeException("Transição de status inválida");
        }
    }

    private boolean isValidStatusTransition(StatusContrato currentStatus, StatusContrato newStatus) {
        // Only allow specific status transitions
        return newStatus == StatusContrato.PENDENTE ||
               newStatus == StatusContrato.VIGENTE ||
               newStatus == StatusContrato.CANCELADO ||
               newStatus == StatusContrato.ARQUIVADO ||
               newStatus == StatusContrato.PRELIMINAR;
    }

}
