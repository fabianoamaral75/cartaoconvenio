package br.com.uaitagcartaoconvenio.cartaoconvenio.util;

import java.util.HashMap;
import java.util.Map;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;
	private final String userMessage;
    private final String developerMessage;
    private final Map<String, Object> details;
    
    public BusinessException(String userMessage, String developerMessage) {
        this.userMessage = userMessage;
        this.developerMessage = developerMessage;
        this.details = new HashMap<>();
    }
    
    // Métodos para adicionar detalhes
    public BusinessException addDetail(String key, Object value) {
        this.details.put(key, value);
        return this;
    }

	public String getUserMessage() {
		return userMessage;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public Map<String, Object> getDetails() {
		return details;
	}
    
    

}
