package com.example.myapplication;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class MainActivity2 extends AppCompatActivity {
    Button btnFetch,btnClear;
    TextView txtData1,txtData2,txtData3,txtData4,emname,emlastname,emuser,empass;
    private String Emname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        txtData1 = (TextView) this.findViewById(R.id.txtData1);
        txtData2 = (TextView) this.findViewById(R.id.txtData2);
        txtData3 = (TextView) this.findViewById(R.id.txtData3);
        txtData4 = (TextView) this.findViewById(R.id.txtData4);
        btnFetch = (Button) findViewById(R.id.btnFetch);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnFetch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ConnectMySql connectMySql = new ConnectMySql();
                connectMySql.execute("");
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtData1.setText("");
                txtData2.setText("");
                txtData3.setText("");
                txtData4.setText("");
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private class ConnectMySql extends AsyncTask<String, Void, String> {
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity2.this, "Please wait...", Toast.LENGTH_SHORT)
                    .show();

        }

        @Override
        protected String doInBackground(String... params) {
            String _user = "sa";
            String _pass = "ua";
            String _DB = "IE";
            String _server = "192.168.1.69";
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Connection conn = null;
            String ConnURL = null;
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                ConnURL = "jdbc:jtds:sqlserver://" + _server + ";"
                        + "databaseName=" + _DB + ";user=" + _user + ";password="
                        + _pass + ";";
                conn = DriverManager.getConnection(ConnURL);
                System.out.println("Databaseection success");

                String result = "";

                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("select TOP(1) Emname, Emlastname, Emuser, Empass from Work_Employee Order by EmID DESC");
                ResultSetMetaData rsmd = rs.getMetaData();

                while (rs.next()) {
                    String txtData1 = rs.getString("Emname").toString() + "\n";
                    result += rs.getString("Emlastname").toString() + "\n";
                    result += rs.getString("Emuser").toString() + "\n";
                    result += rs.getString("Empass").toString() + "\n";
                }
                res = result;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        protected void onPostExecute(String result) {
            txtData1.setText(result);
            txtData2.setText(result);
            //txtData3.setText(result);
            //txtData4.setText(result);
        }
    }

}