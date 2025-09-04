// EmprestimoService.java
package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusEmprestimo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusPrestacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Emprestimo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Funcionario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.LimiteCredito;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PrestacaoEmprestimo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.EmprestimoRequestDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PagamentoPrestacaoRequestDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.EmprestimoRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.FuncionarioRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.PrestacaoEmprestimoRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.VendaService.MensagensValidacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;

/**
 * Serviço responsável por gerenciar operações relacionadas a empréstimos
 * Inclui solicitação, aprovação, reprovação e pagamento de prestações
 */
@Service
public class EmprestimoService {

    // Repositório para operações com empréstimos
    @Autowired
    private EmprestimoRepository emprestimoRepository;
    
    // Repositório para operações com prestações
    @Autowired
    private PrestacaoEmprestimoRepository prestacaoRepository;
    
    // Repositório para operações com funcionários
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    
    // Serviço para gerenciamento de limite de crédito
    @Autowired
    private LimitecreditoService limiteCreditoService;
    
    @Autowired
    private LimitecreditoService limitecreditoService;
    
	private static final Logger logger = LogManager.getLogger(FechamentoCicloService.class);

    /**
     * Calcula a taxa de juros com base no valor solicitado e quantidade de parcelas
     * @param request do tipo EmprestimoRequestDTO 
     * @return Taxa de juros calculada
     */
//    public BigDecimal calcularTaxaJuros(BigDecimal valorSolicitado, Integer quantidadeParcelas) {
    public BigDecimal calcularTaxaJuros( EmprestimoRequestDTO request ) {
    	
        // Taxa base de juros de 1.5% ao mês
        BigDecimal taxaBase = request.getTaxaBase().divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
        
        // Acréscimo de 0.5% para parcelas acima de 12 meses
        if (request.getQuantidadeParcelas() > 12) { 
        	if (request.getAcrescimoParcelasAcima12Meses().compareTo(BigDecimal.ZERO) > 0) {
                taxaBase = taxaBase.add( request.getAcrescimoParcelasAcima12Meses().divide( new BigDecimal(100), 4, RoundingMode.HALF_UP ) ); 
        	}
        }
 
        // Desconto de 0.2% para valores acima de R$ 10.000,00
        if (request.getDescontoValoresAlto().compareTo(BigDecimal.ZERO) > 0) {
        	if (request.getAcrescimoParcelasAcima12Meses().compareTo(BigDecimal.ZERO) > 0) {
                taxaBase = taxaBase.subtract( request.getTaxaDescontoValoresAlto().divide(new BigDecimal(100), 4, RoundingMode.HALF_UP) ); 
        	}
        }
        
        return taxaBase;
    }
    
    /**
     * Calcula o valor da parcela usando a fórmula de prestações fixas
     * Fórmula: PMT = PV * [i * (1 + i)^n] / [(1 + i)^n - 1]
     * @param valorSolicitado Valor do empréstimo
     * @param taxaJuros Taxa de juros mensal
     * @param quantidadeParcelas Número de parcelas
     * @return Valor da parcela calculado
     */
    public BigDecimal calcularValorParcela(BigDecimal valorSolicitado, BigDecimal taxaJuros, Integer quantidadeParcelas) {
        // Taxa de juros mensal
        BigDecimal i = taxaJuros;
        
        // (1 + i)^n
        BigDecimal base = BigDecimal.ONE.add(i);
        BigDecimal potencia = base.pow(quantidadeParcelas);
        
        // PMT = PV * [i * (1 + i)^n] / [(1 + i)^n - 1]
        BigDecimal numerador = i.multiply(potencia);
        BigDecimal denominador = potencia.subtract(BigDecimal.ONE);
        
        // Retorna o valor da parcela com arredondamento para 2 casas decimais
        return valorSolicitado.multiply(numerador).divide(denominador, 2, RoundingMode.HALF_UP);
    }
    
    /**
     * Processa a solicitação de um novo empréstimo
     * @param request DTO com os dados da solicitação
     * @return Empréstimo criado
     */
    @Transactional
    public Emprestimo solicitarEmprestimo( EmprestimoRequestDTO request ) {
        // Verificar se funcionário existe
        Funcionario funcionario = funcionarioRepository.findById(request.getIdFuncionario())
                .orElseThrow(() -> new ExceptionCustomizada("Funcionário não encontrado"));
        
        // Verificar se já existe empréstimo ativo
        if (emprestimoRepository.existsEmprestimoAtivo(request.getIdFuncionario())) {
            throw new ExceptionCustomizada("Funcionário já possui um empréstimo ativo");
        }
        
        // Verificar limite de crédito
        if (funcionario.getLimiteCredito() != null ) {
            BigDecimal valorUtilizado = funcionario.getLimiteCredito().getValorUtilizado();
            BigDecimal limite         = funcionario.getLimiteCredito().getLimite();
            
            // Calcular saldo disponível
            BigDecimal saldoDisponivel = limite.subtract(valorUtilizado);
            
            // Verificar se há saldo suficiente
            if (saldoDisponivel.compareTo(request.getValorSolicitado()) < 0) {
            	throw new ExceptionCustomizada(String.format("Valor solicitado excede o limite de crédito do funcionário. Disponível: R$ %.2f, Solicitado: R$ %.2f", 
                        saldoDisponivel, request.getValorSolicitado()));
            }
            
            // Se chegou aqui, o saldo é suficiente
            // System.out.println("Saldo suficiente! Disponível: " + saldoDisponivel);
        }
        
        // Calcular taxa de juros
//        BigDecimal taxaJuros = calcularTaxaJuros(request.getValorSolicitado(), request.getQuantidadeParcelas());
        BigDecimal taxaJuros = calcularTaxaJuros( request );
        
        // Calcular valor da parcela
        BigDecimal valorParcela = calcularValorParcela(request.getValorSolicitado(), taxaJuros, request.getQuantidadeParcelas());
        
        // Calcular valor total do empréstimo
        BigDecimal valorTotal = valorParcela.multiply(new BigDecimal(request.getQuantidadeParcelas()));
        
        // Criar empréstimo
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setFuncionario       ( funcionario                     );
        emprestimo.setValorSolicitado   ( request.getValorSolicitado()    );
        emprestimo.setTaxaJuros         ( taxaJuros                       );
        emprestimo.setQuantidadeParcelas( request.getQuantidadeParcelas() );
        emprestimo.setValorTotal        ( valorTotal                      );
        emprestimo.setValorParcela      ( valorParcela                    );
        emprestimo.setObservacao        ( request.getObservacao()         );
        emprestimo.setStatus            ( StatusEmprestimo.SOLICITADO     );
        
        // Salvar empréstimo no banco de dados
        Emprestimo emprestimoSalvo = emprestimoRepository.save(emprestimo);
        
        // Gerar as prestações do empréstimo
        gerarPrestacoes( emprestimoSalvo );
        
        return emprestimoSalvo;
    }
    
    /**
     * Gera as prestações do empréstimo com base nos parâmetros definidos
     * @param emprestimo Empréstimo para o qual serão geradas as prestações
     */
    private void gerarPrestacoes(Emprestimo emprestimo) {
        List<PrestacaoEmprestimo> prestacoes = new ArrayList<>();
        BigDecimal saldoDevedor = emprestimo.getValorSolicitado();
        BigDecimal valorParcela = emprestimo.getValorParcela();
        BigDecimal taxaJuros    = emprestimo.getTaxaJuros();
        int totalParcelas       = emprestimo.getQuantidadeParcelas();
        
        // Gerar uma prestação para cada parcela
        for (int i = 1; i <= totalParcelas; i++) {
            PrestacaoEmprestimo prestacao = new PrestacaoEmprestimo();
            prestacao.setEmprestimo   ( emprestimo );
            prestacao.setNumeroParcela( i          );
            
            // Calcular juros do período
            BigDecimal valorJuros = saldoDevedor.multiply( taxaJuros ).setScale( 2, RoundingMode.HALF_UP );
            prestacao.setValorJuros( valorJuros );
            
            // Calcular amortização
            BigDecimal valorAmortizacao;
            if (i == totalParcelas) {
                // Última parcela: amortização deve zerar o saldo devedor
                valorAmortizacao = saldoDevedor;
                // Recalcular o valor da parcela para garantir que seja igual à amortização + juros
                BigDecimal ultimaParcela = valorAmortizacao.add( valorJuros ).setScale( 2, RoundingMode.HALF_UP );
                prestacao.setValorParcela( ultimaParcela );
            } else {
                // Parcelas normais
                valorAmortizacao = valorParcela.subtract( valorJuros ).setScale( 2, RoundingMode.HALF_UP );
                prestacao.setValorParcela(valorParcela);
            }
            
            prestacao.setValorAmortizacao( valorAmortizacao );
            
            // Atualizar saldo devedor
            saldoDevedor = saldoDevedor.subtract( valorAmortizacao ).setScale( 2, RoundingMode.HALF_UP );
            
            // Garantir que o saldo não fique negativo
            if (saldoDevedor.compareTo(BigDecimal.ZERO) < 0) {
                saldoDevedor = BigDecimal.ZERO;
            }
            
            prestacao.setSaldoDevedor(saldoDevedor);
            
            // Definir data de vencimento (mes atual + número da parcela)
            Calendar calendar = Calendar.getInstance();
            calendar.add             ( Calendar.MONTH, i  );
            prestacao.setDtVencimento( calendar.getTime() );
            
            // Adicionar prestação à lista
            prestacoes.add( prestacao );
        }
        
        // Salvar todas as prestações no banco de dados
        prestacaoRepository.saveAll(prestacoes);
    }
    
    /*  
     private void gerarPrestacoes( Emprestimo emprestimo ) {
        List<PrestacaoEmprestimo> prestacoes = new ArrayList<>();
        BigDecimal saldoDevedor = emprestimo.getValorSolicitado();
        BigDecimal valorParcela = emprestimo.getValorParcela();
        BigDecimal taxaJuros    = emprestimo.getTaxaJuros();
        
        // Gerar uma prestação para cada parcela
        for (int i = 1; i <= emprestimo.getQuantidadeParcelas(); i++) {
            PrestacaoEmprestimo prestacao = new PrestacaoEmprestimo();
            prestacao.setEmprestimo   ( emprestimo   );
            prestacao.setNumeroParcela( i            );
            prestacao.setValorParcela ( valorParcela );
            
            // Calcular juros do período
            BigDecimal valorJuros = saldoDevedor.multiply(taxaJuros).setScale(2, RoundingMode.HALF_UP);
            prestacao.setValorJuros( valorJuros );
            
            // Calcular amortização (valor da parcela menos juros)
            BigDecimal valorAmortizacao = valorParcela.subtract(valorJuros);
            prestacao.setValorAmortizacao( valorAmortizacao );
            
            // Atualizar saldo devedor
            saldoDevedor = saldoDevedor.subtract(valorAmortizacao);
            if (saldoDevedor.compareTo(BigDecimal.ZERO) < 0) {
                saldoDevedor = BigDecimal.ZERO;
            }
            
            prestacao.setSaldoDevedor( saldoDevedor );
            
            // Definir data de vencimento (mes atual + número da parcela)
            Calendar calendar = Calendar.getInstance();
            calendar.add( Calendar.MONTH, i );
            prestacao.setDtVencimento( calendar.getTime() );
            
            // Adicionar prestação à lista
            prestacoes.add( prestacao );
        }
        
        // Salvar todas as prestações no banco de dados
        prestacaoRepository.saveAll( prestacoes );
    }
    */
    
    /**
     * Aprova um empréstimo solicitado
     * @param idEmprestimo ID do empréstimo a ser aprovado
     * @return Empréstimo aprovado
     */
    @Transactional
    public Emprestimo aprovarEmprestimo(Long idEmprestimo) {
        // Buscar empréstimo pelo ID
        Emprestimo emprestimo = emprestimoRepository.findById(idEmprestimo)
                .orElseThrow(() -> new ExceptionCustomizada("Empréstimo não encontrado"));
        
        LimiteCredito limiteCredito = limitecreditoService.buscarPorFuncionario( emprestimo.getFuncionario().getIdFuncionario() );
        
        // Verificar se o empréstimo está com status SOLICITADO
        if (emprestimo.getStatus() != StatusEmprestimo.SOLICITADO) {
            throw new ExceptionCustomizada("Empréstimo não está com status SOLICITADO");
        }
        
	    String retornoValidaLimiteCredito = validarComLogs( limiteCredito,  emprestimo);
	    if( retornoValidaLimiteCredito != null ) throw new ExceptionCustomizada(retornoValidaLimiteCredito); 

        // Atualizar status e data de aprovação
        emprestimo.setStatus     ( StatusEmprestimo.APROVADO        );
        emprestimo.setDtAprovacao( Calendar.getInstance().getTime() );
        
        // Soma dos Valores para atualizar o limite de credito do Funcionário	    
//	    BigDecimal valorUtilizadoAtualizada = Optional.ofNullable( limiteCredito.getValorUtilizado() ) .orElse(BigDecimal.ZERO)
//	                                                  .add(Optional.ofNullable( emprestimo.getValorSolicitado() ) .orElse(BigDecimal.ZERO));
        
	    try {
	        // Atualizar limite de crédito do funcionário (aumentar o limite disponível)
	        limiteCreditoService.updateRollbackRestabelecerLimiteCredito( limiteCredito.getIdLimiteCredito(), emprestimo.getValorSolicitado() ) ;
	    }catch ( Exception e ) {
	       	logger.error("Erro ao aprovar o Emprestimo: {}", emprestimo.getIdEmprestimo(), e);
            throw new RuntimeException("Erro na validação de limite de crédito", e);
		}

        return emprestimoRepository.save(emprestimo);
    }

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    public String validarComLogs(LimiteCredito limiteCredito, Emprestimo emprestimo) {
    	logger.debug("Validando limite de crédito para o Emprestimo: {}", emprestimo.getIdEmprestimo());
        
        try {
            BigDecimal limite          = limiteCredito.getLimite();
            BigDecimal valorUtilizado  = limiteCredito.getValorUtilizado();
            BigDecimal valorEmprestimo = emprestimo.getValorSolicitado();
            
            logger.debug("Limite: {}, Utilizado: {}, Valor Emprestimo: {}", limite, valorUtilizado, valorEmprestimo);
            
            BigDecimal saldoDisponivel = limite.subtract(valorUtilizado);
            
            if (saldoDisponivel.compareTo(valorEmprestimo) < 0) {
            	logger.warn("Saldo insuficiente para venda {}: Saldo {} < Valor {}",
            			emprestimo.getIdEmprestimo(), saldoDisponivel, valorEmprestimo);
                
                return String.format(MensagensValidacao.SALDO_INSUFICIENTE,
                    FuncoesUteis.formatarParaReal(saldoDisponivel.doubleValue()));
            }
            
            logger.info("Pagamento aprovado para o Emprestimo: {}", emprestimo.getIdEmprestimo());
            return null;
            
        } catch (Exception e) {
        	logger.error("Erro ao validar limite de crédito para o Emprestimo: {}", emprestimo.getIdEmprestimo(), e);
            throw new RuntimeException("Erro na validação de limite de crédito o Emprestimo: " + emprestimo.getIdEmprestimo() + " Error: ", e);
        }
    }

    /**
     * Reprova um empréstimo solicitado
     * @param idEmprestimo ID do empréstimo a ser reprovado
     * @param motivo Motivo da reprovação
     * @return Empréstimo reprovado
     */
    @Transactional
    public Emprestimo reprovarEmprestimo(Long idEmprestimo, String motivo) {
        // Buscar empréstimo pelo ID
        Emprestimo emprestimo = emprestimoRepository.findById(idEmprestimo)
                .orElseThrow(() -> new ExceptionCustomizada("Empréstimo não encontrado"));
        
        // Verificar se o empréstimo está com status SOLICITADO
        if (emprestimo.getStatus() != StatusEmprestimo.SOLICITADO) {
            throw new ExceptionCustomizada("Empréstimo não está com status SOLICITADO");
        }
        
        // Atualizar status e adicionar motivo da reprovação na observação
        emprestimo.setStatus(StatusEmprestimo.REPROVADO);
        emprestimo.setObservacao(emprestimo.getObservacao() + " | MOTIVO REPROVAÇÃO: " + motivo);
        
        return emprestimoRepository.save(emprestimo);
    }
    
    /**
     * Registra o pagamento de uma prestação
     * @param idPrestacao ID da prestação a ser paga
     * @param request DTO com dados do pagamento
     * @return Prestação atualizada
     */
    @Transactional
    public PrestacaoEmprestimo pagarPrestacao(Long idPrestacao, PagamentoPrestacaoRequestDTO request) {
        // Buscar prestação pelo ID
        PrestacaoEmprestimo prestacao = prestacaoRepository.findById(idPrestacao).orElseThrow(() -> new ExceptionCustomizada("Prestação não encontrada"));
        
        // Verificar se a prestação já está paga
        if (prestacao.getStatus() == StatusPrestacao.PAGA) throw new ExceptionCustomizada("Prestação já está paga");
        
        // Registrar pagamento
        prestacao.setStatus     ( StatusPrestacao.PAGA                                                                           );
        prestacao.setDtPagamento( request.getDtPagamento() != null ? request.getDtPagamento() : Calendar.getInstance().getTime() );
        
        // Salvar prestação atualizada
        PrestacaoEmprestimo prestacaoPaga = prestacaoRepository.save( prestacao );
        
        // Atualizar limite de crédito do funcionário (aumentar o limite disponível)
        limiteCreditoService.aumentarLimiteCredito(
            prestacao.getEmprestimo().getFuncionario().getIdFuncionario(), 
            prestacao.getValorAmortizacao()
        );
        
        // Verificar se o empréstimo foi totalmente quitado
        verificarQuitacaoEmprestimo( prestacao.getEmprestimo().getIdEmprestimo() );
        
        return prestacaoPaga;
    }
    
    /**
     * Verifica se todas as prestações de um empréstimo foram pagas
     * e atualiza o status para QUITADO se for o caso
     * @param idEmprestimo ID do empréstimo a ser verificado
     */
    private void verificarQuitacaoEmprestimo(Long idEmprestimo) {
        // Buscar empréstimo com suas prestações
        Optional<Emprestimo> emprestimoOpt = emprestimoRepository.findByIdWithPrestacoes(idEmprestimo);
        if (emprestimoOpt.isPresent()) {
            Emprestimo emprestimo = emprestimoOpt.get();
            
            // Verificar se todas as prestações estão pagas
            boolean todasPagas = emprestimo.getPrestacoes().stream()
                    .allMatch(p -> p.getStatus() == StatusPrestacao.PAGA);
            
            // Se todas as prestações estão pagas, atualizar status do empréstimo
            if (todasPagas) {
                emprestimo.setStatus( StatusEmprestimo.QUITADO );
                emprestimoRepository.save(emprestimo);
            }
        }
    }
    
    /**
     * Lista todos os empréstimos de um funcionário
     * @param idFuncionario ID do funcionário
     * @return Lista de empréstimos do funcionário
     */
    public List<Emprestimo> listarEmprestimosPorFuncionario(Long idFuncionario) {
        return emprestimoRepository.findByFuncionarioIdFuncionario(idFuncionario);
    }
    
    /**
     * Busca um empréstimo específico com suas prestações
     * @param idEmprestimo ID do empréstimo
     * @return Empréstimo com suas prestações, se encontrado
     */
    public Optional<Emprestimo> buscarEmprestimoComPrestacoes(Long idEmprestimo) {
        return emprestimoRepository.findByIdWithPrestacoes(idEmprestimo);
    }
    
    /**
     * Lista todas as prestações de um empréstimo
     * @param idEmprestimo ID do empréstimo
     * @return Lista de prestações do empréstimo
     */
    public List<PrestacaoEmprestimo> listarPrestacoesPorEmprestimo(Long idEmprestimo) {
        return prestacaoRepository.findByEmprestimoIdEmprestimo(idEmprestimo);
    }
    
    /**
     * Lista todas as prestações vencidas (pendentes e com data de vencimento anterior à data atual)
     * @return Lista de prestações vencidas
     */
    public List<PrestacaoEmprestimo> listarPrestacoesVencidas() {
        return prestacaoRepository.findByStatusAndDtVencimentoBefore(
        		StatusPrestacao.PENDENTE, 
            Calendar.getInstance().getTime()
        );
    }
}