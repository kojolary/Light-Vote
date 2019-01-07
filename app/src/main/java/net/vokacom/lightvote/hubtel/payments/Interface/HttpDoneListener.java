package net.vokacom.lightvote.hubtel.payments.Interface;

import net.vokacom.lightvote.hubtel.payments.Exception.HubtelPaymentException;

public interface HttpDoneListener {
    void onRequestCompleted(String message) throws HubtelPaymentException;
}
