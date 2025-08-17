package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusAntecipacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.AuditoriaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Antecipacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.AuditoriaAntecipacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.AuditoriaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.AuditoriaAntecipacaoRepository;

@Service
public class AuditoriaService {

	@Autowired
    private AuditoriaAntecipacaoRepository auditoriaAntecipacaoRepository;
	
	@Autowired
    private AuditoriaMapper mapper;
    
	Logger logger = LoggerFactory.getLogger(this.getClass());

    public AuditoriaService(AuditoriaAntecipacaoRepository repository, AuditoriaMapper mapper) {
        this.auditoriaAntecipacaoRepository = repository;
        this.mapper = mapper;
    }

    public List<AuditoriaDTO> findByAntecipacaoId(Long antecipacaoId) {
        return auditoriaAntecipacaoRepository.findByAntecipacaoId(antecipacaoId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<AuditoriaDTO> findByPeriodo(String dataInicioStr, String dataFimStr) throws ExceptionCustomizada {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataInicio        = LocalDate.parse(dataInicioStr, formatter);
            LocalDate dataFim           = LocalDate.parse(dataFimStr, formatter);
            
            LocalDateTime inicio = dataInicio.atStartOfDay();
            LocalDateTime fim = dataFim.atTime(23, 59, 59);
            
            List<AuditoriaAntecipacao> auditorias = auditoriaAntecipacaoRepository.findByPeriodo(inicio, fim);
            
            return auditorias.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
                
        } catch (DateTimeParseException e) {
            throw new ExceptionCustomizada("Formato de data inválido. Use DD/MM/AAAA");
        }
    }
    
    public void registrarAuditoria(
            Antecipacao       antecipacao   ,
            StatusAntecipacao statusAnterior,
            StatusAntecipacao statusTentado ,
            String            motivo        ,
            String            token         ,
            String            ipOrigem      ,
            String            userAgent     ,
            String            usuario       ) {
                        
        // Tratamento dos campos conforme limites
        token              = truncarCampo( token                        , 800 );
        ipOrigem           = truncarCampo( ipOrigem                     ,  45 );
        usuario            = truncarCampo( usuario                      , 100 );
        String dispositivo = truncarCampo( extrairDispositivo(userAgent), 800 );
        userAgent          = truncarCampo( userAgent                    , 800 );
        
        // Garante que o motivo não seja nulo
        motivo = motivo != null ? motivo : "Nenhum motivo informado";
       
        AuditoriaAntecipacao auditoria = new AuditoriaAntecipacao();
        auditoria.setAntecipacao   ( antecipacao    );
        auditoria.setStatusAnterior( statusAnterior );
        auditoria.setStatusNovo    ( statusTentado  );
        auditoria.setMotivo        ( motivo         ); // TEXT não precisa de truncagem
        auditoria.setIpOrigem      ( ipOrigem       );
        auditoria.setUsuario       ( usuario        );
        auditoria.setTokenUtilizado( token          );
        auditoria.setUserAgent     ( userAgent      );
        auditoria.setDispositivo   ( dispositivo    );
        
        auditoriaAntecipacaoRepository.save(auditoria);
                
    }    
    
    private String truncarCampo(String valor, int tamanhoMaximo) {
        if (valor == null) {
            return null;
        }
        if (valor.length() > tamanhoMaximo) {
            return valor.substring(0, tamanhoMaximo);
        }
        return valor;
    }

    private String extrairDispositivo(String userAgent) {
        // Lógica simplificada para extrair dispositivo do User-Agent
        if (userAgent == null) return "Desconhecido";
        if (userAgent.contains("Mobile" )) return "Mobile";
        if (userAgent.contains("Windows")) return "Windows";
        if (userAgent.contains("Mac"    )) return "Mac";
        if (userAgent.contains("Linux"  )) return "Linux";
        return "Outro";
    }

    public Page<AuditoriaDTO> findByAntecipacaoIdPaginado(Long idAntecipacao, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataAuditoria").descending());
        Page<AuditoriaAntecipacao> pageResult = auditoriaAntecipacaoRepository.findByAntecipacaoIdPaginado(idAntecipacao, pageable);
        
        return pageResult.map(auditoria -> {
            AuditoriaDTO dto = mapper.toDto(auditoria);
            return dto;
        });
    }

    public boolean isIpSuspeito(String ip) {
        LocalDateTime dataLimite = LocalDateTime.now().minusHours(1);
        long tentativas = auditoriaAntecipacaoRepository.countByIpAndDataAfter(ip, dataLimite);
        return tentativas > 5; // Limite de 5 tentativas por hora
    }
    
    public List<AuditoriaAntecipacao> getAuditoriaPorAntecipacao(Long idAntecipacao) throws ExceptionCustomizada {
        List<AuditoriaAntecipacao> auditorias = auditoriaAntecipacaoRepository.findByAntecipacaoId(idAntecipacao);
        
        if (auditorias == null || auditorias.isEmpty()) {
            throw new ExceptionCustomizada("Nenhum registro de auditoria encontrado");
        }
        
        return auditorias;
    }
}