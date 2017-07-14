package com.hvacci.vaccination;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BookDetailActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    AVLoadingIndicatorView avLoadingIndicatorView;


    TextView bd_date,bd_vaccine,bd_desc,bd_price,bd_pname,bd_pcontact,bd_status,bd_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        getSupportActionBar().setTitle("Booking details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this);

        Bundle bundle = getIntent().getExtras();

        String p_id = bundle.getString("p_id");
        String vaccine_id = bundle.getString("vaccine_id");
        String vaccine_name = bundle.getString("vaccine_name");
        String price = bundle.getString("price");
        String date = bundle.getString("date");
        String status = bundle.getString("status");
        String username = bundle.getString("username");
        String address = bundle.getString("address");

        bd_date = (TextView) findViewById(R.id.bd_date);
        bd_address = (TextView) findViewById(R.id.bd_address);
        bd_vaccine = (TextView) findViewById(R.id.bd_vaccine);
        bd_desc = (TextView) findViewById(R.id.bd_desc);
        bd_price = (TextView) findViewById(R.id.bd_price);
        bd_pname = (TextView) findViewById(R.id.bd_pname);
        bd_pcontact = (TextView) findViewById(R.id.bd_pcontact);
        bd_status = (TextView) findViewById(R.id.bd_status);
        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.mainavi);
        avLoadingIndicatorView.hide();

        bd_date.setText(date);
        bd_vaccine.setText(vaccine_name);
        bd_desc.setText("");
        bd_price.setText(price);
        bd_pname.setText("");
        bd_pcontact.setText("");
        bd_status.setText(status);
        bd_address.setText(address);

        getData(p_id,vaccine_id);
    }

    public void getData(final String pid, final String vaccineid){
        avLoadingIndicatorView.show();
        StringRequest GetData = new StringRequest(Request.Method.POST, "http://harsh2011.esy.es/vaccination/index.php/getvaccineparamedicinfo", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject main = new JSONObject(s);

                    String status = main.getString("status");
                    if(status.equals("true")){
                        JSONObject vaccine = main.getJSONObject("vaccine");

                        bd_desc.setText(vaccine.getString("description"));

                        String pstatus = main.getString("pstatus");
                        if(pstatus.equals("true")){
                            JSONObject paramedics = main.getJSONObject("paramedics");
                            bd_pname.setText(paramedics.getString("name"));
                            bd_pcontact.setText(paramedics.getString("phone_number"));
                        }
                        else{
                            bd_pname.setText("no assigned");
                            bd_pcontact.setText("no assigned");
                        }
                    }
                    else{
                        Toast.makeText(BookDetailActivity.this, "Error in loading", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data =  new HashMap<>();

                data.put("vaccineid",vaccineid);
                data.put("pid",pid);

                return data;
            }
        };

        requestQueue.add(GetData);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

}
