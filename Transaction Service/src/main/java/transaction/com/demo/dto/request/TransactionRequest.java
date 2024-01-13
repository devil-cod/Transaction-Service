package transaction.com.demo.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import transaction.com.demo.enums.Currency;
import transaction.com.demo.enums.TransactionType;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private Long id;
    private TransactionType type;
    private Currency currency;
    private double amountInInr;
    private String amountInForeignCurrency;
}
