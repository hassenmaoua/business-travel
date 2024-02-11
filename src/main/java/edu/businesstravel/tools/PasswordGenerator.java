package edu.businesstravel.tools;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=<>?";

    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder();

        // Generate 3 random uppercase letters
        appendRandomChars(password, UPPERCASE, 3);

        // Generate 3 random lowercase letters
        appendRandomChars(password, LOWERCASE, 3);

        // Generate 3 random numbers
        appendRandomChars(password, NUMBERS, 3);

        // Generate 3 random special characters
        appendRandomChars(password, SPECIAL_CHARACTERS, 3);

        // Shuffle the characters to make the password more random
        shuffle(password);

        return password.toString();
    }

    private static void appendRandomChars(StringBuilder password, String source, int count) {
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < count; i++) {
            int index = random.nextInt(source.length());
            password.append(source.charAt(index));
        }
    }

    private static void shuffle(StringBuilder password) {
        for (int i = password.length() - 1; i > 0; i--) {
            int index = (int) (Math.random() * (i + 1));
            char temp = password.charAt(index);
            password.setCharAt(index, password.charAt(i));
            password.setCharAt(i, temp);
        }
    }

    public static void main(String[] args) {
        String randomPassword = generateRandomPassword();
        System.out.println("Random Password: " + randomPassword);
    }
}

