package com.hvacci.vaccination;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Register extends AppCompatActivity {

    EditText reg_eduser,reg_edname,reg_edemail,reg_date,reg_ednum,
            reg_edpass,reg_edcpass;

    ImageView reg_cal;

    Button reg_submit;
    RequestQueue requestQueue;

    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_eduser  = (EditText) findViewById(R.id.reg_eduser);
        reg_edname  = (EditText) findViewById(R.id.reg_edname);
        reg_edemail  = (EditText) findViewById(R.id.reg_edemail);
        reg_date  = (EditText) findViewById(R.id.reg_date);
        reg_ednum  = (EditText) findViewById(R.id.reg_ednum);
        reg_edpass  = (EditText) findViewById(R.id.reg_edpass);
        reg_edcpass  = (EditText) findViewById(R.id.reg_edcpass);
        reg_cal = (ImageView) findViewById(R.id.reg_cal);
        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.registeravi);
        avLoadingIndicatorView.hide();


        reg_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Register.this);
                dialog.setContentView(R.layout.datepicker);
                dialog.setTitle("Pick a date");
                reg_date.setText("");

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
                        reg_date.setText(dmy);
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }

        });


        reg_submit  = (Button) findViewById(R.id.reg_submit);
        reg_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  username  = reg_eduser.getText().toString();
                String  fullname  = reg_edname.getText().toString();
                String  email  = reg_edemail.getText().toString();
                String  date  = reg_date.getText().toString();
                String  number  = reg_ednum.getText().toString();
                String  password  = reg_edpass.getText().toString();
                String  cpassword  = reg_edcpass.getText().toString();

                if(cpassword.equals(password)){
                    avLoadingIndicatorView.show();
                    register(username,fullname,date,email,number,password);
                }
                else{
                    Toast.makeText(Register.this,"Password is not correct!!",Toast.LENGTH_SHORT).show();
                }
            }
        });


        requestQueue = Volley.newRequestQueue(this);
    }
    void register(final String username, final String fullname, final String birthdate, final String email_id, final String phone_number, final String password){
        StringRequest register = new StringRequest(Request.Method.POST, "http://harsh2011.esy.es/vaccination/index.php/signup", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject all  = new JSONObject(s);

                    String result  = all.getString("result");
                    if(result.equals("success")){
                        JSONObject user  = all.getJSONObject("userSignUp");
                        User object  =  new User();
                        object.setUsername( user.getString("username"));
                        object.setFullname( user.getString("fullname"));
                        object.setBirthdate(user.getString("birthdate"));
                        object.setEmail_id( user.getString("email_id"));
                        object.setPhone_number(user.getString("phone_number"));
                        userregister(object);

                    }
                    else{
                        Toast.makeText(Register.this,"Username is used!!",Toast.LENGTH_SHORT).show();
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
                Map<String, String> parameter = new HashMap<String, String>();
                parameter.put("username",username);
                parameter.put("fullname",fullname);
                parameter.put("birthdate",birthdate);
                parameter.put("email_id",email_id);
                parameter.put("phone_number",phone_number);
                parameter.put("password",password);
                return parameter;
            }
        };

        requestQueue.add(register);
    }
    void userregister(User user){
        new UserFile().setCurrentUser(user,getFilesDir());

        String gcmtoken = FirebaseInstanceId.getInstance().getToken();
        updatefcmtoken(gcmtoken,new UserFile().getCurrentUser(getFilesDir()).getUsername());
    }
    private void updatefcmtoken(final String token, final String username){

        StringRequest updatetoken = new StringRequest(Request.Method.POST,"http://harsh2011.esy.es/vaccination/index.php/fcmtokenaupdate", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                avLoadingIndicatorView.hide();

                Intent mainactivity = new Intent(Register.this,MainActivity.class);
                startActivity(mainactivity);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<String, String>();
                parameter.put("username", username);
                parameter.put("token", token);
                return parameter;
            }
        };
        requestQueue.add(updatetoken);
    }

}

