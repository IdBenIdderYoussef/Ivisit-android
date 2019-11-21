package ensa.mobile.ivisitmobile.beta.activity;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validator {
    public boolean exist ;

    public boolean isValidEmail(String string){
        final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();

    }

    public boolean isValidUsername(final String string) throws InterruptedException {

        return false;
    }

    public boolean isValidPassword(String string){
        if (string.length()>6) return true;
        else return false;
    }

    public boolean isNullOrEmpty(String string){
        return TextUtils.isEmpty(string);
    }



}
