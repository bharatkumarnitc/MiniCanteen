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
import com.example.minicanteen.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    EditText std_roll,std_name,std_email,std_mobile,std_pass,std_re_pass;
    Button otpsend;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        otpsend=(Button)findViewById(R.id.user_Otp_Send);
        std_roll=(EditText)findViewById(R.id.std_signup_roll);
        std_name=(EditText)findViewById(R.id.std_signup_name);
        std_email=(EditText)findViewById(R.id.std_signup_email);
        std_mobile=(EditText)findViewById(R.id.std_singup_mobile);
        std_pass=(EditText)findViewById(R.id.std_singnup_pass);
        std_re_pass=(EditText)findViewById(R.id.std_signup_repass);

        requestQueue= Volley.newRequestQueue(Signup.this);
        progressDialog=new ProgressDialog(this);
        loginPreferences=getSharedPreferences("User_Otp",MODE_PRIVATE);
        loginPrefsEditor=loginPreferences.edit();

        otpsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

         final String Roll=std_roll.getText().toString().trim();
         String Name=std_name.getText().toString().trim();
         String Email=std_email.getText().toString().trim();
         String Mobile=std_mobile.getText().toString().trim();
         String Pass=std_pass.getText().toString().trim();
         String Repass=std_re_pass.getText().toString().trim();


         if(TextUtils.isEmpty(Roll) || TextUtils.isEmpty(Name) || TextUtils.isEmpty(Email)|| TextUtils.isEmpty(Mobile) || TextUtils.isEmpty(Pass) ||TextUtils.isEmpty(Repass))
         {
             Toast.makeText(getApplicationContext(),"please fill all the fields",Toast.LENGTH_SHORT).show();

         }
         else
         {
             if(Roll.length()<9)
             {
                 std_roll.setError("Please Enter Valid Roll Number");
             }
         else if(!Validation.emailValidation(Email))
         {
             std_email.setError("Please Enter Valid Email id");
         }
         else if(!Validation.mobileValidation(Mobile))
         {
             std_mobile.setError("Please Enter the valid Mobile number");
         }
         else if(Pass.length()<5)
             {
                 std_pass.setError("Password should be minimum 5 lenght");
             }
             else if(Repass.length()<5)
             {
                 std_re_pass.setError("Password should be minimum 5 lenght");
             }
         else if(!TextUtils.equals(Pass,Repass))
             {
                 std_pass.setError("Password don't Match");
                 std_re_pass.setError("Password don't Match");

             }
         else
         {

             progressDialog.setTitle("Processing...");
             progressDialog.setMessage("Please wait...");
             progressDialog.setCancelable(false);
             progressDialog.setIndeterminate(true);
             progressDialog.show();

             User_sign_up user_sign_up=new User_sign_up(Roll, Name, Email, Mobile, Pass, Repass, new Response.Listener<String>() {
                 @Override
                 public void onResponse(String response) {
                     Log.i("Response",response);

                     try
                     {
                       if(new JSONObject(response).get("success").equals("true"))
                       {
                           loginPrefsEditor.putString("std_roll",Roll);
                           loginPrefsEditor.commit();
                           progressDialog.dismiss();
                           Toast.makeText(Signup.this, "Check your Mail id", Toast.LENGTH_LONG).show();
                           Intent intent=new Intent(getApplicationContext(), Otp.class);
                           startActivity(intent);


                       }
                       else if(new JSONObject(response).get("success").equals("false"))
                       {

                           progressDialog.dismiss();
                           Toast.makeText(Signup.this, "Network Problem", Toast.LENGTH_SHORT).show();
                       }
                       else if(new JSONObject(response).get("success").equals("duplicate"))
                       {
                           progressDialog.dismiss();
                           Toast.makeText(Signup.this, "Roll Number alredy register", Toast.LENGTH_SHORT).show();
                       }


                     }
                     catch (JSONException e)
                     {
                         e.printStackTrace();
                     }



                 }
             });

             requestQueue.add(user_sign_up);
             //startActivity(new Intent(Signup.this,Otp.class));
         }



         }


            }
        });
    }


    public class User_sign_up extends StringRequest {

        private static final String REGISTER_URL = "https://nitconlinevoting.000webhostapp.com/MiniBook/User/User_Sign_up.php";
        private Map<String, String> parameters;
        public User_sign_up(String Roll_Number,String Name,String Email,String Mobile,String pass,String repass,Response.Listener<String> listener) {
            super(Method.POST, REGISTER_URL, listener, null);

            parameters = new HashMap<>();
            parameters.put("RollNo",Roll_Number);
            parameters.put("Name",Name);
            parameters.put("email",Email);
            parameters.put("mobile",Mobile);
            parameters.put("password",pass);
        }
        @Override
        protected Map<String, String> getParams() throws AuthFailureError
        {
            return parameters;
        }
    }
}
