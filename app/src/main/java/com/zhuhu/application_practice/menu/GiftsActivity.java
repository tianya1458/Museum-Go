package com.zhuhu.application_practice.menu;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.zhuhu.application_practice.R;

public class GiftsActivity extends Activity {

    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);
        setTypeFont();
    }

    //转换字体
    private void setTypeFont(){

        String fontPath = "fonts/FZCuJinLJW.TTF";
        textview=(TextView)findViewById(R.id.gift_text);
        Typeface tf =Typeface.createFromAsset(getAssets(),fontPath);
        textview.setTypeface(tf);
    }
}
