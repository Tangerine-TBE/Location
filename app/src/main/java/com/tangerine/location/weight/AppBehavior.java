package com.tangerine.location.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;

public class AppBehavior extends AppBarLayout.ScrollingViewBehavior  {
    private static final String TAG = "AppBehavior";
    private float parentBeginY;//布局开始的AppLayout起始位置
    private boolean mInitView; //是否已经加载过一次布局了
    private float lastCurrentDependencyY;//上一次AppBarLayout的位置
    private int offsetY;//需要nestScroll偏移多少量
    private float childYUpSet; //布局开始时，只能向上滑动的nestScrollView的位置（已经进行了偏移量）
    private float mAppFoldArea;//AppBar可折叠区域大小
    public AppBehavior() {
    }

    public AppBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        if (!mInitView) {
            TypedArray typedArray = dependency.getContext().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
            int mTitleBarSize = typedArray.getDimensionPixelSize(0, 0);
            int appBarSize = dependency.getHeight();
            mAppFoldArea = appBarSize - mTitleBarSize;
            offsetY = appBarSize / 2 - 100;
            typedArray.recycle();
            mInitView = true;
            parentBeginY = dependency.getY();
            float nestY = child.getY();
            lastCurrentDependencyY = parentBeginY;
            childYUpSet = nestY - offsetY;
            child.setY(childYUpSet);//设置起始高度
        }
        //即时的AppBarY轴变化
        int currentDependencyY = (int) dependency.getY();
        //判断向上或向下移动的逻辑；
        if (currentDependencyY != lastCurrentDependencyY) {
            float foldSize = Math.abs(parentBeginY - currentDependencyY);//折叠距离
            float value = foldSize / mAppFoldArea;//折叠距离/可以折叠的总距离 = 当前折叠占比多少
            float downOffsetValue = value * offsetY;
            float y = childYUpSet - downOffsetValue;
            child.setY(y);
        }
        lastCurrentDependencyY = currentDependencyY;
        return true;
    }


}
