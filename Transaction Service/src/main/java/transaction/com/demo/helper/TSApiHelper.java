package transaction.com.demo.helper;


import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import reactor.core.publisher.Mono;
import transaction.com.demo.dto.response.ApiResponse;
import transaction.com.demo.exception.TSApiException;
import transaction.com.demo.utils.Constants;
import transaction.com.demo.utils.Utils;

import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
@Setter
public class TSApiHelper {
    private static final Logger logger = LoggerFactory.getLogger(TSApiHelper.class);
    private final WebClient webClient;
    public TSApiHelper(WebClient webClient) {
        this.webClient = webClient;
    }
    public ApiResponse makeApiCall(String url, HttpMethod httpMethod, Map<String, Object> params,
                                   Integer requestId, Map<String, String> headers) {
        var apiResponse = new ApiResponse();
        try {
            var startTime = System.currentTimeMillis();
            var uri = new URI(url);
            String response = TsApiCall(uri, httpMethod, params, new HashMap<>());
            var responseTime = System.currentTimeMillis() - startTime;
            apiResponse.setResponse(Utils.getCleanedBody(response));
            apiResponse.setSuccess(true);
            apiResponse.setHttpStatus(HttpStatus.OK);
            apiResponse.setResponseTime(responseTime);
        }  catch (Exception e) {
            logger.error("Exception in Api Calls id: {} ", requestId, e);
            apiResponse.setErrorMessage(Constants.ERROR + e.getMessage());
            apiResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return apiResponse;
    }

    public String TsApiCall(URI uri, HttpMethod httpMethod, Map<String, Object> paramMap,
                            Map<String, String> headers) {
        RequestHeadersSpec<?> body;
        switch (httpMethod.name()) {
            case "POST", "PUT" -> {
                WebClient.RequestBodyUriSpec request = HttpMethod.PUT == httpMethod ? webClient.put() :
                    webClient.post();
                WebClient.RequestBodySpec requestUrl = request.uri(uri);
                headers.forEach(requestUrl::header);
                body = requestUrl.body(BodyInserters.fromValue(paramMap.getOrDefault("request", new Object())));
            }
            case "GET" -> {
                RequestHeadersSpec<?> getRequestUrl = webClient.get().uri(uri);
                headers.forEach(getRequestUrl::header);
                body = getRequestUrl;
            }
            default -> throw new TSApiException("APIs http Method is not supported");
        }
        return body
            .retrieve()
//            .onStatus(HttpStatus::is5xxServerError,
//                (ClientResponse response) ->
//                    response.bodyToMono(String.class).flatMap(
//                        error -> Mono.error(new TSApiException(error))
//                    )
//            )
            .bodyToMono(String.class)
            .timeout(Duration.ofSeconds((Long) paramMap.getOrDefault("timeOut", Constants.DEFAULT_TIMEOUT)))
            .block();
    }


}