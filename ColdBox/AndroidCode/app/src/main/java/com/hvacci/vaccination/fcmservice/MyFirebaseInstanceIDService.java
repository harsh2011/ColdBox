package com.hvacci.vaccination.fcmservice;

import android.app.Service;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.hvacci.vaccination.UserFile;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Harsh on 08-04-2017.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        UserFile user = new UserFile();

        if(user.checkUserDetails(getFilesDir())){
            updatefcmtoken(refreshedToken,user.getCurrentUser(getFilesDir()).getUsername());
        }
    }
    private void updatefcmtoken(final String token, final String username){

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest updatetoken = new StringRequest(Request.Method.POST,"http://harsh2011.esy.es/fcmtokenupdate.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

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
