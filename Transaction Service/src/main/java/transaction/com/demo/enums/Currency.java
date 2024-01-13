package transaction.com.demo.enums;

public enum Currency {

    // Currenlty supporting only USD and INR
    USD("USD"),
    INR("INR"),
    EUR("EUR"),
    GBP("GBP"),
    AUD("AUD"),
    CAD("CAD"),
    SGD("SGD"),
    CHF("CHF"),
    MYR("MYR"),
    JPY("JPY"),
    CNY("CNY");

    private final String msg;

    Currency(String name) {
        this.msg = name;
    }

    public boolean equalsToString(String otherName) {
        return msg.equals(otherName);
    }

    public String getValue() {
        return this.msg;
    }

    public String getProperty() {
        return this.msg;
    }
}
