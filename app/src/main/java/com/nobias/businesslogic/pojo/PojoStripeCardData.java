package com.nobias.businesslogic.pojo;

import com.stripe.android.model.PaymentMethod;

import java.io.Serializable;

public class PojoStripeCardData implements Serializable{
    private String cardType;
    private String cardNumber;
    private String cardCvv;
    private String cardExpiry;
    private String cardLastFourDigit;
    private PaymentMethod.Card card;

    public PaymentMethod.Card getCard() {
        return card;
    }

    public void setCard(PaymentMethod.Card card) {
        this.card = card;
    }

    public String getCardLastFourDigit() {
        return cardLastFourDigit;
    }

    public void setCardLastFourDigit(String cardLastFourDigit) {
        this.cardLastFourDigit = cardLastFourDigit;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardCvv() {
        return cardCvv;
    }

    public void setCardCvv(String cardCvv) {
        this.cardCvv = cardCvv;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }
}
