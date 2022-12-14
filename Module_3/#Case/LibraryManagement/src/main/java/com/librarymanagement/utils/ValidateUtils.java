package com.librarymanagement.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class ValidateUtils {
//    public static final String USERNAME_PATTERN = "^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
    public static final String USERNAME_PATTERN = "^[A-Za-z0-9_]{4,20}$";
    public static final String PASSWORD_PATTERN = "^([a-zA-Z!@#$%^&*()_+=,./;':|0-9]{6,})$";
    public static final String FIST_CASE_PATTERN = "^([A-Z]+[a-z]* ?)+$";
    public static final String PHONE_REGEX = "^0[1-9][0-9]{8}$";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,3}$";
    public static final String ISBN_REGEX = "^(?:ISBN(?:-10)?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$)[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";

    public static boolean isPasswordValid(String password) {
        return Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
    }

    public static boolean isUsernameValid(String username) {
        return Pattern.compile(USERNAME_PATTERN).matcher(username).matches();
    }

    public static boolean isFirstCaseValid(String name) {
        String temp = Normalizer.normalize(name, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String normalizedName =  pattern.matcher(temp).replaceAll("");
        return Pattern.compile(FIST_CASE_PATTERN).matcher(normalizedName).matches();
    }

    public static boolean isPhoneValid(String number) {
        return Pattern.compile(PHONE_REGEX).matcher(number).matches();
    }

    public static boolean isEmailValid(String email) {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    public static boolean isIsbnValid(String isbn){return Pattern.compile(ISBN_REGEX).matcher(isbn).matches();}

}
