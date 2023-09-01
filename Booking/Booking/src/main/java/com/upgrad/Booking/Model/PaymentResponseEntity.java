package com.upgrad.Booking.Model;

import java.util.Objects;

public class PaymentResponseEntity {
    private String message;
    private int statusCode;

    public PaymentResponseEntity(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public PaymentResponseEntity() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "PaymentResponseEntity{" +
                "message='" + message + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentResponseEntity that)) return false;
        return statusCode == that.statusCode && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, statusCode);
    }
}
