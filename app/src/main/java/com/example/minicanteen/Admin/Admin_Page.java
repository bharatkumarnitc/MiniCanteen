package com.example.minicanteen.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minicanteen.MainActivity;
import com.example.minicanteen.R;

public class Admin_Page extends AppCompatActivity {

    TextView addmoney,adminprofile,adminexp;
    private static long backButtonCount;

    @Override
    public void onBackPressed() {
        if(backButtonCount >= 1)
        {
            Intent it = new Intent(getApplicationContext(), MainActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
            finish();
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__page);

        addmoney=(TextView)findViewById(R.id.Admin_add_amount);
        adminprofile=(TextView)findViewById(R.id.Admin_profile);
        adminexp=(TextView)findViewById(R.id.Admin_exp);




        addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Admin_Add_Money.class));
            }
        });


        adminprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Admin_Profile.class));
            }
        });

        adminexp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Admin_Exp_table.class));
            }
        });

    }
}
