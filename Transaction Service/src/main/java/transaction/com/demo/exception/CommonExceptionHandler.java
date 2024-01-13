package transaction.com.demo.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import transaction.com.demo.dto.response.ApiResponse;
import transaction.com.demo.utils.Constants;

@RestControllerAdvice
public class CommonExceptionHandler {

    Logger log = LoggerFactory.getLogger(CommonExceptionHandler.class);

    @ExceptionHandler(value = TSBadRequest.class)
    ResponseEntity handleCbsException(TSBadRequest exception) throws JsonProcessingException {
        log.error(Constants.EXCEPTION_OCCURRED, exception);
        var apiResponse = new ApiResponse();
        apiResponse.setSuccess(false);
        apiResponse.setErrorMessage(exception.getMessage());
        apiResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @ExceptionHandler(value = TSApiException.class)
    ResponseEntity handleTXException(TSApiException exception) throws JsonProcessingException {
        log.error(Constants.EXCEPTION_OCCURRED, exception);
        var apiResponse = new ApiResponse();
        apiResponse.setSuccess(false);
        apiResponse.setErrorMessage(exception.getMessage());
        apiResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
