package com.example.minicanteen.Student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.minicanteen.MainActivity;
import com.example.minicanteen.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class User_show extends AppCompatActivity {


    TextView name,email,rollnumber,amount,mobile;
    RequestQueue requestQueue;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String Roll_Number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_show);
        name=(TextView)findViewById(R.id.std_name);
        email=(TextView)findViewById(R.id.std_email);
        rollnumber=(TextView)findViewById(R.id.std_roll);
        amount=(TextView)findViewById(R.id.std_amount);
        mobile=(TextView)findViewById(R.id.std_mobile);


        requestQueue= Volley.newRequestQueue(User_show.this);

        pref=getSharedPreferences("Login_Roll",MODE_PRIVATE);
        editor=pref.edit();
        Roll_Number=pref.getString("std_roll",null);

        Otp_Verify otp_verify=new Otp_Verify(Roll_Number, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("Response",response);

                try
                {
                    if (new JSONObject(response).get("success").equals("true"))
                    {
                     name.setText(new JSONObject(response).getString("name"));
                     email.setText(new JSONObject(response).getString("email"));
                     rollnumber.setText(new JSONObject(response).getString("rollnumber"));
                     amount.setText(new JSONObject(response).getString("amount"));
                     mobile.setText(new JSONObject(response).getString("mobile"));


                    }
                    else if (new JSONObject(response).get("success").equals("false"))
                    {

                        Toast.makeText(User_show.this, "Invalid Otp", Toast.LENGTH_SHORT).show();

                    }



                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }



            }
        });

        requestQueue.add(otp_verify);


    }
    public class Otp_Verify extends StringRequest {

        private static final String REGISTER_URL = "https://nitconlinevoting.000webhostapp.com/MiniBook/User/Profile.php";
        private Map<String, String> parameters;
        public Otp_Verify(String OTP, Response.Listener<String> listener) {
            super(Method.POST,REGISTER_URL, listener, null);

            parameters = new HashMap<>();
            parameters.put("RollNo",Roll_Number);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError
        {
            return parameters;
        }
    }

}
