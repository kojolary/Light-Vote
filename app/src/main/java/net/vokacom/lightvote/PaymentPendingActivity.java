package net.vokacom.lightvote;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import net.vokacom.lightvote.models.MobileMoneyInvoiceResponse;
import net.vokacom.lightvote.models.TransactionStatusResponse;

import java.util.HashMap;
import java.util.Map;

public class PaymentPendingActivity extends AppCompatActivity {

    String URL = "https://app.tedxafariwaa.com/Vote/hubtel-verify.php?token=";
    String token;
    boolean activityPaused;
    private String nameOfContestant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_pending);

        token = getIntent().getStringExtra("token");
        nameOfContestant = getIntent().getStringExtra("name");

        checkPaymentStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityPaused = false;
    }

    private void checkPaymentStatus() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL+token, new Response.Listener<String>() {
            @SuppressLint("ShowToast")
            @Override
            public void onResponse(String response) {
                Log.d("PaymentPending", "response: "+response);

                TransactionStatusResponse transactionStatusResponse = new Gson().fromJson(response, TransactionStatusResponse.class);

                Intent data = new Intent();
                data.putExtra("name", nameOfContestant);
                if(transactionStatusResponse.getStatus()){
                    if(transactionStatusResponse.isPaid()){
                        setResult(RESULT_OK, data);
                        finish();
                    }else if(transactionStatusResponse.isPending()){
                        // try check status again
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(!activityPaused){
                                    checkPaymentStatus();
                                }
                            }
                        }, 1000*3);
                    }else{
                        setResult(RESULT_CANCELED, data);
                        finish();
                    }
                } else {
                    setResult(RESULT_CANCELED, data);
                    finish();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ContestantsDetail", error.toString());
                error.printStackTrace();
                
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Hiding the progress dialog after all task complete.

                        // Show timeout error message
                        Toast.makeText(PaymentPendingActivity.this,
                                "Check Internet Connection",
                                Toast.LENGTH_LONG).show();
                    }
                } else if (error instanceof NetworkError) {
                    // Show timeout error message
                    Toast.makeText(PaymentPendingActivity.this,
                            "Oops. Network error!",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    // Show timeout error message
                    Toast.makeText(PaymentPendingActivity.this,
                            "Oops. No Internet!",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    // Show timeout error message
                    Toast.makeText(PaymentPendingActivity.this,
                            "Oops. Authentication error!",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    // Show timeout error message
                    Toast.makeText(PaymentPendingActivity.this,
                            "Oops. Timeout error!",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof TimeoutError) {
                    // Show timeout error message
                    Toast.makeText(PaymentPendingActivity.this,
                            "Oops. Timeout error!",
                            Toast.LENGTH_LONG).show();
                }
            }

        });

        //instantiate request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        int socketTimeout = 60000;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);

        //add request to request queue
        requestQueue.add(stringRequest);
    }
}
