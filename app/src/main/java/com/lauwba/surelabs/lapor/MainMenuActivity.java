package com.lauwba.surelabs.lapor;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout laporByTelepon, laporByPosition, hubPolisi, hubBpbd, stokDarahPMI, pemadamKebakaran;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        laporByTelepon = findViewById(R.id.laporByTelepon);
        laporByPosition = findViewById(R.id.laporByPosition);
        hubPolisi = findViewById(R.id.hubPolisi);
        hubBpbd = findViewById(R.id.hubBpbd);
        stokDarahPMI = findViewById(R.id.stokDarahPMI);
        pemadamKebakaran = findViewById(R.id.pemadamKebakaran);

        laporByTelepon.setOnClickListener(this);
        laporByPosition.setOnClickListener(this);
        hubPolisi.setOnClickListener(this);
        hubBpbd.setOnClickListener(this);
        stokDarahPMI.setOnClickListener(this);
        pemadamKebakaran.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.laporByTelepon:
//              UNTUK MELAKUKAN PANGGILAN KE NOMOR YANG TELAH DITENTUKAN
                new AlertDialog.Builder(MainMenuActivity.this)
                        .setMessage("Pilih")
                        .setPositiveButton("119 (Bebas Pulsa)", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "119"));
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("0271890119 (Nomor Lokal)", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0271890119"));
                                startActivity(i);
                            }
                        })
                        .setNeutralButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
                break;
            case R.id.laporByPosition:
                Intent pos = new Intent(MainMenuActivity.this, LaporByPositionActivity.class);
                startActivity(pos);
                break;
            case R.id.hubPolisi:
                Intent pol = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0271891510"));
                startActivity(pol);
                break;
            case R.id.hubBpbd:
                Intent bpbd = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0271891433"));
                startActivity(bpbd);
                break;
            case R.id.pemadamKebakaran:
                Intent pemadamKebakaran = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0271891113"));
                startActivity(pemadamKebakaran);
                break;
            case R.id.stokDarahPMI:
                Intent pmi = new Intent(MainMenuActivity.this, WebViewActivity.class);
                pmi.putExtra(Config.URL, "http://psc119.sragenkab.go.id/");
                startActivity(pmi);
                break;
        }
    }
}
