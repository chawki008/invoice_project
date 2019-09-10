package com.cheikh.invoice.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String SAISISSEUR = "ROLE_SAISISSEUR";

    public static final String VERIFICATEUR = "ROLE_VERIFICATEUR";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
