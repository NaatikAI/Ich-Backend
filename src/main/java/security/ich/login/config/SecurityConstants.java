package security.ich.login.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class SecurityConstants {
    public static final long EXPIRATION_DATE = 86400; // 1 día
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/usuarios";
    public static final Key SECRECT_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
}
