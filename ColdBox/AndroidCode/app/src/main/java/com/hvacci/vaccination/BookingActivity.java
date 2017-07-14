package com.hvacci.vaccination;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {

    EditText book_date,book_address;
    ImageView book_cal;

    TextView book_tvdays,book_tvname,book_tvdes,book_tvprice;

    Button book_btn;

    RequestQueue requestQueue;

    int vaccineid;
    String vaccinename;
    int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        requestQueue= Volley.newRequestQueue(this);

        book_date = (EditText) findViewById(R.id.book_date);
        book_address = (EditText) findViewById(R.id.book_address);
        book_cal = (ImageView) findViewById(R.id.book_cal);
        book_tvdays = (TextView) findViewById(R.id.book_tvdays);
        book_tvname = (TextView) findViewById(R.id.book_tvname);
        book_tvdes = (TextView) findViewById(R.id.book_tvdes);
        book_tvprice = (TextView) findViewById(R.id.book_tvprice);
        book_btn = (Button) findViewById(R.id.book_btn);

        Bundle bundle = getIntent().getExtras();

        vaccineid = bundle.getInt("id");
        book_tvdays.setText(String.valueOf(bundle.getInt("days")));
        book_tvname.setText(bundle.getString("name"));
        vaccinename = bundle.getString("name");
        book_tvdes.setText(bundle.getString("desc"));
        book_tvprice.setText(String.valueOf(bundle.getInt("price")));
        price = bundle.getInt("price");

        book_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(BookingActivity.this);
                dialog.setContentView(R.layout.datepicker);
                dialog.setTitle("Pick a date");
                book_date.setText("");
                // set the custom dialog components - text, image and button

                final DatePicker datepick= (DatePicker)dialog.findViewById(R.id.calendarView);
                Button select = (Button) dialog.findViewById(R.id.select);


                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int datei,month,year;
                        String  dmy;
                        datei = datepick.getDayOfMonth();
                        month = datepick.getMonth()+1;
                        year= datepick.getYear();
                        dmy= ""+year+"-"+month+"-"+datei;
                        book_date.setText(dmy);
                        dialog.dismiss();
                    }
                });
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

            }

        });

        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(book_date.getText().toString().isEmpty() || book_address.getText().toString().isEmpty()) {
                    if (book_date.getText().toString().isEmpty()) {
                        Toast.makeText(BookingActivity.this, "Date is empty", Toast.LENGTH_SHORT).show();
                    }
                    if (book_address.getText().toString().isEmpty()) {
                        Toast.makeText(BookingActivity.this, "Address is empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    final AlertDialog alertDialog = new AlertDialog.Builder(BookingActivity.this).create();

                    alertDialog.setTitle("Order Confirmation");
                    alertDialog.setMessage("Do you want to confirm your order?");

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //booking started
                            String username = new UserFile().getCurrentUser(getFilesDir()).getUsername();
                            book(username,String.valueOf(vaccineid),vaccinename,book_date.getText().toString(),String.valueOf(price),book_address.getText().toString());
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });


    }
    public void book(final String username, final String vaccine_id, final String vaccine_name, final String date, final String price,final String address){
        StringRequest register = new StringRequest(Request.Method.POST, "http://harsh2011.esy.es/vaccination/index.php/neworder", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                System.out.println("responce"+s);

                try {
                    JSONObject main = new JSONObject(s);

                    if(main.getString("status").toString().equals("success")){
                        Toast.makeText(BookingActivity.this, "book successfully", Toast.LENGTH_SHORT).show();

                        Intent mainactivity = new Intent(BookingActivity.this,MainActivity.class);
                        startActivity(mainactivity);
                        finish();
                    }
                    else{
                        Toast.makeText(BookingActivity.this, "booking is unsuccessful", Toast.LENGTH_SHORT).show();
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
                parameter.put("vaccineid",vaccine_id);
                parameter.put("vaccinename",vaccine_name);
                parameter.put("date",date);
                parameter.put("price",price);
                parameter.put("address",address);
                return parameter;
            }
        };

        requestQueue.add(register);
    }
}
