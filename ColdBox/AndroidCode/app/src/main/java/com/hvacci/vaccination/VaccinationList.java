package com.hvacci.vaccination;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hvacci.vaccination.java.User;
import com.hvacci.vaccination.java.Vaccine;
import com.wang.avi.AVLoadingIndicatorView;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VaccinationList extends AppCompatActivity {

    RequestQueue requestQueue;

    RecyclerView vaccination_rv;

    ArrayList<Vaccine> array = new ArrayList<>();
    ArrayList<String> dates =  new ArrayList<>();

    VaccinationListAdapter vaccinationListAdapter;

    Dialog dialog;

    int days;

    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination_list);

        getSupportActionBar().setTitle("Vaccination List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this);

        User user  = new UserFile().getCurrentUser(getFilesDir());

        DateTime dt = new DateTime(user.getBirthdate());

        Date juDate = new Date();
        DateTime cdt = new DateTime(juDate);

        Long cdtl  = cdt.getMillis();
        Long dtl = dt.getMillis();

        Long diff =  cdtl-dtl;

        days = (int) (diff / (1000*60*60*24));

        vaccination_rv = (RecyclerView) findViewById(R.id.vaccination_rv);
        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.vaccilistavi);
        avLoadingIndicatorView.hide();

        vaccinationListAdapter = new VaccinationListAdapter(array,days,dates);

        vaccinationListAdapter.setGetItemPostionOnClick(new GetItemPostionOnClick() {
            @Override
            public void getItemPostionOnClick(View view,int position,int other) {
                Vaccine vaccine = array.get(position);
                if(other ==1){

                    dialogopener(vaccine);


                }
                else if(other==2){
                    dialogopener(vaccine);
                }

            }
        });


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        vaccination_rv.setLayoutManager(mLayoutManager);
        vaccination_rv.setItemAnimator(new DefaultItemAnimator());
        vaccination_rv.setAdapter(vaccinationListAdapter);

        avLoadingIndicatorView.show();
        getting_vaccine();

    }
    void getting_vaccine(){
        final StringRequest getvaccination = new StringRequest(Request.Method.GET, "http://harsh2011.esy.es/gettingvaccination.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                array.clear();

                System.out.println("response"+s);
                try {
                    JSONArray all  = new JSONArray(s);

                    for(int i=0;i<all.length();i++){
                        JSONObject object = all.getJSONObject(i);

                        Vaccine vaccine = new Vaccine(object.getInt("id"),
                                object.getString("timestamp"),
                                object.getString("vaccination_name"),
                                object.getString("description"),
                                object.getInt("price"));
                        array.add(vaccine);

                        String day = vaccine.getdate();
                        int dayint = Integer.parseInt(day);

                        if(dayint < days){
                            dates.add("");}
                        else{
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar c = Calendar.getInstance();
                            c.setTime(new Date()); // Now use today date.
                            c.add(Calendar.DATE, dayint); // Adding 5 days
                            String output = sdf.format(c.getTime());
                            dates.add(output);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                avLoadingIndicatorView.hide();
                vaccinationListAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        requestQueue.add(getvaccination);
    }
    public void dialogopener(final Vaccine vaccine){

        dialog = new Dialog(VaccinationList.this);
        dialog.setContentView(R.layout.dialog_of_vaccine_dis);
        dialog.setTitle("Vaccines Details");


        // set the custom dialog components - text, image and button

        TextView tvdays,tvname,tvdes,tvprice;
        Button dialog_book_button;

        tvdays = (TextView) dialog.findViewById(R.id.tvdays);
        tvname = (TextView) dialog.findViewById(R.id.tvname);
        tvdes = (TextView) dialog.findViewById(R.id.tvdes);
        tvprice = (TextView) dialog.findViewById(R.id.tvprice);
        dialog_book_button = (Button) dialog.findViewById(R.id.dialog_book_button);


        tvdays.setText(Integer.toString(Integer.parseInt(vaccine.getdate())-days));
        tvname.setText(vaccine.getVName());
        tvdes.setText(vaccine.getVDesc());
        tvprice.setText(String.valueOf(vaccine.getPrice()));
        dialog_book_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                bundle.putInt("days",Integer.parseInt(vaccine.getdate())-days);
                bundle.putInt("id",vaccine.getId());
                bundle.putInt("price",vaccine.getPrice());
                bundle.putString("name",vaccine.getVName());
                bundle.putString("desc",vaccine.getVDesc());

                Intent book = new Intent(VaccinationList.this,BookingActivity.class);
                book.putExtras(bundle);
                startActivity(book);
            }
        });


        dialog.show();
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


