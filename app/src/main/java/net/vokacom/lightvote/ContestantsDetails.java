package net.vokacom.lightvote;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hubtel.payments.Exception.HubtelPaymentException;
import com.hubtel.payments.HubtelCheckout;
import com.hubtel.payments.Interfaces.OnPaymentResponse;
import com.hubtel.payments.SessionConfiguration;

import net.vokacom.lightvote.login.RegisterActivity;
import net.vokacom.lightvote.login.login;
import net.vokacom.lightvote.models.AuthenticatedUser;
import net.vokacom.lightvote.models.MobileMoneyInvoiceResponse;

import java.util.HashMap;
import java.util.Map;

public class ContestantsDetails extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_PAYMENT_CONFIRMATION = 1;
    Integer id;
    String title, short_description, town,image;
    EditText number_to_bill, number_of_votes ;
    TextView textViewTitletop, textViewId, textViewShortDesc, textViewRating, textViewPrice, total_amount;
    ImageView imageView;
    Button pay;

    AuthenticatedUser user;


    public static final String dTelephone = "telephone";
    public static final String dId = "id";
    public static final String dSecret= "secret";
    public static final String dName = "name";
    public static final String dRef = "ref";
    public static final String dAmount = "amount";

    // UI references.
    EditText mTelephoneView;
    Button mTelephoneSignInButton, mRegisterButton;
    TextView txt_forgot;
    // Creating Progress dialog.
    ProgressDialog progressDialog;
    String URL = "https://app.tedxafariwaa.com/Vote/response.php";
    private Spinner network;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contestants_details);


        Intent intents = getIntent();

        if(intents.hasExtra("user")){
            user = intents.getParcelableExtra("user");
        }


        id = intents.getIntExtra("EXTRA_ID", 0);
        title = intents.getStringExtra("EXTRA_TITLE");
        short_description = intents.getStringExtra("EXTRA_DESCRIPTION");
        town = intents.getStringExtra("EXTRA_TOWN");
        image = intents.getStringExtra("EXTRA_IMAGE");


        textViewTitletop = findViewById(R.id.text_name_detail);
        number_to_bill = findViewById(R.id.bill_number);
        number_of_votes = findViewById(R.id.vote_number);
        total_amount = findViewById(R.id.total_amount);
        imageView = findViewById(R.id.image_view_detail);
        network = findViewById(R.id.network);
        pay = findViewById(R.id.pay);

        pay.setOnClickListener(this);


        Glide.with(this
        )
                .load(image)
                .into(imageView)
        ;


        textViewTitletop.setText(title);
      //  textViewId.setText(id);
      //  textViewRating.setText(town);
        //textViewShortDesc.setText(short_description);


        number_of_votes.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(!s.equals("") )
                { //do your work here
                    Double value1 = 0.0;
                    try{
                        value1 = Double.parseDouble(number_of_votes.getText().toString());
                    }catch (NumberFormatException e){}
                   // Log.i("result", number_of_votes.toString());
                   // Toast.makeText(ContestantsDetails.this, "Text is:" +number_of_votes.getText(), Toast.LENGTH_SHORT).show();
                    Double num = value1 * 0.5;
                    total_amount.setText(num.toString());
                    // }
                }
                else{
                    Toast.makeText(ContestantsDetails.this, "Number of votes cannot be empty", Toast.LENGTH_SHORT).show();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });

        progressDialog = new ProgressDialog(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay:
                Log.d("ContestantsDetail", "pay button clicked");
                try {

                    final String telephone = number_to_bill.getText().toString().trim();
                    final String Uid = String.valueOf(id);
                    final String secret = number_to_bill.getText().toString().concat(number_of_votes.getText().toString());
                    final String name = title.trim();
                    final String ref = number_to_bill.getText().toString().trim();
                    final String amount = total_amount.getText().toString().trim();

                    if (telephone.equals("")) {
                        Toast.makeText(this, "All Fields are Compulsory", Toast.LENGTH_SHORT).show();
                    } else {

                        // Showing progress dialog at user registration time.
                        progressDialog.setMessage("Making Payment ....please Wait");
                        progressDialog.show();

                        //get string response using url
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @SuppressLint("ShowToast")
                            @Override
                            public void onResponse(String response) {
                                Log.d("ContestantsDetail", "response: "+response);
                                // Hiding the progress dialog after all task complete.
                                progressDialog.dismiss();

                                MobileMoneyInvoiceResponse mobileMoneyInvoiceResponse = new Gson().fromJson(response, MobileMoneyInvoiceResponse.class);

                                if(mobileMoneyInvoiceResponse.isSuccessful()){
                                    Intent pendingIntent = new Intent(ContestantsDetails.this, PaymentPendingActivity.class);
                                    pendingIntent.putExtra("token", mobileMoneyInvoiceResponse.getToken());
                                    pendingIntent.putExtra("name", title.trim());
                                    startActivityForResult(pendingIntent, RC_PAYMENT_CONFIRMATION);
                                }else{
                                    Toast.makeText(ContestantsDetails.this, mobileMoneyInvoiceResponse.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }


                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("ContestantsDetail", error.toString());
                                error.printStackTrace();
                                progressDialog.dismiss();

                                if (error.networkResponse == null) {
                                    if (error.getClass().equals(TimeoutError.class)) {
                                        // Hiding the progress dialog after all task complete.

                                        // Show timeout error message
                                        Toast.makeText(ContestantsDetails.this,
                                                "Check Internet Connection",
                                                Toast.LENGTH_LONG).show();
                                    }
                                } else if (error instanceof NetworkError) {
                                    // Show timeout error message
                                    Toast.makeText(ContestantsDetails.this,
                                            "Oops. Network error!",
                                            Toast.LENGTH_LONG).show();
                                } else if (error instanceof ServerError) {
                                    // Show timeout error message
                                    Toast.makeText(ContestantsDetails.this,
                                            "Oops. No Internet!",
                                            Toast.LENGTH_LONG).show();
                                } else if (error instanceof AuthFailureError) {
                                    // Show timeout error message
                                    Toast.makeText(ContestantsDetails.this,
                                            "Oops. Authentication error!",
                                            Toast.LENGTH_LONG).show();
                                } else if (error instanceof ParseError) {
                                    // Show timeout error message
                                    Toast.makeText(ContestantsDetails.this,
                                            "Oops. Timeout error!",
                                            Toast.LENGTH_LONG).show();
                                } else if (error instanceof TimeoutError) {
                                    // Show timeout error message
                                    Toast.makeText(ContestantsDetails.this,
                                            "Oops. Timeout error!",
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                        })

                        {
                            @Override
                            protected Map<String, String> getParams() {
                                //first parameter variable names, second parameter database column names
                                Map<String, String> params = new HashMap<>();
                                params.put(dId, Uid);
                                params.put(dSecret, secret);
                                params.put(dName, name);
                                params.put(dRef, ref);
                                params.put(dAmount, amount);
                                params.put("user_id", user.getId());

                                int optionPosition  = network.getSelectedItemPosition();
                                String[] optionsArray = getResources().getStringArray(R.array.hubtel_momo_channel_values);

                                Log.v("Payment", "using "+optionsArray[optionPosition] + " as hubtel channel");

                                params.put("payment_channel", optionsArray[optionPosition]);
                                return params;
                            }
                        };

                        //instantiate request queue
                        RequestQueue requestQueue = Volley.newRequestQueue(ContestantsDetails.this);

                        int socketTimeout = 60000;//60 seconds - change to what you want
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        stringRequest.setRetryPolicy(policy);

                        //add request to request queue
                        requestQueue.add(stringRequest);
                    }


                }
                catch (Exception e){
                    e.printStackTrace();
                }


               // Toast.makeText(this, "Payment Clicked", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PAYMENT_CONFIRMATION){
            String nameOfContestant = data.getStringExtra("name");
            if(resultCode == RESULT_OK){

                number_of_votes.setText("");
                number_to_bill.setText("");
                Snackbar.make(findViewById(android.R.id.content), "Your vote for "+nameOfContestant+" was successful!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Okay", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // do nothing
                            }
                        })
                        .show();
            }else{
                Snackbar.make(findViewById(android.R.id.content), "Vote for "+nameOfContestant+" failed. Please try again.", Snackbar.LENGTH_SHORT)
                        .setAction("Okay", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // do nothing
                            }
                        })
                        .show();
            }
        }
    }
}
