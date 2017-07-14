package com.hvacci.vaccination;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hvacci.vaccination.java.User;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditUserDetailsActivity extends AppCompatActivity {

    EditText ed_username,ed_name,ed_birthdate;

    Button edit;

    ImageView book_cal;
    RequestQueue requestQueue;

    String oldusername;
    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_details);

        getSupportActionBar().setTitle("Edit User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(EditUserDetailsActivity.this);


        ed_username = (EditText) findViewById(R.id.ed_username);
        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_birthdate =(EditText) findViewById(R.id.ed_birth);
        edit =(Button) findViewById(R.id.edit_btn_ue);
        book_cal = (ImageView) findViewById(R.id.book_cal);
        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.edituseravi);
        avLoadingIndicatorView.hide();

        User user = new UserFile().getCurrentUser(getFilesDir());


        oldusername = user.getUsername();
        ed_username.setText(user.getUsername());
        ed_name.setText(user.getFullname());
        ed_birthdate.setText(user.getBirthdate());
        book_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(EditUserDetailsActivity.this);
                dialog.setContentView(R.layout.datepicker);
                dialog.setTitle("Pick a date");

                // set the custom dialog components - text, image and button

                final DatePicker datepick= (DatePicker)dialog.findViewById(R.id.calendarView);
                Button select = (Button) dialog.findViewById(R.id.select);


                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int datei,month,year;
                        String  dmy;
                        datei = datepick.getDayOfMonth();
                        Log.d("reg:",""+datei);
                        month = datepick.getMonth()+1;
                        year= datepick.getYear();
                        if(month<10 && datei<10) {
                            dmy = "" + year + "-0" + month + "-0" + datei;
                        }
                        else if(month<10){
                            dmy = "" + year + "-0" + month + "-" + datei;
                        }
                        else if(datei<10){
                            dmy = "" + year + "-" + month + "-0" + datei;
                        }
                        else{
                            dmy = "" + year + "-" + month + "-" + datei;
                        }
                        Log.d("register:",""+dmy);
                        ed_birthdate.setText(dmy);
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }

        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updaterequest(oldusername,ed_username.getText().toString(),ed_name.getText().toString(),ed_birthdate.getText().toString());
            }
        });
    }
    public void updaterequest(final String oldusername, final String username, final String fullname , final String birthdate){
        //upadte request
        avLoadingIndicatorView.show();
        StringRequest update =  new StringRequest(Request.Method.POST, "http://harsh2011.esy.es/vaccination/index.php/updateuser", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                System.out.println("response"+s);
                try {
                    JSONObject all  = new JSONObject(s);

                    String result  = all.getString("status");
                    if(result.equals("true")){
                        JSONObject user  = all.getJSONObject("userlogin");
                        User object  =  new User();
                        object.setUsername( user.getString("username"));
                        object.setFullname( user.getString("fullname"));
                        object.setBirthdate(user.getString("birthdate"));
                        object.setEmail_id( user.getString("email_id"));
                        object.setPhone_number(user.getString("phone_number"));
                        new UserFile().setCurrentUser(object,getFilesDir());

                        startAct();
                    }
                    else{
                        Toast.makeText(EditUserDetailsActivity.this,"Not updated correctly",Toast.LENGTH_SHORT).show();
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
                data.put("oldusername",oldusername);
                data.put("username",username);
                data.put("fullname",fullname);
                data.put("birthdate",birthdate);
                return data;
            }
        };
        requestQueue.add(update);
    }
    public void startAct(){
        avLoadingIndicatorView.hide();
        Intent main = new Intent(EditUserDetailsActivity.this,MainActivity.class);
        startActivity(main);
        finish();
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
