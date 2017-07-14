package com.hvacci.vaccination;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button myacc , book;
    TextView tuser;
    AVLoadingIndicatorView avLoadingIndicatorView;

    TextView dayleft,vaccilist;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.mainavi);
        avLoadingIndicatorView.hide();

        myacc = (Button)findViewById(R.id.btmyacc);
        book = (Button) findViewById(R.id.bkvac);
        tuser = (TextView)findViewById(R.id.textView);

        dayleft = (TextView) findViewById(R.id.main_days_left);
        vaccilist = (TextView) findViewById(R.id.main_vacci_list);

        tuser.setText("Hello " +new UserFile().getCurrentUser(getFilesDir()).getFullname() + "!");
        myacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a  = new Intent(MainActivity.this,VaccinationList.class);
                startActivity(a);
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a  = new Intent(MainActivity.this,ApprovedList.class);
                startActivity(a);
            }
        });

        String username = new UserFile().getCurrentUser(getFilesDir()).getUsername();

        getdata(username);
        avLoadingIndicatorView.show();
    }
    private void getdata(final String username){
        StringRequest getdata = new StringRequest(Request.Method.POST, "http://harsh2011.esy.es/vaccination/index.php/upcomingvaccination/"+username, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                avLoadingIndicatorView.hide();
                try {
                    JSONObject main = new JSONObject(s);

                    String days =main.getString("dayleft");
                    String vaccination = main.getString("vaccination");

                    dayleft.setText(days+" days left");
                    vaccilist.setText(vaccination);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        requestQueue.add(getdata);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_menu_user:
                Intent i = new Intent(MainActivity.this,UserDetailsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
