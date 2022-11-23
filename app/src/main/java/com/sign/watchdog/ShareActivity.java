package com.sign.watchdog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class ShareActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Uri data = intent.getData();
        String host = data.getHost();
        String path = data.getPath();
        String str = data.getQueryParameter("id");
        Toast.makeText(this, "WD: " + host + " " + path + " " + str, Toast.LENGTH_LONG).show();
        finish();
    }
}
