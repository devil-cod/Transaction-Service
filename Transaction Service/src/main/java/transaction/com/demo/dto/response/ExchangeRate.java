package transaction.com.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {
    private boolean success;
    private String terms;
    private String privacy;
    private long timestamp;
    private String date;
    private String base;
    private Map<String, Double> rates;

    public ExchangeRate(Map<String, Object> data) {
        this.success = (boolean) data.get("success");
        this.terms = (String) data.get("terms");
        this.privacy = (String) data.get("privacy");
        this.timestamp = (long) data.get("timestamp");
        this.date = (String) data.get("date");
        this.base = (String) data.get("base");
        this.rates = (Map<String, Double>) data.get("rates");
    }

    public double getExchangeRate(String currencyCode) {
        if (rates.containsKey(currencyCode)) {
            return rates.get(currencyCode);
        } else {
            return -1; // Return a default value or handle the case as needed
        }
    }
}


