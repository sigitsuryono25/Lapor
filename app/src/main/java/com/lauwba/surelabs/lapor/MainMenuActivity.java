package com.lauwba.surelabs.lapor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout laporByTelepon, laporByPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        laporByTelepon = findViewById(R.id.laporByTelepon);
        laporByPosition = findViewById(R.id.laporByPosition);

        laporByTelepon.setOnClickListener(this);
        laporByPosition.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.laporByTelepon:
//              UNTUK MELAKUKAN PANGGILAN KE NOMOR YANG TELAH DITENTUKAN
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "119"));
                startActivity(i);
                break;
            case R.id.laporByPosition:
                Intent pos = new Intent(MainMenuActivity.this, LaporByPositionActivity.class);
                startActivity(pos);
                break;
        }
    }
}
