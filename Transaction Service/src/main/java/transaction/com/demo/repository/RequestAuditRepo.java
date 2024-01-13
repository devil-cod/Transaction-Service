package transaction.com.demo.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import transaction.com.demo.model.TransactionAudit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RequestAuditRepo extends JpaRepository<TransactionAudit, Long> {

    List<TransactionAudit> findByDate(LocalDateTime date);
}
