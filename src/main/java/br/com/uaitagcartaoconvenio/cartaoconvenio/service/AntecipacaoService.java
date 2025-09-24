package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusAntecipacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaPg;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.excecoes.AntecipacaoException;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.AntecipacaoMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Antecipacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.AntecipacaoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Venda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.AntecipacaoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.DadosCalculoAntcipacaoCicloDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.DadosCalculoAntcipacaoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.EmailAprovacaoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ResultadoCalculoAntecipacaoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.AntecipacaoRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.AntecipacaoVendaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.CicloPagamentoVendaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ConveniadosRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.VendaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.CalculadoraJurosCompostos;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.EmailFechamentoException;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.EmailService;

@Service
public class AntecipacaoService {

    @Autowired
    private AntecipacaoRepository antecipacaoRepository;
    
    
    @Autowired
    private AntecipacaoVendaRepository antecipacaoVendaRepository;
    
    @Autowired
    private ConveniadosRepository conveniadosRepository;
    
    @Autowired
    private VendaRepository vendaRepository;
    
    @Autowired
    private CicloPagamentoVendaRepository cicloPagamentoVendaRepository;
    
    @Autowired
    private AntecipacaoMapper antecipacaoMapper;
    
    @Autowired
    private FechamentoCicloAntecipacaoService fechamentoAntecipacaoService;
    
    @Autowired
    private EmailService emailService;
    
    private static final Logger logger = LogManager.getLogger(AntecipacaoService.class);
    
    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	    
    @Transactional
//    public AntecipacaoDTO criarPreAntecipacaoVendasMesCorrente(Long idConveniados, List<Long> idsVendas, String loginUser) {
    public AntecipacaoDTO criarPreAntecipacaoVendasMesCorrente( DadosCalculoAntcipacaoDTO dto ) {
        
        validarDadosCalculoCompleto(dto);
        
        // Validar conveniada
        Conveniados conveniados = conveniadosRepository.findById(/*idConveniados*/ dto.getIdConveniados())
            .orElseThrow(() -> new AntecipacaoException("Conveniada não encontrada com ID: " + dto.getIdConveniados()));
        
        // Validar vendas
        List<Venda> vendas = vendaRepository.findAllById(/*idsVendas*/ dto.getIdsVendas() );
        if (vendas.isEmpty()) {
            throw new AntecipacaoException("Nenhuma venda válida encontrada para antecipação");
        }
        
        // Verificar se todas as vendas pertencem à conveniada
        vendas.forEach(venda -> {
            if (!venda.getConveniados().getIdConveniados().equals(/*idConveniados*/ dto.getIdConveniados() )) {
                throw new AntecipacaoException("Venda com ID " + venda.getIdVenda() + " não pertence à conveniada informada");
            }
        });
        
        // Atualizar status das vendas para PRE_ANTECIPACAO
        vendaRepository.atualizarStatusParaPreAntecipacao( /*idsVendas*/ dto.getIdsVendas(), StatusVendas.PRE_ANTECIPACAO, StatusVendaPg.PRE_ANTECIPACAO);
        
        // Calcular valor base (soma das vendas)
        BigDecimal valorBase = vendas.stream()
            .map(Venda::getValorVenda)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calcular juros
/*        
        LocalDate dataCorte          = LocalDate.now();         // Data Corte
        LocalDate dataPagamento      = dataCorte.plusDays(5);   // Pagamento em 5 dias
        LocalDate dataVencimento     = dataCorte.plusMonths(1); // Vencimento em 1 mês        
        BigDecimal taxaNominalMensal = new BigDecimal("2.00");  // 2% ao mês (configurável)
        
        ResultadoCalculoAntecipacaoDTO calculo = CalculadoraJurosCompostos.calcularJuros( taxaNominalMensal, dataCorte, dataPagamento, dataVencimento, valorBase );
*/                
        // Calcular juros
        ResultadoCalculoAntecipacaoDTO calculo = CalculadoraJurosCompostos.calcularJuros( dto.getTaxaNominalMensal(), dto.getDataCorte(), dto.getDataPagamento(), dto.getDataVencimento(), valorBase );

        // Criar e salvar antecipação
        Antecipacao antecipacao = new Antecipacao();
        antecipacao.setTaxaMes              ( calculo.getTaxaMes()       );
        antecipacao.setTaxaDia              ( calculo.getTaxaDia()       );
        antecipacao.setTaxaPeriodo          ( calculo.getTaxaPeriodo()   );
        antecipacao.setDtCorte              ( calculo.getDtCorte()       );
        antecipacao.setDtPagamento          ( calculo.getDtPagamento()   );
        antecipacao.setDtVencimento         ( calculo.getDtVencimento()  );
        antecipacao.setPeriodoDias          ( calculo.getPeriodoDias()   );
        antecipacao.setValorDesconto        ( calculo.getValorDesconto() );
        antecipacao.setValorNominal         ( calculo.getValorNominal()  );
        antecipacao.setValorBase            ( calculo.getValorBase()     );
        antecipacao.setLoginUser            ( dto.getLoginUser()         );
        antecipacao.setDescStatusAntecipacao( StatusAntecipacao.PENDENTE );
        antecipacao.setConveniados          ( conveniados                );
        
        Antecipacao antecipacaoSalva = antecipacaoRepository.save( antecipacao );
        
        // Vincular vendas à antecipação
        List<AntecipacaoVenda> antecipacaoVendas = vendas.stream()
            .map(venda -> {
                AntecipacaoVenda av = new AntecipacaoVenda();
                av.setAntecipacao(antecipacaoSalva);
                av.setVenda(venda);
                return av;
            })
            .collect(Collectors.toList());
        
        antecipacaoVendaRepository.saveAll(antecipacaoVendas);
        
        return antecipacaoMapper.toDTO(antecipacaoSalva);
    }
    
    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	    
  public void validarDadosCalculoCompleto( DadosCalculoAntcipacaoDTO dto) {
        if (dto == null) {
            throw new AntecipacaoException("DTO de cálculo de antecipação não pode ser nulo");
        }

        StringBuilder errors = new StringBuilder();
        
        if (dto.getIdConveniados() == null) {
            errors.append("ID da conveniada é obrigatório. ");
        }

        if (dto.getIdsVendas() == null || dto.getIdsVendas().isEmpty()) {
            errors.append("Lista de IDs das vendas é obrigatória. ");
        }

        if (dto.getLoginUser() == null || dto.getLoginUser().trim().isEmpty()) {
            errors.append("Login do usuário é obrigatório. ");
        }

        if (dto.getDataCorte() == null) {
            errors.append("Data de corte é obrigatória. ");
        }

        if (dto.getDataPagamento() == null) {
            errors.append("Data de pagamento é obrigatória. ");
        }

        if (dto.getDataVencimento() == null) {
            errors.append("Data de vencimento é obrigatória. ");
        }

        if (dto.getTaxaNominalMensal() == null) {
            errors.append("Taxa nominal mensal é obrigatória. ");
        } else if (dto.getTaxaNominalMensal().compareTo(BigDecimal.ZERO) <= 0) {
            errors.append("Taxa nominal mensal deve ser maior que zero. ");
        }

        // Se encontrou erros, lança a exceção com todos eles
        if (errors.length() > 0) {
            throw new AntecipacaoException(errors.toString().trim());
        }

        // Validações de lógica de negócio
        if (dto.getDataPagamento().isBefore(dto.getDataCorte())) {
            throw new AntecipacaoException("Data de pagamento não pode ser anterior à data de corte");
        }

        if (dto.getDataVencimento().isBefore(dto.getDataPagamento())) {
            throw new AntecipacaoException("Data de vencimento não pode ser anterior à data de pagamento");
        }
    }
    public static LocalDate dateToLocalDate(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant()
                  .atZone(ZoneId.systemDefault())
                  .toLocalDate();
    }

    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	    
    @Transactional
    public AntecipacaoDTO criarPreAntecipacaoFechamentoExistente( DadosCalculoAntcipacaoCicloDTO dto ) {

        validarDadosCicloCalculoCompleto(dto);
    	
        CicloPagamentoVenda ciclo = cicloPagamentoVendaRepository.findById( /*idCicloPagamentoVenda*/ dto.getIdCicloPagamentoVenda())
            .orElseThrow(() -> new AntecipacaoException("Fechamento não encontrado com ID: " + dto.getIdCicloPagamentoVenda()));
        
        if (!ciclo.getDescStatusPagamento().equals(StatusCicloPgVenda.AGUARDANDO_UPLOAD_NF)) {
            throw new AntecipacaoException("Fechamento não está no status AGUARDANDO_UPLOAD_NF");
        }
        
        dto.setDataCorte     ( dateToLocalDate( ciclo.getDtCriacao()   ) );
        dto.setDataVencimento( dateToLocalDate( ciclo.getDtPagamento() ) );

        Conveniados conveniados = ciclo.getConveniados();
         
        // Calcular valor base (valor líquido do fechamento)
        BigDecimal valorBase = ciclo.getVlrLiquidoPagamento();
/*        
        // Calcular juros (simplificado - implementar lógica real)
        LocalDate dataCorte          = LocalDate.now();         // Data de Corte.
        LocalDate dataPagamento      = dataCorte.plusDays(5);   // Exemplo: pagamento em 5 dias.
        LocalDate dataVencimento     = dataCorte.plusMonths(1); // Exemplo: vencimento em 1 mês.        
        BigDecimal taxaNominalMensal = new BigDecimal("2.00");  // 2% ao mês (configurável).
        
        ResultadoCalculoAntecipacaoDTO calculo = CalculadoraJurosCompostos.calcularJuros( taxaNominalMensal, dataCorte, dataPagamento, dataVencimento, valorBase );
*/ 
        // Calcular juros 
        ResultadoCalculoAntecipacaoDTO calculo = CalculadoraJurosCompostos.calcularJuros( dto.getTaxaNominalMensal(), dto.getDataCorte(), dto.getDataPagamento(), dto.getDataVencimento(), valorBase );
               
        // Criar antecipação (pendente)
        Antecipacao antecipacao = new Antecipacao();
        antecipacao.setTaxaMes              ( calculo.getTaxaMes()       );
        antecipacao.setTaxaDia              ( calculo.getTaxaDia()       );
        antecipacao.setTaxaPeriodo          ( calculo.getTaxaPeriodo()   );
        antecipacao.setDtCorte              ( calculo.getDtCorte()       );
        antecipacao.setDtPagamento          ( calculo.getDtPagamento()   );
        antecipacao.setDtVencimento         ( calculo.getDtVencimento()  );
        antecipacao.setPeriodoDias          ( calculo.getPeriodoDias()   );
        antecipacao.setValorDesconto        ( calculo.getValorDesconto() );
        antecipacao.setValorNominal         ( calculo.getValorNominal()  );
        antecipacao.setValorBase            ( calculo.getValorBase()     );
        antecipacao.setLoginUser            ( dto.getLoginUser()         );
        antecipacao.setDescStatusAntecipacao( StatusAntecipacao.PENDENTE );
        antecipacao.setConveniados          ( conveniados                );
        antecipacao.setCicloPagamentoVenda  ( ciclo                      );
        
        antecipacao = antecipacaoRepository.save(antecipacao);
        
        return antecipacaoMapper.toDTO(antecipacao);
    }
  
        /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	    
  public void validarDadosCicloCalculoCompleto( DadosCalculoAntcipacaoCicloDTO dto) {
        if (dto == null) {
            throw new AntecipacaoException("DTO de cálculo de antecipação não pode ser nulo");
        }

        StringBuilder errors = new StringBuilder();
        
        if (dto.getIdCicloPagamentoVenda() == null) {
            errors.append("ID do Ciclo de Pagamento Venda é obrigatório. ");
        }
        if (dto.getLoginUser() == null || dto.getLoginUser().trim().isEmpty()) {
            errors.append("Login do usuário é obrigatório. ");
        }

 //       if (dto.getDataCorte() == null) {
 //           errors.append("Data de corte é obrigatória. ");
 //       }

        if (dto.getDataPagamento() == null) {
            errors.append("Data de pagamento é obrigatória. ");
        }

        // if (dto.getDataVencimento() == null) {
        //     errors.append("Data de vencimento é obrigatória. ");
        // }

        if (dto.getTaxaNominalMensal() == null) {
            errors.append("Taxa nominal mensal é obrigatória. ");
        } else if (dto.getTaxaNominalMensal().compareTo(BigDecimal.ZERO) <= 0) {
            errors.append("Taxa nominal mensal deve ser maior que zero. ");
        }

        // Se encontrou erros, lança a exceção com todos eles
        if (errors.length() > 0) {
            throw new AntecipacaoException(errors.toString().trim());
        }

        // Validações de lógica de negócio
        // if (dto.getDataPagamento().isBefore(dto.getDataCorte())) {
        //     throw new AntecipacaoException("Data de pagamento não pode ser anterior à data de corte");
        // }

        // if (dto.getDataVencimento().isBefore(dto.getDataPagamento())) {
        //     throw new AntecipacaoException("Data de vencimento não pode ser anterior à data de pagamento");
        // }
    }

    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	    
    @Transactional
    public void enviarEmailAprovacao( EmailAprovacaoDTO dto ) {
    	
        Antecipacao antecipacao = antecipacaoRepository.findById(dto.getIdAntecipacao()).orElseThrow(() -> new AntecipacaoException("Antecipação não encontrada com ID: " + dto.getIdAntecipacao() ) );
        
        if (!antecipacao.getDescStatusAntecipacao().equals(StatusAntecipacao.PENDENTE)) {
            throw new AntecipacaoException("Antecipação não está no status PENDENTE");
        }
         // Envie o e-mail
        try {
            emailService.enviarEmailAprovacaoAntecipacao(antecipacao, dto.getDestinatarioPrincipal(), dto.getDestinatariosCopia() );
            
            logger.info("E-mail de aprovação enviado com sucesso");
        } catch (EmailFechamentoException e) {
            logger.error("Falha ao enviar e-mail de aprovação", e);
            // Trate o erro conforme necessário
        }
    }
      
    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	    
    @Transactional
    public AntecipacaoDTO registrarComprovante(Long idAntecipacao, MultipartFile arquivoComprovante) {
        Antecipacao antecipacao = antecipacaoRepository.findById(idAntecipacao)
            .orElseThrow(() -> new AntecipacaoException("Antecipação não encontrada com ID: " + idAntecipacao));
        
        if (!antecipacao.getDescStatusAntecipacao().equals(StatusAntecipacao.APROVADO)) {
            throw new AntecipacaoException("Antecipação não está no status APROVADO");
        }
        
        try {
            // Salvar arquivo de comprovante
            antecipacao.setNomeArquivoComprovante(arquivoComprovante.getOriginalFilename());
            antecipacao.setConteudoBase64Comprovante(Base64.getEncoder().encodeToString(arquivoComprovante.getBytes()));
            antecipacao.setTamanhoBytesComprovante(arquivoComprovante.getSize());
            
            // Atualizar status
            antecipacao.setDescStatusAntecipacao(StatusAntecipacao.FINALIZADA);
            
            antecipacao = antecipacaoRepository.save(antecipacao);
            
            return antecipacaoMapper.toDTO(antecipacao);
        } catch (IOException e) {
            throw new AntecipacaoException("Erro ao processar arquivo de comprovante", e);
        }
    }

    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	    
    @Transactional
    public void processarAntecipacao(Antecipacao antecipacao) {
    	
        if ( antecipacao.getCicloPagamentoVenda() != null ) {
            // Processa antecipação de fechamento existente
            fechamentoAntecipacaoService.marcarFechamentoComoAntecipado( antecipacao.getCicloPagamentoVenda().getIdCicloPagamentoVenda() );
       	
        } else {
            // Processa antecipação de vendas do mês corrente            
            fechamentoAntecipacaoService.processarFechamentoAntecipacaoMesCorrente(antecipacao);
        }
        
 
    }
 
    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	    
    public List<AntecipacaoDTO> listarPorConveniada(Long idConveniados) {
        return antecipacaoRepository.findByConveniadosIdConveniados(idConveniados).stream()
            .map(antecipacaoMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	    
   public List<AntecipacaoDTO> listarPorConveniadaEStatus(Long idConveniados, StatusAntecipacao status) {
        return antecipacaoRepository.findByConveniadosIdConveniadosAndDescStatusAntecipacao(idConveniados, status).stream()
            .map(antecipacaoMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	    
   public AntecipacaoDTO buscarPorId(Long idAntecipacao) {
        return antecipacaoRepository.findById(idAntecipacao)
            .map(antecipacaoMapper::toDTO)
            .orElseThrow(() -> new AntecipacaoException("Antecipação não encontrada com ID: " + idAntecipacao));
    }
    
    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	    
    @Transactional
    public void cancelarAntecipacao(Long idAntecipacao, String motivo) {
        Antecipacao antecipacao = antecipacaoRepository.findById(idAntecipacao)
            .orElseThrow(() -> new AntecipacaoException("Antecipação não encontrada com ID: " + idAntecipacao));
        
        if (!antecipacao.getDescStatusAntecipacao().equals(StatusAntecipacao.PENDENTE)) {
            throw new AntecipacaoException("Só é possível cancelar antecipações no status PENDENTE");
        }
        
        antecipacao.setDescStatusAntecipacao(StatusAntecipacao.CANCELADO);
        antecipacao.setObservacao(motivo);
        antecipacaoRepository.save(antecipacao);
    }
    
    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	    
    public Antecipacao getAntecipacaoById(Long id) {
        return antecipacaoRepository.findById(id)
            .orElseThrow(() -> new AntecipacaoException("Antecipação não encontrada com ID: " + id));
    }
    
    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	    
    public List<Antecipacao> listarPorStatus(StatusAntecipacao status) {
        return antecipacaoRepository.findByDescStatusAntecipacao(status);
    }
    
    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	    
    public List<AntecipacaoDTO> getAntecipacoesByStatus(StatusAntecipacao status) {
        return antecipacaoRepository.findByDescStatusAntecipacao(status).stream()
            .map(antecipacaoMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    /******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	    
    @Transactional
    public Antecipacao atualizarStatusAntecipacao(Long idAntecipacao, StatusAntecipacao novoStatus) {
        Antecipacao antecipacao = antecipacaoRepository.findById(idAntecipacao)
            .orElseThrow(() -> new AntecipacaoException("Antecipação não encontrada com ID: " + idAntecipacao));
        
        // Valida transições de status
        if (antecipacao.getDescStatusAntecipacao() == StatusAntecipacao.FINALIZADA) {
            throw new AntecipacaoException("Não é possível alterar o status de uma antecipação FINALIZADA");
        }
        
        if (novoStatus == StatusAntecipacao.APROVADO && 
            antecipacao.getDescStatusAntecipacao() != StatusAntecipacao.PENDENTE) {
            throw new AntecipacaoException("Só é possível aprovar antecipações no status PENDENTE");
        }
        
        antecipacao.setDescStatusAntecipacao(novoStatus);
        return antecipacaoRepository.save(antecipacao);
    }
    
    
}
