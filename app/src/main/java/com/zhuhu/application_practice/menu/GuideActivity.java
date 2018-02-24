package com.zhuhu.application_practice.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zhuhu.application_practice.R;
import com.zhuhu.application_practice.menu.search.HallGuideActivity;

import java.util.Timer;
import java.util.TimerTask;

public class GuideActivity extends Activity {

    EditText editText;
    View guide_view;
    String str2 = "青铜展厅";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initCtrl();
        setTypeface();
        initViews();
        setListeners();


    }

    private void initCtrl() {
        editText = (EditText) findViewById(R.id.Guide_edit);
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString("输入目的地关键字，例如：青铜展厅");
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

    private void setTypeface() {
        String fontPath = "fonts/FZCuJinLJW.TTF";

        //得到TextView控件对象
        EditText textView1 = (EditText) findViewById(R.id.Guide_edit);

        //将字体文件保存在assets/fonts/目录下，创建Typeface对象
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        //使用字体
        textView1.setTypeface(tf);
    }

    private void initViews() {
        guide_view = findViewById(R.id.Guide_button);
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
            }
        }, cnt);
    }

    private void setListeners() {
        guide_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = editText.getText().toString();
                if (str1.equals(str2)) {

                    Toast toast = Toast.makeText(GuideActivity.this, "正在寻找路径", Toast.LENGTH_LONG);
                    showMyToast(toast, 5 * 1000);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent it = new Intent(GuideActivity.this, HallGuideActivity.class);
                            startActivity(it);
                        }
                    }, 5000);

                }
            }
        });

    }
}
