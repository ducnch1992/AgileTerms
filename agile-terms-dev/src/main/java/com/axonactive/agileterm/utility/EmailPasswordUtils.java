package com.axonactive.agileterm.utility;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EmailPasswordUtils {

    private EmailPasswordUtils(){
    }
    public static final String EMAIL_VALIDATOR = "^[a-zA-Z]+[0-9a-zA-Z._]*@[a-zA-Z0-9]{3,10}(\\.[a-zA-Z]+){1,3}$";

    public static final String SPECIAL_CHARACTER_VALIDATOR = "^(?=.*[@$!%*?&^#\\{}\\[\\]`~\\(\\)\\|\"';:\\/.,<>_\\+=\\\\-])[A-Za-z\\d@$!%*?&^#\\{}\\[\\]`~\\(\\)\\|\"';:\\/.,<>_\\+=\\\\-]*$";

    public static Boolean isEmailValid(String inputEmail) {

        Pattern emailPattern = Pattern.compile(EMAIL_VALIDATOR);
        Matcher emailChecker = emailPattern.matcher(inputEmail);

        return emailChecker.find();
    }

    public static Boolean isPasswordValid(String inputPassword) {
        String whiteSpace = " ";

        if (inputPassword.contains(whiteSpace)) {
            return false;
        }

        if (inputPassword.length() < 10) {
            return false;
        }

        int numberPoint = 0;
        int uppercasePoint = 0;
        int lowercasePoint = 0;
        int specialCharacterPoint = 0;

        for (int i = 0; i < inputPassword.length(); i++) {
            if (Character.isDigit(inputPassword.charAt(i))) {
                numberPoint = 1;
            }
            if (Character.isLowerCase(inputPassword.charAt(i))) {
                lowercasePoint = 1;
            }
            if (Character.isUpperCase(inputPassword.charAt(i))) {
                uppercasePoint = 1;
            }
            if (numberPoint + uppercasePoint + lowercasePoint == 3) {
                return true;
            }
        }

        Pattern specialCharacterPattern = Pattern.compile(SPECIAL_CHARACTER_VALIDATOR);
        Matcher specialCharacterChecker = specialCharacterPattern.matcher(inputPassword);

        if (specialCharacterChecker.find()) {
            specialCharacterPoint = 1;
        }

        return numberPoint + lowercasePoint + uppercasePoint + specialCharacterPoint == 3;
    }
}
