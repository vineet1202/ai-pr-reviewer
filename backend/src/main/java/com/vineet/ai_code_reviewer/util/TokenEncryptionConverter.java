package com.vineet.ai_code_reviewer.util;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Component
@Converter
public class TokenEncryptionConverter implements AttributeConverter<String, String> {

    private static StringEncryptor encryptor;

    @Autowired
    public void setEncryptor(StringEncryptor encryptor) {
        TokenEncryptionConverter.encryptor = encryptor;
    }

    @Override
    public String convertToDatabaseColumn(String plainToken) {
        return plainToken == null ? null : encryptor.encrypt(plainToken);
    }

    @Override
    public String convertToEntityAttribute(String encryptedToken) {
        return encryptedToken == null ? null : encryptor.decrypt(encryptedToken);
    }
}