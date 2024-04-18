package pizzahub.api.utils;

import java.util.regex.*;

public class RegexValidator {
    private static String cpfRegex = "(^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$)";
    private static String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private static String cepRegex = "(^[0-9]{5})-?([0-9]{3}$)";
    private static String telephoneNumberRegex = "^\\(?(\\d{2})\\)?[-.\\s]?(\\d{4,5})[-.\\s]?(\\d{4})$";

    public static boolean validateCPF(String validating) {
        return Pattern.matches(cpfRegex, validating);
    }

    public static boolean validateCEP(String validating) {
        return Pattern.matches(cepRegex, validating);
    }

    public static boolean validateEmail(String validating) {
        return Pattern.matches(emailRegex, validating);
    }

    public static boolean validateTelephoneNumber(String validating) {
        return Pattern.matches(telephoneNumberRegex, validating);
    }
}
