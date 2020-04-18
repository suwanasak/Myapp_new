package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_QR_SCAN = 4;
    TextView lblEmail, lblName, lblPass, UserId, OnDate, UserRole,tsearch, textContent, tresult, tid, eid, euser, tstyle, tbind, tsize, tcolor, tamount, tcode, tplant, tname;
    String Emuser, Emname;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Permission StrictMode
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // txtSearch
        // textContent = (TextView) findViewById(R.id.txtSearch);
        // txtResult
        tresult = (TextView) findViewById(R.id.txtResult);
        tsearch = (TextView) findViewById(R.id.txtBarcode);
        // txtTextView
        tid = (TextView) findViewById(R.id.txtId);
        tcode = (TextView) findViewById(R.id.txtCode);
        tbind = (TextView) findViewById(R.id.txtBind);
        tstyle = (TextView) findViewById(R.id.txtStyle);
        tcolor = (TextView) findViewById(R.id.txtColor);
        tsize = (TextView) findViewById(R.id.txtSize);
        tamount = (TextView) findViewById(R.id.txtAmount);
        tplant = (TextView) findViewById(R.id.txtPlant);

        lblEmail = findViewById(R.id.editTextname);
        lblPass = findViewById(R.id.editTextuser);
        final Button search = (Button) findViewById(R.id.btnScaner);
        Button buttonIntent = (Button) findViewById(R.id.btnScaner);
        buttonIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(Intent.createChooser(intent
                        , "Scan with"), REQUEST_QR_SCAN);
            }
        });

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userdetails", 0);

        lblEmail.setText(sharedPreferences.getString("user", "0"));
        lblPass.setText(sharedPreferences.getString("name", "0"));

        // btnSearch
        Button button3 = (Button) findViewById(R.id.btnSearch);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "btnSearch Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }
        private class ShowUser extends AsyncTask<String, Void, String> {
          String  emuser,emname;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    ConnectionHelper con = new ConnectionHelper();
                    Connection connect = ConnectionHelper.CONN();

                    String query = "SELECT * FROM Work_Employee WHERE Emuser ='" + lblEmail + "'";
                    PreparedStatement ps = connect.prepareStatement(query);

                    Log.e("query",query);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        String user = rs.getString("emuser");
                        String name = rs.getString("emname");
                        connect.close();
                        rs.close();
                        ps.close();
                        if (user != null && !user.trim().equals("") && user.equals(emuser) || name != null && !name.trim().equals("") && name.equals(emname))
                            return "success";
                        else
                            return "Invalid User ID";

                    } else
                        return "User ID Does Not Exists.";
                } catch (SQLException e) {

                    return "Error:" + e.getMessage().toString();
                } catch (Exception e) {
                    return "Error:" + e.getMessage().toString();
                }
            }
        }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_QR_SCAN && resultCode == RESULT_OK) {
            String Code = intent.getStringExtra("SCAN_RESULT");
            textContent.setText(Code);
        }
    }

}


