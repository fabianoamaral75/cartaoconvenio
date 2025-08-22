package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.awt.Color; // Para cores (usado em adicionarCelulaCabecalho)
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal; // Já deve estar importado, mas confirmando\
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat; // Para formatação de datas
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
//NOVOS IMPORTS
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.draw.LineSeparator;


import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.LayoutModelo;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusRelatorioFaturamento;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.CicloPagamentoVenda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.RelatorioFaturamentoConveniado;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RelCicloPagamentoConveniadosDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RelCicloPagamentoConveniadosTaxasDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RelCicloPagamentoVendasDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RelCicloPagamentoVendasItemProdutosDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RelatorioFaturamentoRequestDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RelatorioFaturamentoResponseDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.CicloPagamentoVendaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.RelatorioFaturamentoRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/////////////////////////

/**
 * Service responsável pela lógica de negócio de relatórios de faturamento
 * Inclui geração de PDF, armazenamento e recuperação de relatórios
 */
@Service // Indica que esta classe é um service do Spring
@RequiredArgsConstructor // Gera construtor com injeção de dependências
@Slf4j // Habilita logging via SLF4J
public class RelatorioFaturamentoService {

    // Repositório para operações de banco de dados de relatórios
    private final RelatorioFaturamentoRepository relatorioFaturamentoRepository;
    // Repositório para operações com ciclos de pagamento
    private final CicloPagamentoVendaRepository cicloPagamentoVendaRepository;
    // Service para dados do ciclo de conveniados
    private final RelCicloPagamentoConveniadosService relCicloService;
    // Service para taxas do ciclo
    private final RelCicloPagamentoConveniadosTaxasService relTaxasService;
    // Service para vendas do ciclo
    private final RelCicloPagamentoVendasService relVendasService;
    // Service para itens de produtos das vendas
    private final RelCicloPagamentoVendasItensService relVendasItensService;
    
    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");
    private static final NumberFormat NF = NumberFormat.getCurrencyInstance(PT_BR);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");


    /**
     * Gera um novo relatório de faturamento baseado nos dados fornecidos
     * @param requestDTO DTO com parâmetros para geração do relatório
     * @return DTO com informações do relatório gerado
     * @throws ExceptionCustomizada Em caso de erro durante a geração
     */
    @Transactional // Garante que todas as operações de banco sejam atômicas
    public RelatorioFaturamentoResponseDTO gerarRelatorioFaturamento(RelatorioFaturamentoRequestDTO requestDTO) 
            throws ExceptionCustomizada {
        
        try {
            // Verifica se já existe relatório atual para o conveniado e mês
            Optional<RelatorioFaturamentoConveniado> relatorioExistente = 
                relatorioFaturamentoRepository.findByIdConveniadosAndAnoMesAndStatus(
                    requestDTO.getIdConveniados(), // ID do conveniado
                    requestDTO.getAnoMes(), // Mês/ano de referência (formato yyyyMM)
                    StatusRelatorioFaturamento.ATUAL); // Status "ATUAL"

            // Se existir relatório atual, marca como desatualizado
            if (relatorioExistente.isPresent()) {
                RelatorioFaturamentoConveniado relatorio = relatorioExistente.get();
                relatorio.setStatus(StatusRelatorioFaturamento.REL_DESATUALIZADO); // Altera status
                relatorioFaturamentoRepository.save(relatorio); // Salva alteração
            }

            // Busca dados principais do ciclo de pagamento
            RelCicloPagamentoConveniadosDTO dadosCiclo = 
                relCicloService.buscarDadosCicloPorId(requestDTO.getIdCicloPagamentoVenda());

            // Busca taxas extras aplicadas ao ciclo
            List<RelCicloPagamentoConveniadosTaxasDTO> taxas = 
                relTaxasService.buscarTaxasPorCiclo(requestDTO.getIdCicloPagamentoVenda());

            // Busca vendas incluídas no ciclo
            List<RelCicloPagamentoVendasDTO> vendas = 
                relVendasService.buscarVendasPorCiclo(requestDTO.getIdCicloPagamentoVenda());

            // Busca itens de produtos das vendas do ciclo
            List<RelCicloPagamentoVendasItemProdutosDTO> itensProdutos = 
                relVendasItensService.buscarItensProdutosPorCiclo(requestDTO.getIdCicloPagamentoVenda());

            // Gera o PDF com todos os dados coletados
            byte[] pdfBytes = gerarPDF(dadosCiclo, taxas, vendas, itensProdutos);
            // Converte bytes do PDF para Base64 para armazenamento
            String base64 = java.util.Base64.getEncoder().encodeToString(pdfBytes);

            // Busca entidade do ciclo de pagamento para relacionamento
            CicloPagamentoVenda ciclo = cicloPagamentoVendaRepository.findById(requestDTO.getIdCicloPagamentoVenda())
                .orElseThrow(() -> new ExceptionCustomizada("Ciclo de pagamento não encontrado"));

            // Cria nova entidade de relatório
            RelatorioFaturamentoConveniado relatorio = new RelatorioFaturamentoConveniado();
            relatorio.setCicloPagamentoVenda( ciclo                                                      ); // Relaciona com ciclo
            relatorio.setIdConveniados      ( requestDTO.getIdConveniados()                              ); // ID do conveniado
            relatorio.setAnoMes             ( requestDTO.getAnoMes()                                     ); // Período de referência
            relatorio.setArquivoTipo        ( "application/pdf"                                          );
            relatorio.setNomeArquivo        ( "relatorio_faturamento_" + requestDTO.getAnoMes() + ".pdf" ); // Nome do arquivo
            relatorio.setConteudoBase64     ( base64                                                     ); // Conteúdo do PDF em Base64
            relatorio.setTamanhoBytes       ( (long) pdfBytes.length                                     ); // Tamanho em bytes
            relatorio.setStatus             ( StatusRelatorioFaturamento.ATUAL                           ); // Status como atual
            relatorio.setObservacao         ( "Relatório gerado automaticamente"                         ); // Observação padrão

            // Salva relatório no banco de dados
            relatorio = relatorioFaturamentoRepository.save(relatorio);

            // Converte entidade para DTO de resposta
            return toResponseDTO(relatorio);

        } catch (Exception e) {
            // Log do erro detalhado
            log.error("Erro ao gerar relatório de faturamento", e);
            // Lança exceção customizada com mensagem do erro original
            throw new ExceptionCustomizada("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    /**
     * Busca um relatório existente pelo ID
     * @param idRelatorio ID do relatório a ser buscado
     * @return DTO com informações do relatório
     * @throws ExceptionCustomizada Se o relatório não for encontrado
     */
    public RelatorioFaturamentoResponseDTO buscarRelatorioPorId(Long idRelatorio) throws ExceptionCustomizada {
        // Busca relatório pelo ID ou lança exceção se não encontrado
        RelatorioFaturamentoConveniado relatorio = relatorioFaturamentoRepository.findById(idRelatorio)
            .orElseThrow(() -> new ExceptionCustomizada("Relatório não encontrado"));
        // Converte entidade para DTO
        return toResponseDTO(relatorio);
    }

    /**
     * Realiza download do relatório em formato PDF
     * @param idRelatorio ID do relatório a ser baixado
     * @return Bytes do arquivo PDF
     * @throws ExceptionCustomizada Se o relatório não for encontrado
     */
    public byte[] downloadRelatorio(Long idRelatorio) throws ExceptionCustomizada {
        // Busca relatório pelo ID
        RelatorioFaturamentoConveniado relatorio = relatorioFaturamentoRepository.findById(idRelatorio)
            .orElseThrow(() -> new ExceptionCustomizada("Relatório não encontrado"));
        
        // Decodifica Base64 para obter bytes do PDF
        return java.util.Base64.getDecoder().decode(relatorio.getConteudoBase64());
    }

    /**
     * Converte entidade RelatorioFaturamentoConveniado para DTO de resposta
     * @param relatorio Entidade a ser convertida
     * @return DTO com dados formatados para resposta
     */
    private RelatorioFaturamentoResponseDTO toResponseDTO(RelatorioFaturamentoConveniado relatorio) {
        return RelatorioFaturamentoResponseDTO.builder()
            .idRelatorioFaturamentoConveniado(relatorio.getIdRelatorioFaturamentoConveniado()) // ID do relatório
            .idCicloPagamentoVenda(relatorio.getCicloPagamentoVenda().getIdCicloPagamentoVenda()) // ID do ciclo relacionado
            .idConveniados(relatorio.getIdConveniados()) // ID do conveniado
            .anoMes(relatorio.getAnoMes()) // Período de referência
            .nomeArquivo(relatorio.getNomeArquivo()) // Nome do arquivo
            .tamanhoBytes(relatorio.getTamanhoBytes()) // Tamanho em bytes
            .status(relatorio.getStatus()) // Status do relatório
            .dtCriacao(relatorio.getDtCriacao()) // Data de criação
            .observacao(relatorio.getObservacao()) // Observações
            .downloadUrl("/uaitag_cartao_convenio/api/relatorios-faturamento/download/" + relatorio.getIdRelatorioFaturamentoConveniado()) // URL para download
            .build();
    }

    /**
     * Gera PDF com todos os dados do relatório de faturamento
     * @param dadosCiclo Dados principais do ciclo
     * @param taxas Lista de taxas aplicadas
     * @param vendas Lista de vendas do período
     * @param itensProdutos Lista de itens de produtos das vendas
     * @return Bytes do PDF gerado
     * @throws DocumentException Em caso de erro na geração do documento
     * @throws IOException Em caso de erro de I/O
     */
    private byte[] gerarPDF(RelCicloPagamentoConveniadosDTO dadosCiclo, 
                           List<RelCicloPagamentoConveniadosTaxasDTO> taxas,
                           List<RelCicloPagamentoVendasDTO> vendas,
                           List<RelCicloPagamentoVendasItemProdutosDTO> itensProdutos) 
            throws DocumentException, IOException {
        
        // Cria novo documento PDF em formato A4 paisagem (landscape)
        Document document = new Document(PageSize.A4.rotate());

        // OutputStream para capturar bytes gerados
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Associa writer ao documento e output stream
        PdfWriter.getInstance(document, baos);

        // Abre documento para edição
        document.open();

        // Adiciona logo da empresa
        adicionarLogo(document, dadosCiclo.getRazaoSocial());

        // Adiciona cabeçalho com dados da empresa e período
        adicionarCabecalho(document, dadosCiclo);

        // Adiciona seção de resumo financeiro
        adicionarResumoFaturamento(document, dadosCiclo);

        // Adiciona seção de taxas extras aplicadas
        adicionarTaxasExtras(document, taxas);

        // Adiciona seção de vendas do período
        adicionarVendas(document, vendas);

        // Adiciona seção de itens de produtos (detalhamento das vendas)
        adicionarItensProdutos(document, itensProdutos);

        // Fecha documento (finaliza geração)
        document.close();
        // Retorna bytes do PDF gerado
        return baos.toByteArray();
    }

    /**
     * Adiciona seção de itens de produtos ao PDF
     * @param document Documento PDF sendo construído
     * @param itensProdutos Lista de itens de produtos a serem adicionados
     * @throws DocumentException Em caso de erro na adição ao documento
     */
    private void adicionarItensProdutos(Document document, List<RelCicloPagamentoVendasItemProdutosDTO> itensProdutos) 
            throws DocumentException {
        
        // Se não houver itens, não adiciona seção
        if (itensProdutos == null || itensProdutos.isEmpty()) {
            return;
        }

        // Configura fonte para título da seção
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        // Adiciona título da seção
        document.add(new Paragraph("ITENS DE PRODUTOS DAS VENDAS", fontTitulo));
        // Adiciona espaço em branco
        document.add(new Paragraph(" "));

        // Cria tabela com 6 colunas
        PdfPTable table = new PdfPTable(6);
        // Configura tabela para usar 100% da largura da página
        table.setWidthPercentage(100);

        // Adiciona cabeçalhos das colunas
        adicionarCelulaCabecalho(table, "Produto");
        adicionarCelulaCabecalho(table, "Quantidade");
        adicionarCelulaCabecalho(table, "Valor Unitário");
        adicionarCelulaCabecalho(table, "Valor Total");
        adicionarCelulaCabecalho(table, "Valor Produto");
        adicionarCelulaCabecalho(table, "ID Venda");

        // Inicializa total geral
        BigDecimal totalGeral = BigDecimal.ZERO;

        // Itera sobre todos os itens de produtos
        for (RelCicloPagamentoVendasItemProdutosDTO item : itensProdutos) {
            // Adiciona células com dados do item
            adicionarCelula(table, item.getProduto()                    , false); // Nome do produto
            adicionarCelula(table, item.getQtyItem().toString()         , false); // Quantidade
            adicionarCelula(table, formatarMoeda(item.getVlrUnitario()) , false); // Valor unitário
            adicionarCelula(table, formatarMoeda(item.getVlrTotalItem()), false); // Valor total
            adicionarCelula(table, formatarMoeda(item.getVlrProduto())  , false); // Valor do produto
            adicionarCelula(table, item.getIdVenda().toString()         , false); // ID da venda

            // Acumula total geral
            if (item.getVlrTotalItem() != null) {
                totalGeral = totalGeral.add(item.getVlrTotalItem());
            }
        }

        // Adiciona linha de total geral
        adicionarCelula(table, "TOTAL GERAL:", true); // Label
        adicionarCelula(table, "", true); // Espaço vazio
        adicionarCelula(table, "", true); // Espaço vazio
        adicionarCelula(table, formatarMoeda(totalGeral), true); // Valor total formatado
        adicionarCelula(table, "", true); // Espaço vazio
        adicionarCelula(table, "", true); // Espaço vazio

        // Adiciona tabela ao documento
        document.add(table);
        // Adiciona espaço em branco após tabela
        document.add(new Paragraph(" "));
    }

 // ========== MÉTODOS AUXILIARES PARA CONSTRUÇÃO DO PDF ==========

    /**
     * Adiciona logo da empresa ao documento PDF
     * @param document Documento PDF sendo construído
     * @throws DocumentException Em caso de erro na adição da imagem
     * @throws IOException Em caso de erro ao carregar a imagem
     */
    private void adicionarLogo(Document document, String razaoSocial) throws DocumentException, IOException {
        try {
            // Usa ClassPathResource para acessar a logo do classpath
            ClassPathResource logoResource = new ClassPathResource("static/images/LogoPreta.png");
            
            // Verifica se o arquivo existe
            if (logoResource.exists()) {
                Image logo = Image.getInstance(logoResource.getURL());
                // Redimensiona a logo para caber no documento
                logo.scaleToFit(100, 60);
                // Centraliza a logo
                logo.setAlignment(Image.ALIGN_CENTER);
                // Adiciona a logo ao documento
                document.add(logo);
                // Adiciona espaço após a logo
                document.add(new Paragraph(" "));
            } else {
                throw new FileNotFoundException("Logo não encontrada no classpath");
            }
        } catch (Exception e) {
            // Loga aviso se logo não for encontrada, mas continua sem ela
            log.warn("Logo não encontrada, continuando sem logo: {}", e.getMessage());
            // Adiciona nome da empresa como fallback
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph titulo = new Paragraph(razaoSocial, fontTitulo);
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph(" "));
        }
    }
    /**
     * Adiciona cabeçalho com dados da empresa e período ao PDF
     * @param document Documento PDF sendo construído
     * @param dadosCiclo Dados do ciclo para preenchimento do cabeçalho
     * @throws DocumentException Em caso de erro na adição ao documento
     */
    private void adicionarCabecalho(Document document, RelCicloPagamentoConveniadosDTO dadosCiclo) 
            throws DocumentException {
        
        Font fontNormal  = FontFactory.getFont(FontFactory.HELVETICA     , 10);
        Font fontNegrito = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        
        String end = dadosCiclo.getLogradoro() + ", " + dadosCiclo.getNumero() + " - " + dadosCiclo.getBairro() + " - " + dadosCiclo.getCidade() + " - " + dadosCiclo.getCep();
        
        // Dados da empresa
        Paragraph empresa = new Paragraph();
        empresa.add(new Chunk("CNPJ: ", fontNegrito));
        empresa.add(new Chunk(FuncoesUteis.formatar( dadosCiclo.getCnpj() ), fontNormal));
        empresa.add(new Chunk(" - ", fontNormal));
        empresa.add(new Chunk("ENDEREÇO: ", fontNegrito));
        empresa.add(new Chunk(end, fontNormal));
        document.add(empresa);
        
        Paragraph contato = new Paragraph();
        contato.add(new Chunk("TELEFONE: ", fontNegrito));
        contato.add(new Chunk(dadosCiclo.getTelefone(), fontNormal));
        contato.add(new Chunk(" - ", fontNormal));
        contato.add(new Chunk("EMAIL: ", fontNegrito));
        contato.add(new Chunk(dadosCiclo.getEmail(), fontNormal));
        document.add(contato);
        
        // Período do relatório
        if (dadosCiclo != null && dadosCiclo.getAnoMes() != null ) {
            Paragraph periodo = new Paragraph();
            periodo.add(new Chunk("PERÍODO: ", fontNegrito));
            periodo.add(new Chunk( FuncoesUteis.getPrimeiroDiaMes(dadosCiclo.getAnoMes()) + " a " + FuncoesUteis.getUltimoDiaMes(dadosCiclo.getAnoMes()), fontNormal));
            document.add(periodo);
        }
        
        document.add(new Paragraph(" "));
        document.add(new Paragraph("RELATÓRIO DE FATURAMENTO", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        document.add(new Paragraph(" "));
    }

    /**
     * Adiciona seção de resumo financeiro ao PDF
     * @param document Documento PDF sendo construído
     * @param dadosCiclo Dados do ciclo para preenchimento do resumo
     * @throws DocumentException Em caso de erro na adição ao documento
     */
    private void adicionarResumoFaturamento(Document document, RelCicloPagamentoConveniadosDTO dadosCiclo) 
            throws DocumentException {
        
        if (dadosCiclo == null) return;
        
        Font fontTitulo  = FontFactory.getFont( FontFactory.HELVETICA_BOLD, 12 );
        Font fontNegrito = FontFactory.getFont( FontFactory.HELVETICA_BOLD, 10 );
        Font fontNormal  = FontFactory.getFont( FontFactory.HELVETICA     , 10 );
        
        document.add(new Paragraph("RESUMO DO FATURAMENTO", fontTitulo));
        document.add(new Paragraph(" "));
        
        // Tabela com 2 colunas para o resumo
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 2});
        
        // Valores principais
        adicionarLinhaResumo(table, "Valor Total Bruto:"      , formatarMoeda( dadosCiclo.getVlrCicloBruto()       ), fontNegrito, fontNormal );
        adicionarLinhaResumo(table, "Valor Total Líquido:"    , formatarMoeda( dadosCiclo.getVlrLiquido()          ), fontNegrito, fontNormal );
        adicionarLinhaResumo(table, "Valor Líquido Pagamento:", formatarMoeda( dadosCiclo.getVlrLiquidoPagamento() ), fontNegrito, fontNormal );
        
        // Linha separadora
        adicionarLinhaResumo(table, " ", " ", fontNegrito, fontNormal);
        
        // Taxas e retenções (baseado no exemplo do PDF)
        adicionarLinhaResumo(table, "Valor Taxa Extra Percentual:" , formatarMoeda( dadosCiclo.getVlrTaxaExtraPercentual() ), fontNegrito, fontNormal );
        adicionarLinhaResumo(table, "Valor Taxa Extra:"            , formatarMoeda( dadosCiclo.getVlrTaxaExtraValor()      ), fontNegrito, fontNormal );
        adicionarLinhaResumo(table, "Valor Taxa Secundaria:"       , formatarMoeda( dadosCiclo.getVlrTaxaSecundaria()      ), fontNegrito, fontNormal );
        adicionarLinhaResumo(table, "Valor Taxas Faixa Vendas:"    , formatarMoeda( dadosCiclo.getVlrTaxasFaixaVendas()    ), fontNegrito, fontNormal );
        adicionarLinhaResumo(table, "Retenção IR - Serviço (4,8%):", formatarMoeda(BigDecimal.ZERO), fontNegrito, fontNormal);
        
        // Linha separadora
        adicionarLinhaResumo(table, " ", " ", fontNegrito, fontNormal);
        
        // Valor líquido
        adicionarLinhaResumo(table, "VALOR LÍQUIDO:", formatarMoeda( dadosCiclo.getVlrLiquidoPagamento() ), 
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11), 
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11));
        
        document.add(table);
        document.add(new Paragraph(" "));
        
        // Informação adicional
        Paragraph info = new Paragraph();
        info.add(new Chunk("Não Optante pelo Simples Nacional - Retenção de IR", fontNormal));
        document.add(info);
        document.add(new Paragraph(" "));
    }

    /**
     * Adiciona seção de taxas extras ao PDF
     * @param document Documento PDF sendo construído
     * @param taxas Lista de taxas extras a serem listadas
     * @throws DocumentException Em caso de erro na adição ao documento
     */
    private void adicionarTaxasExtras(Document document, List<RelCicloPagamentoConveniadosTaxasDTO> taxas) 
            throws DocumentException {
        
        if (taxas == null || taxas.isEmpty()) {
            return;
        }
        
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        document.add(new Paragraph("TAXAS EXTRAS APLICADAS", fontTitulo));
        document.add(new Paragraph(" "));
        
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        
        adicionarCelulaCabecalho(table, "Descrição da Taxa");
        adicionarCelulaCabecalho(table, "Período");
        adicionarCelulaCabecalho(table, "Valor");
        
        for (RelCicloPagamentoConveniadosTaxasDTO taxa : taxas) {
            adicionarCelula( table, taxa.getDescricaoTaxa()           , false );
            adicionarCelula( table, taxa.getDescTipoPeriodo()         , false );
            adicionarCelula( table, formatarMoeda(taxa.getValorTaxa()), false );
        }
        
        document.add(table);
        document.add(new Paragraph(" "));
    }

    /**
     * Adiciona seção de vendas ao PDF
     * @param document Documento PDF sendo construído
     * @param vendas Lista de vendas a serem listadas
     * @throws DocumentException Em caso de erro na adição ao documento
     */
    private void adicionarVendas(Document document, List<RelCicloPagamentoVendasDTO> vendas) 
            throws DocumentException {
        
        if (vendas == null || vendas.isEmpty()) {
            return;
        }
        
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        document.add(new Paragraph("VENDAS DO PERÍODO", fontTitulo));
        document.add(new Paragraph(" "));
        
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 3, 3, 3, 3, 3, 3});
        
        adicionarCelulaCabecalho(table, "Id Venda"        );
        adicionarCelulaCabecalho(table, "Data Venda"      );
        adicionarCelulaCabecalho(table, "Valor Taxa"      );
        adicionarCelulaCabecalho(table, "Percentual Taxa" );
        adicionarCelulaCabecalho(table, "Tipo Taxa"       );
        adicionarCelulaCabecalho(table, "Valor Venda"     );
        adicionarCelulaCabecalho(table, "Entidade"        );

        
        BigDecimal totalVlrCalcTaxaConv = BigDecimal.ZERO;
        BigDecimal totalVlrVenda        = BigDecimal.ZERO;
        
        for (RelCicloPagamentoVendasDTO venda : vendas) {
            adicionarCelula(table, venda.getIdVenda().toString()                     , false );
            adicionarCelula(table, formatarData(venda.getDtVenda() )                 , false );
            adicionarCelula(table, formatarMoeda(venda.getValorCalcTaxaConveniado() ), false );
            adicionarCelula(table, FuncoesUteis.formatarPercentual( venda.getTaxa() ), false );
            adicionarCelula(table, venda.getTipo_taxa() == true ? "Taxa Expecifica Con. para Ent.": "Taxa Default", false);
            adicionarCelula(table, formatarMoeda(venda.getValorVenda())              , false );
            adicionarCelula(table, venda.getEntidade()                               , false );

            
            if (venda.getValorCalcTaxaConveniado() != null) totalVlrCalcTaxaConv = totalVlrCalcTaxaConv.add   ( venda.getValorCalcTaxaConveniado() );
            if (venda.getValorVenda()              != null) totalVlrVenda        = totalVlrVenda.add( venda.getValorVenda()              );
        }
        
        // Linha de totais
        adicionarCelula(table, "TOTAIS:"                          , true );
        adicionarCelula(table, ""                                 , true );
        adicionarCelula(table, formatarMoeda(totalVlrCalcTaxaConv), true );
        adicionarCelula(table, ""                                 , true );
        adicionarCelula(table, ""                                 , true );
        adicionarCelula(table, formatarMoeda(totalVlrVenda)       , true );
        adicionarCelula(table, ""                                 , true );
        
        document.add(table);
        document.add(new Paragraph(" "));
    }

    /**
     * Adiciona célula de cabeçalho à tabela
     * @param table Tabela onde a célula será adicionada
     * @param texto Texto do cabeçalho
     */
    private void adicionarCelulaCabecalho(PdfPTable table, String texto) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        com.lowagie.text.pdf.PdfPCell cell = new com.lowagie.text.pdf.PdfPCell(new Phrase(texto, font));
        cell.setBackgroundColor(new Color(220, 220, 220)); // Cinza claro
        cell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
        cell.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        table.addCell(cell);
    }

    /**
     * Adiciona célula normal à tabela
     * @param table Tabela onde a célula será adicionada
     * @param texto Texto da célula
     * @param negrito Se o texto deve ser em negrito
     */
    private void adicionarCelula(PdfPTable table, String texto, boolean negrito) {
        Font font = negrito ? 
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10) : 
            FontFactory.getFont(FontFactory.HELVETICA, 10);
        
        com.lowagie.text.pdf.PdfPCell cell = new com.lowagie.text.pdf.PdfPCell(new Phrase(texto, font));
        cell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_LEFT);
        cell.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        table.addCell(cell);
    }

    // ========== MÉTODOS AUXILIARES ADICIONAIS ==========

    /**
     * Adiciona linha de resumo financeiro
     */
    private void adicionarLinhaResumo(PdfPTable table, String label, String valor, Font fontLabel, Font fontValor) {
        com.lowagie.text.pdf.PdfPCell cellLabel = new com.lowagie.text.pdf.PdfPCell(new Phrase(label, fontLabel));
        cellLabel.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        cellLabel.setPadding(5);
        table.addCell(cellLabel);
        
        com.lowagie.text.pdf.PdfPCell cellValor = new com.lowagie.text.pdf.PdfPCell(new Phrase(valor, fontValor));
        cellValor.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        cellValor.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_RIGHT);
        cellValor.setPadding(5);
        table.addCell(cellValor);
    }

    /**
     * Formata data para exibição
     */
    private String formatarData(Date data) {
        if (data == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(data);
    }    
    
    

    /**
     * Formata valor monetário para exibição
     * @param valor Valor BigDecimal a ser formatado
     * @return String formatada como moeda brasileira (R$ 0,00)
     */
    private String formatarMoeda(BigDecimal valor) {
        // Se valor for nulo, retorna formato zero
        if (valor == null) return "R$ 0,00";
        // Formata com separador de milhar e decimal brasileiro
        return String.format("R$ %,.2f", valor);
    }
    
    
/***************************************************************************************************/    
/***************************************************************************************************/    
/***************************************************************************************************/    
/***************************************************************************************************/    
/***************************************************************************************************/    
    

    
    @Transactional
    public RelatorioFaturamentoResponseDTO gerarRelatorioFaturamentoPorTipoRel(RelatorioFaturamentoRequestDTO requestDTO) {
	
        Objects.requireNonNull(requestDTO                           , "requestDTO é obrigatório"            );
        Objects.requireNonNull(requestDTO.getIdCicloPagamentoVenda(), "idCicloPagamentoVenda é obrigatório" );
        Objects.requireNonNull(requestDTO.getIdConveniados()        , "idConveniados é obrigatório"         );
        Objects.requireNonNull(requestDTO.getAnoMes()               , "anoMes é obrigatório"                );
        
        LayoutModelo layout = requestDTO.getLayout() == null ? LayoutModelo.MODERNO :  LayoutModelo.valueOf(requestDTO.getLayout());

        // 1) BUSCAS
        RelCicloPagamentoConveniadosDTO dadosCiclo =
                relCicloService.buscarDadosCicloPorId(requestDTO.getIdCicloPagamentoVenda());

        List<RelCicloPagamentoConveniadosTaxasDTO> taxas =
                relTaxasService.buscarTaxasPorCiclo(requestDTO.getIdCicloPagamentoVenda());

        List<RelCicloPagamentoVendasDTO> vendas =
                relVendasService.buscarVendasPorCiclo(requestDTO.getIdCicloPagamentoVenda());

        List<RelCicloPagamentoVendasItemProdutosDTO> itensProdutos =
                relVendasItensService.buscarItensProdutosPorCiclo(requestDTO.getIdCicloPagamentoVenda());

        // 2) TOTAIS
        BigDecimal totalTaxasExtras = taxas.stream()
                .map(RelCicloPagamentoConveniadosTaxasDTO::getValorTaxa)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalValorCalcTaxaConveniado = vendas.stream()
                .map(RelCicloPagamentoVendasDTO::getValorCalcTaxaConveniado)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalValorCalcTaxaEntidade = vendas.stream()
                .map(RelCicloPagamentoVendasDTO::getValorCalcTaxaEntidade)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalValorVendas = vendas.stream()
                .map(RelCicloPagamentoVendasDTO::getValorVenda)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTaxaPercentual = vendas.stream()
                .map(RelCicloPagamentoVendasDTO::getTaxa)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3) GERA PDF
        byte[] pdf = buildPdf(layout, requestDTO, dadosCiclo, taxas, vendas, itensProdutos,
                totalTaxasExtras, totalValorCalcTaxaConveniado, totalValorCalcTaxaEntidade,
                totalValorVendas, totalTaxaPercentual);

        // 4) REGRAS DE NEGÓCIO – ATUALIZAÇÃO E PERSISTÊNCIA
        Optional<RelatorioFaturamentoConveniado> relatorioExistente =
                relatorioFaturamentoRepository.findByIdConveniadosAndAnoMesAndStatus(
                        requestDTO.getIdConveniados(),
                        requestDTO.getAnoMes(),
                        StatusRelatorioFaturamento.ATUAL);

        relatorioExistente.ifPresent(rel -> {
            rel.setStatus(StatusRelatorioFaturamento.REL_DESATUALIZADO);
            relatorioFaturamentoRepository.save(rel);
        });
        
        // 5) Busca entidade do ciclo de pagamento para relacionamento
        CicloPagamentoVenda ciclo = cicloPagamentoVendaRepository.findById(requestDTO.getIdCicloPagamentoVenda())
            .orElseThrow(() -> new ExceptionCustomizada("Ciclo de pagamento não encontrado"));
        
        // 6) Converte para Base64
        String base64 = Base64.getEncoder().encodeToString(pdf);
 
        RelatorioFaturamentoConveniado relatorio = new RelatorioFaturamentoConveniado();
        relatorio.setCicloPagamentoVenda( ciclo                              ); // Relaciona com ciclo
        relatorio.setIdConveniados      ( requestDTO.getIdConveniados()      ); // ID do conveniado
        relatorio.setAnoMes             ( requestDTO.getAnoMes()             ); // Período de referência
        relatorio.setNomeArquivo        ( buildFileName(requestDTO)          ); // Nome do arquivo
        relatorio.setConteudoBase64     ( base64                             ); // Conteúdo do PDF em Base64
        relatorio.setArquivoTipo        ( "application/pdf"                  ); // Tipo de arquivo gerado
        relatorio.setTamanhoBytes       ( (long) pdf.length                  ); // Tamanho em bytes
        relatorio.setStatus             ( StatusRelatorioFaturamento.ATUAL   ); // Status como atual
        relatorio.setObservacao         ( "Relatório gerado automaticamente" ); // Observação padrão


        // 7) Salva relatório no banco de dados
        relatorio = relatorioFaturamentoRepository.save(relatorio);

        // 8) Converte entidade para DTO de resposta
        return toResponseDTO(relatorio);

    }


    // =========================
    //   CONSTRUÇÃO DO PDF
    // =========================
/*    
    private byte[] buildPdf(LayoutModelo layout,
                            RelatorioFaturamentoRequestDTO req,
                            RelCicloPagamentoConveniadosDTO dados,
                            List<RelCicloPagamentoConveniadosTaxasDTO> taxas,
                            List<RelCicloPagamentoVendasDTO> vendas,
                            List<RelCicloPagamentoVendasItemProdutosDTO> itens,
                            BigDecimal totalTaxasExtras,
                            BigDecimal totalValorCalcTaxaConveniado,
                            BigDecimal totalValorCalcTaxaEntidade,
                            BigDecimal totalValorVendas,
                            BigDecimal totalTaxaPercentual) {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
        	// Cria novo documento PDF em formato A4 paisagem (landscape)
            Document doc = new Document(PageSize.A4.rotate(), 30, 30, 36, 36);
            PdfWriter.getInstance(doc, baos);
            doc.open();

            switch (layout) {
                case MODERNO -> buildPdfModerno(doc, req, dados, taxas, vendas, itens,
                        totalTaxasExtras, totalValorCalcTaxaConveniado, totalValorCalcTaxaEntidade,
                        totalValorVendas, totalTaxaPercentual);
                case TABELADO -> buildPdfTabelado(doc, req, dados, taxas, vendas, itens,
                        totalTaxasExtras, totalValorCalcTaxaConveniado, totalValorCalcTaxaEntidade,
                        totalValorVendas, totalTaxaPercentual);
                case MINIMAL -> buildPdfMinimal(doc, req, dados, taxas, vendas, itens,
                        totalTaxasExtras, totalValorCalcTaxaConveniado, totalValorCalcTaxaEntidade,
                        totalValorVendas, totalTaxaPercentual);
            }

            doc.close();
            return baos.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar recursos do PDF", e);
        }
    }
*/
    
    private byte[] buildPdf(LayoutModelo layout,
            RelatorioFaturamentoRequestDTO req,
            RelCicloPagamentoConveniadosDTO dados,
            List<RelCicloPagamentoConveniadosTaxasDTO> taxas,
            List<RelCicloPagamentoVendasDTO> vendas,
            List<RelCicloPagamentoVendasItemProdutosDTO> itens,
            BigDecimal totalTaxasExtras,
            BigDecimal totalValorCalcTaxaConveniado,
            BigDecimal totalValorCalcTaxaEntidade,
            BigDecimal totalValorVendas,
            BigDecimal totalTaxaPercentual) {

    	try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			// A4 paisagem
			Document doc = new Document(PageSize.A4.rotate(), 30, 30, 36, 36);
			
			// >>> Writer + rodapé em todas as páginas
			PdfWriter writer = PdfWriter.getInstance(doc, baos);
			Image logoFooter = null;
			try {
				logoFooter = tryLoadLogo(); // tenta carregar a logo do classpath
			} catch (Exception ignore) {}
			
			writer.setPageEvent(new UaiTagFooter(logoFooter)); // remova esta linha se NÃO quiser rodapé
			
			doc.open();
			
			switch (layout) {
			case MODERNO -> buildPdfModerno(doc, req, dados, taxas, vendas, itens,
			        						totalTaxasExtras, totalValorCalcTaxaConveniado, totalValorCalcTaxaEntidade,
			        						totalValorVendas, totalTaxaPercentual);
			case TABELADO -> buildPdfTabelado(doc, req, dados, taxas, vendas, itens,
			        						  totalTaxasExtras, totalValorCalcTaxaConveniado, totalValorCalcTaxaEntidade,
			        						  totalValorVendas, totalTaxaPercentual);
			case MINIMAL -> buildPdfMinimal(doc, req, dados, taxas, vendas, itens,
			        						totalTaxasExtras, totalValorCalcTaxaConveniado, totalValorCalcTaxaEntidade,
			        						totalValorVendas, totalTaxaPercentual);
			}
			
			doc.close();
			return baos.toByteArray();
		
		} catch (DocumentException e) {
			throw new RuntimeException("Erro ao gerar PDF", e);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao carregar recursos do PDF", e);
		}
	}
    
    
 // Bloco de assinatura ao final do relatório
    private void addAssinaturaUaiTag(Document doc) throws DocumentException, IOException {
    	
        Paragraph titulo = new Paragraph("ASSINATURA", font(11, Font.BOLD, null));
        titulo.setSpacingBefore(18f);
        titulo.setSpacingAfter(8f);
        doc.add(titulo);

        // Linha divisória sutil
        LineSeparator ls = new LineSeparator(0.5f, 100f, new Color(210,210,210), Element.ALIGN_CENTER, 0);
        doc.add(ls);
        doc.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(new float[]{25, 75});
        table.setWidthPercentage(100);

        // Coluna 1: logo
        Image logo = null;
        try {
            logo = tryLoadLogo();
        } catch (Exception ignore) {}

        if (logo != null) {
            logo.scaleToFit(120, 40);
            PdfPCell imgCell = new PdfPCell(logo, false);
            imgCell.setBorder(Rectangle.NO_BORDER);
            imgCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            imgCell.setPaddingTop(4f);
            table.addCell(imgCell);
        } else {
            PdfPCell empty = new PdfPCell(new Phrase(""));
            empty.setBorder(Rectangle.NO_BORDER);
            table.addCell(empty);
        }

        // Coluna 2: linha para assinatura + informações
        PdfPTable assinatura = new PdfPTable(1);
        assinatura.setWidthPercentage(100);

        // Linha horizontal para assinatura
        PdfPCell linhaAss = new PdfPCell(new Phrase(" "));
        linhaAss.setBorder(Rectangle.BOTTOM);
        linhaAss.setBorderWidthBottom(1.0f);
        linhaAss.setFixedHeight(24f);
        linhaAss.setPadding(0f);
        assinatura.addCell(linhaAss);

        PdfPCell nome = new PdfPCell(new Phrase("UaiTag – Cartão Convênio", font(10, Font.BOLD, null)));
        nome.setBorder(Rectangle.NO_BORDER);
        nome.setPaddingTop(6f);
        assinatura.addCell(nome);

        PdfPCell cargo = new PdfPCell(new Phrase("Representante Legal", font(9, Font.NORMAL, null)));
        cargo.setBorder(Rectangle.NO_BORDER);
        cargo.setPaddingTop(2f);
        assinatura.addCell(cargo);

        // Data do dia
        PdfPCell data = new PdfPCell(new Phrase("Data: " + SDF.format(new Date()), font(9, Font.NORMAL, null)));
        data.setBorder(Rectangle.NO_BORDER);
        data.setPaddingTop(2f);
        assinatura.addCell(data);

        PdfPCell blocoAss = new PdfPCell(assinatura);
        blocoAss.setBorder(Rectangle.NO_BORDER);
        table.addCell(blocoAss);

        doc.add(table);
    }

    /* ********************************************************* */ 
    /*                                                           */
    /* Rodapé em todas as páginas – adicione esta classe interna */
    /*  - Rodapé com identidade visual em todas as páginas       */
    /*                                                           */
    /* ********************************************************* */ 
    public static class UaiTagFooter extends PdfPageEventHelper {
        private final Image logo;

        public UaiTagFooter(Image logo) {
            this.logo = logo;
            if (this.logo != null) {
                this.logo.scaleToFit(60, 20);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Rectangle page = document.getPageSize();

            // Faixa de fundo do rodapé
            float height = 24f;
            cb.saveState();
            cb.setColorFill(new Color(246, 246, 246));
            cb.rectangle(page.getLeft(), page.getBottom(), page.getWidth(), height);
            cb.fill();
            cb.restoreState();

            try {
                PdfPTable footer = new PdfPTable(new float[]{15, 85});
                footer.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());

                PdfPCell logoCell;
                if (logo != null) {
                    logoCell = new PdfPCell(logo, false);
                } else {
                    logoCell = new PdfPCell(new Phrase(""));
                }
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                logoCell.setPadding(0f);

                PdfPCell textCell = new PdfPCell(new Phrase(
                    "UaiTag • Relatório de Prestação de Contas • Página " + writer.getPageNumber(),
                    FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, Color.DARK_GRAY)
                ));
                textCell.setBorder(Rectangle.NO_BORDER);
                textCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                textCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                textCell.setPaddingRight(2f);

                footer.addCell(logoCell);
                footer.addCell(textCell);

                footer.writeSelectedRows(
                    0, -1,
                    document.leftMargin(),
                    document.bottomMargin() - 4f,
                    writer.getDirectContent()
                );
            } catch (Exception ignored) {}
        }
    }
    
    
/**/    
    
    
    
    // ===== Layout: MODERNO ====================================================
    private void buildPdfModerno(Document doc,
                                 RelatorioFaturamentoRequestDTO req,
                                 RelCicloPagamentoConveniadosDTO dados,
                                 List<RelCicloPagamentoConveniadosTaxasDTO> taxas,
                                 List<RelCicloPagamentoVendasDTO> vendas,
                                 List<RelCicloPagamentoVendasItemProdutosDTO> itens,
                                 BigDecimal totalTaxasExtras,
                                 BigDecimal totalValorCalcTaxaConveniado,
                                 BigDecimal totalValorCalcTaxaEntidade,
                                 BigDecimal totalValorVendas,
                                 BigDecimal totalTaxaPercentual) throws DocumentException, IOException {

        Image logo = tryLoadLogo(); // opcional
        if (logo != null) {
            logo.scaleToFit(120, 60);
            logo.setAlignment(Image.LEFT);
            doc.add(logo);
        }

        Paragraph titulo = p("PRESTAÇÃO DE CONTAS — CONVENIADO", 16, Font.BOLD, new Color(20, 20, 20));
        titulo.setSpacingBefore(5);
        titulo.setSpacingAfter(10);
        doc.add(titulo);

        doc.add(sectionHeader("RESUMO DO FATURAMENTO"));
        doc.add(tableResumo(dados, vendas));

        doc.add(space(8));

        doc.add(sectionHeader("VALOR LÍQUIDO"));
        doc.add(tableValorLiquido(dados));

        doc.add(space(8));

        doc.add(sectionHeader("TAXAS EXTRAS APLICADAS"));
        doc.add(tableTaxas(taxas, totalTaxasExtras));

        doc.add(space(8));

        doc.add(sectionHeader("VENDAS DO PERÍODO"));
        doc.add(tableVendas(vendas, totalValorCalcTaxaConveniado, totalValorCalcTaxaEntidade, totalValorVendas, totalTaxaPercentual));

        doc.add(space(8));

        doc.add(sectionHeader("ITENS DE PRODUTOS DAS VENDAS"));
        doc.add(tableItens(itens));
        
        // <<< NOVO: bloco de assinatura no final
        addAssinaturaUaiTag(doc);
    }

    // ===== Layout: TABELADO ===================================================
    private void buildPdfTabelado(Document doc,
                                  RelatorioFaturamentoRequestDTO req,
                                  RelCicloPagamentoConveniadosDTO dados,
                                  List<RelCicloPagamentoConveniadosTaxasDTO> taxas,
                                  List<RelCicloPagamentoVendasDTO> vendas,
                                  List<RelCicloPagamentoVendasItemProdutosDTO> itens,
                                  BigDecimal totalTaxasExtras,
                                  BigDecimal totalValorCalcTaxaConveniado,
                                  BigDecimal totalValorCalcTaxaEntidade,
                                  BigDecimal totalValorVendas,
                                  BigDecimal totalTaxaPercentual) throws DocumentException, IOException {

        Image logo = tryLoadLogo();
        if (logo != null) {
            logo.scaleToFit(100, 50);
            logo.setAlignment(Image.RIGHT);
            doc.add(logo);
        }

        Paragraph titulo = p("Relatório de Faturamento (Modelo Tabelado)", 14, Font.BOLD, null);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(8);
        doc.add(titulo);

        doc.add(tableResumo(dados, vendas));
        doc.add(space(6));
        doc.add(tableValorLiquido(dados));
        doc.add(space(6));
        doc.add(tableTaxas(taxas, totalTaxasExtras));
        doc.add(space(6));
        doc.add(tableVendas(vendas, totalValorCalcTaxaConveniado, totalValorCalcTaxaEntidade, totalValorVendas, totalTaxaPercentual));
        doc.add(space(6));
        doc.add(tableItens(itens));
        
        // <<< NOVO
        addAssinaturaUaiTag(doc);
    }

    // ===== Layout: MINIMAL ====================================================
    private void buildPdfMinimal(Document doc,
                                 RelatorioFaturamentoRequestDTO req,
                                 RelCicloPagamentoConveniadosDTO dados,
                                 List<RelCicloPagamentoConveniadosTaxasDTO> taxas,
                                 List<RelCicloPagamentoVendasDTO> vendas,
                                 List<RelCicloPagamentoVendasItemProdutosDTO> itens,
                                 BigDecimal totalTaxasExtras,
                                 BigDecimal totalValorCalcTaxaConveniado,
                                 BigDecimal totalValorCalcTaxaEntidade,
                                 BigDecimal totalValorVendas,
                                 BigDecimal totalTaxaPercentual) throws DocumentException, IOException {

        Image logo = tryLoadLogo();
        if (logo != null) {
            logo.scaleToFit(100, 50);
            logo.setAlignment(Image.RIGHT);
            doc.add(logo);
        }
        
        Paragraph titulo = p("Prestação de Contas", 13, Font.BOLD, null);
        titulo.setSpacingAfter(6);
        doc.add(titulo);

        doc.add(p("Resumo do faturamento", 11, Font.BOLD, null));
        doc.add(tableResumoMinimal(dados, req));
        doc.add(space(4));

        doc.add(p("Valor líquido", 11, Font.BOLD, null));
        doc.add(tableValorLiquidoMinimal(dados));
        doc.add(space(4));

        doc.add(p("Taxas extras aplicadas", 11, Font.BOLD, null));
        doc.add(tableTaxasMinimal(taxas, totalTaxasExtras));
        doc.add(space(4));

        doc.add(p("Vendas do período", 11, Font.BOLD, null));
        doc.add(tableVendasMinimal(vendas, totalValorCalcTaxaConveniado, totalValorCalcTaxaEntidade, totalValorVendas, totalTaxaPercentual));
        doc.add(space(4));

        doc.add(p("Itens de produtos das vendas", 11, Font.BOLD, null));
        doc.add(tableItensMinimal(itens));
        
        // <<< NOVO
        addAssinaturaUaiTag(doc);
    }

    // =========================
    //   TABELAS / SEÇÕES
    // =========================
    private PdfPTable tableResumo( RelCicloPagamentoConveniadosDTO d, List<RelCicloPagamentoVendasDTO> vendas ) {
        PdfPTable t = new PdfPTable(new float[]{30, 70});
        t.setWidthPercentage(100);

        t.addCell(kv("Conveniado"               , safe(d == null ? null : d.getRazaoSocial()))            );
        t.addCell(kv("CNPJ"                     , safe(d == null ? null : FuncoesUteis.formatar(d.getCnpj() ))) );
        t.addCell(kv("Referência"               , safe(d.getAnoMes()))                                    );
        // Informações basicas.
        t.addCell(kv("Qtde de Vendas"           , String.valueOf(safe(d == null ? null : vendas.size()))) );
        t.addCell(kv("Total Vendas"             , fmt(d == null ? null : d.getVlrCicloBruto()))           );
        t.addCell(kv("Valor Líquido"            , fmt(d == null ? null : d.getVlrLiquido()))              );
        t.addCell(kv("Valor Líquido a Pagar"    , fmt(d == null ? null : d.getVlrLiquidoPagamento()))     );
        // Valores das Taxas.
        t.addCell(kv("Total Taxa (Percentual)"  , fmt(d == null ? null : d.getVlrTaxaExtraPercentual()))  );
        t.addCell(kv("Total Taxa (Extra)"       , fmt(d == null ? null : d.getVlrTaxaExtraValor()))       );
        t.addCell(kv("Total Taxa (Secundária)"  , fmt(d == null ? null : d.getVlrTaxaSecundaria()))       );
        t.addCell(kv("Total Taxa (Faixa Vendas)", fmt(d == null ? null : d.getVlrTaxaExtraValor()))       );
        return t;
    }

    private PdfPTable tableResumoMinimal(RelCicloPagamentoConveniadosDTO d, RelatorioFaturamentoRequestDTO req) {
        PdfPTable t = new PdfPTable(new float[]{40, 60});
        t.setWidthPercentage(100);
        t.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        t.addCell(kvNoBorder("Conveniado", safe(d == null ? null : d.getRazaoSocial()))  );
        t.addCell(kvNoBorder("CNPJ"      , safe(d == null ? null : FuncoesUteis.formatar(d.getCnpj() ) )) );
        t.addCell(kvNoBorder("Referência", safe(req.getAnoMes()))                        );
        t.addCell(kvNoBorder("Vendas"    , fmt(d == null ? null : d.getVlrCicloBruto())) );
        t.addCell(kvNoBorder("Líquido"   , fmt(d == null ? null : d.getVlrLiquido()))    );
        return t;
    }

    private PdfPTable tableValorLiquido(RelCicloPagamentoConveniadosDTO d) {
        PdfPTable t = new PdfPTable(new float[]{50, 50});
        t.setWidthPercentage(100);
        t.addCell(cellHeader("Valor Líquido Apurado"));
        t.addCell(cellValue(fmt(d == null ? null : d.getVlrLiquidoPagamento())));
        return t;
    }

    private PdfPTable tableValorLiquidoMinimal(RelCicloPagamentoConveniadosDTO d) {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);
        t.addCell(cellValue(fmt(d == null ? null : d.getVlrLiquidoPagamento())));
        return t;
    }

    private PdfPTable tableTaxas(List<RelCicloPagamentoConveniadosTaxasDTO> taxas, BigDecimal total) {
        PdfPTable t = new PdfPTable(new float[]{50, 20, 30});
        t.setWidthPercentage(100);
        t.addCell(th("Descrição"));
        t.addCell(th("Período"));
        t.addCell(th("Valor (R$)"));

        for (RelCicloPagamentoConveniadosTaxasDTO it : taxas) {
            t.addCell(td(safe(it.getDescricaoTaxa())));
            String periodo = safe(it.getAnoMes()) ;
//            	   + " " + (it.getDataInicio() != null ? it.getDataInicio() : "") + " - " + (it.getDataFim()    != null ? it.getDataFim()    : "");
            
            
            // t.addCell(td(periodo.trim()));
            t.addCell(tdCenter(periodo.trim()));
            t.addCell(tdRight(fmt(it.getValorTaxa())));
        }

        PdfPCell totalCell = new PdfPCell(new Phrase("TOTAL", font(10, Font.BOLD, null)));
        totalCell.setColspan(2);
        totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalCell.setBackgroundColor(new Color(240, 240, 240));
        totalCell.setPadding(6f);
        t.addCell(totalCell);
        t.addCell(tdRightBold(fmt(total)));
        return t;
    }
    
    // Centralizar uma celular.
    private PdfPCell tdCenter(String text) {
        PdfPCell c = td(text);                 // usa seu helper existente
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        return c;
    }

    private PdfPTable tableTaxasMinimal(List<RelCicloPagamentoConveniadosTaxasDTO> taxas, BigDecimal total) {
        PdfPTable t = new PdfPTable(new float[]{70, 30});
        t.setWidthPercentage(100);
        t.addCell(th("Descrição"));
        t.addCell(th("Valor (R$)"));
        for (RelCicloPagamentoConveniadosTaxasDTO it : taxas) {
            t.addCell(td(safe(it.getDescricaoTaxa())));
            t.addCell(tdRight(fmt(it.getValorTaxa())));
        }
        t.addCell(tdRightBold("TOTAL"));
        t.addCell(tdRightBold(fmt(total)));
        return t;
    }

    private PdfPTable tableVendas(List<RelCicloPagamentoVendasDTO> vendas,
                                  BigDecimal totConveniado, BigDecimal totEntidade,
                                  BigDecimal totVendas, BigDecimal totTaxaPerc) {
        PdfPTable t = new PdfPTable(new float[]{12, 12, 14, 15, 15, 16, 16});
        t.setWidthPercentage(100);
        t.addCell( th("Venda"     ) );
        t.addCell( th("Data"      ) );
        t.addCell( th("Entidade"  ) );
        t.addCell( th("Taxa (%)"  ) );
        t.addCell( th("Tipo Taxa" ) );
        t.addCell( th("Vlr Venda" ) );
        t.addCell( th("Taxa Conv.") );

        for (RelCicloPagamentoVendasDTO v : vendas) {
        	
        	PdfPCell idVenda = td(String.valueOf( safe(v.getIdVenda() )));
            idVenda.setPadding(5f);
            idVenda.setBorderColor(new Color(230,230,230));
        	idVenda.setHorizontalAlignment(Element.ALIGN_CENTER);
        	
            t.addCell( idVenda );
            t.addCell( td( v.getDtVenda() != null ? SDF.format(v.getDtVenda()) : "") );
            t.addCell( td( safe(v.getEntidade())));
            t.addCell( tdCenter( percent(v.getTaxa() ) ) );
            
            t.addCell( tdCenter( v.getTipo_taxa() == true ? "Taxa Expecifica Con. para Ent.": "Taxa Default" ) );
            t.addCell( tdRight( fmt( v.getValorVenda()              ) ) );
            t.addCell( tdRight( fmt( v.getValorCalcTaxaConveniado() ) ) );
           
        }

        t.addCell( totalCell  ( "TOTAIS", 5)          );
        t.addCell( tdRightBold( fmt(totVendas     ) ) );
        t.addCell( tdRightBold( fmt(totConveniado ) ) );

        return t;
    }

    private PdfPTable tableVendasMinimal(List<RelCicloPagamentoVendasDTO> vendas,
                                         BigDecimal totConveniado, BigDecimal totEntidade,
                                         BigDecimal totVendas, BigDecimal totTaxaPerc) {
        PdfPTable t = new PdfPTable(new float[]{18, 22, 20, 20, 20});
        t.setWidthPercentage(100);
        t.addCell( th( "Venda"       ) );
        t.addCell( th( "Data"        ) );
        t.addCell( th( "Vlr Venda"   ) );
        t.addCell( th( "Taxa Conv."  ) );
        t.addCell( th( "Taxa Entid." ) );

        for (RelCicloPagamentoVendasDTO v : vendas) {
            t.addCell(td(String.valueOf(safe(v.getIdVenda()))));
            t.addCell(td(v.getDtVenda() != null ? SDF.format(v.getDtVenda()) : ""));
            t.addCell(tdRight(fmt(v.getValorVenda())));
            t.addCell(tdRight(fmt(v.getValorCalcTaxaConveniado())));
            t.addCell(tdRight(fmt(v.getValorCalcTaxaEntidade())));
        }

        t.addCell(totalCell  ( "TOTAIS", 2));
        t.addCell(tdRightBold( fmt( totVendas     ) ) );
        t.addCell(tdRightBold( fmt( totConveniado ) ) );
        t.addCell(tdRightBold( fmt( totEntidade   ) ) );
        return t;
    }

    private PdfPTable tableItens(List<RelCicloPagamentoVendasItemProdutosDTO> itens) {
        PdfPTable t = new PdfPTable(new float[]{12, 40, 12, 18, 18});
        t.setWidthPercentage(100);
        t.addCell(th( "Venda"             ) );
        t.addCell(th( "Produto/Descrição" ) );
        t.addCell(th( "Qtd"               ) );
        t.addCell(th( "Vlr Unitário"      ) );
        t.addCell(th( "Vlr Total"         ) );

        // Inicializa total geral
        BigDecimal VlrUnitario = BigDecimal.ZERO;
        BigDecimal VlrTotal    = BigDecimal.ZERO;
        
        for (RelCicloPagamentoVendasItemProdutosDTO it : itens) {
        	
        	PdfPCell idVenda = td(String.valueOf( safe(it.getIdVenda() )));
            idVenda.setPadding(5f);
            idVenda.setBorderColor(new Color(230,230,230));
        	idVenda.setHorizontalAlignment(Element.ALIGN_CENTER);
        	
        	PdfPCell qtyItem = td(it.getQtyItem() != null ? it.getQtyItem().toString() : "");
        	qtyItem.setPadding(5f);
        	qtyItem.setBorderColor(new Color(230,230,230));
        	qtyItem.setHorizontalAlignment(Element.ALIGN_CENTER);
        	
            t.addCell( idVenda );
            t.addCell( td( safe(it.getProduto()))                                         );
            t.addCell( qtyItem );
            t.addCell( tdRight( fmt( it.getVlrUnitario()  ) )                             );
            t.addCell( tdRight( fmt( it.getVlrTotalItem() ) )                             );
            
            
            // Acumula total geral
            if (it.getVlrUnitario()  != null)  VlrUnitario = VlrUnitario.add( it.getVlrUnitario()  );
            if (it.getVlrTotalItem() != null)  VlrTotal    = VlrTotal.add   ( it.getVlrTotalItem() );
        }
        
        t.addCell(totalCell  ( "TOTAIS", 3        ) );
        t.addCell(tdRightBold( fmt( VlrUnitario ) ) );
        t.addCell(tdRightBold( fmt( VlrTotal    ) ) );
 
        return t;
    }

    private PdfPTable tableItensMinimal(List<RelCicloPagamentoVendasItemProdutosDTO> itens) {
        PdfPTable t = new PdfPTable(new float[]{15, 55, 15, 15});
        t.setWidthPercentage(100);
        t.addCell(th("Venda"));
        t.addCell(th("Produto"));
        t.addCell(th("Qtd"));
        t.addCell(th("Total"));

        for (RelCicloPagamentoVendasItemProdutosDTO it : itens) {
            t.addCell(td(String.valueOf(safe(it.getIdVenda()))));
            t.addCell(td(safe(it.getProduto())));
            t.addCell(tdRight(it.getQtyItem() != null ? it.getQtyItem().toString() : ""));
            t.addCell(tdRight(fmt(it.getVlrTotalItem())));
        }
        return t;
    }

    // =========================
    //   COMPONENTES VISUAIS
    // =========================

    private Paragraph sectionHeader(String text) {
        Paragraph p = p(text, 12, Font.BOLD, Color.WHITE);
        PdfPTable box = new PdfPTable(1);
        box.setWidthPercentage(100);
        PdfPCell c = new PdfPCell(p);
        c.setBackgroundColor(new Color(33, 150, 243)); // azul
        c.setPadding(6f);
        c.setBorder(Rectangle.NO_BORDER);
        box.addCell(c);
        Paragraph wrap = new Paragraph();
        wrap.add(box);
        return wrap;
    }

    private Paragraph space(float h) {
        Paragraph p = new Paragraph(" ");
        p.setSpacingAfter(h);
        return p;
    }

    private PdfPCell kv(String k, String v) {
        PdfPTable inner = new PdfPTable(new float[]{35, 65});
        inner.setWidthPercentage(100);
        PdfPCell ck = new PdfPCell(new Phrase(safe(k), font(9, Font.BOLD, null)));
        PdfPCell cv = new PdfPCell(new Phrase(safe(v), font(9, Font.NORMAL, null)));
        ck.setBackgroundColor(new Color(248, 248, 248));
        ck.setBorderColor(new Color(230,230,230));
        cv.setBorderColor(new Color(230,230,230));
        ck.setPadding(5f);
        cv.setPadding(5f);
        PdfPCell container = new PdfPCell(inner);
        container.setColspan(2);
        container.setPadding(0);
        container.setBorder(Rectangle.NO_BORDER);

        inner.addCell(ck);
        inner.addCell(cv);
        return container;
    }

    private PdfPCell kvNoBorder(String k, String v) {
        PdfPTable inner = new PdfPTable(new float[]{35, 65});
        inner.setWidthPercentage(100);
        PdfPCell ck = new PdfPCell(new Phrase(safe(k), font(9, Font.BOLD, null)));
        PdfPCell cv = new PdfPCell(new Phrase(safe(v), font(9, Font.NORMAL, null)));
        ck.setBorder(Rectangle.NO_BORDER);
        cv.setBorder(Rectangle.NO_BORDER);
        ck.setPadding(3f);
        cv.setPadding(3f);
        PdfPCell container = new PdfPCell(inner);
        container.setColspan(2);
        container.setPadding(0);
        container.setBorder(Rectangle.NO_BORDER);

        inner.addCell(ck);
        inner.addCell(cv);
        return container;
    }

    private PdfPCell th(String text) {
        PdfPCell c = new PdfPCell(new Phrase(text, font(9, Font.BOLD, null)));
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setBackgroundColor(new Color(245, 245, 245));
        c.setPadding(6f);
        c.setBorderColor(new Color(230,230,230));
        return c;
    }

    private PdfPCell td(String text) {
        PdfPCell c = new PdfPCell(new Phrase(safe(text), font(9, Font.NORMAL, null)));
        c.setPadding(5f);
        c.setBorderColor(new Color(230,230,230));
        return c;
    }

    private PdfPCell tdRight(String text) {
        PdfPCell c = td(text);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return c;
    }

    private PdfPCell tdRightBold(String text) {
        PdfPCell c = new PdfPCell(new Phrase(safe(text), font(9, Font.BOLD, null)));
        c.setPadding(5f);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c.setBackgroundColor(new Color(248, 248, 248));
        c.setBorderColor(new Color(220,220,220));
        return c;
    }

    private PdfPCell totalCell(String label, int colspan) {
        PdfPCell c = new PdfPCell(new Phrase(label, font(9, Font.BOLD, null)));
        c.setColspan(colspan);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c.setBackgroundColor(new Color(240, 240, 240));
        c.setPadding(6f);
        return c;
    }

    private PdfPCell cellHeader(String text) {
        PdfPCell c = new PdfPCell(new Phrase(text, font(10, Font.BOLD, null)));
        c.setBackgroundColor(new Color(240, 240, 240));
        c.setPadding(6f);
        return c;
    }

    private PdfPCell cellValue(String text) {
        PdfPCell c = new PdfPCell(new Phrase(text, font(11, Font.BOLD, new Color(0, 120, 0))));
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c.setPadding(8f);
        return c;
    }

    private Paragraph p(String text, int size, int style, Color color) {
        Font f = FontFactory.getFont(FontFactory.HELVETICA, size, style, color == null ? Color.BLACK : color);
        Paragraph p = new Paragraph(text, f);
        return p;
    }

    private Font font(int size, int style, Color color) {
        return FontFactory.getFont(FontFactory.HELVETICA, size, style, color == null ? Color.BLACK : color);
    }

    private String fmt(BigDecimal v) {
        return v == null ? "-" : NF.format(v.setScale(2, RoundingMode.HALF_UP));
    }

    private String percent(BigDecimal v) {
        if (v == null) return "-";
        return v.stripTrailingZeros().toPlainString() + " %";
    }

    private String safe(Object v) {
        return v == null ? "-" : String.valueOf(v);
    }

    private Image tryLoadLogo() throws IOException, DocumentException {
        try {
            // Usa ClassPathResource para acessar a logo do classpath
            ClassPathResource logoResource = new ClassPathResource("static/images/LogoPreta.png");
            
            // Verifica se o arquivo existe
            if (logoResource.exists()) {
                Image logo = Image.getInstance(logoResource.getURL());
                // Redimensiona a logo para caber no documento
//                logo.scaleToFit(100, 60);
                // Centraliza a logo
//                logo.setAlignment(Image.ALIGN_CENTER);
                // Adiciona a logo ao documento
                
                return logo;

            } else {
                throw new FileNotFoundException("Logo não encontrada no classpath");
            }
        } catch (Exception e) {
            // Loga aviso se logo não for encontrada, mas continua sem ela
            log.warn("Logo não encontrada, continuando sem logo: {}", e.getMessage());
            }        

        return null;   
    }

    private String buildFileName(RelatorioFaturamentoRequestDTO req) {
        return "prestacao_contas_" + req.getIdConveniados() + "_" + safe(req.getAnoMes()) + "_" + req.getLayout() + ".pdf";
    }
   
    
    
    
    
    
}