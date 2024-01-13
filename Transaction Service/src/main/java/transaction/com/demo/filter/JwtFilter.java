package transaction.com.demo.filter;//package com.transaction.tsservice.filter;

import io.jsonwebtoken.Claims;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import transaction.com.demo.exception.TSApiException;
import transaction.com.demo.model.ClientDetails;
import transaction.com.demo.utils.CipherUtil;
import transaction.com.demo.utils.Constants;
import transaction.com.demo.utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Component
@ConfigurationProperties(prefix = "jwt")
@Setter
public class JwtFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtUtil jwtUtil;

    private String clientId;
    private String secret;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void validateJwtToken(HttpServletRequest request) throws TSApiException {
        String jwtToken = request.getHeader(Constants.AUTHORIZATION);
        Claims claims = null;
        try {
            LOG.info("JWT Details:: jwt-token={}", jwtToken);
            if (StringUtils.isBlank(jwtToken)) {
                throw new ServiceException("Invalid client ID / token");
            }
            var clientDetails = getJwtUserDetails();
            String decryptedSecret = decryptSecret(clientDetails);
            claims = jwtUtil.validateTokenAndGetClaims(decryptedSecret, jwtToken);

            request.setAttribute("claims", new HashMap<>(claims));

        } catch (Exception ex) {
            LOG.info("JWT Authentication failed. QueryString: {}, Claims: {}, jwtToken: {}. Error: {}",
                request.getQueryString(), claims, jwtToken, ex.getMessage());
            throw new TSApiException(ex.getMessage());
        }
    }

    public ClientDetails getJwtUserDetails() throws ServiceException {
        var clientDetails = new ClientDetails(clientId, secret);
        if (clientDetails == null ||
            StringUtils.isEmpty(clientDetails.getClientId()) ||
            StringUtils.isEmpty(clientDetails.getSecret())) {
            LOG.error("Client ID/ secret not found in DB");
            throw new ServiceException("Client ID/ secret not found in DB");
        }
        return clientDetails;
    }

    private String decryptSecret(ClientDetails clientDetails) throws ServiceException {
        try {
            return CipherUtil.getAesCipher().decryptKey(clientDetails.getSecret());
        } catch (Exception e) {
            LOG.error("Error in decrypting jwt token {}", e.getMessage());
            throw new ServiceException("Error in decrypting jwt token");
        }
    }

}



