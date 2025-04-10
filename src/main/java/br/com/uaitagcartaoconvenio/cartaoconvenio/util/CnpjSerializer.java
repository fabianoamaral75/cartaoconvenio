package br.com.uaitagcartaoconvenio.cartaoconvenio.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CnpjSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) 
        throws IOException {
        
        if (value != null && value.length() == 14) {
            String formatted = String.format("%s.%s.%s/%s-%s", 
                value.substring(0, 2),
                value.substring(2, 5),
                value.substring(5, 8),
                value.substring(8, 12),
                value.substring(12));
            gen.writeString(formatted);
        } else {
            gen.writeString(value);
        }
    }
}

