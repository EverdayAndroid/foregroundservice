package com.everday.foregroundservice.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.everday.foregroundservice.R;

public class KeppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_kepp);
        Window window = getWindow();
        window.setGravity(Gravity.START|Gravity.TOP);
        window.setBackgroundDrawable(new ColorDrawable(Color.RED));
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = 10;
        attributes.height = 10;
        attributes.x = 0;
        attributes.y = 0;
        window.setAttributes(attributes);
        KeepManager.getInstance().setKeep(this);

    }
}
