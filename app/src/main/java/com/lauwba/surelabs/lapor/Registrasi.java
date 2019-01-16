package com.lauwba.surelabs.lapor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class Registrasi extends AppCompatActivity {

    EditText email, password, namaLengkap, nomorTelepon, noKTP;
    Button registrasi;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        namaLengkap = findViewById(R.id.namalengkap);
        nomorTelepon = findViewById(R.id.nomorTelepon);
        noKTP = findViewById(R.id.nomorKTP);

        registrasi = findViewById(R.id.registrasi);
        login = findViewById(R.id.login);

        // ketika tombol registrasi di klik, eksekusi yang ada didalam onClick
        registrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //kondisi bila semua atau salah satu kolom tidak di isi
                if (email.getText().toString().equals("") || password.getText().toString().equals("") || namaLengkap.getText().toString().equals("") || nomorTelepon.getText().toString().equals("")) {
                    Toast.makeText(Registrasi.this, "Semua Kolom Harus Diisikan", Toast.LENGTH_SHORT).show();

                    //kondisi bila semua kolom telah di isi
                } else {

                    new ProsesRegistrasi().execute(new String[]{
                            email.getText().toString(),
                            namaLengkap.getText().toString(),
                            noKTP.getText().toString(),
                            nomorTelepon.getText().toString(),
                            password.getText().toString()
                    });
                }
            }
        });

        // proses ketika tulisan klik disini di klik
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //untuk menampilkan halaman Login
                startActivity(new Intent(Registrasi.this, MainActivity.class));

                //untuk menutup halaman atau mengakhiri sebuah halaman
                finish();
            }
        });
    }

    private class ProsesRegistrasi extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Registrasi.this, "",
                    "Mengirimkan data...", false, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestHandler handler = new RequestHandler();
            HashMap<String, String> parameter = new HashMap<>();

            parameter.put(Config.EMAIL, strings[0]);
            parameter.put(Config.NAMA, strings[1]);
            parameter.put(Config.NO_KTP, strings[2]);
            parameter.put(Config.NO_TELP, strings[3]);
            parameter.put(Config.PASSWORD, strings[4]);

            return handler.sendPostRequest(Config.URL_REGISTRASI, parameter);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            // tanda ! berarti kebalikan atau negasi
            if (!s.isEmpty()) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.getJSONArray("response");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status == "0") {
                            Toast.makeText(Registrasi.this, message + ". Silahkan Login", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Registrasi.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(Registrasi.this, message + ". {Response Code : " + status + "}", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
