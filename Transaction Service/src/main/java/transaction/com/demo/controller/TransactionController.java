package transaction.com.demo.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import transaction.com.demo.dto.request.TransactionRequest;
import transaction.com.demo.dto.response.ApiResponse;
import transaction.com.demo.service.TransactionflowService;


@RestController
@RequestMapping("api/v1")
public class TransactionController{
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionflowService transactionflowService;

    public TransactionController(TransactionflowService transactionflowService) { this.transactionflowService = transactionflowService; }

    @PostMapping("/transaction")
    public ApiResponse recordTransaction(@RequestBody TransactionRequest transaction) {
        return transactionflowService.recordTransaction(transaction);
    }

    @GetMapping("/transaction")
    public ApiResponse getTransactions(
        @RequestParam(value = "date",required = false) String date) {
        return transactionflowService.getTransactionsByDate(date);
    }

}
