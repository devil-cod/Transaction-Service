package transaction.com.demo.service;


import org.springframework.stereotype.Service;
import transaction.com.demo.dto.request.TransactionRequest;
import transaction.com.demo.dto.response.ApiResponse;

@Service
public interface TransactionflowService {

     ApiResponse recordTransaction(TransactionRequest transaction);

     ApiResponse getTransactionsByDate(String date);
}
