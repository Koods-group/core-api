package ci.koodysgroup.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;


@Service
public class JwtService {

    private static final String SECRET_KEY = "ddd004208ab2f9a759ea1d86b6f4bb404891fb23e0e9cac71409a41f8b1a158";

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String login = extractUserLogin(token);
        return (login.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith((SecretKey) this.getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Key getSignInKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Date dateExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUserLogin(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(String user) {
        Map<String, Object> claims = new HashMap<>();
        return this.generateToken(claims,user);
    }

    public String generateToken(Map<String, Object> extraClaims,
                                String userDetails) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(this.getSignInKey())
                .compact();
    }
}
