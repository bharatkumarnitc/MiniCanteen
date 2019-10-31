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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.minicanteen.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Scanner_QR extends AppCompatActivity  implements View.OnClickListener {
    Button buttonScan,sendmoney;
    private TextView textViewName, textViewAddress;
    private IntentIntegrator qrScan;
    RequestQueue requestQueue;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String Roll_Number,id;
    EditText amount;
    int flag=0;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner__qr);

        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        sendmoney=(Button)findViewById(R.id.std_send_money);
        amount=(EditText)findViewById(R.id.amount);

        qrScan = new IntentIntegrator(this);
        buttonScan.setOnClickListener(this);

        requestQueue= Volley.newRequestQueue(Scanner_QR.this);
        progressDialog=new ProgressDialog(this);

        pref=getSharedPreferences("Login_Roll",MODE_PRIVATE);
        editor=pref.edit();
        Roll_Number=pref.getString("std_roll",null);



        sendmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Money=amount.getText().toString().trim();

                if(flag==0)
                {
                    Toast.makeText(Scanner_QR.this, "Scan Qr Code",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(Money))
                {
                    Toast.makeText(Scanner_QR.this, "Enter the amount",Toast.LENGTH_LONG).show();
                }
                else
                {
                    progressDialog.setTitle("Processing...");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();

                    ScanCode scancode=new ScanCode(Roll_Number, id, Money, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("Response",response);


                            try
                            {
                                if (new JSONObject(response).get("success").equals("true"))
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(Scanner_QR.this, "Money Send Sucessfully", Toast.LENGTH_SHORT).show();

                                }
                                else if (new JSONObject(response).get("success").equals("false"))
                                {
                                   progressDialog.dismiss();
                                    Toast.makeText(Scanner_QR.this, "Amount is not Sufficient", Toast.LENGTH_SHORT).show();

                                }



                            }
                            catch (JSONException e)
                            {
                                progressDialog.dismiss();
                                Toast.makeText(Scanner_QR.this, "Network Problem", Toast.LENGTH_SHORT).show();
                                //e.printStackTrace();
                            }

                        }
                    });

                    requestQueue.add(scancode);
                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    flag=1;
                    textViewName.setText(obj.getString("Id"));
                    textViewAddress.setText(obj.getString("Name"));
                    id=textViewName.getText().toString().trim();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
        qrScan.initiateScan();

    }


    public class ScanCode extends StringRequest {
        private static final String REGISTER_URL = "https://nitconlinevoting.000webhostapp.com/MiniBook/User/transaction.php";
        private Map<String, String> parameters;
        public ScanCode(String Roll,String id,String amount, Response.Listener<String> listener) {
            super(Method.POST,REGISTER_URL, listener,null);
            parameters = new HashMap<>();
            parameters.put("roll",Roll);
            parameters.put("id", id);
            parameters.put("amount", amount);
        }
        @Override
        protected Map<String, String> getParams() throws AuthFailureError
        {
            return parameters;
        }
    }
}
