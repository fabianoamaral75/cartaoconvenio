package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RelCicloPagamentoConveniadosDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.projection.RelCicloPagamentoConveniadosProjection;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.RelatorioFaturamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelCicloPagamentoConveniadosService {

    private final RelatorioFaturamentoRepository relatorioFaturamentoRepository;

    public RelCicloPagamentoConveniadosDTO buscarDadosCicloPorId(Long idCicloPagamentoVenda) throws ExceptionCustomizada {
        try {
            Optional<RelCicloPagamentoConveniadosProjection> dados = 
                relatorioFaturamentoRepository.findDadosCicloConveniados(idCicloPagamentoVenda);
            
            if (dados.isEmpty()) {
                throw new ExceptionCustomizada("Dados do ciclo de pagamento não encontrados para o ID: " + idCicloPagamentoVenda);
            }
            
            return converterProjectionParaDTO(dados.get());
            
        } catch (Exception e) {
            log.error("Erro ao buscar dados do ciclo de pagamento", e);
            throw new ExceptionCustomizada("Erro ao buscar dados do ciclo: " + e.getMessage());
        }
    }

    private RelCicloPagamentoConveniadosDTO converterProjectionParaDTO(RelCicloPagamentoConveniadosProjection projection) {
        return RelCicloPagamentoConveniadosDTO.builder()
            .idCicloPagamentoVenda(projection.getIdCicloPagamentoVenda())
            .anoMes(projection.getAnoMes())
            .dtCriacao(projection.getDtCriacao())
            .dtAlteracao(projection.getDtAlteracao())
            .dtPagamento(projection.getDtPagamento())
            .observacao(projection.getObservacao())
            .vlrCicloBruto(projection.getVlrCicloBruto())
            .vlrTaxaSecundaria(projection.getVlrTaxaSecundaria())
            .vlrLiquido(projection.getVlrLiquido())
            .vlrTaxaExtraPercentual(projection.getVlrTaxaExtraPercentual())
            .vlrTaxaExtraValor(projection.getVlrTaxaExtraValor())
            .vlrLiquidoPagamento(projection.getVlrLiquidoPagamento())
            .vlrTaxasFaixaVendas(projection.getVlrTaxasFaixaVendas())
            .idConveniados(projection.getIdConveniados())
            .site(projection.getSite())
            .anoMesUltinoFechamento(projection.getAnoMesUltinoFechamento())
            .idTaxasFaixaVendas(projection.getIdTaxasFaixaVendas())
            .descricaoTaxa(projection.getDescricaoTaxa())
            .bairro(projection.getBairro())
            .cep(projection.getCep())
            .cidade(projection.getCidade())
            .email(projection.getEmail())
            .logradoro(projection.getLogradoro())
            .numero(projection.getNumero())
            .telefone(projection.getTelefone())
            .uf(projection.getUf())
            .cnpj(projection.getCnpj())
            .razaoSocial(projection.getRazaoSocial())
            .build();
    }

    public RelCicloPagamentoConveniadosDTO buscarDadosCicloPorConveniadoEMes(Long idConveniados, String anoMes) throws ExceptionCustomizada {
        try {
            // Implementação para buscar por conveniado e mês (se necessário)
            throw new ExceptionCustomizada("Implementação requer consulta adicional para obter ID do ciclo");
            
        } catch (Exception e) {
            log.error("Erro ao buscar dados do ciclo por conveniado e mês", e);
            throw new ExceptionCustomizada("Erro ao buscar dados do ciclo: " + e.getMessage());
        }
    }
}