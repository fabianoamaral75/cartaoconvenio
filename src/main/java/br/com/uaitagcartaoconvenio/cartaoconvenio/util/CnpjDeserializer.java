package br.com.uaitagcartaoconvenio.cartaoconvenio.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CnpjDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) 
        throws IOException {
        
        String value = p.getValueAsString();
        return value != null ? value.replaceAll("[^0-9]", "") : null;
    }
}