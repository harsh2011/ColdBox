package com.hvacci.vaccination;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hvacci.vaccination.java.User;

import java.util.HashMap;
import java.util.Map;

public class UserDetailsActivity extends AppCompatActivity {

    TextView tusername,tname,tbirthdate,tcontact;

    Button logout;

    ImageView user_details_edit,phone_edit;

    RequestQueue requestQueue;

    String username;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        getSupportActionBar().setTitle("User Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(UserDetailsActivity.this);

        tusername = (TextView) findViewById(R.id.tusername);
        tname = (TextView) findViewById(R.id.tname);
        tbirthdate =(TextView) findViewById(R.id.tbirth);
        tcontact =(TextView) findViewById(R.id.tcontact);
        logout = (Button) findViewById(R.id.logout_btn);

        user_details_edit = (ImageView) findViewById(R.id.edit_user_details);
        phone_edit = (ImageView) findViewById(R.id.edit_phone);

        User user = new UserFile().getCurrentUser(getFilesDir());

        username = user.getUsername();
        tusername.setText(user.getUsername());
        tname.setText(user.getFullname());
        tbirthdate.setText(user.getBirthdate());
        tcontact.setText(user.getPhone_number());


        user_details_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(UserDetailsActivity.this,EditUserDetailsActivity.class);
                startActivity(edit);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //request edit fcm
                updatefcmtoken("none",username);
            }
        });
    }
    private void updatefcmtoken(final String token, final String username){

        StringRequest updatetoken = new StringRequest(Request.Method.POST,"http://harsh2011.esy.es/vaccination/index.php/fcmtokenaupdate", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                new UserFile().clearCurrentUser(getFilesDir());

                Intent logout = new Intent(UserDetailsActivity.this,Login.class);
                startActivity(logout);
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
