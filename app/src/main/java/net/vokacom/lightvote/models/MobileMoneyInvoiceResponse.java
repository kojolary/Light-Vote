package net.vokacom.lightvote.models;

public class MobileMoneyInvoiceResponse {
    private String token;
    private String message;
    private boolean status;

    public String getToken() {
        return token;
    }

    public MobileMoneyInvoiceResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public MobileMoneyInvoiceResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean isSuccessful(){
        return status;
    }
}
