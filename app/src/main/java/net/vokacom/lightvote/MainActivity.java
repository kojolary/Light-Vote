package net.vokacom.lightvote;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.vokacom.lightvote.mData.Contestants;
import net.vokacom.lightvote.mListView.ContestantAdapter;
import net.vokacom.lightvote.models.AuthenticatedUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ContestantAdapter.OnItemClickListener {

    private static final int VERTICAL_ITEM_SPACE = 48;
    String URL = "https://app.tedxafariwaa.com/Vote/products.php";
    //a list to store all the products
// Creating Progress dialog.
    ProgressDialog progressDialog;
    AuthenticatedUser user;
    //a list to store all the products
    List<Contestants> productList;
    //the recyclerview
    RecyclerView rV;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getIntent().hasExtra("user")){
            user = getIntent().getParcelableExtra("user");
        }


        // Assigning Activity this to progress dialog.
        progressDialog = new ProgressDialog(MainActivity.this);
        rV = findViewById(R.id.listRecycleView);

        rV.setHasFixedSize(true);
        rV.setLayoutManager(new LinearLayoutManager(this));


        productList = new ArrayList<>();
        adapter = new ContestantAdapter(getApplicationContext(), productList);


        rV.setAdapter(adapter);

        getData();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        //get string response using url
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @SuppressLint("ShowToast")
            @Override
            public void onResponse(String response) {
                Log.d("MainActivity", response);
                // Hiding the progress dialog after all task complete.
                progressDialog.dismiss();
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting product object from json array
                        JSONObject product = array.getJSONObject(i);

                        //adding the product to product list
                        productList.add(new Contestants(
                                product.getInt("id"),
                                product.getString("title"),
                                product.getString("short_description"),
                                product.getString("town"),
                                product.getString("image")
                        ));
                    }
                    //creating adapter object and setting it to recyclerview
                    ContestantAdapter adapter = new ContestantAdapter(MainActivity.this, productList);
                    rV.setAdapter(adapter);
                    adapter.setOnItemClickListener(MainActivity.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(MainActivity.this));
        requestQueue.add(stringRequest);
    }


    @Override
    public void onItemClick(int position) {
        Contestants clickedItem = productList.get(position);

        Intent myIntents;
        myIntents = new Intent(MainActivity.this, ContestantsDetails.class);

         myIntents.putExtra("EXTRA_ID", clickedItem.getId());
        myIntents.putExtra("EXTRA_TITLE", clickedItem.getTitle());
        myIntents.putExtra("EXTRA_DESCRIPTION", clickedItem.getshort_description());
        myIntents.putExtra("EXTRA_TOWN", clickedItem.getTown());
        myIntents.putExtra("EXTRA_IMAGE", clickedItem.getImage());
        myIntents.putExtra("user", user);

        Log.d("MainActivity", ""+clickedItem.getId());

        startActivity(myIntents);
    }
}
