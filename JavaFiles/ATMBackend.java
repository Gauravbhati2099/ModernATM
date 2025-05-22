package ModernATM;

import java.util.ArrayList;
import java.util.List;

public class ATMBackend {
    private String correctPin = "1234";
    private double balance = 1252.12;
    private String accountNumber = "01978291";

    public static final double MAX_TRANSACTION_AMOUNT = 10000.00;

    private List<TransactionRecord> history = new ArrayList<>();

    public enum TransactionResult {
        SUCCESS,
        INSUFFICIENT_FUNDS,
        INVALID_AMOUNT,
        EXCEEDS_LIMIT
    }

    public static class TransactionRecord {
        public final String type;
        public final double amount;
        public final double balanceAfter;
        public final long timestamp;
        public TransactionRecord(String type, double amount, double balanceAfter) {
            this.type = type;
            this.amount = amount;
            this.balanceAfter = balanceAfter;
            this.timestamp = System.currentTimeMillis();
        }
    }

    public boolean validatePin(String pin) {
        return correctPin.equals(pin);
    }
    public double getBalance() {
        recordTransaction("Balance", 0);
        return balance;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public List<TransactionRecord> getTransactionHistory() {
        return new ArrayList<>(history);
    }
    public boolean changePin(String currentPin, String newPin) {
        if (validatePin(currentPin) && newPin != null && newPin.matches("\\d{4}")) {
            correctPin = newPin;
            return true;
        }
        return false;
    }
    public void recordTransaction(String type, double amount) {
        history.add(new TransactionRecord(type, amount, balance));
    }
    public TransactionResult tryWithdraw(double amount) {
        if (amount <= 0) return TransactionResult.INVALID_AMOUNT;
        if (amount > MAX_TRANSACTION_AMOUNT) return TransactionResult.EXCEEDS_LIMIT;
        if (amount > balance) return TransactionResult.INSUFFICIENT_FUNDS;
        balance -= amount;
        recordTransaction("Withdraw", amount);
        return TransactionResult.SUCCESS;
    }
    public TransactionResult tryDeposit(double amount) {
        if (amount <= 0) return TransactionResult.INVALID_AMOUNT;
        if (amount > MAX_TRANSACTION_AMOUNT) return TransactionResult.EXCEEDS_LIMIT;
        balance += amount;
        recordTransaction("Deposit", amount);
        return TransactionResult.SUCCESS;
    }
} 