package com.zhuhu.application_practice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    VerticalScrollTextView mSampleView;
    List lst=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mSampleView = (VerticalScrollTextView)findViewById(R.id.sampleView1);

        for(int i=0;i<30;i++){
            if(i%2==0){
                Sentence sen=new Sentence(i,i+"、金球奖三甲揭晓 C罗梅西哈维入围 ");
                lst.add(i, sen);
            }else{
                Sentence sen=new Sentence(i,i+"、公牛欲用三大主力换魔兽？？？？");
                lst.add(i, sen);
            }
        }
        //给View传递数据
        mSampleView.setList(lst);
        //更新View
        mSampleView.updateUI();
     }
}
