package com.example.minicanteen;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
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
import com.example.minicanteen.Admin.Admin_Login;
import com.example.minicanteen.Admin.Admin_Register;
import com.example.minicanteen.Student.Signup;
import com.example.minicanteen.Student.User_Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    TextView signup,admin;
    Button signin;
    EditText std_login_roll,std_login_pass;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    private static final Integer[] XMEN= {R.drawable.bigbang,R.drawable.game_of_thrones,R.drawable.hannibal,R.drawable.house};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        signup=(TextView)findViewById(R.id.Sign_Up);
        signin=(Button)findViewById(R.id.user_submit);
        admin=(TextView)findViewById(R.id.Admin_Login);


        std_login_roll=(EditText)findViewById(R.id.login_id);
        std_login_pass=(EditText)findViewById(R.id.login_pass);

        progressDialog=new ProgressDialog(this);
        requestQueue= Volley.newRequestQueue(MainActivity.this);

        loginPreferences=getSharedPreferences("Login_Roll",MODE_PRIVATE);
        loginPrefsEditor=loginPreferences.edit();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Signup.class));
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Admin_Login.class));
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Roll_Number=std_login_roll.getText().toString().trim();
                String Password=std_login_pass.getText().toString().trim();

                if(TextUtils.isEmpty(Roll_Number) || TextUtils.isEmpty(Password))
                {
                    Toast.makeText(getApplicationContext(),"please fill all the fields",Toast.LENGTH_SHORT).show();


                }

                else
                {
                    if(Roll_Number.length()<9)
                    {
                        std_login_roll.setError("Please Enter Valid Roll Number");
                    }
                    else if(Password.length()<6)
                    {
                        std_login_pass.setError("Minimum 6 lengths required");
                    }

                    else
                    {
                        progressDialog.setTitle("Processing...");
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCancelable(false);
                        progressDialog.setIndeterminate(true);
                        progressDialog.show();

                        StudenLogin studenLogin=new StudenLogin(Roll_Number, Password, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("Response",response);
                             try
                             {
                                 if (new JSONObject(response).get("success").equals("true"))
                                 {
                                     loginPrefsEditor.putString("std_roll",Roll_Number);
                                     loginPrefsEditor.commit();
                                     progressDialog.dismiss();


                                     Intent intent=new Intent(getApplicationContext(), User_Profile.class);
                                     startActivity(intent);
                                 }
                                 else if (new JSONObject(response).get("success").equals("false"))
                                 {
                                     progressDialog.dismiss();
                                     Toast.makeText(MainActivity.this, "Invalid password or user id", Toast.LENGTH_SHORT).show();

                                 }

                             }
                             catch (JSONException e)
                             {
                                 e.printStackTrace();
                             }
                            }
                        });
                        requestQueue.add(studenLogin);


                    }

                }


               //startActivity(new Intent(MainActivity.this, User_Profile.class));
            }
        });
    }

    private void init() {
        for(int i=0;i<XMEN.length;i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(MainActivity.this,XMENArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }

    public class StudenLogin extends StringRequest
    {
        private static final String REGISTER_URL = "https://nitconlinevoting.000webhostapp.com/MiniBook/User/Login.php";
        private Map<String, String> parameters;

        public StudenLogin(String Rollnmber, String pass, Response.Listener<String> listener) {
            super(Method.POST, REGISTER_URL, listener, null);
            parameters = new HashMap<>();
            parameters.put("roll",Rollnmber);
            parameters.put("pass", pass);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError
        {
            return parameters;
        }
    }


}
