package models;

public class Card {
    private final String cardNumber;
    private final String pin;
    private final Account account;

    public Card(String cardNumber, String pin, Account account) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.account = account;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public Account getAccount() {
        return account;
    }
}
