package com.example.minicanteen.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.minicanteen.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Admin_Profile extends AppCompatActivity {

    TextView name,email,id,amount,mobile;
    RequestQueue requestQueue;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String Shop_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__profile);

        name=(TextView)findViewById(R.id.admin_profile_name);
        email=(TextView)findViewById(R.id.admin_profile_email);
        id=(TextView)findViewById(R.id.admin_profile_id);
        amount=(TextView)findViewById(R.id.admin_profile_amount);
        mobile=(TextView)findViewById(R.id.admin_profile_mobile);


        requestQueue= Volley.newRequestQueue(Admin_Profile.this);

        pref=getSharedPreferences("Admin",MODE_PRIVATE);
        editor=pref.edit();
        Shop_id=pref.getString("admin_id",null);

        Admin_Profile_Show admin_profile_show=new Admin_Profile_Show(Shop_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("Response",response);
                try
                {
                    if (new JSONObject(response).get("success").equals("true"))
                    {
                        name.setText(new JSONObject(response).getString("name"));
                        email.setText(new JSONObject(response).getString("email"));
                        id.setText(new JSONObject(response).getString("rollnumber"));
                        amount.setText(new JSONObject(response).getString("amount"));
                        mobile.setText(new JSONObject(response).getString("mobile"));
                    }
                    else if (new JSONObject(response).get("success").equals("false"))
                    {

                        Toast.makeText(getApplicationContext(), "Invalid password or user id", Toast.LENGTH_SHORT).show();

                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        });
        requestQueue.add(admin_profile_show);


    }


    public class Admin_Profile_Show extends StringRequest {
        private static final String REGISTER_URL = "https://nitconlinevoting.000webhostapp.com/MiniBook/Admin/Profile.php";
        private Map<String, String> parameters;

        public Admin_Profile_Show(String id, Response.Listener<String> listener) {
            super(Method.POST,REGISTER_URL, listener, null);
            parameters = new HashMap<>();
            parameters.put("roll",id);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError
        {
            return parameters;
        }
    }
}
