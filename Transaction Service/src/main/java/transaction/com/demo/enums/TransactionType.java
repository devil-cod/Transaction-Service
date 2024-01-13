package transaction.com.demo.enums;

public enum TransactionType {
    DEBIT("DEBIT"),
    CREDIT("CREDIT");

    private final String msg;

    TransactionType(String name) {
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
