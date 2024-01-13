package transaction.com.demo.helper;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import transaction.com.demo.dao.RequestAuditDao;
import transaction.com.demo.dto.request.TransactionRequest;
import transaction.com.demo.model.TransactionAudit;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DbHelper {
    private static final Logger logger = LoggerFactory.getLogger(DbHelper.class);
    private final RequestAuditDao requestAuditDao;

    public DbHelper(RequestAuditDao requestAuditDao) { this.requestAuditDao = requestAuditDao; }

    public TransactionAudit saveToRequestAudit(TransactionRequest transactionRequest) {
        var transactionAudit = new TransactionAudit();
        try {
            transactionAudit.setCurrency(transactionRequest.getCurrency());
            transactionAudit.setMoney(transactionRequest.getAmountInInr());
            transactionAudit.setType(transactionRequest.getType());
            transactionAudit.setDate(LocalDateTime.now());
            try {
                requestAuditDao.save(transactionAudit);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        } catch (Exception exception) {
            logger.error("Exception occurred while saving data to request_audit table :: {}", exception.getMessage());
        }
        return transactionAudit;
    }
    public List<TransactionAudit> getAllTransactions() {
        return requestAuditDao.findAll();
    }

     public List<TransactionAudit> findAllByDateBetween(LocalDateTime startDate, LocalDateTime endDate){
        return requestAuditDao.findAllByDateBetween(startDate,endDate);
    }
}
