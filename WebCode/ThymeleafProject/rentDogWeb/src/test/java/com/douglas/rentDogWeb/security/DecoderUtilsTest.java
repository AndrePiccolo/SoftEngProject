package com.douglas.rentDogWeb.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecoderUtilsTest {

    String password = "12345";
    String emailSalt = "test@student.douglascollege.ca";
    String securePassword;
    boolean validPassword;

    @Test
    void generateSecurePassword() {
        String password = "12345";
        String emailSalt = "test@student.douglascollege.ca";
        securePassword = DecoderUtils.generateSecurePassword(password, emailSalt);

        assertNotEquals("12345", securePassword);
        assertNotNull(securePassword);
    }

    @Test
    void verifyUserPassword() {
        generateSecurePassword();
        // Test same password
        validPassword = DecoderUtils.verifyUserPassword(password, securePassword, emailSalt);
        assertTrue(validPassword);

        // Test different password
        validPassword = DecoderUtils.verifyUserPassword("1234", securePassword, emailSalt);
        assertFalse(validPassword);

        // Test empty password
        validPassword = DecoderUtils.verifyUserPassword("", securePassword, emailSalt);
        assertFalse(validPassword);

        // Test different salt
        validPassword = DecoderUtils.verifyUserPassword(password, securePassword, "salt-teste");
        assertFalse(validPassword);

        // Test empty salt
        try{
            validPassword = DecoderUtils.verifyUserPassword(password, securePassword, "");
            assertFalse(validPassword);
        } catch (IllegalArgumentException e){
            assertTrue(e.getMessage().contains("the salt parameter must not be empty"));
        }

    }
}