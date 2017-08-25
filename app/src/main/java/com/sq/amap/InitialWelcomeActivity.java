package com.sq.amap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InitialWelcomeActivity extends Activity implements View.OnClickListener {

    public static void start(Context context) {
        Intent starter = new Intent(context, InitialWelcomeActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_welcome);
        Button beijingButton = findViewById(R.id.bj_button);
        Button shanghaiButton = findViewById(R.id.sh_button);
        Button guangzhouButton = findViewById(R.id.gz_button);
        beijingButton.setOnClickListener(this);
        shanghaiButton.setOnClickListener(this);
        guangzhouButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bj_button:
                intent = new Intent(InitialWelcomeActivity.this, InitialCenterActivity.class);
                intent.putExtra(InitialCenterActivity.CITI_KEY, InitialCenterActivity.BEIJING);
                startActivity(intent);
                break;
            case R.id.gz_button:
                intent = new Intent(InitialWelcomeActivity.this, InitialCenterActivity.class);
                intent.putExtra(InitialCenterActivity.CITI_KEY, InitialCenterActivity.GUANGZHOU);
                startActivity(intent);
                break;
            case R.id.sh_button:
                intent = new Intent(InitialWelcomeActivity.this, InitialCenterActivity.class);
                intent.putExtra(InitialCenterActivity.CITI_KEY, InitialCenterActivity.SHANGHAI);
                startActivity(intent);
                break;

        }
    }
}
