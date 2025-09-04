// EmprestimoFechamentoService.java
package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContasReceber;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContasReceberEmprestimo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Emprestimo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Entidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PrestacaoEmprestimo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.FechamentoEmprestimoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PagamentoPrestacaoRequestDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ContasReceberRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.EmprestimoFechamentoRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.EmprestimoRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.FuncionarioRepository;

@Service
public class EmprestimoFechamentoService {

    @Autowired
    private EmprestimoFechamentoRepository emprestimoFechamentoRepository;
    
    @Autowired
    private EmprestimoRepository emprestimoRepository;
    
    @Autowired
    private ContasReceberRepository contasReceberRepository;
    
    @Autowired
    private EmprestimoService emprestimoService; 
    
    @Autowired
    private FuncionarioRepository funcionarioRepository;

/*    
    @Autowired
    private LimitecreditoService limiteCreditoService;
*/    
    @Transactional
    public List<ContasReceber> processarFechamentoEmprestimos(String anoMes) {
        List<FechamentoEmprestimoDTO> prestacoesVencidas = emprestimoFechamentoRepository.findPrestacoesVencidasFechamento(anoMes);
        
        List<ContasReceber> contasReceberCriadas = new ArrayList<>();
        
        // Agrupar por funcionário para criar uma conta a receber por funcionário
        prestacoesVencidas.stream()
            .collect(Collectors.groupingBy(FechamentoEmprestimoDTO::getIdFuncionario))
            .forEach((idFuncionario, prestacoes) -> {
                ContasReceber contaReceber = criarContaReceberParaFuncionario(idFuncionario, prestacoes, anoMes);
                contasReceberCriadas.add(contaReceber);
                
                // QUITAR AS PARCELAS APÓS CRIAR A CONTA A RECEBER
                quitarParcelasDoFuncionario(prestacoes);
            });
        
        return contasReceberRepository.saveAll(contasReceberCriadas);
    }


    private ContasReceber criarContaReceberParaFuncionario(Long idFuncionario, 
                                                           List<FechamentoEmprestimoDTO> prestacoes, 
                                                           String anoMes) {
        BigDecimal valorTotal = prestacoes.stream()
            .map(FechamentoEmprestimoDTO::getValorParcela)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Optional<Entidade> entidade = funcionarioRepository.findEntidadeWithCurrentTaxaByFuncionarioId(idFuncionario);

        String textObs = "Cobrança de empréstimos Nº Parcela - " + String.format("%03d", prestacoes.get(0).getNumeroParcela()) + " - Período - " + anoMes;

        ContasReceber contaReceber = new ContasReceber();
        contaReceber.setAnoMes                    ( anoMes                                  );
        contaReceber.setDescStatusReceber         ( StatusReceber.AGUARDANDO_UPLOAD_NF      );
        contaReceber.setValorReceber              ( valorTotal                              );
        contaReceber.setValorCalcTaxaEntidadeCiclo( BigDecimal.ZERO                         );
        contaReceber.setDtPrevisaoRecebimento     ( Calendar.getInstance().getTime()        );
        contaReceber.setObservacao                ( textObs                                 );
        contaReceber.setEntidade                  ( entidade.get()                          );  
        contaReceber.setTaxaEntidade              ( entidade.get().getTaxaEntidade().get(0) );
        
        // Criar relacionamentos com empréstimos
        List<ContasReceberEmprestimo> relacionamentos = new ArrayList<>();
        
        
        for (FechamentoEmprestimoDTO prestacao : prestacoes) {
            ContasReceberEmprestimo relacionamento = new ContasReceberEmprestimo();
            relacionamento.setContasReceber   ( contaReceber                 );
            relacionamento.setValorParcela    ( prestacao.getValorParcela()  );
            relacionamento.setNumeroParcela   ( prestacao.getNumeroParcela() );
            relacionamento.setAnoMesReferencia( anoMes                       );
            
            Emprestimo emprestimo = emprestimoRepository.findById(prestacao.getIdEmprestimo())
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));
            relacionamento.setEmprestimo( emprestimo );
            
            relacionamentos.add(relacionamento);
            
            // Atualizar status da prestação para EM_COBRANCA
            // atualizarStatusPrestacao( emprestimo, prestacao.getNumeroParcela() );
        }
        
        contaReceber.setContasReceberEmprestimos( relacionamentos );
        return contaReceber;
    }

    /**
     * Nova função para quitar as parcelas usando o EmprestimoService
     */
    private void quitarParcelasDoFuncionario(List<FechamentoEmprestimoDTO> prestacoes ) {
        for (FechamentoEmprestimoDTO prestacaoDTO : prestacoes) {
            try {
                // Criar DTO de pagamento
            	String numeroContrato = prestacaoDTO.getIdEmprestimo() != null ? String.format("%06d", prestacaoDTO.getIdEmprestimo()) : "N/A";
            	PagamentoPrestacaoRequestDTO pagamentoRequest = new PagamentoPrestacaoRequestDTO();
                pagamentoRequest.setDtPagamento(Calendar.getInstance().getTime());
                pagamentoRequest.setObservacao("Quitação da parcela via processo automático de fechamento do mês " + prestacaoDTO.getAnoMesReferencia() + ". " +
                		                       "Valor quitado em conformidade com o contrato de empréstimo nº "    + numeroContrato                     + ". " +
                		                       "Transação autorizada pelo sistema em "                             + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + ".");

                // Buscar a prestação real pelo ID do empréstimo e número da parcela
                PrestacaoEmprestimo prestacao = buscarPrestacaoPorEmprestimoEParcela(
                    prestacaoDTO.getIdEmprestimo(), 
                    prestacaoDTO.getNumeroParcela()
                );
                
                if (prestacao != null) {
                    // Chamar o serviço para quitar a parcela
                    emprestimoService.pagarPrestacao( prestacao.getIdPrestacao(), pagamentoRequest );
                }
                
            } catch (Exception e) {
                // Logar erro mas continuar processando as demais parcelas
                System.err.println( "Erro ao quitar parcela " + prestacaoDTO.getNumeroParcela() + " do empréstimo " + prestacaoDTO.getIdEmprestimo() + ": " + e.getMessage() );
            }
        }
    }

    /**
     * Função auxiliar para buscar prestação por empréstimo e número da parcela
     */
    private PrestacaoEmprestimo buscarPrestacaoPorEmprestimoEParcela(Long idEmprestimo, Integer numeroParcela) {
        Optional<Emprestimo> emprestimoOpt = emprestimoRepository.findByIdWithPrestacoes(idEmprestimo);
        if (emprestimoOpt.isPresent()) {
            return emprestimoOpt.get().getPrestacoes().stream()
                .filter(p -> p.getNumeroParcela().equals(numeroParcela))
                .findFirst()
                .orElse(null);
        }
        return null;
    }
 
    /**
     * Versão otimizada para processamento em lote
     */
    
    public void quitarParcelasDoFuncionarioTeste(List<FechamentoEmprestimoDTO> prestacoes) {
        // Agrupar por empréstimo para processamento mais eficiente
        Map<Long, List<FechamentoEmprestimoDTO>> agrupadoPorEmprestimo = prestacoes.stream()
            .collect(Collectors.groupingBy(FechamentoEmprestimoDTO::getIdEmprestimo));
        
        for ( Map.Entry<Long, List<FechamentoEmprestimoDTO>> entry : agrupadoPorEmprestimo.entrySet() ) {
            Long idEmprestimo = entry.getKey();
            List<FechamentoEmprestimoDTO> parcelas = entry.getValue();
            
            try {
                // Buscar todas as prestações do empréstimo de uma vez
                Optional<Emprestimo> emprestimoOpt = emprestimoRepository.findByIdWithPrestacoes(idEmprestimo);
                
                if (emprestimoOpt.isPresent()) {
                    Emprestimo emprestimo = emprestimoOpt.get();
                    
                    for (FechamentoEmprestimoDTO prestacaoDTO : parcelas) {
                        PrestacaoEmprestimo prestacao = emprestimo.getPrestacoes().stream()
                            .filter(p -> p.getNumeroParcela().equals(prestacaoDTO.getNumeroParcela()))
                            .findFirst()
                            .orElse(null);
                        
                        if (prestacao != null) {
                            PagamentoPrestacaoRequestDTO pagamentoRequest = new PagamentoPrestacaoRequestDTO();
                            pagamentoRequest.setDtPagamento(Calendar.getInstance().getTime());
                            
                            emprestimoService.pagarPrestacao(prestacao.getIdPrestacao(), pagamentoRequest);
                        }
                    }
                }
                
            } catch (Exception e) {
                System.err.println("Erro ao processar empréstimo " + idEmprestimo + ": " + e.getMessage());
            }
        }
    }  
         
}