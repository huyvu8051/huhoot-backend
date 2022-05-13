package com.huhoot.auth;

import com.huhoot.TestObj;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {
    private final int SEVENT_DAYS = 1000 * 60 * 60 * 24 * 7; // 7 days

    @Value("${huhoot.secret-key}")
    private String SECRET_KEY = null;

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        return createToken(claims, userDetails.getUsername());
    }

    public void testToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        TestObj testObj = new TestObj();
        testObj.setUsername(userDetails.getUsername());
        testObj.setPassword(userDetails.getPassword());

        claims.put("user", testObj);
        String token = createToken(claims, userDetails.getUsername());

        Claims claims1 = this.extractAllClaims(token);
    }

    private String createToken(Map<String, Object> claims, String subject) {


        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SEVENT_DAYS))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String createComboToken(String username, String nextQuestionSign, String comboCount) {
        final String PER_QUESTION_AND_STUDENT_KEY = username + nextQuestionSign + SECRET_KEY;

        Map<String, Object> claims = new HashMap<>();

        String compact = Jwts.builder()
                .setClaims(claims)
                .setSubject(comboCount)
                .signWith(SignatureAlgorithm.HS256, PER_QUESTION_AND_STUDENT_KEY)
                .compact();


        return compact;


    }

    public int extractCombo(String token, String username, String questionSign) {

        final String PER_QUESTION_AND_STUDENT_KEY = username + questionSign + SECRET_KEY;

        int combo = 0;

        try {
            Claims claims1 = Jwts.parser().setSigningKey(PER_QUESTION_AND_STUDENT_KEY).parseClaimsJws(token).getBody();

            String subject = claims1.getSubject();

            combo = Integer.parseInt(subject);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return combo;
    }

    public String createAnswerResultToken(final String perQuestionEncryptKey, double totalPointsReceived, double comboPointsReceived,  int comboCount) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("totalPointsReceived", totalPointsReceived);
        claims.put("comboPointsReceived", comboPointsReceived);
        claims.put("comboCount", comboCount);

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, perQuestionEncryptKey)
                .compact();

        return token;
    }
}
