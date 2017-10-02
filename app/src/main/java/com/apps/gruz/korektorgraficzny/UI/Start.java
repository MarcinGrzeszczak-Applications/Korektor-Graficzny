package com.apps.gruz.korektorgraficzny.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.apps.gruz.korektorgraficzny.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.module.ManifestParser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Start extends AppCompatActivity {

    @BindView(R.id.equalizer_gif) ImageView equalizerView;

    final static int EXTERLNAL_STORAGE_PERMISSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ButterKnife.bind(this);

        Glide.with(this).load(R.drawable.eqalizer).asGif().into(equalizerView);

        checkPermissions();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(Start.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERLNAL_STORAGE_PERMISSION);
    }

    @OnClick(R.id.start_button)
    public void goToMain() {
        startActivity(new Intent(this,Main.class));
        finish();
    }

    @OnClick(R.id.load_button)
    public void goToLoad() {
        startActivity(new Intent(this,Load.class));
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case EXTERLNAL_STORAGE_PERMISSION:
                if( grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    checkPermissions();
                break;
        }
    }
}
