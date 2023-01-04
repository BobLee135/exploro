package com.example.exploro;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelpFunctions {
    public boolean nonEmptyField(String fullName, String email, String username, String password, String confirmPassword){
        if (fullName.length() == 0
                || email.length() == 0
                || username.length() == 0
                || password.length() == 0
                || confirmPassword.length() == 0) {
            return false;
        }
        return true;
    }
    public boolean validEmail(String email){
        Pattern emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher emailValidator = emailRegex.matcher(email);
        if(!emailValidator.find()) {
            return false;
        }
        return true;

    }
    public boolean validPasswordUppercase(String password) {
        Pattern upperCase = Pattern.compile("[A-Z]");
        Matcher hasUpperCase = upperCase.matcher(password);
        if (!hasUpperCase.find()) {
            return false;
        }
        return true;
    }
    public boolean validPasswordDigit(String password) {
        Pattern digit = Pattern.compile("[0-9]");
        Matcher hasDigit = digit.matcher(password);
        if (!hasDigit.find()) {
            return false;
        }
        return true;
    }
    public boolean validPasswordSpecial(String password) {
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Matcher hasSpecial = special.matcher(password);
        if (!hasSpecial.find()) {
            return false;
        }
        return true;
    }
    public boolean validPasswordLength(String password) {
        if (password.length() < 8 || password.length() > 16) {
            return false;
        }
        return true;
    }
    public boolean validPasswordConfirm(String password, String confirmPassword) {
        if (!confirmPassword.equals(password)) {
            return false;
        }
        return true;
    }


}
