package net.vokacom.lightvote.hubtel.payments.Interface;

public interface OnPaymentResponse {
    void onFailed(String token, String reason);
    void onCancelled();
    void onSuccessful(String token);
}
