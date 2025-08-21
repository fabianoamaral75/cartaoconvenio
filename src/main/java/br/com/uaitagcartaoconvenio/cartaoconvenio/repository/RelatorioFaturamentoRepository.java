package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusRelatorioFaturamento;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.RelatorioFaturamentoConveniado;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection.RelCicloPagamentoConveniadosProjection;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection.RelCicloPagamentoConveniadosTaxasProjection;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection.RelCicloPagamentoVendasItemProdutosProjection;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection.RelCicloPagamentoVendasProjection;


@Repository
public interface RelatorioFaturamentoRepository extends JpaRepository<RelatorioFaturamentoConveniado, Long> {

    Optional<RelatorioFaturamentoConveniado> findByIdConveniadosAndAnoMesAndStatus(
            @Param("idConveniados") Long idConveniados,
            @Param("anoMes") String anoMes,
            @Param("status") StatusRelatorioFaturamento status);

    List<RelatorioFaturamentoConveniado> findByIdConveniadosAndAnoMes(
            @Param("idConveniados") Long idConveniados,
            @Param("anoMes") String anoMes);

    @Query("SELECT r FROM RelatorioFaturamentoConveniado r WHERE r.idConveniados = :idConveniados AND r.anoMes = :anoMes ORDER BY r.dtCriacao DESC")
    List<RelatorioFaturamentoConveniado> findLatestByIdConveniadosAndAnoMes(
            @Param("idConveniados") Long idConveniados,
            @Param("anoMes") String anoMes);

    List<RelatorioFaturamentoConveniado> findByCicloPagamentoVenda_IdCicloPagamentoVenda(Long idCicloPagamentoVenda);
    
    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/
    @Query(value = """
            SELECT
                cpv.id_ciclo_pagamento_venda as idCicloPagamentoVenda,
                cpv.ano_mes as anoMes,
                cpv.status as descStatusPagamento,
                cpv.dt_criacao as dtCriacao,
                cpv.dt_alteracao as dtAlteracao,
                cpv.dt_pagamento as dtPagamento,
                cpv.observacao as observacao,
                cpv.valor_ciclo_bruto as vlrCicloBruto,
                cpv.valor_liquido as vlrLiquido,
                cpv.valor_liquido_pagamento as vlrLiquidoPagamento,
                cpv.valor_taxa_extra_percentual as vlrTaxaExtraPercentual,
                cpv.valor_taxa_extra_valor as vlrTaxaExtraValor,
                cpv.valor_taxa_secundaria as vlrTaxaSecundaria,
                cpv.id_taxas_faixa_vendas as idTaxasFaixaVendas,
                tfv.desc_taxa as descricaoTaxa,
                cpv.valor_taxas_faixa_vendas as vlrTaxasFaixaVendas,
                con.id_conveniados as idConveniados,
                con.site as site,
                con.ano_mes_ultino_fechamento as anoMesUltinoFechamento,
                pes.bairro as bairro,
                pes.cep as cep,
                pes.cidade as cidade,
                pes.email as email,
                pes.logradoro as logradoro,
                pes.numero as numero,
                pes.telefone as telefone,
                pes.uf as uf,
                pej.cnpj as cnpj,
                pej.razao_social as razaoSocial
            FROM
                ciclo_pagamento_venda cpv
                INNER JOIN conveniados con ON con.id_conveniados = cpv.id_conveniados
                INNER JOIN pessoa pes ON pes.id_conveniados = con.id_conveniados
                INNER JOIN pessoa_juridica pej ON pej.id_pessoa = pes.id_pessoa
                LEFT JOIN taxas_faixa_vendas tfv ON tfv.id_taxas_faixa_vendas = cpv.id_taxas_faixa_vendas
            WHERE cpv.id_ciclo_pagamento_venda = :idCiclo
            """, nativeQuery = true)
        Optional<RelCicloPagamentoConveniadosProjection> findDadosCicloConveniados(@Param("idCiclo") Long idCicloPagamentoVenda);
    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/
    @Query(value = """
	    SELECT 
	        cpv.id_ciclo_pagamento_venda as idCicloPagamentoVenda,
	        cpv.ano_mes as anoMes,
	        itec.id_item_taxa_extra_conveniada as idItemTaxaExtraConveniada,
	        CASE
	            WHEN itec.cobranca_valor_bruto = true THEN 'Sim'
	            ELSE 'Não'
	        END AS cobrancaValorBruto,
	        CASE
	            WHEN itec.tipo_cobranca_percentual = true THEN 'Sim'
	            ELSE 'Não'
	        END AS tipoCobrancaPercentual,
	        itec.vlr_taxa as valorTaxa,
	        ite.id_taxas_extra_conveniada as idTaxasExtraConveniada,
	        ite.desc_taxa as descricaoTaxa,
	        ite.status_taxa as statusTaxa,
	        pct.id_periodo_cobranca_taxa as idPeriodoCobrancaTaxa,
	        pct.dt_inicio as dataInicio,
	        pct.dt_fim as dataFim,
	        pct.desc_periodo_cobranca_taxa as descPeriodoCobrancaTaxa,
	        pct.observacao as obsPeriodoCobrancaTaxa,
	        pct.qty_cobranca as qtyCobranca,
	        pct.data_ultima_cobranca as dtUltimaCobranca,
	        pct.data_proxima_cobranca as dtProximaCobranca,
	        tip.id_tipo_periodo as idTipoPeriodo,
	        tip.desc_tipo_periodo as descTipoPeriodo,
	        tip.tipo as tipoPeriodo
	    FROM 
	        ciclo_pagamento_venda cpv
	        INNER JOIN itens_taxas_extra_conveniada itec ON itec.id_ciclo_pagamento_venda = cpv.id_ciclo_pagamento_venda
	        INNER JOIN taxas_extra_conveniada ite ON ite.id_taxas_extra_conveniada = itec.id_taxas_extra_conveniada
	        INNER JOIN periodo_cobranca_taxa pct ON pct.id_periodo_cobranca_taxa = ite.id_periodo_cobranca_taxa
	        INNER JOIN tipo_periodo tip ON tip.id_tipo_periodo = pct.id_tipo_periodo
	    WHERE cpv.id_ciclo_pagamento_venda = :idCicloPagamentoVenda
	""", nativeQuery = true)
    List<RelCicloPagamentoConveniadosTaxasProjection> findTaxasPorCiclo(@Param("idCicloPagamentoVenda") Long idCicloPagamentoVenda);
    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/
    @Query(value = """
        SELECT 
            ven.id_venda as idVenda,
            ven.ano_mes as anoMes,
            ven.status as descStatusVendas,
            ven.status_venda_paga as descStatusVendaPg,
            ven.status_venda_recebida as descStatusVendaReceb,
            ven.status_limite_credito_restabelecido as descRestLimiteCredito,
            ven.dt_venda as dtVenda,
            ven.valor_calc_taxa_conveniado as valorCalcTaxaConveniado,
            ven.valor_calc_taxa_entidade as valorCalcTaxaEntidade,
            ven.valor_venda as valorVenda,
            tac.taxa as taxa,
            false as tipo_taxa,
            (SELECT ent.nome_entidade FROM entidade ent WHERE ent.id_entidade = ven.id_taxa_entidade) as entidade,
            (SELECT pej.razao_social
             FROM conveniados con
             INNER JOIN pessoa pes ON pes.id_conveniados = con.id_conveniados
             INNER JOIN pessoa_juridica pej ON pej.id_pessoa = pes.id_pessoa
             WHERE con.id_conveniados = ven.id_conveniados) as conveniada
        FROM
            fechamento_conv_itens_vendas fciv
            INNER JOIN venda ven ON ven.id_venda = fciv.id_venda
            INNER JOIN taxa_conveiniados tac ON tac.id_taxa_conveiniados = ven.id_taxa_conveiniados
        WHERE fciv.id_ciclo_pagamento_venda = :idCicloPagamentoVenda
        
        UNION ALL
        
        SELECT 
            ven.id_venda                            as idVenda                ,
            ven.ano_mes                             as anoMes                 ,
            ven.status                              as descStatusVendas       ,
            ven.status_venda_paga                   as descStatusVendaPg      ,
            ven.status_venda_recebida               as descStatusVendaReceb   ,
            ven.status_limite_credito_restabelecido as descRestLimiteCredito  ,
            ven.dt_venda                            as dtVenda                ,
            ven.valor_calc_taxa_conveniado          as valorCalcTaxaConveniado,
            ven.valor_calc_taxa_entidade            as valorCalcTaxaEntidade  ,
            ven.valor_venda                         as valorVenda             ,
            tce.vlr_taxa                            as taxa                   ,
            true                                    as tipo_taxa              ,
            (SELECT ent.nome_entidade FROM entidade ent WHERE ent.id_entidade = ven.id_taxa_entidade) as entidade,
            (SELECT pej.razao_social
             FROM conveniados                  con
             INNER JOIN pessoa pes          ON pes.id_conveniados = con.id_conveniados
             INNER JOIN pessoa_juridica pej ON pej.id_pessoa       = pes.id_pessoa
             WHERE con.id_conveniados = ven.id_conveniados) as conveniada
        FROM
            fechamento_conv_itens_vendas        fciv
            INNER JOIN venda                    ven  ON ven.id_venda                    = fciv.id_venda
            INNER JOIN taxa_conveniada_entidade tce  ON tce.id_taxa_conveniada_entidade = ven.id_taxa_conveniados_entidate
        WHERE fciv.id_ciclo_pagamento_venda = :idCicloPagamentoVenda
    """, nativeQuery = true)
    List<RelCicloPagamentoVendasProjection> findVendasPorCiclo(@Param("idCicloPagamentoVenda") Long idCicloPagamentoVenda);

    /******************************************************************/
    /*                                                                */
    /*                                                                */
    /******************************************************************/
    @Query(value = """
    SELECT
        itv.id_itens_venda as idItensVenda,
        itv.qty_item as qtyItem,
        itv.vlr_unitario as vlrUnitario,
        itv.vlr_total_item as vlrTotalItem,
        pro.produto as produto,
        pro.vlr_produto as vlrProduto,
        itv.id_venda as idVenda
    FROM  
        itens_venda itv
        INNER JOIN produto pro ON pro.id_produto = itv.id_produto
    WHERE itv.id_venda IN (
        SELECT ven.id_venda
        FROM fechamento_conv_itens_vendas fciv
        INNER JOIN venda ven ON ven.id_venda = fciv.id_venda
        WHERE fciv.id_ciclo_pagamento_venda = :idCicloPagamentoVenda
    )
    """, nativeQuery = true)
List<RelCicloPagamentoVendasItemProdutosProjection> findItensProdutosPorCiclo(@Param("idCicloPagamentoVenda") Long idCicloPagamentoVenda);   
   
}