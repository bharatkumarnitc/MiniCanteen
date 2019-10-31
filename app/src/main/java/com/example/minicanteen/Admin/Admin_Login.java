package com.example.minicanteen.Admin;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.minicanteen.MainActivity;
import com.example.minicanteen.R;
import com.example.minicanteen.Student.User_Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Admin_Login extends AppCompatActivity {

    TextView admin_register;
    EditText admin_login,admin_pass;
    Button Admin_signin;

    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__login);

        admin_register=(TextView)findViewById(R.id.Admin_register);
        admin_login=(EditText)findViewById(R.id.Admin_login_email);
        admin_pass=(EditText)findViewById(R.id.Admin_login_pass);
        Admin_signin=(Button)findViewById(R.id.admin_submit);

        progressDialog=new ProgressDialog(this);
        requestQueue= Volley.newRequestQueue(Admin_Login.this);

        loginPreferences=getSharedPreferences("Admin",MODE_PRIVATE);
        loginPrefsEditor=loginPreferences.edit();

        admin_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Admin_Register.class));
            }
        });

       Admin_signin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               final String id=admin_login.getText().toString().trim();
               String Pass=admin_pass.getText().toString().trim();

               if(TextUtils.isEmpty(id) || TextUtils.isEmpty(Pass))
               {
                   Toast.makeText(getApplicationContext(),"please fill all the fields",Toast.LENGTH_SHORT).show();


               }
               else if(Pass.length()<6)
               {
                   admin_pass.setError("Minimum 6 lengths required");
               }

               else
               {

                   progressDialog.setTitle("Processing...");
                   progressDialog.setMessage("Please wait...");
                   progressDialog.setCancelable(false);
                   progressDialog.setIndeterminate(true);
                   progressDialog.show();

                   Login_Admin login_admin=new Login_Admin(id, Pass, new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {

                           Log.i("Response",response);
                           try
                           {
                               if (new JSONObject(response).get("success").equals("true"))
                               {
                                   loginPrefsEditor.putString("admin_id",id);
                                   loginPrefsEditor.commit();
                                   progressDialog.dismiss();


                                   Intent intent=new Intent(getApplicationContext(), Admin_Page.class);
                                   startActivity(intent);
                               }
                               else if (new JSONObject(response).get("success").equals("false"))
                               {
                                   progressDialog.dismiss();
                                   Toast.makeText(Admin_Login.this, "Invalid password or user id", Toast.LENGTH_SHORT).show();

                               }

                           }
                           catch (JSONException e)
                           {
                               e.printStackTrace();
                           }

                       }
                   });

                   requestQueue.add(login_admin);


               }

           }
       });



    }

    public class Login_Admin extends StringRequest {
        private static final String REGISTER_URL = "https://nitconlinevoting.000webhostapp.com/MiniBook/Admin/Admin_Login.php";
        private Map<String, String> parameters;


        public Login_Admin(String id,String Pass,Response.Listener<String> listener) {
            super(Method.POST,REGISTER_URL, listener, null);

            parameters = new HashMap<>();
            parameters.put("roll",id);
            parameters.put("pass", Pass);
        }
        @Override
        protected Map<String, String> getParams() throws AuthFailureError
        {
            return parameters;
        }
    }
}
