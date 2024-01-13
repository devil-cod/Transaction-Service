package transaction.com.demo.filter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import transaction.com.demo.exception.TSApiException;
import transaction.com.demo.utils.Constants;
import transaction.com.demo.utils.Utils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE)
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);
    private final JwtFilter jwtFilter;

    public JwtRequestFilter(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    private static void setRequestId(HttpServletRequest request) {
        log.info("Header Names :: {}", request.getHeaderNames());
        Enumeration<String> enum1 = request.getHeaderNames();
        //todo added for checking the header names.
        while (enum1.hasMoreElements()) {
            log.info(enum1.nextElement());
        }
        String requestId = request.getHeader(Constants.X_REQUEST_ID);
        if (null != requestId) {
            ThreadContext.put(Constants.REQUEST_ID_KEY_UNIVERSAL, request.getHeader(Constants.X_REQUEST_ID));
        } else {
            requestId = UUID.randomUUID().toString().replace("-", "");
            ThreadContext.put(Constants.REQUEST_ID_KEY_UNIVERSAL, requestId);
        }
        log.info("Request Id is :: {} for :: {}", requestId, request.getServletPath());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("api request path : {}", request.getServletPath());
        setRequestId(request);
        log.info("Request received for API");
        if (
            request.getServletPath().contains("api/v1/transaction")
            //FOR AUTHORIZATION WE HAVE PUSHED EVERY REQUEST WILL COME AT THIS POINT ONLY FIRST
        ) {
            try {
                jwtFilter.validateJwtToken(request);
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                log.info("Failed to authenticate the request :: {}", ExceptionUtils.getStackTrace(e));
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(Utils.getObjectMapper().writeValueAsString(new TSApiException(
                    e.getMessage())));
                response.setContentType(Constants.APPLICATION_JSON);
            }

        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        logger.info("Removing headers from threadLocal");
        ThreadContext.clearAll();
    }

}
