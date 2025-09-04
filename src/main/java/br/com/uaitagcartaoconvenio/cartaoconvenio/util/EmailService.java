package br.com.uaitagcartaoconvenio.cartaoconvenio.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Antecipacao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.security.AntecipacaoTokenService;
import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	
	@Autowired
    private AntecipacaoTokenService tokenService;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    // Caminho para o template no classpath
    private static final String TEMPLATE_PATH           = "templates/email-fechamento.html";
    private static final String TEMPLATE_NF_PATH        = "templates/email-anexo-nf.html";
    private static final String TEMPLATE_CONF_RECB_PATH = "templates/email-confimacao-reebimento-entidade.html";
    private static final String TEMPLATE_CONF_PG_PATH   = "templates/email-confimacao-pagamento-conveniada.html";
    
    @Value("${app.base.url}")
    private String baseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

 // Método atualizado no serviço
    public String enviarEmailSimples(String[] destinatarios, String assunto, String conteudo) {
        try {
            JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;
            logger.info("Tentando conectar em: {}:{}", 
                mailSenderImpl.getHost(), 
                mailSenderImpl.getPort());
            
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setTo(destinatarios); // Aceita array de strings
            mensagem.setSubject(assunto);
            mensagem.setText(conteudo);
//            mensagem.setFrom("Sistema Cartão Convenio <sistema@uaitag.com.br>");
            
            mailSender.send(mensagem);
            logger.info("E-mail enviado com sucesso para {} destinatários", destinatarios.length);
            
            return "OK";
            
        } catch (MailException ex) {
            logger.error("Erro no envio:", ex);
            throw new RuntimeException("Falha ao enviar e-mail: " + ex.getMessage(), ex);
        }
    }
    
    // Método para enviar e-mail HTML
    public void enviarEmailHtml(String destinatario, String assunto, String conteudoHtml) 
        throws MessagingException {
        
        MimeMessage mensagem = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");
        
        helper.setTo(destinatario);
        helper.setSubject(assunto);
        helper.setText(conteudoHtml, true); // true indica que é HTML
        helper.setFrom("fabiano.amaral@uaitag.com.br");
        
        mailSender.send(mensagem);
    }

    // Método para enviar e-mail com anexo
    public void enviarEmailComAnexo(String destinatario, String assunto, String conteudo, 
                                  String caminhoAnexo) throws MessagingException {
        
        MimeMessage mensagem     = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensagem, true);
        
        helper.setTo(destinatario);
        helper.setSubject(assunto);
        helper.setText(conteudo);
        helper.setFrom("fabiano.amaral@uaitag.com.br");
        
        // Adicionar anexo
        FileSystemResource arquivo = new FileSystemResource(new File(caminhoAnexo));
        helper.addAttachment(arquivo.getFilename(), arquivo);
        
        mailSender.send(mensagem);
    }
    
    public void enviarEmailFechamento(	String tipoFechamento,
							            List<String> destinatarios,
							            String periodo,
							            String dataInicio,
							            String dataFim,
							            List<Map<String, String>> conveniadas,
							            List<Map<String, String>> entidades)  throws EmailFechamentoException {

    	try {
			// Validação dos parâmetros
			validarParametros(tipoFechamento, destinatarios, periodo, dataInicio, dataFim, conveniadas, entidades);
			
			MimeMessage     mensagem = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");
			
			// Carrega e valida template
			String htmlTemplate = loadEmailTemplate();
			validateTemplate(htmlTemplate);
			
			// Prepara as linhas das tabelas
			String conveniadasRows = generateTableRows( conveniadas);
			String entidadesRows   = generateTableRows( entidades  );
			
			// Substitui as variáveis
			htmlTemplate = processarTemplate(htmlTemplate, tipoFechamento, periodo, dataInicio, dataFim, conveniadasRows, entidadesRows);
			
			// Configura mensagem
			configurarMensagem(helper, destinatarios, periodo, htmlTemplate, "Fechamento do Ciclo " + periodo + " - Cartão Convênio");
			
			// Adiciona logo
			adicionarLogo(helper);
			
			// Envia e-mail
			mailSender.send(mensagem);

		} catch (MessagingException e) {
				throw new EmailFechamentoException("SMTP", "Erro ao configurar/enviar mensagem", e);
		} catch (IOException e) {
				throw new EmailFechamentoException("TEMPLATE", "Erro ao carregar template ou recurso", e);
		} catch (IllegalArgumentException e) {
				throw new EmailFechamentoException("VALIDACAO", e.getMessage(), e);
		} catch (Exception e) {
				throw new EmailFechamentoException("DESCONHECIDO", "Erro inesperado no envio", e);
		}
    }

	//Métodos auxiliares (exemplos)
	private void validarParametros(	String tipoFechamento, List<String> destinatarios, 
							         String periodo, String dataInicio, String dataFim,
							         List<Map<String, String>> conveniadas, 
							         List<Map<String, String>> entidades) {
		
		if (destinatarios == null || destinatarios.isEmpty()) {
			throw new IllegalArgumentException("Lista de destinatários não pode ser vazia");
		}
		if (periodo == null || periodo.trim().isEmpty()) {
			throw new IllegalArgumentException("Período não pode ser vazio");
		}
		// Adicione outras validações conforme necessário
	}

	private String processarTemplate(String template, String tipoFechamento, String periodo, String dataInicio, String dataFim, String conveniadasRows, String entidadesRows) {
			return template.replace("${tipoFechamento}"  , tipoFechamento                          )
							.replace("${periodo}"        , periodo                                 )
							.replace("${dataInicio}"     , dataInicio                              )
							.replace("${dataFim}"        , dataFim                                 )
							.replace("${dataEnvio}"      , FuncoesUteis.getCurrentDateTimeBrasil() )
							.replace("${conveniadasRows}", conveniadasRows                         )
							.replace("${entidadesRows}"  , entidadesRows                           );
	}

	private void configurarMensagem(MimeMessageHelper helper, List<String> destinatarios, String periodo, String htmlTemplate, String subject) throws MessagingException {
			helper.setTo(destinatarios.toArray(new String[0]));
			helper.setSubject(subject);
			helper.setText(htmlTemplate, true);
			helper.setFrom("sistema@uaitag.com.br");
	}

	private void adicionarLogo(MimeMessageHelper helper) throws MessagingException, IOException {
		InputStreamSource logoSrc = new ClassPathResource("static/images/LogoPreta.png");
		if (!((ClassPathResource) logoSrc).exists()) {
			throw new IOException("Arquivo do logo não encontrado");
		}
		helper.addInline("logo", logoSrc, "image/png");
	}
    
    
    // Gera as linhas da tabela a partir dos dados
    private String generateTableRows(List<Map<String, String>> data) {
        StringBuilder rows = new StringBuilder();
        for (Map<String, String> item : data) {
            rows.append("<tr>")
                   .append("<td>").append(item.get("nome"          )).append("</td>")
                   .append("<td>").append(item.get("periodo"       )).append("</td>")
                   .append("<td>").append(item.get("valor"         )).append("</td>")
                   .append("<td>").append(item.get("taxaValor"     )).append("</td>")
                   .append("<td align='center'>").append(item.get("taxaPercentual")).append("</td>")
                .append("</tr>");
        }
        return rows.toString();
    }
        
    /**
     * Carrega o template de e-mail HTML do classpath
     * @return String com o conteúdo do template HTML
     * @throws IOException se o arquivo não puder ser lido
     */
    private String loadEmailTemplate() throws EmailTemplateException {
        try {
            ClassPathResource resource = new ClassPathResource(TEMPLATE_PATH);
            
            if (!resource.exists()) {
                throw new EmailTemplateException("Template não encontrado no caminho: " + TEMPLATE_PATH);
            }
            
            try (InputStreamReader reader = new InputStreamReader(
                    resource.getInputStream(), 
                    StandardCharsets.UTF_8)) {
                String template = FileCopyUtils.copyToString(reader);
                
                if (StringUtils.isEmpty(template)) {
                    throw new EmailTemplateException("Template está vazio");
                }
                
                return template;
            }
        } catch (IOException e) {
            throw new EmailTemplateException("Erro ao ler template de e-mail", e);
        }
    }
    
    /**
     * Carrega o template de e-mail HTML do classpath
     * @return String com o conteúdo do template HTML
     * @throws IOException se o arquivo não puder ser lido
     */
    private String loadEmailTemplateNF() throws EmailTemplateException {
        try {
            ClassPathResource resource = new ClassPathResource(TEMPLATE_NF_PATH);
            
            if (!resource.exists()) {
                throw new EmailTemplateException("Template não encontrado no caminho: " + TEMPLATE_NF_PATH);
            }
            
            try (InputStreamReader reader = new InputStreamReader(
                    resource.getInputStream(), 
                    StandardCharsets.UTF_8)) {
                String template = FileCopyUtils.copyToString(reader);
                
                if (StringUtils.isEmpty(template)) {
                    throw new EmailTemplateException("Template está vazio");
                }
                
                return template;
            }
        } catch (IOException e) {
            throw new EmailTemplateException("Erro ao ler template de e-mail", e);
        }
    }
    
    /**
     * Carrega o template de e-mail HTML do classpath
     * @return String com o conteúdo do template HTML
     * @throws IOException se o arquivo não puder ser lido
     */
    private String loadEmailTemplateConfRecebEnt() throws EmailTemplateException {
        try {
            ClassPathResource resource = new ClassPathResource(TEMPLATE_CONF_RECB_PATH);
            
            if (!resource.exists()) {
                throw new EmailTemplateException("Template não encontrado no caminho: " + TEMPLATE_CONF_RECB_PATH);
            }
            
            try (InputStreamReader reader = new InputStreamReader(
                    resource.getInputStream(), 
                    StandardCharsets.UTF_8)) {
                String template = FileCopyUtils.copyToString(reader);
                
                if (StringUtils.isEmpty(template)) {
                    throw new EmailTemplateException("Template está vazio");
                }
                
                return template;
            }
        } catch (IOException e) {
            throw new EmailTemplateException("Erro ao ler template de e-mail", e);
        }
    }

    /**
     * Carrega o template de e-mail HTML do classpath
     * @return String com o conteúdo do template HTML
     * @throws IOException se o arquivo não puder ser lido
     */
    private String loadEmailTemplateConfPgConv() throws EmailTemplateException {
        try {
            ClassPathResource resource = new ClassPathResource(TEMPLATE_CONF_PG_PATH);
            
            if (!resource.exists()) {
                throw new EmailTemplateException("Template não encontrado no caminho: " + TEMPLATE_CONF_PG_PATH);
            }
            
            try (InputStreamReader reader = new InputStreamReader(
                    resource.getInputStream(), 
                    StandardCharsets.UTF_8)) {
                String template = FileCopyUtils.copyToString(reader);
                
                if (StringUtils.isEmpty(template)) {
                    throw new EmailTemplateException("Template está vazio");
                }
                
                return template;
            }
        } catch (IOException e) {
            throw new EmailTemplateException("Erro ao ler template de e-mail", e);
        }
    }

    // Classe de exceção customizada
    public class EmailTemplateException extends Exception {
      
		private static final long serialVersionUID = 1L;
		public EmailTemplateException(String message) {
            super(message);
        }
        public EmailTemplateException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    private void validateTemplate(String template) throws EmailTemplateException {
        String[] requiredPlaceholders = {"${periodo}", "${dataInicio}", "${dataFim}"};
        for (String placeholder : requiredPlaceholders) {
            if (!template.contains(placeholder)) {
                throw new EmailTemplateException("Template não contém placeholder: " + placeholder);
            }
        }
    }
    
    private void validateTemplateNF(String template) throws EmailTemplateException {
        String[] requiredPlaceholders = {"${TipoEmpresa}", "${dataEnvio}", "${Empresa}", "${periodo}", "${tbodyRows}"};
        for (String placeholder : requiredPlaceholders) {
            if (!template.contains(placeholder)) {
                throw new EmailTemplateException("Template não contém placeholder: " + placeholder);
            }
        }
    }
    
    private void validateTemplateRceb(String template) throws EmailTemplateException {
        String[] requiredPlaceholders = {"${dataEnvio}", "${Empresa}", "${periodo}", "${tbodyRows}" };
        for (String placeholder : requiredPlaceholders) {
            if (!template.contains(placeholder)) {
                throw new EmailTemplateException("Template não contém placeholder: " + placeholder);
            }
        }
    }
   
    public void enviarEmailAnexoNF(	String tipoEmpresa,
            						List<String> destinatarios,
            						String periodo,
            						String empresa,
            						List<Map<String, String>> infoNF)  throws EmailFechamentoException {

			try {
			
			MimeMessage     mensagem = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");
			
			// Carrega e valida template
			String htmlTemplate = loadEmailTemplateNF();
			validateTemplateNF(htmlTemplate);
			
			// Prepara as linhas das tabelas
			String tbodyRows = generateTableRowsNF( infoNF);
			
			// Substitui as variáveis
			htmlTemplate = processarTemplateNF(htmlTemplate, tipoEmpresa, empresa, periodo,  tbodyRows);
			
			// Configura mensagem
			configurarMensagem(helper, destinatarios, periodo, htmlTemplate, "Confirmação Anexo NF ( " + empresa + " ) - Período: " + periodo + " - Cartão Convênio");
			
			// Adiciona logo
			adicionarLogo(helper);
			
			// Envia e-mail
			mailSender.send(mensagem);
			
			} catch (MessagingException e) {
				throw new EmailFechamentoException("SMTP", "Erro ao configurar/enviar mensagem", e);
			} catch (IOException e) {
				throw new EmailFechamentoException("TEMPLATE", "Erro ao carregar template ou recurso", e);
			} catch (IllegalArgumentException e) {
				throw new EmailFechamentoException("VALIDACAO", e.getMessage(), e);
			} catch (Exception e) {
				throw new EmailFechamentoException("DESCONHECIDO", "Erro inesperado no envio", e);
			}
    }

    // Gera as linhas da tabela a partir dos dados
    private String generateTableRowsNF(List<Map<String, String>> data) {
        StringBuilder rows = new StringBuilder();
        for (Map<String, String> item : data) {
            rows.append("<tr>")
                   .append("<td>").append(item.get("empresa" )).append("</td>")
                   .append("<td>").append(item.get("periodo" )).append("</td>")
                   .append("<td>").append(item.get("valor"   )).append("</td>")
                   .append("<td>").append(item.get("nomeArq" )).append("</td>")
                   .append("<td>").append(item.get("dtAnexo" )).append("</td>")
                .append("</tr>");
        }
        return rows.toString();
    }
    
	private String processarTemplateNF(String template, String TipoEmpresa, String Empresa, String periodo, String tbodyRows) {
		return template.replace("${TipoEmpresa}", TipoEmpresa                             )
					   .replace("${Empresa}"   , Empresa                                 )
					   .replace("${periodo}"   , periodo                                 )
					   .replace("${tbodyRows}" , tbodyRows                               )
					   .replace("${dataEnvio}" , FuncoesUteis.getCurrentDateTimeBrasil() );
	}

    public void enviarEmailConfRecebEnt(List<String> destinatarios,
                                        String periodo,
                                        String empresa,
                                        List<Map<String, String>> infoNF)  throws EmailFechamentoException {

        try {
             MimeMessage     mensagem = mailSender.createMimeMessage();
             MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

             // Carrega e valida template
             String htmlTemplate = loadEmailTemplateConfRecebEnt();
             validateTemplateRceb(htmlTemplate);

            // Prepara as linhas das tabelas
            String tbodyRows = generateTableRowsPG( infoNF);

            // Substitui as variáveis
            htmlTemplate = processarTemplateReb(htmlTemplate, empresa, periodo,  tbodyRows);

           // Configura mensagem
           configurarMensagem(helper, destinatarios, periodo, htmlTemplate, "Confirmação Recebimento ( " + empresa + " ) - Período: " + periodo + " - Cartão Convênio");

           // Adiciona logo
           adicionarLogo(helper);

           // Envia e-mail
           mailSender.send(mensagem);
           
        } catch (MessagingException e) {
             throw new EmailFechamentoException("SMTP", "Erro ao configurar/enviar mensagem", e);
        } catch (IOException e) {
             throw new EmailFechamentoException("TEMPLATE", "Erro ao carregar template ou recurso", e);
        } catch (IllegalArgumentException e) {
             throw new EmailFechamentoException("VALIDACAO", e.getMessage(), e);
        } catch (Exception e) {
             throw new EmailFechamentoException("DESCONHECIDO", "Erro inesperado no envio", e);
        }
    }
    
	private String processarTemplateReb(String template, String Empresa, String periodo, String tbodyRows) {
		return template.replace("${Empresa}"   , Empresa                                 )
					   .replace("${periodo}"   , periodo                                 )
					   .replace("${tbodyRows}" , tbodyRows                               )
					   .replace("${dataEnvio}" , FuncoesUteis.getCurrentDateTimeBrasil() );
	}
	
    // Gera as linhas da tabela a partir dos dados
    private String generateTableRowsPG(List<Map<String, String>> data) {
        StringBuilder rows = new StringBuilder();
        for (Map<String, String> item : data) {
            rows.append("<tr>")
                   .append("<td>").append(item.get("empresa" )).append("</td>")
                   .append("<td>").append(item.get("periodo" )).append("</td>")
                   .append("<td>").append(item.get("valor"   )).append("</td>")
                   .append("<td>").append(item.get("docBanco")).append("</td>")
                   .append("<td>").append(item.get("dtPG"    )).append("</td>")
                .append("</tr>");
        }
        return rows.toString();
    }

    public void enviarEmailConfPagamento(List<String> destinatarios, String periodo, String empresa, List<Map<String, String>> infoNF)  throws EmailFechamentoException {

			try {
				MimeMessage     mensagem = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");
				
				// Carrega e valida template
				String htmlTemplate = loadEmailTemplateConfPgConv();
				validateTemplateRceb(htmlTemplate);
				
				// Prepara as linhas das tabelas
				String tbodyRows = generateTableRowsPG( infoNF);
				
				// Substitui as variáveis
				htmlTemplate = processarTemplateReb(htmlTemplate, empresa, periodo,  tbodyRows);
				
				// Configura mensagem
				configurarMensagem(helper, destinatarios, periodo, htmlTemplate, "Confirmação Pagamento ( " + empresa + " ) - Período: " + periodo + " - Cartão Convênio");
				
				// Adiciona logo
				adicionarLogo(helper);
				
				// Envia e-mail
				mailSender.send(mensagem);
			
			} catch (MessagingException e) {
			     throw new EmailFechamentoException("SMTP", "Erro ao configurar/enviar mensagem", e);
			} catch (IOException e) {
			     throw new EmailFechamentoException("TEMPLATE", "Erro ao carregar template ou recurso", e);
			} catch (IllegalArgumentException e) {
			     throw new EmailFechamentoException("VALIDACAO", e.getMessage(), e);
			} catch (Exception e) {
			     throw new EmailFechamentoException("DESCONHECIDO", "Erro inesperado no envio", e);
			}
    }

    /**
     * Envia e-mail de aprovação de antecipação com tratamento diferenciado para destinatário principal e cópias
     * @param antecipacao Dados da antecipação
     * @param destinatarioPrincipal E-mail do destinatário principal (recebe os links de ação)
     * @param destinatariosCopia Lista de e-mails em cópia (não recebem links de ação)
     * @throws EmailFechamentoException Se ocorrer erro no envio do e-mail
     */
    public void enviarEmailAprovacaoAntecipacao(Antecipacao antecipacao, String destinatarioPrincipal, 
            List<String> destinatariosCopia) throws EmailFechamentoException {
        if (destinatarioPrincipal == null || destinatarioPrincipal.trim().isEmpty()) {
            throw new IllegalArgumentException("Destinatário principal não pode ser vazio");
        }
        
        try {
            // Gera um único token para a antecipação
            String token = tokenService.gerarToken(antecipacao);
            
            // Carregar template
            String htmlTemplate = loadEmailTemplateAprovacaoAntecipacao();
            
            // Preparar dados
            Map<String, String> dados = prepararDadosAntecipacao(antecipacao);
            dados.put("token", token);
            dados.put("idAntecipacao", antecipacao.getIdAntecipacao().toString());
            dados.put("baseUrl", baseUrl);
            
            // 1. Enviar para o destinatário principal (com botões de ação)
            enviarEmailParaDestinatario(antecipacao, destinatarioPrincipal, htmlTemplate, dados, true);
            
            // 2. Enviar para cópias (sem botões de ação)
            if (destinatariosCopia != null && !destinatariosCopia.isEmpty()) {
                enviarEmailParaDestinatario(antecipacao, null, htmlTemplate, dados, false, 
                        destinatariosCopia.toArray(new String[0]));
            }
            
            logger.info("E-mail de aprovação de antecipação enviado com sucesso. Principal: {}, Cópias: {}", 
            destinatarioPrincipal, destinatariosCopia != null ? destinatariosCopia.size() : 0);
        
        } catch (Exception e) {
            logger.error("Erro ao enviar e-mail de aprovação", e);
            throw new EmailFechamentoException("ERRO_EMAIL", "Falha ao enviar e-mail de aprovação", e);
        }
    }

    /**
     * Método auxiliar para enviar e-mail para um destinatário específico
     */
     
    private void enviarEmailParaDestinatario(Antecipacao antecipacao, String destinatario, String template, Map<String, String> dados, boolean incluirLinks, String... copias) throws MessagingException, IOException {
		
		MimeMessage     mensagem = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");
		
		// Processa template com ou sem links
		String htmlTemplate = processarTemplateAprovacao(template, dados, incluirLinks);
		
		// Configura mensagem
		if (destinatario != null) {
		    helper.setTo(destinatario);
		}
		
		// Adiciona cópias se existirem
		if (copias != null && copias.length > 0) {
		    helper.setCc(copias);
		}
		
		helper.setSubject("Solicitação de Aprovação de Antecipação - " + antecipacao.getConveniados().getPessoa().get(0).getNomePessoa());
		helper.setText(htmlTemplate, true);
		helper.setFrom("sistema@uaitag.com.br");
		
		// Adiciona logo
		adicionarLogo(helper);
		
		// Envia e-mail
		mailSender.send(mensagem);
	}

    /**
     * Processa o template de aprovação, controlando a exibição dos links/controles de ação.
     * Compatível com ambas implementações: links diretos ou elementos que disparam popups.
     * 
     * @param template Conteúdo HTML do template
     * @param dados Mapa de valores para substituição
     * @param incluirLinks Se true, mantém os controles de ação; se false, substitui por mensagem informativa
     * @return Template processado
     */
    /*
    private String processarTemplateAprovacao(String template, Map<String, String> dados, boolean incluirLinks) {
    	
        // Primeiro processa todos os dados normais
        for (Map.Entry<String, String> entry : dados.entrySet()) {
            template = template.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        
        // Controle dos links de ação
        if (!incluirLinks) {
            // Usando regex para ser mais flexível com espaços e quebras de linha
            template = template.replaceAll(
                "(?s)<div[^>]*class=\"actions\"[^>]*>.*?</div>",
                "<div class=\"actions\">\n" +
                "    <p>Links de aprovação/reprovação enviados apenas para o destinatário principal</p>\n" +
                "</div>"
            );
        }
        
        return template;
    }  
    */

    private String processarTemplateAprovacao(String template, Map<String, String> dados, boolean incluirLinks) {
        // Primeiro substitui todas as variáveis padrão
        for (Map.Entry<String, String> entry : dados.entrySet()) {
            template = template.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        
        // Tratamento especial para os botões de ação
        if (incluirLinks) {
            // Mantém os IDs dos botões para o JavaScript
            template = template.replace(
                "href=\"#\" id=\"approveBtn\"", 
                "href=\"#\" id=\"approveBtn\""
            ).replace(
                "href=\"#\" id=\"rejectBtn\"", 
                "href=\"#\" id=\"rejectBtn\""
            );
            
            // Adiciona o JavaScript necessário
            template = template.replace("</body>", 
                "<script type=\"text/javascript\">" +
                "document.addEventListener('DOMContentLoaded', function() {" +
                "   const approveBtn = document.getElementById('approveBtn');" +
                "   const rejectBtn = document.getElementById('rejectBtn');" +
                "   const popupBlockedWarning = document.getElementById('popupBlockedWarning');" +
                "   const fallbackLink = document.getElementById('fallbackLink');" +
                "" +
                "   function openApprovalPopup(action) {" +
                "       const width = 600;" +
                "       const height = 500;" +
                "       const left = (screen.width - width) / 2;" +
                "       const top = (screen.height - height) / 2;" +
                "" +
                "       const popupUrl = `${baseUrl}/antecipacao/popup?id=${encodeURIComponent('${idAntecipacao}')}&token=${encodeURIComponent('${token}')}&action=${action}`;" +
                "" +
                "       const popup = window.open(" +
                "           popupUrl, " +
                "           'AntecipacaoPopup', " +
                "           `width=${width},height=${height},top=${top},left=${left},resizable=no,scrollbars=no,toolbar=no,location=no,status=no`" +
                "       );" +
                "" +
                "       if (!popup || popup.closed || typeof popup.closed === 'undefined') {" +
                "           popupBlockedWarning.style.display = 'block';" +
                "           document.querySelector('.fallback-text').style.display = 'block';" +
                "           fallbackLink.setAttribute('data-action', action);" +
                "       } else {" +
                "           popup.focus();" +
                "       }" +
                "   }" +
                "" +
                "   approveBtn.addEventListener('click', function(e) {" +
                "       e.preventDefault();" +
                "       openApprovalPopup('approve');" +
                "   });" +
                "" +
                "   rejectBtn.addEventListener('click', function(e) {" +
                "       e.preventDefault();" +
                "       openApprovalPopup('reject');" +
                "   });" +
                "" +
                "   fallbackLink.addEventListener('click', function(e) {" +
                "       e.preventDefault();" +
                "       const action = this.getAttribute('data-action');" +
                "       const popupUrl = `${baseUrl}/antecipacao/popup?id=${encodeURIComponent('${idAntecipacao}')}&token=${encodeURIComponent('${token}')}&action=${action}`;" +
                "       window.location.href = popupUrl;" +
                "   });" +
                "});" +
                "</script>" +
                "</body>"
            );
        } else {
            template = template.replaceAll(
                "(?s)<div[^>]*class=\"actions\"[^>]*>.*?</div>",
                "<div class=\"actions\">\n" +
                "    <p>Links de aprovação/reprovação enviados apenas para o destinatário principal</p>\n" +
                "</div>"
            );
        }
        
        return template;
    }    
    /**
     * Carrega o template de e-mail de aprovação de antecipação
     */
    private String loadEmailTemplateAprovacaoAntecipacao() throws EmailTemplateException {
        try {
            ClassPathResource resource = new ClassPathResource("templates/email-aprovacao-antecipacao.html");
            if (!resource.exists()) {
                throw new EmailTemplateException("Template de aprovação não encontrado");
            }
            
            try (InputStreamReader reader = new InputStreamReader(
                    resource.getInputStream(), 
                    StandardCharsets.UTF_8)) {
                return FileCopyUtils.copyToString(reader);
            }
        } catch (IOException e) {
            throw new EmailTemplateException("Erro ao ler template de aprovação", e);
        }
    }

    /**
     * Prepara os dados da antecipação para substituição no template
     */
    private Map<String, String> prepararDadosAntecipacao(Antecipacao antecipacao) {
        Map<String, String> dados = new HashMap<>();
        
        // Dados da conveniada
        dados.put("nomeConveniada", antecipacao.getConveniados().getPessoa().get(0).getNomePessoa());
        
        // Dados das taxas
        dados.put("taxaMes", formatarDecimal(antecipacao.getTaxaMes()));
        dados.put("taxaDia", formatarDecimal(antecipacao.getTaxaDia()));
        dados.put("taxaPeriodo", formatarDecimal(antecipacao.getTaxaPeriodo()));
        
        // Datas
        dados.put("dtCorte", formatarData(antecipacao.getDtCorte()));
        dados.put("dtPagamento", formatarData(antecipacao.getDtPagamento()));
        dados.put("dtVencimento", formatarData(antecipacao.getDtVencimento()));
        
        // Valores
        dados.put("periodoDias", antecipacao.getPeriodoDias().toString());
        dados.put("valorDesconto", formatarMoeda(antecipacao.getValorDesconto()));
        dados.put("valorNominal", formatarMoeda(antecipacao.getValorNominal()));
        dados.put("valorBase", formatarMoeda(antecipacao.getValorBase()));
        
        // Data de envio
        dados.put("dataEnvio", FuncoesUteis.getCurrentDateTimeBrasil());
        
        return dados;
    }

    /**
     * Processa o template substituindo os placeholders pelos valores reais
     
    private String processarTemplateAprovacao(String template, Map<String, String> dados) {
        for (Map.Entry<String, String> entry : dados.entrySet()) {
            template = template.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return template;
    }
    */
    
    // Métodos auxiliares de formatação
    private String formatarDecimal(BigDecimal valor) {
        return String.format("%.4f", valor).replace(".", ",");
    }

    private String formatarMoeda(BigDecimal valor) {
        return "R$ " + String.format("%,.2f", valor).replace(".", ",");
    }

    private String formatarData(LocalDate data) {
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    
    
    /************************************************************************************/
  
    
    public void enviarEmailConfirmacaoAntecipacao(Antecipacao antecipacao, String status, String motivo, List<String> destinatarios) throws EmailFechamentoException {
        try {
            MimeMessage mensagem = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");
            
            // Carrega template
            String htmlTemplate = loadTemplateConfirmacao();
            
            // Prepara dados
            Map<String, String> dados = new HashMap<>();
            dados.put( "status"        , status                                                   );
            dados.put( "statusLower"   , status.toLowerCase()                                     );
            dados.put( "dataEnvio"     , FuncoesUteis.getCurrentDateTimeBrasil()                  );
            dados.put( "nomeConveniada", antecipacao.getConveniados().getPessoa().get(0).getNomePessoa() );
            dados.put( "idAntecipacao" , antecipacao.getIdAntecipacao().toString()                );
            dados.put( "valorNominal"  , formatarMoeda(antecipacao.getValorNominal())             );
            dados.put( "dtPagamento"   , formatarData(antecipacao.getDtPagamento())               );
            dados.put( "dtVencimento"  , formatarData(antecipacao.getDtVencimento())              );
            dados.put( "taxaPeriodo"   , formatarDecimal(antecipacao.getTaxaPeriodo())            );
            
            if (motivo != null && !motivo.isEmpty()) {
                dados.put("motivoRow", "<tr><th>Motivo</th><td>" + motivo + "</td></tr>");
            } else {
                dados.put("motivoRow", "");
            }
            
            // Processa template
            for (Map.Entry<String, String> entry : dados.entrySet()) {
                htmlTemplate = htmlTemplate.replace("${" + entry.getKey() + "}", entry.getValue());
            }
            
            // Configura mensagem
            helper.setTo     ( destinatarios.toArray(new String[0])                                                                      );
            helper.setSubject( "Confirmação de Antecipação " + status + " - " + antecipacao.getConveniados().getPessoa().get(0).getNomePessoa() );
            helper.setText   ( htmlTemplate, true                                                                                        );
            helper.setFrom   ( "sistema@uaitag.com.br"                                                                                   );
            
            // Adiciona logo
            adicionarLogo( helper );
            
            // Envia e-mail
            mailSender.send( mensagem );
            
        } catch (Exception e) {
        	logger.error("Falha ao enviar e-mail de confirmação", e);
            throw new EmailFechamentoException("ERRO_EMAIL", "Falha ao enviar e-mail de confirmação", e);
        }
        
    }

    private String loadTemplateConfirmacao() throws EmailTemplateException {
        try {
            ClassPathResource resource = new ClassPathResource("templates/email-confirmacao-antecipacao.html");
            try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                return FileCopyUtils.copyToString(reader);
            }
        } catch (IOException e) {
            throw new EmailTemplateException("Erro ao ler template de confirmação", e);
        }
    }    
    
    public String enviarEmailTeste(String destinatario) {
        try {
            JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;
            logger.info("Testando conexão SMTP em: {}:{}", 
                mailSenderImpl.getHost(), 
                mailSenderImpl.getPort());
            
            // Criar mensagem de teste simples
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setTo(destinatario);
            mensagem.setSubject("Teste de Configuração SMTP - Cartão Convênio");
            mensagem.setText(
                "Este é um e-mail de teste para verificar a configuração SMTP.\n\n" +
                "Data/Hora: " + FuncoesUteis.getCurrentDateTimeBrasil() + "\n" +
                "Servidor: " + mailSenderImpl.getHost() + ":" + mailSenderImpl.getPort() + "\n" +
                "Status: Conexão bem-sucedida!\n\n" +
                "Sistema Cartão Convênio"
            );
            mensagem.setFrom("sistema@uaitag.com.br");
            
            // Tentar enviar
            mailSender.send(mensagem);
            
            logger.info("E-mail de teste enviado com sucesso para: {}", destinatario);
            return "OK - E-mail de teste enviado com sucesso para: " + destinatario;
            
        } catch (MailException ex) {
            logger.error("Erro no envio do e-mail teste:", ex);
            throw new RuntimeException("Falha ao enviar e-mail teste: " + ex.getMessage(), ex);
        }
    }
    
    
    public String testarConexaoSmtp() {
        try {
            JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;
            
            logger.info("Testando conectividade com: {}:{}", 
                mailSenderImpl.getHost(), 
                mailSenderImpl.getPort());

            var impl = (JavaMailSenderImpl) mailSender;
            System.out.println("===============================================================================");
            System.out.println("===============================================================================");
            System.out.println("===============================================================================");
            System.out.println("Host="+impl.getHost()+" Port="+impl.getPort()+" Protocol="+impl.getProtocol());
            impl.getJavaMailProperties().forEach((k,v)-> System.out.println(k+"="+v));           
            System.out.println("===============================================================================");
            System.out.println("===============================================================================");
            System.out.println("===============================================================================");
            
            // Testa a conexão criando uma sessão
            mailSenderImpl.testConnection();
            
            String resultado = "CONEXÃO SMTP OK - Servidor: " + mailSenderImpl.getHost() + 
                              ":" + mailSenderImpl.getPort() + 
                              " - " + FuncoesUteis.getCurrentDateTimeBrasil();
            
            logger.info(resultado);
            return resultado;
            
        } catch (Exception ex) {
            String erro = "FALHA NA CONEXÃO SMTP: " + ex.getMessage();
            logger.error(erro, ex);
            return erro;
        }
    }   
    
}

