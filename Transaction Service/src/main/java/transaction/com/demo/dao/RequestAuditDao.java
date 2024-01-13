package transaction.com.demo.dao;



import org.springframework.stereotype.Repository;
import transaction.com.demo.model.TransactionAudit;
import transaction.com.demo.repository.RequestAuditRepo;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RequestAuditDao {
    private final RequestAuditRepo requestAuditRepo;

    public RequestAuditDao(RequestAuditRepo requestAuditRepo) { this.requestAuditRepo = requestAuditRepo; }

    public void save(TransactionAudit transactionAudit) {
         requestAuditRepo.save(transactionAudit);
    }

    public List<TransactionAudit> findAll() {
        return requestAuditRepo.findAll();
    }
    public List<TransactionAudit> findByDate(LocalDateTime date) {
        return requestAuditRepo.findByDate(date);
    }

}
