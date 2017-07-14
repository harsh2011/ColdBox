package com.hvacci.vaccination;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {

    RequestQueue requestQueue;

    EditText login_pwd,login_username;
    TextView fsignup;
    Button login_btn;

    AVLoadingIndicatorView avLoadingIndicatorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(this);

        login_pwd  =  (EditText) findViewById(R.id.login_pwd);
        login_username= (EditText) findViewById(R.id.login_username);
        login_btn = (Button) findViewById(R.id.login_btn);
        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.loginavi);
        avLoadingIndicatorView.hide();

        fsignup = (TextView)findViewById(R.id.fsignup);

        fsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this,Register.class);
                startActivity(i);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username  = login_username.getText().toString();
                login(username);
                //avLoadingIndicatorView.setVisibility(View.VISIBLE);
                avLoadingIndicatorView.show();
            }
        });

    }

    void login(final String username){
        final StringRequest login = new StringRequest(Request.Method.POST, "http://harsh2011.esy.es/vaccination/index.php/login", new Response.Listener<String>() {
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
                        String password  = user.getString("password");

                        userlogin(object,password);
                    }
                    else{
                        Toast.makeText(Login.this,"You have not registered",Toast.LENGTH_SHORT).show();
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
                Map<String,String> data = new HashMap<>();
                data.put("username",username);
                return data;
            }
        };

        requestQueue.add(login);

    }
    void userlogin(User user,String password){
        if(password.equals(login_pwd.getText().toString())){
            new UserFile().setCurrentUser(user,getFilesDir());
            String gcmtoken = FirebaseInstanceId.getInstance().getToken();
            updatefcmtoken(gcmtoken,new UserFile().getCurrentUser(getFilesDir()).getUsername());
        }
        else{
            Toast.makeText(Login.this,"Password is incorrect!!",Toast.LENGTH_SHORT).show();
        }
    }
    private void updatefcmtoken(final String token, final String username){

        StringRequest updatetoken = new StringRequest(Request.Method.POST,"http://harsh2011.esy.es/vaccination/index.php/fcmtokenaupdate", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                avLoadingIndicatorView.hide();
                Intent mainactivity = new Intent(Login.this,MainActivity.class);
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
