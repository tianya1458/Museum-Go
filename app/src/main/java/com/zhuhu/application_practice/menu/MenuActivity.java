package com.zhuhu.application_practice.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuhu.application_practice.R;

public class MenuActivity extends Activity {

    ImageView mImageView01;
    ImageView mImageView02;
    ImageView mImageView03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setTypeFont();
        initViews();
        setListeners();

    }

    private void setTypeFont(){
        String fontPath = "fonts/FZCuJinLJW.TTF";

        //得到TextView控件对象
        TextView textView1 =(TextView)findViewById (R.id.text_guide);
        TextView textView2 =(TextView)findViewById (R.id.text_search);
        TextView textView3 =(TextView)findViewById (R.id.text_gifts);

        //将字体文件保存在assets/fonts/目录下，创建Typeface对象
        Typeface tf =Typeface.createFromAsset(getAssets(),fontPath);

        //使用字体
        textView1.setTypeface(tf);
        textView2.setTypeface(tf);
        textView3.setTypeface(tf);
    }

    private void initViews(){
        mImageView01 = (ImageView) findViewById(R.id.Guide);
        mImageView02 = (ImageView) findViewById(R.id.Search);
        mImageView03 = (ImageView) findViewById(R.id.Gifts);
    }

    private void setListeners(){
        mImageView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent();
                it.setAction("action.GUIDE");
                it.addCategory("category.GUIDE");
                startActivity(it);
            }
        });

        mImageView02.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent it = new Intent();
                it.setAction("action.SEARCH");
                it.addCategory("category.SEARCH");
                startActivity(it);
            }
        });

        mImageView03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, GiftsActivity.class);
                startActivity(it);
            }
        });


    }





}

