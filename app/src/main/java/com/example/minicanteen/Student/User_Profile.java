package com.example.minicanteen.Student;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minicanteen.MainActivity;
import com.example.minicanteen.R;

import org.w3c.dom.Text;

public class User_Profile extends AppCompatActivity {

    TextView qrcode,stdprofile,stdexpens;
    private static long backButtonCount;
    //boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed()
    {
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
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.commonmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.logout)
        {

            Intent it = new Intent(getApplicationContext(), MainActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__profile);
        qrcode=(TextView)findViewById(R.id.scan_qr);
        stdprofile=(TextView)findViewById(R.id.Profile);
        stdexpens=(TextView)findViewById(R.id.exp);



        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(User_Profile.this,Scanner_QR.class));
            }
        });

        stdprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(User_Profile.this,User_show.class));
            }
        });

        stdexpens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_Profile.this,Expenses.class));
            }
        });
    }
}
