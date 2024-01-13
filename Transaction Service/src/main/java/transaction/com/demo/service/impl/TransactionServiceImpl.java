package transaction.com.demo.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.util.internal.StringUtil;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import transaction.com.demo.dto.request.TransactionRequest;
import transaction.com.demo.dto.response.ApiResponse;
import transaction.com.demo.dto.response.ExchangeRate;
import transaction.com.demo.enums.Currency;
import transaction.com.demo.helper.DbHelper;
import transaction.com.demo.helper.TSApiHelper;
import transaction.com.demo.model.TransactionAudit;
import transaction.com.demo.service.TransactionflowService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Service
@Setter
@ConfigurationProperties(prefix = "currency")
public class TransactionServiceImpl implements TransactionflowService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    private final TSApiHelper tsApiHelper;
    private final DbHelper dbHelper;
    private String url;


    public TransactionServiceImpl(TSApiHelper tsApiHelper, DbHelper dbHelper) {
        this.tsApiHelper = tsApiHelper;
        this.dbHelper = dbHelper;
    }


    @Override
    public ApiResponse recordTransaction(TransactionRequest transaction) {
        logger.info("TransactionServiceImpl.recordTransaction() called");
        if (transaction == null) {
            return getDefaultApiResponse("Empty Request Body", HttpStatus.BAD_REQUEST);
        }
        if (transaction.getCurrency() == null) {
            return getDefaultApiResponse("Currency is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (!transaction.getCurrency().name().equalsIgnoreCase(Currency.INR.name()) && !transaction.getCurrency().name().equalsIgnoreCase(Currency.USD.name())) {
            return getDefaultApiResponse("Currency is not valid", HttpStatus.BAD_REQUEST);
        }

        if (transaction.getCurrency().name().equalsIgnoreCase(Currency.USD.name())) {
            convertToINR(transaction);
        }
        var transactionAudit = dbHelper.saveToRequestAudit(transaction);
        if (transactionAudit == null) {
            return getDefaultApiResponse("Error while saving data to request_audit table", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        response.setData(transactionAudit);
        response.setHttpStatus(HttpStatus.OK);
        return response;
    }

    private void convertToINR(TransactionRequest transaction) {
        logger.info("TransactionServiceImpl.converttoINR() called");
        ApiResponse apiResponse = tsApiHelper.makeApiCall(url, HttpMethod.GET, new HashMap<>(), 1, new HashMap<>());

        try {
            ExchangeRate exchangeRate = new ObjectMapper().readValue(apiResponse.getResponse(), ExchangeRate.class);
            Double amount = Double.parseDouble(transaction.getAmountInForeignCurrency());
            //BigDecimal famount = BigDecimal.valueOf(amount * exchangeRate.getExchangeRate(Currency.INR.name()));
            amount = amount * exchangeRate.getExchangeRate(Currency.INR.name());
            transaction.setAmountInInr((amount));
        } catch (JsonProcessingException e) {
            logger.warn("Unable to process the request");
        }
    }

    private static ApiResponse getDefaultApiResponse(String message, HttpStatus httpStatus) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(false);
        response.setResponse(message);
        response.setHttpStatus(httpStatus);
        return response;
    }

    @Override
    public ApiResponse getTransactionsByDate(String dateString) {
        List<TransactionAudit> getAllTransactions = null;
        if (!StringUtil.isNullOrEmpty(dateString)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
            getAllTransactions = dbHelper.getAllTransactionsByDate(localDateTime);
        } else {
            getAllTransactions = dbHelper.getAllTransactions();
        }
        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        response.setData(getAllTransactions);
        response.setHttpStatus(HttpStatus.OK);
        return response;
    }
}
