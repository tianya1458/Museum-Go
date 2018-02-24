package com.zhuhu.application_practice;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class Scan_instructionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_instruction);
        initTypeFont();
    }

    private void initTypeFont() {

        String fontPath = "fonts/FZCuJinLJW.TTF";

        //得到TextView控件对象
        TextView textView1 = (TextView) findViewById(R.id.scan_text1);
        TextView textView2 = (TextView) findViewById(R.id.scan_text2);
        TextView textView3 = (TextView) findViewById(R.id.scan_text3);

        //将字体文件保存在assets/fonts/目录下，创建Typeface对象
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        //使用字体
        textView1.setTypeface(tf);
        textView2.setTypeface(tf);
        textView3.setTypeface(tf);
    }
}
