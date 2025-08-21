package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCicloPgVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaPg;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.excecoes.BusinessException;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaExtraConveniadaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Antecipacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.FechamentoConvItensVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ItemTaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PeriodoCobrancaTaxa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxasExtraordinarias;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxasFaixaVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TipoPeriodo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Venda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.PeriodoCobrancaTaxaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaExtraConveniadaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.CicloPagamentoVendaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ConveniadosRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TaxasExtraordinariasRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.TipoPeriodoRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.VendaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;

@Service
public class FechamentoCicloAntecipacaoService {
    
	@Autowired
    private VendaRepository vendaRepository;
	
    @Autowired
    private CicloPagamentoVendaRepository cicloPagamentoVendaRepository;
    
    @Autowired
    private TaxaExtraConveniadaService taxaExtraConveniadaService;
    
    @Autowired
    private TaxasFaixaVendasService taxasFaixaVendasService;
    
    @Autowired
    private ConveniadosRepository conveniadosRepository;
    
    @Autowired
    private TaxaExtraConveniadaMapper mapper;
    
    @Autowired
    private TipoPeriodoRepository tipoPeriodoRepository;
    
    @Autowired
    private TaxasExtraordinariasRepository taxasExtraordinariasRepository;
    
    @Autowired
	private ConveniadosService conveniadosService;
    
    private static final Logger logger = LogManager.getLogger(FechamentoCicloAntecipacaoService.class);

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    @Transactional
    public CicloPagamentoVenda processarFechamentoAntecipacaoMesCorrente( Antecipacao antecipacao ) throws BusinessException {
    	
    	// 1. Definição de constantes para logging
        final String METHOD_NAME = "processarAntecipacaoMesCorrente";
        
    	// 2. Log inicial do processo com informações da conveniada e quantidade de vendas
        logger.info("[{}] Iniciando antecipação para conveniada {} - {} vendas", 
                METHOD_NAME,antecipacao.getConveniados().getIdConveniados(), antecipacao.getVendas().size());
        try {
	        // 3. Validação dos dados básicos da antecipação
	        validarDadosAntecipacao(antecipacao);
	
	        // 4. Obtém a lista dos ID's das vendas relacionadas à antecipação
	        List<Long> idsVendas = antecipacao.getVendas().stream()
	                .map(av -> av.getVenda().getIdVenda())
	                .collect(Collectors.toList());
	        
	        // 5. Busca das vendas elegíveis para antecipação (já filtradas por conveniada e status)
	        List<Venda> vendas = buscarVendasValidasParaAntecipacao(idsVendas, antecipacao);
	        
	        // 6. Criação do ciclo de pagamento para a antecipação
	        CicloPagamentoVenda ciclo = criarCicloAntecipacao(antecipacao, vendas);
	        
	        // 7. Processamento dos itens de venda e associação ao ciclo
	        processarItensEVendas(ciclo, vendas);
	        
	        // 8. Processamento das taxas extras aplicáveis à conveniada
	        processarTaxas(ciclo, antecipacao.getConveniados());
	        
	        // 9. Cálculo do valor líquido final após todas as deduções
	        calcularValorLiquidoFinal(ciclo);
	        
	        // 10. Tratamento especial caso o valor líquido seja negativo
	        tratarSaldoNegativoSeNecessario(ciclo, antecipacao.getConveniados().getIdConveniados());
	
	        // 11. Persistência do ciclo de pagamento no banco de dados
	        ciclo = cicloPagamentoVendaRepository.save(ciclo);
	 
	        // 12. Atualização do status das vendas para ANTECIPACAO_PROCESSADA
	        atualizarStatusVendasAntecipacaoProcessada( idsVendas );
	        
	        // 13. Atualizata a tabela de Conveniados com a informação da última data de faturamento para a Conveniadas.
	        if (antecipacao.getConveniados() != null && antecipacao.getConveniados().getIdConveniados() != null) {
	            atualizarUltimoFechamentoConveniada(antecipacao.getConveniados().getIdConveniados());
	        }
	
	        // 14. Log de conclusão do processo
	        logger.info("Antecipação concluída com sucesso para conveniada {}", antecipacao.getConveniados().getIdConveniados());
	        
	        // 15. Retorno do ciclo de pagamento criado
	        return ciclo;
	        
        } catch (BusinessException e) {
            logger.error("[{}] Erro de negócio: {}", METHOD_NAME, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("[{}] Erro inesperado: {}", METHOD_NAME, e.getMessage(), e);
            throw new ExceptionCustomizada("Falha ao processar antecipação: " + e.getMessage());
        }
    }
    
    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    private void atualizarUltimoFechamentoConveniada( Long idConveniado ) {
        try {
        	String                  anoMes = FuncoesUteis.getDataAtualFormatoYYYMM();
        	List<Long> listaIdsConveniados = new ArrayList<Long>();
        	listaIdsConveniados.add(idConveniado);
        	
        	// Atualizata a tabela de Conveniados com a informação da última data de faturamento para a Conveniadas.
            conveniadosService.atualizarAnoMesRecebimentoPosFechamentoEmLote(listaIdsConveniados, anoMes);
            
        } catch (Exception e) {
            logger.error("Erro ao criar taxa extra para adiantamento: {}", e.getMessage(), e);
            throw new BusinessException("Erro ao processar taxa de adiantamento: " + e.getMessage());
        }
    }


    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    @Transactional
    public void processarAntecipacaoFechamentoExistente(Long idCicloPagamento) throws BusinessException {
        CicloPagamentoVenda ciclo = cicloPagamentoVendaRepository.findById(idCicloPagamento)
            .orElseThrow(() -> new BusinessException("Ciclo de pagamento não encontrado com ID: " + idCicloPagamento));
        
        ciclo.setDescStatusPagamento(StatusCicloPgVenda.PAGAMENTO_ANTECIPADO);
        ciclo.setDtAlteracao(new Date());
        
        cicloPagamentoVendaRepository.save(ciclo);
        logger.info("Status do ciclo {} atualizado para PAGAMENTO_ANTECIPADO", idCicloPagamento);
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    private void validarDadosAntecipacao(Antecipacao antecipacao) throws BusinessException {
    	
        if ( antecipacao.getDtCorte() == null ) {
            throw new BusinessException("Parâmetro anoMes não pode ser nulo ou vazio");
        }
        if (antecipacao.getConveniados() == null || antecipacao.getConveniados().getIdConveniados() == null) {
            throw new BusinessException("Conveniada não pode ser nula");
        }
        if (antecipacao.getVendas() == null || antecipacao.getVendas().isEmpty()) {
            throw new BusinessException("Nenhuma venda selecionada para antecipação");
        }
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    private List<Venda> buscarVendasValidasParaAntecipacao(List<Long> idsVendas, Antecipacao antecipacao) throws BusinessException {
        List<Venda> vendas = vendaRepository.findByIdVendaInAndConveniadosAndDescStatusVendasAndDescStatusVendaPg(
        	idsVendas, 
            antecipacao.getConveniados(), 
            StatusVendas.PRE_ANTECIPACAO, 
            StatusVendaPg.PRE_ANTECIPACAO);
        
        if (vendas.isEmpty()) {
            throw new BusinessException("Nenhuma venda válida encontrada para antecipação");
        }
        return vendas;
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    private CicloPagamentoVenda criarCicloAntecipacao( Antecipacao antecipacao, List<Venda> vendas ) {
    	
        CicloPagamentoVenda ciclo = new CicloPagamentoVenda();
        Date            dataAtual = new Date();
        
        ciclo.setAnoMes             ( FuncoesUteis.formataAnoMes( antecipacao.getDtCorte() ) );
        ciclo.setDescStatusPagamento( StatusCicloPgVenda.PAGAMENTO_ANTECIPADO                );
        ciclo.setDtAlteracao        ( dataAtual                                              );
        ciclo.setDtCriacao          ( dataAtual                                              );
        ciclo.setDtPagamento        ( convertToDate(antecipacao.getDtPagamento() )           );
        ciclo.setConveniados        ( antecipacao.getConveniados()                           );
        
        BigDecimal valorBruto     = calcularValorBrutoVendas( vendas         );
        BigDecimal taxaSecundaria = calcularTaxaSecundaria  ( vendas         );
        BigDecimal valorLiquido   = valorBruto.subtract     ( taxaSecundaria );
        
        ciclo.setVlrCicloBruto      ( valorBruto     );
        ciclo.setVlrTaxaSecundaria  ( taxaSecundaria );
        ciclo.setVlrLiquido         ( valorLiquido   );

//        ciclo.setVlrLiquidoPagamento( valorLiquido   ); // será calculado após o valor das taxas extras.
        
        ciclo.setObservacao("Fechamento por antecipação - Vendas: " + vendas.stream().map(v -> v.getIdVenda().toString()).collect(Collectors.joining(", ")));
        
        return ciclo;
        
    }
    
    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    private BigDecimal calcularTaxaSecundaria(List<Venda> vendas) {
        return vendas.stream()
            .map(Venda::getValorCalcTaxaConveniado)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    public static Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    private BigDecimal calcularValorBrutoVendas(List<Venda> vendas) {
        return vendas.stream()
            .map(Venda::getValorVenda)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    private void processarItensEVendas(CicloPagamentoVenda ciclo, List<Venda> vendas) {
    	
        List<FechamentoConvItensVendas> itensVendas = vendas.stream()
            .map(venda -> {
                FechamentoConvItensVendas item = new FechamentoConvItensVendas();
                item.setVenda(venda);
                item.setCicloPagamentoVenda(ciclo);
                return item;
            })
            .collect(Collectors.toList());
        
        ciclo.setFechamentoConvItensVendas(itensVendas);
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    private void processarTaxas(CicloPagamentoVenda ciclo, Conveniados conveniado) {
        processarTaxasExtras(ciclo, conveniado);
        
        if (conveniado.getIsTaxasFaixaVendas()) {
            processarTaxasFaixaVendas(ciclo);
        }
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/
    private void processarTaxasExtras(CicloPagamentoVenda ciclo, Conveniados conveniado) {
        // Inicializa a lista se for nula - usando a coleção existente
        if (ciclo.getItemTaxaExtraConveniada() == null) {
            ciclo.setItemTaxaExtraConveniada(new ArrayList<>());
        } else {
            ciclo.getItemTaxaExtraConveniada().clear();
        }
        
        BigDecimal vlrTxTotalizado = BigDecimal.ZERO;
        BigDecimal vlrTxTotalizadoPercentual = BigDecimal.ZERO;
        
        // Cria a taxa Extra de Antecipação, cobrada em toda Antecipação
        criarTaxaExtraAdiantamento(ciclo, conveniado.getIdConveniados());
        
        List<TaxaExtraConveniada> taxasExtra = taxaExtraConveniadaService.findAllTaxaByConveniadoId(conveniado.getIdConveniados())
                                          .stream()
                                          .filter(tec -> "ATIVA".equals(tec.getStatusTaxa()))
                                          .filter(tec -> !deveCobrarTaxaExtra(tec))
                                          .collect(Collectors.toList());
        
        for (TaxaExtraConveniada tec : taxasExtra) {
            // Cria novo item sem substituir a coleção
            ItemTaxaExtraConveniada item = processarTaxaExtra(tec, ciclo);
            ciclo.getItemTaxaExtraConveniada().add(item);
            
            if (tec.getTipoCobrancaPercentual()) {
                BigDecimal baseCalculo = tec.getCobrancaValorBruto() ? ciclo.getVlrCicloBruto() : ciclo.getVlrLiquido();
                vlrTxTotalizadoPercentual = vlrTxTotalizadoPercentual.add(
                    FuncoesUteis.calcularPorcentagem(baseCalculo, tec.getValorTaxa()))
                    .setScale(2, RoundingMode.HALF_UP);
            } else {
                vlrTxTotalizado = vlrTxTotalizado.add(tec.getValorTaxa()).setScale(2, RoundingMode.HALF_UP);
            }
            
            atualizarPeriodoCobranca(tec);
        }
        
        ciclo.setVlrTaxaExtraValor(vlrTxTotalizado);
        ciclo.setVlrTaxaExtraPercentual(vlrTxTotalizadoPercentual);
    }   

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/     
    private void criarTaxaExtraAdiantamento(CicloPagamentoVenda ciclo, Long idConveniado) {
        try {
            // Buscar a taxa extraordinária com ID 1L
            TaxasExtraordinarias taxaExtraordinaria = taxasExtraordinariasRepository.findById(1L)
                .orElseThrow(() -> new BusinessException("Taxa extraordinária padrão para adiantamento não encontrada"));
            
            TaxaExtraConveniada taxaExtraEntity =  criarTaxaExtraAdiantamentoEntity( taxaExtraordinaria, idConveniado ) ;
             taxaExtraConveniadaService.save( taxaExtraEntity );
            
        } catch (Exception e) {
            logger.error("Erro ao criar taxa extra para adiantamento: {}", e.getMessage(), e);
            throw new BusinessException("Erro ao processar taxa de adiantamento: " + e.getMessage());
        }
    }
    
    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/    
    public TaxaExtraConveniada criarTaxaExtraAdiantamentoEntity(TaxasExtraordinarias taxaExtraordinaria, Long idConveniada) {
    	
        if (taxaExtraordinaria == null) {
            throw new IllegalArgumentException("A taxa extraordinária não pode ser nula");
        }

        TaxaExtraConveniada entity = new TaxaExtraConveniada();
         
        Conveniados conveniado = conveniadosRepository.findByIdWithBasicRelationships(idConveniada)
        	    .orElseThrow(() -> new BusinessException("Conveniado não encontrado"));
        
        TipoPeriodo tipoPeriodo = tipoPeriodoRepository.findByIdTipoPeriodo(7L)
        		.orElseThrow(() -> new BusinessException("Tipo Período não encontrado"));
                
        
        // Preenche os atributos básicos com valores da taxa extraordinária
        entity.setDescricaoTaxa         ( taxaExtraordinaria.getDescricaoTaxaExtraordinarias() );
        entity.setDataCriacao           ( LocalDateTime.now()                                  );
        entity.setValorTaxa             ( taxaExtraordinaria.getValorTaxaExtraordinarias()     );
        entity.setStatusTaxa            ( "ATIVA"                                              );
        entity.setTipoCobrancaPercentual( false                                                );
        entity.setCobrancaValorBruto    ( false                                                );
        entity.setConveniados           ( conveniado                                           );
      
        // Cria e preenche o PeriodoCobrancaTaxaDTO para cobrança única
        PeriodoCobrancaTaxa periodo = new PeriodoCobrancaTaxa();
        LocalDate                    hoje = LocalDate.now();
        LocalDate            ultimoDiaMes = hoje.withDayOfMonth( hoje.lengthOfMonth() );
        
        periodo.setDescricao            ( "Cobrança de taxa extra de adiantamento"                    );
        periodo.setDataInicio           ( hoje                                                        );
        periodo.setDataFim              ( ultimoDiaMes                                                );
        periodo.setObservacao           ( "Taxa gerada automaticamente para cobrança de adiantamento" );
        periodo.setDataCriacao          ( LocalDateTime.now()                                         );
        periodo.setDtUltimaCobranca     ( hoje                                                        );
        periodo.setDtProximaCobranca    ( ultimoDiaMes                                                );
        periodo.setQtyCobranca          ( 0L                                                          );
        periodo.setTipoPeriodo          ( tipoPeriodo                                                 ); // Tipo de período para cobrança única
        
        entity.setPeriodoCobrancaTaxa(periodo);
                
        return entity;
    }    

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    private boolean deveCobrarTaxaExtra(TaxaExtraConveniada taxaExtraConveniada) {
        // Implementação específica para verificar se a taxa deve ser cobrada
        // Pode considerar período de cobrança, data de validade, etc.
    	
        // Valida se o objeto e seus componentes necessários existem
        if (taxaExtraConveniada == null || 
            taxaExtraConveniada.getPeriodoCobrancaTaxa() == null || 
            taxaExtraConveniada.getPeriodoCobrancaTaxa().getTipoPeriodo() == null) {
            return true;
        }

        PeriodoCobrancaTaxa periodo     = taxaExtraConveniada.getPeriodoCobrancaTaxa();
        TipoPeriodo         tipoPeriodo = periodo.getTipoPeriodo();
        String              tipo        = tipoPeriodo.getTipo();
        LocalDate           dataAtual   = LocalDate.now();

        // 1ª Regra: Cobrança Única ("U")
        if ("U".equals(tipo)) {
            // Verifica se a quantidade de cobranças é maior que zero
            return periodo.getQtyCobranca() != null && periodo.getQtyCobranca() > 0;
        }

        // 2ª Regra: Cobrança Anual ("A")
        if ("A".equals(tipo)) {
            // Verifica se o ano atual é diferente do ano da última cobrança
            return periodo.getDtUltimaCobranca() == null || 
                   periodo.getDtUltimaCobranca().getYear() != dataAtual.getYear();
        }

        // 3ª Regra: Cobrança por Período ("P")
        if ("P".equals(tipo)) {
            // Verifica se a data atual está dentro do período de cobrança
            return dataAtual.isAfter(periodo.getDataInicio()) && 
                   dataAtual.isBefore(periodo.getDataFim());
        }

        // Caso o tipo não seja reconhecido ou não atenda a nenhuma condição
        return false;
        
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    private ItemTaxaExtraConveniada processarTaxaExtra(TaxaExtraConveniada tec, CicloPagamentoVenda ciclo) {
    	
        ItemTaxaExtraConveniada item = new ItemTaxaExtraConveniada();
        
        item.setTaxaExtraConveniada   ( tec                             );
        item.setCicloPagamentoVenda   ( ciclo                           );
        item.setValorTaxa             ( tec.getValorTaxa()              );
        item.setTipoCobrancaPercentual( tec.getTipoCobrancaPercentual() );
        item.setCobrancaValorBruto    ( tec.getCobrancaValorBruto()     );
        return item;
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    private void atualizarPeriodoCobranca(TaxaExtraConveniada tec) {
    	
        PeriodoCobrancaTaxa periodo = tec.getPeriodoCobrancaTaxa();
        periodo.setQtyCobranca      ( (periodo.getQtyCobranca() != null ? periodo.getQtyCobranca() : 0) + 1 );
        periodo.setDtUltimaCobranca ( LocalDate.now()                                                       );
        periodo.setDtProximaCobranca( LocalDate.now().withDayOfMonth(1)                                     );
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/
    
    private void processarTaxasFaixaVendas(CicloPagamentoVenda ciclo) {
        try {
        	
            List<TaxasFaixaVendas> taxasFaixa = taxasFaixaVendasService.findAtivasOrderByFaixasAsc();
            TaxasFaixaVendas taxaAplicavel    = encontrarTaxaAplicavel(taxasFaixa, ciclo.getVlrCicloBruto());
            
            if (taxaAplicavel != null) {
            	
                BigDecimal vlrTaxasFaixaVendas = calcularValorTaxa(taxaAplicavel, ciclo.getVlrCicloBruto());
                ciclo.setVlrTaxasFaixaVendas(vlrTaxasFaixaVendas);
                ciclo.setTaxasFaixaVendas(taxaAplicavel);
                
            }
            
        } catch (Exception e) {
            logger.error("Erro ao processar taxas por faixa de vendas: {}", e.getMessage(), e);
            throw new BusinessException("Erro ao processar taxas por faixa de vendas: " + e.getMessage());
        }
    }
    /**
     * Encontra a taxa aplicável com base no valor de comparação e faixas de taxas ativas.
     * 
     * A função percorre a lista de taxas para encontrar aquela cuja faixa (valor mínimo e máximo)
     * contém o valor informado, considerando apenas taxas com status "ATIVO". Caso não encontre
     * uma faixa que contenha o valor, verifica se o valor é maior que o valor máximo da última
     * taxa ativa encontrada.
     * 
     * @param taxas Lista de TaxasFaixaVendas a serem verificadas
     * @param valorComparacao Valor a ser comparado com as faixas de taxas
     * @return TaxasFaixaVendas correspondente à faixa encontrada ou última taxa ativa se o valor
     *         for maior que o máximo, ou null se nenhuma taxa ativa for encontrada
     * @throws IllegalArgumentException Se a lista de taxas for nula ou vazia, ou se o valor de
     *         comparação for nulo
     */
    public TaxasFaixaVendas encontrarTaxaAplicavel(List<TaxasFaixaVendas> taxas, BigDecimal valorComparacao) {
    	
        // Validação dos parâmetros de entrada
        if (taxas == null || taxas.isEmpty()) {
            throw new IllegalArgumentException("A lista de taxas não pode ser nula ou vazia");
        }
        
        if (valorComparacao == null) {
            throw new IllegalArgumentException("O valor de comparação não pode ser nulo");
        }

        // Variável para armazenar a última taxa ativa encontrada
        TaxasFaixaVendas ultimaTaxaAtiva = null;

        // Percorre todas as taxas da lista
        for (TaxasFaixaVendas taxa : taxas) {
            // Verifica se a taxa está ativa
            if ("ATIVO".equalsIgnoreCase(taxa.getStatusTaxa())) {
                // Atualiza a referência da última taxa ativa
                ultimaTaxaAtiva = taxa;
                
                // Verifica se o valor está dentro da faixa atual
                boolean dentroDaFaixa = valorComparacao.compareTo(taxa.getValorFaixaTaxaMin()) >= 0 && 
                                        valorComparacao.compareTo(taxa.getValorFaixaTaxaMax()) <= 0;
                
                if (dentroDaFaixa) {
                    // Retorna a taxa cuja faixa contém o valor
                    return taxa;
                }
            }
        }
        
        // Se não encontrou nenhuma faixa que contenha o valor
        // Verifica se existe uma última taxa ativa e se o valor é maior que seu máximo
        if (ultimaTaxaAtiva != null && 
            valorComparacao.compareTo(ultimaTaxaAtiva.getValorFaixaTaxaMax()) > 0) {
            return ultimaTaxaAtiva;
        }
        
        // Se não encontrou nenhuma taxa válida, retorna null
        return null;
    }  
    
    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    private BigDecimal calcularValorTaxa(TaxasFaixaVendas taxa, BigDecimal valorBruto) {
        if (taxa.getTipoCobrancaPercentual()) {
            return valorBruto.multiply(taxa.getValorTaxa().divide(new BigDecimal(100)))
                .setScale(2, RoundingMode.HALF_UP);
        }
        return taxa.getValorTaxa();
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    private void calcularValorLiquidoFinal(CicloPagamentoVenda ciclo) {

 		// Realiza o calculo do valor liquido a pagar para a conveniada
		BigDecimal valorLiquidoFinal = FuncoesUteis.calcularSubtracao( ciclo.getVlrLiquido(), ciclo.getVlrTaxaExtraValor(), ciclo.getVlrTaxaExtraPercentual(), ciclo.getVlrTaxasFaixaVendas() );
		ciclo.setVlrLiquidoPagamento(valorLiquidoFinal);

    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    private void tratarSaldoNegativoSeNecessario(CicloPagamentoVenda ciclo, Long idConveniado) {
    	
        if (ciclo.getVlrLiquidoPagamento().compareTo(BigDecimal.ZERO) < 0) {
            try {
                TaxaExtraConveniadaDTO dtoTaxaExtra = criarTaxaExtraConveniadaDTO( ciclo.getVlrLiquidoPagamento(), idConveniado );
                
                TaxaExtraConveniada entity = mapper.toEntity(dtoTaxaExtra);
                Conveniados            con = conveniadosRepository.findById(idConveniado).orElse(null);
                TipoPeriodo          tipoP = tipoPeriodoRepository.findById(dtoTaxaExtra.getPeriodoCobrancaTaxa().getTipoPeriodoId()).orElse(null);
                
                entity.getPeriodoCobrancaTaxa().setTipoPeriodo(tipoP);
                entity.setConveniados(con);
                taxaExtraConveniadaService.save(entity);
                
            } catch (Exception e) {
                logger.error("Erro ao criar taxa extra para saldo negativo: {}", e.getMessage(), e);
                throw new BusinessException("Erro ao processar saldo negativo: " + e.getMessage());
            }
        }
    }
    
    /**
     * Cria e retorna um TaxaExtraConveniadaDTO preenchido com valores padrão,
     * utilizando o valor absoluto fornecido para o campo valorTaxa.
     * 
     * @param valorTaxa Valor absoluto que será atribuído ao campo valorTaxa
     * @return TaxaExtraConveniadaDTO preenchido
     * @throws IllegalArgumentException Se o valorTaxa for nulo
     */
    public static TaxaExtraConveniadaDTO criarTaxaExtraConveniadaDTO(BigDecimal valorTaxa, Long idConveniada) {
        if (valorTaxa == null) {
            throw new IllegalArgumentException("O valor da taxa não pode ser nulo");
        }

        TaxaExtraConveniadaDTO dto = new TaxaExtraConveniadaDTO();
        
        // Preenche os atributos básicos com valores padrão
        dto.setDescricaoTaxa         ( "Taxa de saldo negativo");
        dto.setDataCriacao           ( LocalDateTime.now() );
        dto.setValorTaxa             ( valorTaxa.abs()     ); // Usa o valor absoluto do parâmetro
        dto.setStatusTaxa            ( "ATIVA"             );
        dto.setTipoCobrancaPercentual( false               );
        dto.setCobrancaValorBruto    ( false               );
        dto.setConveniadosId         ( idConveniada        );
        
        // Cria e preenche o PeriodoCobrancaTaxaDTO com valores padrão
        PeriodoCobrancaTaxaDTO periodoDTO = new PeriodoCobrancaTaxaDTO();
        periodoDTO.setDescricao            ( "Cobrança Única"                                           );
        periodoDTO.setDataInicio           ( LocalDate.now()                                            );
        periodoDTO.setDataFim              ( LocalDate.now().plusMonths(1)                              );
        periodoDTO.setObservacao           ( "Taxa gerada automaticamente para cobrir saldo negativo no fechamento");
        periodoDTO.setDataCriacao          ( LocalDateTime.now()                                        );
        periodoDTO.setDtUltimaCobranca     ( null                                                       );
        periodoDTO.setDtProximaCobranca    ( LocalDate.now().plusMonths(1)                              );
        periodoDTO.setQtyCobranca          ( 0L                                                         );
        periodoDTO.setTipoPeriodoId        ( 7L                                                         );
        // Campos ignorados conforme relacionamento:
        periodoDTO.getTaxaExtraConveniada().setConveniadosId(idConveniada);
        
        dto.setPeriodoCobrancaTaxa(periodoDTO);
                
        return dto;
    }

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/	
    private void atualizarStatusVendasAntecipacaoProcessada(List<Long> idsVendas) {
        vendaRepository.updateStatusVendasAntecipacaoProcessada( idsVendas, StatusVendas.ANTECIPACAO_CONCLUIDA, StatusVendaPg.ANTECIPACAO_CONCLUIDA );
    }
    
    /**
     * Marca um fechamento existente como antecipado
     */
    @Transactional
    public CicloPagamentoVenda marcarFechamentoComoAntecipado(Long idCicloPagamento) throws BusinessException {
    	
        CicloPagamentoVenda ciclo = cicloPagamentoVendaRepository.findById(idCicloPagamento).orElseThrow(() -> new BusinessException( "Ciclo de pagamento não encontrado com ID: " + idCicloPagamento ) );
        
        // Valida se já não está antecipado
        if (ciclo.getDescStatusPagamento() == StatusCicloPgVenda.PAGAMENTO_ANTECIPADO) { throw new BusinessException( "Este fechamento já está marcado como antecipado" ); }
        
        // Atualiza status
        ciclo.setDescStatusPagamento( StatusCicloPgVenda.PAGAMENTO_ANTECIPADO               );
        ciclo.setDtAlteracao        ( new Date()                                            );
        ciclo.setObservacao         ( "Fechamento marcado como antecipado em " + new Date() );
        
        return cicloPagamentoVendaRepository.save(ciclo);
    }

}



