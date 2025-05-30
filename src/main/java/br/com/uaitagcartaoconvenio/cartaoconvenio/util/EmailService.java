package br.com.uaitagcartaoconvenio.cartaoconvenio.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    // Caminho para o template no classpath
    private static final String TEMPLATE_PATH           = "templates/email-fechamento.html";
    private static final String TEMPLATE_NF_PATH        = "templates/email-anexo-nf.html";
    private static final String TEMPLATE_CONF_RECB_PATH = "templates/email-confimacao-reebimento-entidade.html";
    private static final String TEMPLATE_CONF_PG_PATH   = "templates/email-confimacao-pagamento-conveniada.html";
    


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
        
        MimeMessage mensagem = mailSender.createMimeMessage();
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

}

