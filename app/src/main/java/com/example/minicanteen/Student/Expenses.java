package com.example.minicanteen.Student;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.minicanteen.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnPxWidthModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Expenses extends AppCompatActivity {

    InputStream in;
    String line;
    String result;
    StringBuffer sb;
    static String[][] data;
    static String[] header={"Date","Shop Id","Amount"};
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String Roll_Number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        pref=getSharedPreferences("Login_Roll",MODE_PRIVATE);
        editor=pref.edit();
        Roll_Number=pref.getString("std_roll",null);


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        final TableView<String[]> tableView = (TableView<String[]>)findViewById(R.id.showcustomer);
        tableView.setHeaderBackgroundColor(Color.parseColor("#B3E5FC"));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this,header));
        tableView.setColumnCount(3);

        alldetails();
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, data));


    }

    public void alldetails()
    {
        try {
            URL url=new URL("https://nitconlinevoting.000webhostapp.com/MiniBook/User/Expenses.php");
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            in=new BufferedInputStream(con.getInputStream());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            BufferedReader bf=new BufferedReader(new InputStreamReader(in));
            sb=new StringBuffer();
            while((line=bf.readLine())!=null)
            {
                sb.append(line+"\n");
            }
            in.close();
            result=sb.toString();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try{
            JSONArray jsonArray=new JSONArray(result);
            JSONObject jsonObject=null;
            data=new String[jsonArray.length()][3];
            int k=0;
            for(int i=0;i<jsonArray.length();i++) {
                k=0;
                for (int j = 0; j <3 ; j++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.getString("RollNo").equalsIgnoreCase(Roll_Number)) {

                        if (k == 0) {
                            data[i][j] = jsonObject.getString("Date");
                        } else if (k == 1) {
                            data[i][j] = jsonObject.getString("ShopNo");
                        } else if (k == 2) {
                            data[i][j] = jsonObject.getString("PurchaseAmount");
                        }
                        k++;
                    }
                }
            }



        }
        catch (Exception e)
        {
            e.printStackTrace();
       }


    }


}
