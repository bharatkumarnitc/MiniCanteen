package com.example.minicanteen.Student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class Otp extends AppCompatActivity {
    EditText otp_number;
    Button verify;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String Roll_Number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otp_number=(EditText)findViewById(R.id.otp_enter);
        verify=(Button)findViewById(R.id.otp_verify);

        progressDialog=new ProgressDialog(this);
        requestQueue= Volley.newRequestQueue(Otp.this);

        pref=getSharedPreferences("User_Otp",MODE_PRIVATE);
        editor=pref.edit();
        Roll_Number=pref.getString("std_roll",null);



        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String OTP=otp_number.getText().toString().trim();

                if(TextUtils.isEmpty(OTP))
                {
                    Toast.makeText(getApplicationContext(),"please fill all the fields",Toast.LENGTH_SHORT).show();
                }

                else
                {

                    progressDialog.setTitle("Processing...");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();
                    Otp_Verify otp_verify=new Otp_Verify(OTP,Roll_Number, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("Response",response);

                            try
                            {
                                if (new JSONObject(response).get("success").equals("true"))
                                {

                                    progressDialog.dismiss();
                                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                                else if (new JSONObject(response).get("success").equals("false"))
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(Otp.this, "Invalid Otp", Toast.LENGTH_SHORT).show();

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


            }
        });
    }

    public class Otp_Verify extends StringRequest {

        private static final String REGISTER_URL = "https://nitconlinevoting.000webhostapp.com/MiniBook/User/OTP_VERIFY.php";
        private Map<String, String> parameters;
        public Otp_Verify(String OTP, String Roll,Response.Listener<String> listener) {
            super(Method.POST,REGISTER_URL, listener, null);

            parameters = new HashMap<>();
            parameters.put("RollNo",Roll_Number);
            parameters.put("otp",OTP);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError
        {
            return parameters;
        }
    }
}
