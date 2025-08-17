package br.com.uaitagcartaoconvenio.cartaoconvenio.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Antecipacao;

@Component
public class AntecipacaoTokenService {

    @Value("${api.security.token.secret}")
    private String SECRET_KEY;
    
    private static final long TOKEN_EXPIRATION_MINUTES = 120;
    private static final Logger logger = LoggerFactory.getLogger(AntecipacaoTokenService.class);
    private static final String TOKEN_DELIMITER = "|";

    /**
     * Gera um token seguro para aprovação/reprovação de antecipação
     */
    public String gerarToken(Antecipacao antecipacao) {
        Instant now = Instant.now();
        Instant expiration = now.plus(TOKEN_EXPIRATION_MINUTES, ChronoUnit.MINUTES);
        
        String payload = construirPayload(
            antecipacao.getIdAntecipacao().toString(),
            String.valueOf(antecipacao.getDtAlteracao().getTime()),
            String.valueOf(expiration.getEpochSecond())
        );
        
        String token = DigestUtils.sha256Hex(payload);
        
        // Codificamos o token com o tempo de expiração para poder recuperá-lo depois
        String tokenCompleto = String.join(TOKEN_DELIMITER,
            antecipacao.getIdAntecipacao().toString(),
            String.valueOf(expiration.getEpochSecond()),
            token
        );
        
        logger.debug("Token gerado para antecipação {}: {}", antecipacao.getIdAntecipacao(), tokenCompleto);
        return Base64.getEncoder().encodeToString(tokenCompleto.getBytes());
    }

    /**
     * Valida se um token é válido para uma determinada antecipação
     */
    public boolean validarToken(Antecipacao antecipacao, String tokenCodificado) {
        try {
            // Decodifica o token recebido
            String tokenCompleto = new String(Base64.getDecoder().decode(tokenCodificado));
            String[] partes = tokenCompleto.split("\\" + TOKEN_DELIMITER);
            
            if (partes.length != 3) {
                logger.warn("Token com formato inválido");
                return false;
            }
            
            Long idAntecipacao = Long.parseLong(partes[0]);
            long expirationEpochSecond = Long.parseLong(partes[1]);
            String tokenRecebido = partes[2];
            
            // Verifica se o ID da antecipação corresponde
            if (!antecipacao.getIdAntecipacao().equals(idAntecipacao)) {
                logger.warn("Token não pertence a esta antecipação");
                return false;
            }
            
            // Verifica se o token expirou
            Instant expiration = Instant.ofEpochSecond(expirationEpochSecond);
            if (Instant.now().isAfter(expiration)) {
                logger.warn("Token expirado para antecipação {}", antecipacao.getIdAntecipacao());
                return false;
            }
            
            // Reconstrói o payload original com os mesmos parâmetros
            String payloadOriginal = construirPayload(
                partes[0], // idAntecipacao
                String.valueOf(antecipacao.getDtAlteracao().getTime()),
                partes[1]  // expirationEpochSecond
            );
            
            // Gera o hash esperado
            String tokenEsperado = DigestUtils.sha256Hex(payloadOriginal);
            
            // Compara com o token recebido
            return tokenEsperado.equals(tokenRecebido);
            
        } catch (Exception e) {
            logger.error("Erro ao validar token", e);
            return false;
        }
    }

    /**
     * Gera a URL para aprovação de antecipação
     */
    public String gerarUrlAprovacao(Antecipacao antecipacao, String baseUrl) {
        String token = gerarToken(antecipacao);
        return String.format("%s/api/antecipacoes/%d/aprovar?token=%s", 
            baseUrl, antecipacao.getIdAntecipacao(), token);
    }

    /**
     * Gera a URL para reprovação de antecipação
     */
    public String gerarUrlReprovacao(Antecipacao antecipacao, String baseUrl) {
        String token = gerarToken(antecipacao);
        return String.format("%s/api/antecipacoes/%d/reprovar?token=%s", 
            baseUrl, antecipacao.getIdAntecipacao(), token);
    }

    /**
     * Método auxiliar para construir o payload do token de forma consistente
     */
    private String construirPayload(String idAntecipacao, String timestampAlteracao, String expiration) {
        return String.join(TOKEN_DELIMITER,
            idAntecipacao,
            timestampAlteracao,
            expiration,
            SECRET_KEY
        );
    }
    
 // Adicionar este método para gerar tokens para o popup (sem modificar o payload)
    public String gerarTokenPopup(Antecipacao antecipacao) {
        // Usa o mesmo método de geração de token existente
        return gerarToken(antecipacao);
    }
    
}