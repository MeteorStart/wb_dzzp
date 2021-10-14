package com.qiyang.wb_dzzp.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author: X_Meteor
 * @description: RecycleView简单分割线
 * @version: V_1.0.0
 * @date: 2017/5/26 14:47
 * @company:
 * @email: lx802315@163.com
 */
public class SimpleDicider extends RecyclerView.ItemDecoration {

    public float mDividerHeight;

    private Paint mPaint;

    int lineColor = Color.parseColor("#e7e7e7");

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        mPaint.setColor(lineColor);
    }


    public SimpleDicider() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(lineColor);
    }

    public SimpleDicider(int lineColor) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(lineColor);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //如果不是第一个，则设置top的值。
        if (parent.getChildAdapterPosition(view) != 0) {
            //这里直接硬编码为1px
            outRect.top = 1;
            mDividerHeight = 2;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);

            int index = parent.getChildAdapterPosition(view);
            //第一个ItemView不需要绘制
            if (index <= 0) {
                continue;
            }

            float dividerTop = view.getTop() - mDividerHeight;
            float dividerLeft = parent.getPaddingLeft();
            float dividerBottom = view.getTop();
            float dividerRight = parent.getWidth() - parent.getPaddingRight();

            c.drawRect(dividerLeft, dividerTop, dividerRight, dividerBottom, mPaint);
        }
    }

}
