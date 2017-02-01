package com.start_up.dev.apilinkus.Model;


import com.start_up.dev.apilinkus.API.APILinkUS;

/**
 * Created by Huong on 29/01/2017.
 */

public class Authentification {

    private static String userId;
    private static String firstName;
    private static String lastName;
    private static String userName;

    /*TOKENS*/
    private static String mode_auth;
    private static String access_token;
    private static String token_type;
    private static String refresh_token;

    public static String getUserId() {
        return userId;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getMode_auth() {
        return mode_auth;
    }

    public static String getAccess_token() {
        return access_token;
    }

    public static String getToken_type() {
        return token_type;
    }

    public static String getRefresh_token() {
        return refresh_token;
    }


    /**---------------SET : NE JAMAIS APPELER CES FONCTIONS SET AUTREMENT QUE DANS LA CLASSE DBHandler.
     * En effet les variables ne doivent être actualisées qu'en passant par la BD Ceci explique pourquoi
     * ces fonctions ne contiennent pas le mot clé public (restrein au package) */

    static void withBDsetUserId(String userId) {
        Authentification.userId = userId;
    }

    static void withBDsetFirstName(String firstName) {
        Authentification.firstName = firstName;
    }

    static void withBDsetLastName(String lastName) {
        Authentification.lastName = lastName;
    }

    static void withBDsetUserName(String userName) {
        Authentification.userName = userName;
    }

    static void withBDsetMode_auth(String mode_auth) {
        Authentification.mode_auth = mode_auth;
    }

    static void withBDsetAccess_token(String access_token) {
        Authentification.access_token = access_token;
    }

    static void withBDsetToken_type(String token_type) {
        Authentification.token_type = token_type;
    }

    static void withBDsetRefresh_token(String refresh_token) {
        Authentification.refresh_token = refresh_token;
    }
//NE CREER AUCUNE FONCTION ICI PASSER PAR LA BD LOCALE
}
