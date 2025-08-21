package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.awt.Color; // Para cores (usado em adicionarCelulaCabecalho)
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal; // Já deve estar importado, mas confirmando\
import java.text.SimpleDateFormat; // Para formatação de datas
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
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
        empresa.add(new Chunk(dadosCiclo.getCnpj(), fontNormal));
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
}