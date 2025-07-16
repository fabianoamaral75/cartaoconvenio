package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusContrato;
import br.com.uaitagcartaoconvenio.cartaoconvenio.excecoes.BusinessException;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.VigenciaContratoMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.VigenciaContratoEntidadeDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ContratoEntidadeRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.VigenciaContratoEntidadeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VigenciaContratoEntidadeService {

    private final VigenciaContratoEntidadeRepository repository;
    private final ContratoEntidadeRepository contratoEntidadeRepository;
    private final VigenciaContratoMapper mapper;

    // CRUD básico
    public VigenciaContratoEntidadeDTO create(VigenciaContratoEntidadeDTO dto) {
        VigenciaContratoEntidade entity = mapper.toEntity(dto);
        validateVigencia(entity);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    public VigenciaContratoEntidadeDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new BusinessException("Vigência não encontrada com ID: " + id));
    }

    public List<VigenciaContratoEntidadeDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public VigenciaContratoEntidadeDTO update(Long id, VigenciaContratoEntidadeDTO dto) {
        VigenciaContratoEntidade existing = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Vigência não encontrada com ID: " + id));
        
        mapper.updateFromDTO(dto, existing);
        validateVigencia(existing);
        existing = repository.save(existing);
        return mapper.toDTO(existing);
    }

    public void delete(Long id) {
        VigenciaContratoEntidade entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Vigência não encontrada com ID: " + id));
        repository.delete(entity);
    }

    // Endpoints específicos
    public List<VigenciaContratoEntidadeDTO> findByEntidadeId(Long idEntidade) {
        return repository.findByEntidadeId(idEntidade).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<VigenciaContratoEntidadeDTO> findByEntidadeIdAndStatus(Long idEntidade, StatusContrato status) {
        return repository.findByEntidadeIdAndStatus(idEntidade, status).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public VigenciaContratoEntidadeDTO findVigenciaAtualByEntidadeId(Long idEntidade) {
        LocalDate hoje = LocalDate.now();
        List<VigenciaContratoEntidade> vigencias = repository.findVigenciaAtualByEntidadeId(idEntidade, hoje);
        
        if (vigencias.isEmpty()) {
            // Se não encontrou por data, busca a última vigência
            vigencias = repository.findLastVigenciaByEntidadeId(idEntidade);
        }
        
        return vigencias.stream()
                .findFirst()
                .map(mapper::toDTO)
                .orElseThrow(() -> new BusinessException("Nenhuma vigência encontrada para a entidade com ID: " + idEntidade));
    }

    @Transactional
    public VigenciaContratoEntidadeDTO renovarVigencia(Long idContratoEntidade, VigenciaContratoEntidadeDTO novaVigenciaDTO, String username) {
        // Busca a vigência atual (VIGENTE ou a última)
        VigenciaContratoEntidade vigenciaAtual = repository
                .findFirstByContratoEntidadeIdContratoEntidadeAndDescStatusContratoOrderByIdVigenciaContratoEntidadeDesc(
                        idContratoEntidade, StatusContrato.VIGENTE)
                .orElseGet(() -> repository.findLastVigenciaByEntidadeId(idContratoEntidade)
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new BusinessException("Nenhuma vigência encontrada para o contrato com ID: " + idContratoEntidade)));

        // Valida se a nova vigência é posterior à atual
        if (novaVigenciaDTO.getDtInicio().isBefore(vigenciaAtual.getDtFinal())) {
            throw new BusinessException("A nova vigência deve começar após o término da vigência atual");
        }

        // Atualiza a vigência atual para NAO_VIGENTE
        vigenciaAtual.setDescStatusContrato(StatusContrato.NAO_VIGENTE);
        vigenciaAtual.setDtDesativacao(LocalDateTime.now());
        vigenciaAtual.setUserDesativacao(username);
        repository.save(vigenciaAtual);

        // Cria a nova vigência
        VigenciaContratoEntidade novaVigencia = mapper.toEntity(novaVigenciaDTO);
        novaVigencia.setDescStatusContrato(StatusContrato.VIGENTE);
        
        // Associa ao contrato
        ContratoEntidade contrato = contratoEntidadeRepository.findById(idContratoEntidade)
                .orElseThrow(() -> new BusinessException("Contrato não encontrado com ID: " + idContratoEntidade));
        novaVigencia.setContratoEntidade(contrato);
        
        novaVigencia = repository.save(novaVigencia);
        return mapper.toDTO(novaVigencia);
    }

    @Transactional
    public VigenciaContratoEntidadeDTO updateStatus(Long idVigencia, StatusContrato novoStatus, String username) {
        // Verifica se o status é permitido
        if (!isStatusPermitido(novoStatus)) {
            throw new BusinessException("Status " + novoStatus + " não permitido para atualização");
        }

        // Busca a vigência
        VigenciaContratoEntidade vigencia = repository.findById(idVigencia)
                .orElseThrow(() -> new BusinessException("Vigência não encontrada com ID: " + idVigencia));

        // Verifica se é a última vigência
        List<VigenciaContratoEntidade> ultimas = repository.findLastVigenciaByEntidadeId(
                vigencia.getContratoEntidade().getEntidade().getIdEntidade());
        
        if (ultimas.isEmpty() || !Objects.equals(ultimas.get(0).getIdVigenciaContratoEntidade(), idVigencia)) {
            throw new BusinessException("Apenas a última vigência pode ter seu status atualizado");
        }

        // Atualiza o status
        vigencia.setDescStatusContrato(novoStatus);
        
        // Se for CANCELADO ou ARQUIVADO, registra quem desativou
        if (novoStatus == StatusContrato.CANCELADO || novoStatus == StatusContrato.ARQUIVADO) {
            vigencia.setDtDesativacao(LocalDateTime.now());
            vigencia.setUserDesativacao(username);
        }
        
        vigencia = repository.save(vigencia);
        return mapper.toDTO(vigencia);
    }

    private boolean isStatusPermitido(StatusContrato status) {
        return status == StatusContrato.PENDENTE ||
               status == StatusContrato.VIGENTE ||
               status == StatusContrato.CANCELADO ||
               status == StatusContrato.ARQUIVADO ||
               status == StatusContrato.PRELIMINAR;
    }

    private void validateVigencia(VigenciaContratoEntidade vigencia) {
        if (vigencia.getDtInicio().isAfter(vigencia.getDtFinal())) {
            throw new BusinessException("Data de início não pode ser posterior à data final");
        }
        
        // Outras validações podem ser adicionadas aqui
    }
}