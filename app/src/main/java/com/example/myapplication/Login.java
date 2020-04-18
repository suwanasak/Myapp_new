package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends AppCompatActivity {

    EditText Emuser, Emname;
    Button btnLogin;
    ProgressBar progressBar;
    LinearLayout lvparent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Emuser = findViewById(R.id.edtEmailAddress);
        Emname = findViewById(R.id.edtEmailAddress);
        btnLogin = findViewById(R.id.btnLogin);
        lvparent = findViewById(R.id.lvparent);

        progressBar = findViewById(R.id.progressBar);
    }


    private class DoLoginForUser extends AsyncTask<String, Void, String> {
        String emuser,emname;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            emuser = Emuser.getText().toString();
            emname = Emname.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ConnectionHelper con = new ConnectionHelper();
                Connection connect = ConnectionHelper.CONN();

                String query = "SELECT * FROM Work_Employee WHERE Emuser ='" + emuser + "'";
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

        @Override
        protected void onPostExecute(String result) {

            //Toast.makeText(Login.this, result, Toast.LENGTH_SHORT).show();
            ShowSnackBar(result);
            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            if (result.equals("success")) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userdetails",0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("user", emuser);
                editor.putString("name", emname);
                editor.commit();

                Intent i = new Intent(Login.this, MainActivity.class);
                finish();
                startActivity(i);

            } else {
                ShowSnackBar(result);
            }
        }
    }

    public void ShowSnackBar(String message) {
        Snackbar.make(lvparent, message, Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    public void DoLogin(View v)
    {
        DoLoginForUser login = new DoLoginForUser();
        login.execute("");
    }

}
