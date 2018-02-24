package com.zhuhu.application_practice.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.EditText;

import com.zhuhu.application_practice.R;
import com.zhuhu.application_practice.menu.search.SiyangActivity;

public class SearchActivity extends Activity {

    View search_view;
    EditText editText;
    String str2 = "四羊方鼎";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initCtrl();
        setTypeface();
        search_view = findViewById(R.id.Search_button);
        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText1 = (EditText) findViewById(R.id.Search_edit);
                String str1 = editText1.getText().toString();
                if(str1.equals(str2) ) {
                    Intent it = new Intent(SearchActivity.this, SiyangActivity.class);
                    startActivity(it);
                }
                else {
                    System.out.println("找不到数据");
                }

            }
        });

    }
    private void initCtrl() {
        editText = (EditText) findViewById(R.id.Search_edit);
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString("输入藏品关键字，例如：后母戊鼎");
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
        EditText textView1 = (EditText) findViewById(R.id.Search_edit);

        //将字体文件保存在assets/fonts/目录下，创建Typeface对象
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        //使用字体
        textView1.setTypeface(tf);
    }


}
