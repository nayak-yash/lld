package factory;

import enums.PaymentType;
import strategy.payment.CardPaymentStrategy;
import strategy.payment.PaymentStrategy;
import strategy.payment.UpiPaymentStrategy;

public class PaymentStrategyFactory {
    public static PaymentStrategy getStrategy(PaymentType paymentType) {
        return switch (paymentType) {
            case CARD -> new CardPaymentStrategy();
            case UPI -> new UpiPaymentStrategy();
        };
    }
}
