package com.lauwba.surelabs.lapor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lauwba.surelabs.lapor.library.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button login;
    Button register;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        // define session
        sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.chekSession()) {
            finish();
            startActivity(new Intent(MainActivity.this, MainMenuActivity.class));
        } else {
            return;
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("") && password.getText().toString().equals("")) {
//                    Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(MainActivity.this, MainMenuActivity.class));
//                    finish();
                    Toast.makeText(MainActivity.this, "Lengkapi Semua Kolom", Toast.LENGTH_SHORT).show();
                } else {
                    new ProsesLogin().execute(new String[]{username.getText().toString(), password.getText().toString()});
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Toast", Toast.LENGTH_SHORT).show();
            }
        });

//        register = findViewById(R.id.register);
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, Registrasi.class));
//                finish();
//            }
//        });
    }

    private class ProsesLogin extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "",
                    "Mengirimkan data...", false, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestHandler handler = new RequestHandler();
            HashMap<String, String> parameter = new HashMap<>();

            parameter.put(Config.EMAIL, strings[0]);
            parameter.put(Config.PASSWORD, strings[1]);

            String result = handler.sendPostRequest(Config.URL_LOGIN, parameter);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            // tanda ! berarti kebalikan atau negasi
            if (!s.isEmpty()) {
                try {
                    JSONObject object = new JSONObject(s);
                    Log.i("RESPONSE", "onPostExecute: " + s);
                    JSONArray array = object.getJSONArray("response");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        String nik = jsonObject.getString("nik");
                        String nama = jsonObject.getString("nama");
                        String email = jsonObject.getString("email");

                        if (status.equals("0")) {
                            if (sessionManager.simpanSession(nik, nama, email)) {
                                finish();
                                startActivity(new Intent(MainActivity.this, MainMenuActivity.class));
                                Toast.makeText(MainActivity.this, "Login Berhasil. Selamat Datang, " + nama, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Terjadi Kesalah Saat Membuat Session", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
