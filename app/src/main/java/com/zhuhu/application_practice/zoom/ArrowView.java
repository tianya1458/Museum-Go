package com.zhuhu.application_practice.zoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.zhuhu.application_practice.R;

/**
 * Created by Administrator on 2017/11/29 0029.
 */

public class ArrowView extends View {
    public float bitmapX;
    public float bitmapY;
    public ArrowView(Context context) {
        super(context);
        //设置箭头的起始坐标
        bitmapX = 0;
        bitmapY = 0;
    }

    //重写View类的onDraw()方法
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //创建,并且实例化Paint的对象
        Paint paint = new Paint();
        //根据图片生成位图对象
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.pic_arrow);
        //绘制箭头
        canvas.drawBitmap(bitmap, bitmapX, bitmapY,paint);
        //判断图片是否回收,木有回收的话强制收回图片
        if(bitmap.isRecycled())
        {
            bitmap.recycle();
        }
    }
}
