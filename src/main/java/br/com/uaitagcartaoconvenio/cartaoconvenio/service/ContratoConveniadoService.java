package br.com.uaitagcartaoconvenio.cartaoconvenio.service;


import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

// import br.com.uaitagcartaoconvenio.cartaoconvenio.dto.ContratoConveniadoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContratoConveniado;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ContratoConveniadoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ContratoConveniadoRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ConveniadosRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ContratoConveniadoService {

    @Autowired
    private ContratoConveniadoRepository contratoConveniadoRepository;

    @Autowired
    private ConveniadosRepository conveniadosRepository;

    public List<ContratoConveniado> findAllByConveniadoId(Long idConveniado) {
        return contratoConveniadoRepository.findByConveniadoId(idConveniado);
    }

    public ContratoConveniado findById(Long idConveniado, Long idContrato) {
        return contratoConveniadoRepository.findByConveniadoIdAndContratoId(idConveniado, idContrato)
                .orElseThrow(() -> new EntityNotFoundException("Contrato não encontrado para o conveniado informado"));
    }

    @Transactional
    public ContratoConveniado create(Long idConveniado, ContratoConveniadoDTO dto) {
    	
        Conveniados conveniado = conveniadosRepository.findById(idConveniado)
                .orElseThrow(() -> new EntityNotFoundException("Conveniado não encontrado"));

        ContratoConveniado contrato = new ContratoConveniado();
        contrato.setConveniados(conveniado);
        contrato.setArqContrato(dto.getArqContrato());
        contrato.setConteudoBase64(dto.getConteudoBase64());
        contrato.setTamanhoBytes(calculateFileSize(dto.getConteudoBase64()));
        contrato.setObservacao(dto.getObservacao());
        contrato.setDataUpload(new Date());

        // Adiciona vigencias se existirem
        if (dto.getVigencias() != null) {
            dto.getVigencias().forEach(vigenciaDTO -> {
                VigenciaContratoConveniada vigencia = new VigenciaContratoConveniada();
                vigencia.setDataInicio(vigenciaDTO.getDataInicio());
                vigencia.setDataFinal(vigenciaDTO.getDataFinal());
                vigencia.setRenovacao(vigenciaDTO.getRenovacao());
                vigencia.setObservacao(vigenciaDTO.getObservacao());
                contrato.adicionarVigencia(vigencia);
            });
        }

        return contratoConveniadoRepository.save(contrato);
    }

    @Transactional
    public ContratoConveniado update(Long idConveniado, Long idContrato, ContratoConveniadoDTO dto) {
        ContratoConveniado contrato = findById(idConveniado, idContrato);

        if (StringUtils.hasText(dto.getArqContrato())) {
            contrato.setArqContrato(dto.getArqContrato());
        }
        
        if (StringUtils.hasText(dto.getConteudoBase64())) {
            contrato.setConteudoBase64(dto.getConteudoBase64());
            contrato.setTamanhoBytes(calculateFileSize(dto.getConteudoBase64()));
            contrato.setDataUpload(new Date());
        }
        
        if (StringUtils.hasText(dto.getObservacao())) {
            contrato.setObservacao(dto.getObservacao());
        }

        // Atualiza vigencias - implementação simplificada
        // Em uma implementação real, seria necessário tratar atualizações mais complexas
        if (dto.getVigencias() != null) {
            contrato.getVigencias().clear();
            dto.getVigencias().forEach(vigenciaDTO -> {
                VigenciaContratoConveniada vigencia = new VigenciaContratoConveniada();
                vigencia.setDataInicio(vigenciaDTO.getDataInicio());
                vigencia.setDataFinal(vigenciaDTO.getDataFinal());
                vigencia.setRenovacao(vigenciaDTO.getRenovacao());
                vigencia.setObservacao(vigenciaDTO.getObservacao());
                contrato.adicionarVigencia(vigencia);
            });
        }

        return contratoConveniadoRepository.save(contrato);
    }

    @Transactional
    public void delete(Long idConveniado, Long idContrato) {
        ContratoConveniado contrato = findById(idConveniado, idContrato);
        contratoConveniadoRepository.delete(contrato);
    }

    private Long calculateFileSize(String base64) {
        if (base64 == null) return 0L;
        // Remove o prefixo se existir (data:application/pdf;base64,)
        String encoded = base64.split(",").length > 1 ? base64.split(",")[1] : base64;
        byte[] decodedBytes = Base64.getDecoder().decode(encoded);
        return (long) decodedBytes.length;
    }
}
