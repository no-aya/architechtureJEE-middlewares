package ma.enset.ebankingbackend.exceptions;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String insufficientBalance) {
        super(insufficientBalance);
    }
}
