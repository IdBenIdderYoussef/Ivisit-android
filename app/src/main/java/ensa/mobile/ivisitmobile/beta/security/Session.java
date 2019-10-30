package ensa.mobile.ivisitmobile.beta.security;

public interface Session {
    boolean isLoggedIn();

    void saveToken(String token);

    String getToken();

    void saveEmail(String email);

    String getEmail();

    void savePassword(String password);

    String getPassword();

    void invalidate();
}