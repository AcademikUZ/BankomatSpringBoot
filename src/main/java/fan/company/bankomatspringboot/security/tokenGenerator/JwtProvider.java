package fan.company.bankomatspringboot.security.tokenGenerator;

import fan.company.bankomatspringboot.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private String keyForToken = "StrongPasswordForToken";
    private long expireTime = 1000 * 60 * 60 * 24; //tokenni amal qilish muddati | 1 minut

    public String generatorToken(String username, Role role) {
        Date expiteDate = new Date(System.currentTimeMillis() + expireTime);

        String token = Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiteDate)
                .claim("roles", role)
                .signWith(SignatureAlgorithm.HS512, keyForToken)
                .compact();
        return token;

    }

    public String getUsernameFromToken(String token) {
        try {

            String username = Jwts
                    .parser()
                    .setSigningKey(keyForToken)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            return username;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }


    }


}
