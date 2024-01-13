package transaction.com.demo.model;


import lombok.Data;
import transaction.com.demo.enums.Currency;
import transaction.com.demo.enums.TransactionType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "transactional")
public class TransactionAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;
    @Column(name = "money")
    private double money;
    @Column(name = "date")
    private LocalDateTime date;
}
