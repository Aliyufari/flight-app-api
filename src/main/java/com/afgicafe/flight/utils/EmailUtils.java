package com.afgicafe.flight.utils;

public class EmailUtils {
    public static String getEmailVerificationMessage (String name, String host, String token) {
        return "Congratulations " + name + ", \n\n" +
                "Your new account has been created. \n\n" +
                "Please click the link below to verify your account. \n\n" +
                getVerificationUrl(host, token);
    }

    private static String getVerificationUrl(String host, String token) {
        return host + "/api/v1/auth/verify-email?token=" + token;
    }
}
