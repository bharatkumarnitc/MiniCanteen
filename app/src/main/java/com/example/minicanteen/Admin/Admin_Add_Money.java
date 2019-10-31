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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.minicanteen.R;
import com.example.minicanteen.Student.User_show;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Admin_Add_Money extends AppCompatActivity {

    EditText rollnumber,amount;
    Button send_money;

    RequestQueue requestQueue;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String Shop_id;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__add__money);

        rollnumber=(EditText)findViewById(R.id.admin_std_roll);
        amount=(EditText)findViewById(R.id.admin_std_money);
        send_money=(Button)findViewById(R.id.admin_std_submit);

        requestQueue= Volley.newRequestQueue(Admin_Add_Money.this);
        progressDialog=new ProgressDialog(this);

        pref=getSharedPreferences("Admin",MODE_PRIVATE);
        editor=pref.edit();
        Shop_id=pref.getString("admin_id",null);

        send_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Roll=rollnumber.getText().toString().trim();
                String Money=amount.getText().toString().trim();

                if(TextUtils.isEmpty(Roll) || TextUtils.isEmpty(Money))
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
                    Send_Money send_money=new Send_Money(Shop_id, Roll, Money, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("Response",response);
                            try
                            {
                                if (new JSONObject(response).get("success").equals("true"))
                                {

                                    progressDialog.dismiss();
                                    Toast.makeText(Admin_Add_Money.this, "Money Send Successfully", Toast.LENGTH_SHORT).show();

                                }
                                else if (new JSONObject(response).get("success").equals("false"))
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(Admin_Add_Money.this, "Invalid Roll number or Amount", Toast.LENGTH_SHORT).show();

                                }
                                else
                                { progressDialog.dismiss();
                                    Toast.makeText(Admin_Add_Money.this, "Network Problem", Toast.LENGTH_SHORT).show();
                                }

                            }
                            catch (JSONException e)
                            {
                                progressDialog.dismiss();
                                Toast.makeText(Admin_Add_Money.this, "Please Enroll your roll number to particular shop", Toast.LENGTH_SHORT).show();
                                //e.printStackTrace();
                            }


                        }
                    });

                    requestQueue.add(send_money);


                }



            }
        });
    }


    public class Send_Money extends StringRequest {
        private static final String REGISTER_URL = "https://nitconlinevoting.000webhostapp.com/MiniBook/Admin/Add_Amount.php";
        private Map<String, String> parameters;
        public Send_Money(String id,String Roll,String Money, Response.Listener<String> listener) {
            super(Method.POST, REGISTER_URL,listener, null);
            parameters = new HashMap<>();

            parameters.put("id",id);
            parameters.put("roll", Roll);
            parameters.put("money", Money);
        }
        @Override
        protected Map<String, String> getParams() throws AuthFailureError
        {
            return parameters;
        }
    }
}
