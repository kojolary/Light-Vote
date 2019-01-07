package net.vokacom.lightvote.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import net.vokacom.lightvote.MainActivity;
import net.vokacom.lightvote.R;
import net.vokacom.lightvote.admins.admins;
import net.vokacom.lightvote.models.Login;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity implements View.OnClickListener {
    public static final String dTelephone = "telephone";
    // UI references.
    EditText mTelephoneView;
    Button mTelephoneSignInButton, mRegisterButton;
    TextView txt_forgot;
    // Creating Progress dialog.
    ProgressDialog progressDialog;
    String URL = "https://app.tedxafariwaa.com/Vote/login.php";
//    String URL = "https://tedx.ngrok.io/Vote/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mTelephoneView = findViewById(R.id.email);

        mTelephoneSignInButton = findViewById(R.id.email_sign_in_button);
        mRegisterButton = findViewById(R.id.btnRegister);


        // Assigning Activity this to progress dialog.
        progressDialog = new ProgressDialog(login.this);
        mTelephoneSignInButton.setOnClickListener(login.this);
        mRegisterButton.setOnClickListener(login.this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.email_sign_in_button) {


            final String telephone = mTelephoneView.getText().toString().trim();

            if (telephone.equals("")) {


                Toast.makeText(login.this, "All Fields are Compulsory", Toast.LENGTH_SHORT).show();
            } else {

                // Showing progress dialog at user registration time.
                progressDialog.setMessage("Logging In....please Wait");
                progressDialog.show();

                //get string response using url
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onResponse(String response) {
                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        Login loginResponse = new Gson().fromJson(response, Login.class);
                        if(loginResponse.isSuccessful()){
                            openProfile(loginResponse);
                        }else{
                            Toast.makeText(login.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    private void openProfile(Login loginResponse) {
                        Intent intent = new Intent(login.this, MainActivity.class);
                        intent.putExtra(dTelephone, telephone);
                        intent.putExtra("user", loginResponse.getUser());
                        startActivity(intent);
                        finish();
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        if (error.networkResponse == null) {
                            if (error.getClass().equals(TimeoutError.class)) {

                                // Show timeout error message
                                Toast.makeText(login.this,
                                        "Check Internet Connection",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else if (error instanceof NetworkError) {
                            // Show timeout error message
                            Toast.makeText(login.this,
                                    "Oops. Network error!",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            // Show timeout error message
                            Toast.makeText(login.this,
                                    "Oops. Severe error occurred!",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            // Show timeout error message
                            Toast.makeText(login.this,
                                    "Oops. Authentication error!",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            // Show timeout error message
                            Toast.makeText(login.this,
                                    "Oops. Timeout error!",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof TimeoutError) {
                            // Show timeout error message
                            Toast.makeText(login.this,
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
                        params.put(dTelephone, telephone);
                        return params;
                    }
                };
                //instantiate request queue
                RequestQueue requestQueue = Volley.newRequestQueue(login.this);

                //add request to request queue
                requestQueue.add(stringRequest);
            }


        } else if (v.getId() == R.id.btnRegister) {
            Intent register_Intent = new Intent(login.this, RegisterActivity.class);
            register_Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(register_Intent);
            finish();

        }
    }
}