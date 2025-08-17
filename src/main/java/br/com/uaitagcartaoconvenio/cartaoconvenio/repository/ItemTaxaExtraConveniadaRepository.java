package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ItemTaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemTaxaExtraConveniadaRepository extends JpaRepository<ItemTaxaExtraConveniada, Long> {

    /**
     * Busca itens de taxa extra por lista de ciclos de pagamento onde a taxa extra tem período de cobrança definido
     * @param ciclos Lista de ciclos de pagamento
     * @return Lista de itens de taxa extra encontrados
     */
	@Query("SELECT i FROM ItemTaxaExtraConveniada i " +
		       "JOIN FETCH i.taxaExtraConveniada t " +
		       "JOIN FETCH t.periodoCobrancaTaxa p " +
		       "JOIN FETCH p.tipoPeriodo " +  // Este é o JOIN FETCH adicional
		       "WHERE i.cicloPagamentoVenda IN :ciclos " +
		       "AND t.periodoCobrancaTaxa IS NOT NULL")
		List<ItemTaxaExtraConveniada> findByCicloPagamentoVendaInAndTaxaExtraConveniadaPeriodoCobrancaTaxaIsNotNull(
		        @Param("ciclos") List<CicloPagamentoVenda> ciclos);

    /**
     * Busca itens de taxa extra por ciclo de pagamento
     * @param cicloPagamentoVenda Ciclo de pagamento
     * @return Lista de itens de taxa extra encontrados
     */
    List<ItemTaxaExtraConveniada> findByCicloPagamentoVenda(CicloPagamentoVenda cicloPagamentoVenda);

    /**
     * Busca itens de taxa extra por taxa extra
     * @param taxaExtraConveniada Taxa extra
     * @return Lista de itens de taxa extra encontrados
     */
    List<ItemTaxaExtraConveniada> findByTaxaExtraConveniada(TaxaExtraConveniada taxaExtraConveniada);

    /**
     * Busca itens de taxa extra por ciclo de pagamento e tipo de cobrança
     * @param cicloPagamentoVenda Ciclo de pagamento
     * @param tipoCobrancaPercentual Tipo de cobrança (true para percentual, false para valor fixo)
     * @return Lista de itens de taxa extra encontrados
     */
    List<ItemTaxaExtraConveniada> findByCicloPagamentoVendaAndTipoCobrancaPercentual(
            CicloPagamentoVenda cicloPagamentoVenda, Boolean tipoCobrancaPercentual);

    /**
     * Verifica se existe algum item de taxa extra para um ciclo de pagamento
     * @param cicloPagamentoVenda Ciclo de pagamento
     * @return true se existir, false caso contrário
     */
    boolean existsByCicloPagamentoVenda(CicloPagamentoVenda cicloPagamentoVenda);

    /**
     * Conta a quantidade de itens de taxa extra para um ciclo de pagamento
     * @param cicloPagamentoVenda Ciclo de pagamento
     * @return Quantidade de itens
     */
    long countByCicloPagamentoVenda(CicloPagamentoVenda cicloPagamentoVenda);
    
    
    @Query("SELECT t FROM TaxaExtraConveniada t " +
    	       "LEFT JOIN FETCH t.itemTaxaExtraConveniada " +  // Adicione esta linha
    	       "LEFT JOIN FETCH t.periodoCobrancaTaxa p " +
    	       "LEFT JOIN FETCH p.tipoPeriodo " +
    	       "LEFT JOIN FETCH t.conveniados " +
    	       "WHERE t.conveniados.id = :idConveniado")
    List<TaxaExtraConveniada> findByConveniadoId(@Param("idConveniado") Long idConveniado);
}