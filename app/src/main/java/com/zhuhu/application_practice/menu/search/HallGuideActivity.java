package com.zhuhu.application_practice.menu.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhuhu.application_practice.MainActivity;
import com.zhuhu.application_practice.R;

import java.util.Timer;
import java.util.TimerTask;

public class HallGuideActivity extends Activity {

    ImageView imageView1;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hallguide);

        imageView1=(ImageView)findViewById(R.id.hall1);
        handler=new Handler();

        //设置两个异步延时，一个延时转换图片，另一个跳转到主界面并弹出toast
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView1.setImageDrawable(getResources().getDrawable(R.drawable.pic_hall2));
            }
        }, 3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(HallGuideActivity.this, MainActivity.class);
                startActivity(intent);
                Toast toast = Toast.makeText(HallGuideActivity.this, "导航结束", Toast.LENGTH_LONG);
                showMyToast(toast, 1000);
            }
        }, 6000);

    }
    public void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3500);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
                HallGuideActivity.this.finish();
            }
        }, cnt);
    }

}
