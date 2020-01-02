package com.tangerine.location.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;


public class CardViewSimpleBehavior extends CoordinatorLayout.Behavior {
    private static final String TAG = "CardViewSimpleBehavior";
    //监听NestedScrollView的状态改变CardView的位置
    private float scrollY; //cardView的偏移量
    private float nestScrollViewStarY ;
    private float cardViewStarY;

    public CardViewSimpleBehavior() {
    }

    public CardViewSimpleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull LinearLayoutCompat child, @NonNull View dependency) {
//        //监听NestedScrollView的状态
//        return dependency instanceof NestedScrollView;
//
//    }
//
//    @Override
//    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull LinearLayoutCompat child, @NonNull View dependency) {
//        //监听状态并且实时改变CardView的偏移位置
//        //根据比例进行改变CardView的偏移位置
//        //初始化时对NestScrollView进行监听，拿到NestScrollView的当前的高度，以及CardView的高度
//        //scrollY 为可偏移的高度
//
//        return true;
//    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return dependency instanceof  NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        if (nestScrollViewStarY == 0){
            nestScrollViewStarY = dependency.getY();//初始化一开始的nestScrollView最大Y
        }
        float y = dependency.getY();//拿到实时改变的NestScrollView进行变化的Y
        double dy = (nestScrollViewStarY-y)/nestScrollViewStarY ;//y轴方向上偏移量百分比
        if (cardViewStarY == 0){
            cardViewStarY = child.getY();
        }
//        child.setTranslationY((float) (cardViewStarY * dy));
        child.setTranslationY(y/2 + 50); //设置cardView的偏移量
        Log.e(TAG, "onDependentViewChanged: " + y );
        Log.e(TAG, "nestScrollViewStarY: " + nestScrollViewStarY );
        Log.e(TAG, "Y轴方向上的改变偏移量" + dy );
        Log.e(TAG, "CardView的初始化Y轴" + cardViewStarY );
        return true;
    }
}
