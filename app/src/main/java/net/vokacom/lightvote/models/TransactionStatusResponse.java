package net.vokacom.lightvote.models;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class TransactionStatusResponse {
    private String message;

    @SerializedName("status")
    private boolean status;

    @SerializedName("transaction_status")
    private String transactionStatus;

    public String getMessage() {
        return message;
    }

    public boolean isPaid() {
        return !TextUtils.isEmpty(transactionStatus) && transactionStatus.equalsIgnoreCase("successful");
    }

    public boolean isPending() {
        return !TextUtils.isEmpty(transactionStatus) && transactionStatus.equalsIgnoreCase("pending");
    }

    public boolean getStatus() {
        return status;
    }
}
