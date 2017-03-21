package com.zhouyaming.floatview;

/**
 * Created by Administrator on 2017/3/21.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class FloatView2 {
    private Context c;
    public FloatView2(Context c) {
        this.c=c;
    }

    private WindowManager wm;
    private View view;// 浮动按钮
    /**
     * 添加悬浮View
     * @param paddingBottom 悬浮View与屏幕底部的距离
     */
    public void createFloatView(int paddingBottom) {
        int w = 200;// 大小
        wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        view = LayoutInflater.from(c).inflate(R.layout.floatview, null);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_BASE_APPLICATION;// 所有程序窗口的“基地”窗口，其他应用程序窗口都显示在它上面。
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSLUCENT;// 不设置这个弹出框的透明遮罩显示为黑色
        params.width = w;
        params.height = w;
        params.gravity = Gravity.TOP | Gravity.LEFT;
        int screenWidth = c.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = c.getResources().getDisplayMetrics().heightPixels;
        params.x = screenWidth - w;
        params.y = screenHeight - w - paddingBottom;
        view.setBackgroundColor(Color.TRANSPARENT);
        view.setVisibility(View.VISIBLE);
        view.setOnTouchListener(new View.OnTouchListener() {
            // 触屏监听
            float lastX, lastY;
            int oldOffsetX, oldOffsetY;
            int tag = 0;// 悬浮球 所需成员变量

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                float x = event.getX();
                float y = event.getY();
                if (tag == 0) {
                    oldOffsetX = params.x; // 偏移量
                    oldOffsetY = params.y; // 偏移量
                }
                if (action == MotionEvent.ACTION_DOWN) {
                    lastX = x;
                    lastY = y;
                } else if (action == MotionEvent.ACTION_MOVE) {
                    params.x += (int) (x - lastX) / 3; // 减小偏移量,防止过度抖动
                    params.y += (int) (y - lastY) / 3; // 减小偏移量,防止过度抖动
                    tag = 1;
                    wm.updateViewLayout(view, params);
                } else if (action == MotionEvent.ACTION_UP) {
                    int newOffsetX = params.x;
                    int newOffsetY = params.y;
                    // 只要按钮一动位置不是很大,就认为是点击事件
                    if (Math.abs(oldOffsetX - newOffsetX) <= 20
                            && Math.abs(oldOffsetY - newOffsetY) <= 20) {
                        if(l != null){
                            l.onClick(view);
                        }
                    } else {
                        tag = 0;
                    }
                }
                return true;
            }
        });
        wm.addView(view, params);
    }

    /**
     * 点击浮动按钮触发事件，需要override该方法
     */
    private View.OnClickListener l;
    public void onFloatViewClick(View.OnClickListener l) {
        this.l=l;
    }

    /**
     * 将悬浮View从WindowManager中移除，需要与createFloatView()成对出现
     */
    public void removeFloatView() {
        if (wm != null && view != null) {
            wm.removeViewImmediate(view);
// wm.removeView(view);//不要调用这个，WindowLeaked
            view = null;
            wm = null;
        }
    }

    /**
     * 隐藏悬浮View
     */
    public void hideFloatView() {
        if (wm != null && view != null&&view.isShown()) {
            view.setVisibility(View.GONE);
        }
    }
    /**
     * 显示悬浮View
     */
    public void showFloatView(){
        if (wm != null && view != null&&!view.isShown()) {
            view.setVisibility(View.VISIBLE);
        }
    }
}