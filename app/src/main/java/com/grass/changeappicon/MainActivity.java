package com.grass.changeappicon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChangeIconManager.getInstance().bindContext(getApplication());

        findViewById(R.id.use_default).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIconManager.getInstance().changeAppIcon(ChangeIconManager.CHANGEE_ICON_CMD_DEFAULT, "");
            }
        });

        findViewById(R.id.use_welfare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeIconManager.getInstance().changeAppIcon(ChangeIconManager.CHANGEE_ICON_CMD_WALFARE, "");
            }
        });
    }
}
