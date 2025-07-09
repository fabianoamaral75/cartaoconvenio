package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxasFaixaVendas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxasFaixaVendasRepository extends JpaRepository<TaxasFaixaVendas, Long> {

    List<TaxasFaixaVendas> findByDescricaoTaxaContainingIgnoreCase(String descricao);
    
    List<TaxasFaixaVendas> findByStatusTaxa(String status);
    
    /**
     * Busca todas as taxas ordenadas por valorFaixaTaxaMin e valorFaixaTaxaMax em ordem crescente
     * @return Lista de TaxasFaixaVendas ordenada
     */
    @Query("SELECT t FROM TaxasFaixaVendas t ORDER BY t.valorFaixaTaxaMin ASC, t.valorFaixaTaxaMax ASC")
    List<TaxasFaixaVendas> findAllOrderByFaixasAsc();
    
    /**
     * Busca todas as taxas ordenadas por valorFaixaTaxaMin e valorFaixaTaxaMax em ordem decrescente
     * @return Lista de TaxasFaixaVendas ordenada
     */
    @Query("SELECT t FROM TaxasFaixaVendas t ORDER BY t.valorFaixaTaxaMin DESC, t.valorFaixaTaxaMax DESC")
    List<TaxasFaixaVendas> findAllOrderByFaixasDesc();
    
    /**
     * Busca taxas ativas ordenadas por faixas em ordem crescente
     * @return Lista de TaxasFaixaVendas ativas ordenada
     */
    @Query("SELECT t FROM TaxasFaixaVendas t WHERE t.statusTaxa = 'ATIVO' ORDER BY t.valorFaixaTaxaMin ASC, t.valorFaixaTaxaMax ASC")
    List<TaxasFaixaVendas> findAtivasOrderByFaixasAsc();
    
    /**
     * Busca taxas ativas ordenadas por faixas em ordem decrescente
     * @return Lista de TaxasFaixaVendas ativas ordenada
     */
    @Query("SELECT t FROM TaxasFaixaVendas t WHERE t.statusTaxa = 'ATIVO' ORDER BY t.valorFaixaTaxaMin DESC, t.valorFaixaTaxaMax DESC")
    List<TaxasFaixaVendas> findAtivasOrderByFaixasDesc();
}

