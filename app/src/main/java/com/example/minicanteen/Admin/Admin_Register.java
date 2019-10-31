package com.example.minicanteen.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.minicanteen.R;
import com.example.minicanteen.Student.Otp;
import com.example.minicanteen.Student.Signup;
import com.example.minicanteen.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Admin_Register extends AppCompatActivity {

    EditText admin_id,admin_name,admin_email,admin_mobile,admin_pass,admin_re_pass;
    Button admin_submit;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__register);

        admin_submit=(Button)findViewById(R.id.admin_shop_submit);
        admin_id=(EditText)findViewById(R.id.admin_shop_id);
        admin_name=(EditText)findViewById(R.id.admin_shop_name);
        admin_email=(EditText)findViewById(R.id.admin_shope_email);
        admin_mobile=(EditText)findViewById(R.id.admin_mobile);
        admin_pass=(EditText)findViewById(R.id.admin_shop_pass);
        admin_re_pass=(EditText)findViewById(R.id.admin_shop_repass);

        admin_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Id=admin_id.getText().toString().trim();
                String Name=admin_name.getText().toString().trim();
                String Email=admin_email.getText().toString().trim();
                String Mobile=admin_mobile.getText().toString().trim();
                String Pass=admin_pass.getText().toString().trim();
                String Repass=admin_re_pass.getText().toString().trim();

                requestQueue= Volley.newRequestQueue(Admin_Register.this);
                progressDialog=new ProgressDialog(Admin_Register.this);




                if(TextUtils.isEmpty(Id) || TextUtils.isEmpty(Name) || TextUtils.isEmpty(Email)|| TextUtils.isEmpty(Mobile) || TextUtils.isEmpty(Pass) ||TextUtils.isEmpty(Repass))
                {
                    Toast.makeText(getApplicationContext(),"please fill all the fields",Toast.LENGTH_SHORT).show();

                }
                else
                {
                   if(!Validation.emailValidation(Email))
                    {
                        admin_email.setError("Please Enter Valid Email id");
                    }
                    else if(!Validation.mobileValidation(Mobile))
                    {
                        admin_mobile.setError("Please Enter the valid Mobile number");
                    }
                    else if(Pass.length()<5)
                    {
                        admin_pass.setError("Password should be minimum 5 lenght");
                    }
                    else if(Repass.length()<5)
                    {
                        admin_re_pass.setError("Password should be minimum 5 lenght");
                    }
                    else if(!TextUtils.equals(Pass,Repass))
                    {
                        admin_pass.setError("Password don't Match");
                        admin_re_pass.setError("Password don't Match");

                    }

                    else
                    {
                        progressDialog.setTitle("Processing...");
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCancelable(false);
                        progressDialog.setIndeterminate(true);
                        progressDialog.show();

                        Admin_register admin_register=new Admin_register(Id, Name, Email, Mobile, Pass, Repass, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("Response",response);
                                try
                                {
                                    if(new JSONObject(response).get("success").equals("true"))
                                    {

                                        progressDialog.dismiss();
                                        Toast.makeText(Admin_Register.this, "Register Successfull", Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(getApplicationContext(), Admin_Login.class);
                                        startActivity(intent);


                                    }
                                    else if(new JSONObject(response).get("success").equals("false"))
                                    {

                                        progressDialog.dismiss();
                                        Toast.makeText(Admin_Register.this, "Network Problem", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(new JSONObject(response).get("success").equals("duplicate"))
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(Admin_Register.this, "Id Number alredy register", Toast.LENGTH_SHORT).show();
                                    }


                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }




                            }
                        });

                        requestQueue.add(admin_register);



                    }


                    }

            }
        });


    }


    public class Admin_register extends StringRequest
    {
        private static final String REGISTER_URL = "https://nitconlinevoting.000webhostapp.com/MiniBook/Admin/Admin_Register.php";
        private Map<String, String> parameters;
        public Admin_register(String Roll_Number, String Name, String Email, String Mobile, String pass, String repass, Response.Listener<String> listener) {
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
