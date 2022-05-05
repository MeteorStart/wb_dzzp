package com.qiyang.wb_dzzp.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2017/7/10 21:51
 * @company:
 * @email: lx802315@163.com
 */
public class RecycleViewUtils {

    /**
     * 设置简单分割线的垂直管理器
     *
     * @param context
     * @param recyclerView
     */
    public static RecyclerView.LayoutManager getVerticalLayoutManager(Context context, RecyclerView recyclerView) {

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDicider());
        return layoutManager;
    }

    /**
     * 设置简单分割线的垂直管理器
     *
     * @param context
     * @param recyclerView
     */
    public static RecyclerView.LayoutManager getVerticalGridLayoutManager(Context context, int spanCount, RecyclerView recyclerView) {

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, spanCount, LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecycleGridDivider(20));
        return layoutManager;
    }

    /**
     * 设置简单分割线的垂直管理器
     *
     * @param context
     * @param recyclerView
     */
    public static RecyclerView.LayoutManager getVerticalLayoutManagerNoDecoration(Context context, RecyclerView recyclerView) {

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        return layoutManager;
    }

    /**
     * 设置简单分割线的水平管理器
     *
     * @param context
     * @param recyclerView
     */
    public static RecyclerView.LayoutManager getHorizontalLayoutManager(Context context, RecyclerView recyclerView) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        return layoutManager;
    }

}
