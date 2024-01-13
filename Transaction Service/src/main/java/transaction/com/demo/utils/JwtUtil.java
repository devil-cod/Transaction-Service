package transaction.com.demo.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt_token_validity.ttl.sec}")
    private long jwtTokenValidity;

    public Claims validateTokenAndGetClaims(String decryptedSecret, String token) throws ServiceException {
        try {
            var claims = getClaimFromToken(decryptedSecret, token);
            checkForTokenExpiry(claims);
            return claims;
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private void checkForTokenExpiry(Claims claims) throws ServiceException {
        var issueDate = claims.getIssuedAt();
        long currTimeInMilliseconds = System.currentTimeMillis();
        var lowerDate = new Date(currTimeInMilliseconds - (jwtTokenValidity * 1000));
        var upperDate = new Date(currTimeInMilliseconds + (jwtTokenValidity * 1000));
        boolean isTokenExpired = issueDate.after(upperDate) || issueDate.before(lowerDate);
        if (isTokenExpired) {
            throw new ServiceException("JWT Token expired");
        }
    }

    public Claims getClaimFromToken(String decryptedSecret, String token) throws ServiceException {
        try {
            return Jwts.parser()
                .setSigningKey(decryptedSecret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();

        } catch (Exception ex) {
            throw new ServiceException("Invalid Jwt Token: " + ex.getMessage());
        }
    }
}
