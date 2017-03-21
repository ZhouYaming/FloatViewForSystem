package com.zhouyaming.floatview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatView2();
    }

    private FloatView2 floatView2;
    private void floatView2() {
        floatView2 = new FloatView2(this);
        floatView2.createFloatView(200);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        floatView2.removeFloatView();
    }
}
