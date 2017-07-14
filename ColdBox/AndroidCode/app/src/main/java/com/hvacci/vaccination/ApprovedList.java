package com.hvacci.vaccination;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hvacci.vaccination.java.BookVaccine;
import com.hvacci.vaccination.java.Vaccine;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApprovedList extends AppCompatActivity {
    AVLoadingIndicatorView avLoadingIndicatorView;

    RequestQueue requestQueue;

    ArrayList<BookVaccine> array  = new ArrayList<>();

    ApprovedListAdapter approvedListAdapter;

    RecyclerView book_vaccince_rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_list);

        getSupportActionBar().setTitle("Booking List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this);

        book_vaccince_rv  = (RecyclerView) findViewById(R.id.book_vaccince_rv);
        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.approvedlistavi);
        avLoadingIndicatorView.hide();

        approvedListAdapter = new ApprovedListAdapter(array);

        approvedListAdapter.setGetItemPostionOnClick(new GetItemPostionOnClick() {
            @Override
            public void getItemPostionOnClick(View view, int position, int other) {
                BookVaccine bookVaccine = array.get(position);

                Bundle bundle = new Bundle();

                bundle.putString("p_id",bookVaccine.getParamedics_id());
                bundle.putString("vaccine_id",bookVaccine.getVaccine_id());
                bundle.putString("vaccine_name",bookVaccine.getVaccine_name());
                bundle.putString("price",bookVaccine.getPrice());
                bundle.putString("date",bookVaccine.getDue_date());
                bundle.putString("status",bookVaccine.getStatus());
                bundle.putString("username",bookVaccine.getUsername());
                bundle.putString("address",bookVaccine.getAddress());


                Intent bookdetails = new Intent(ApprovedList.this,BookDetailActivity.class);
                bookdetails.putExtras(bundle);
                startActivity(bookdetails);



            }
        });

        book_vaccince_rv.setAdapter(approvedListAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        book_vaccince_rv.setLayoutManager(mLayoutManager);
        book_vaccince_rv.setItemAnimator(new DefaultItemAnimator());


        String username = new UserFile().getCurrentUser(getFilesDir()).getUsername();

        getting_vaccine(username);

    }
    void getting_vaccine(final String username){
        avLoadingIndicatorView.show();
        final StringRequest data = new StringRequest(Request.Method.POST, "http://harsh2011.esy.es/vaccination/index.php/getordered", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                array.clear();
                System.out.println("response"+s);
                try {
                    JSONArray all  = new JSONArray(s);

                    for(int i=0;i<all.length();i++){
                        JSONObject object = all.getJSONObject(i);

                        String vaccine_id =  object.getString("vaccine_id");
                        String vaccine_name =  object.getString("vaccine_name");
                        String date = object.getString("date");
                        String username = object.getString("username");
                        String status = object.getString("status");
                        String p_id =object.getString("p_id");
                        String price = object.getString("price");
                        String address = object.getString("address");

                        BookVaccine vaccine= new BookVaccine();

                        vaccine.setAddress(address);
                        vaccine.setDue_date(date);
                        vaccine.setParamedics_id(p_id);
                        vaccine.setPrice(price);
                        vaccine.setStatus(status);
                        vaccine.setUsername(username);
                        vaccine.setVaccine_id(vaccine_id);
                        vaccine.setVaccine_name(vaccine_name);

                        array.add(vaccine);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                approvedListAdapter.notifyDataSetChanged();
                avLoadingIndicatorView.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data = new HashMap<>();
                data.put("username",username);
                return data;
            }
        };

        requestQueue.add(data);
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
