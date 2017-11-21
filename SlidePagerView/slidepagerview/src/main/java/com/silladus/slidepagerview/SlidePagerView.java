package com.silladus.slidepagerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

/**
 * Created by silladus on 2017/11/16/0016.
 * GitHub: https://github.com/silladus
 * Description:
 */

public class SlidePagerView extends LinearLayout implements GestureDetector.OnGestureListener {
    /**
     * 手势监听器
     */
    private GestureDetector mGestureDetector;
    /**
     * 当前选中界面的宽度
     */
    private int selectWidth;
    /**
     * ChildView之间的间距
     */
    private int distance;
    /**
     * 滑动方向
     */
    private int direction = 0;
    /**
     * 滑动item位置
     */
    private int index;
    /**
     * 滑动变化量
     */
    private float mScrollX = selectWidth + distance;
    /**
     * 缩放动画持续时间
     */
    private long scaleAnimDuration = 600;

    public void setScaleAnimDuration(long scaleAnimDuration) {
        this.scaleAnimDuration = scaleAnimDuration;
    }

    public SlidePagerView(@NonNull Context context) {
        this(context, null);
    }

    public SlidePagerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidePagerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setClickable(true);
        mGestureDetector = new GestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                performClick();
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (getChildCount() < 2) {
            throw new IllegalArgumentException("SlidePagerView must have 2 child at least.");
        }

        // 初始化大小
        for (int i = 1; i < getChildCount(); i++) {
            scale(getChildAt(i), evaluate(1, 1, 0.9));// 缩小
        }
        selectWidth = getChildAt(0).getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (distance == 0) {// onLayout 会被调用多次，只需要第一次初始化distance，避免重复计算;
            distance = getChildAt(1).getLeft() - getChildAt(0).getRight();
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float offset = e1.getX() - e2.getX();
        if (offset > 0) {// 向左滑动
            if (index < getChildCount() - 1) {
                index++;
                direction = -1;
            } else {
                direction = 1;
            }
        }
        if (offset < 0) {// 向右滑动
            if (index > 0) {
                index--;
                direction = 1;
            } else {
                direction = -1;
            }
        }
        doFlingAnimator(selectWidth + distance);
        return false;
    }

    /**
     * 滑行动画
     */
    private void doFlingAnimator(final float scrollX) {
        ValueAnimator mFlingAnimator = ValueAnimator.ofFloat(scrollX, 0);
        mFlingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mScrollX = (float) animation.getAnimatedValue();
                scrollTo((int) (mScrollX * direction + index * scrollX), 0);
                // 计算动画速率
                // float fraction = (scrollX - Math.abs(mScrollX * direction)) / scrollX;
                float fraction = 1 - Math.abs(mScrollX * direction) / scrollX;
                if (index == 0) {
                    scale(getChildAt(index), evaluate(fraction, 0.9, 1));// 放大
                    scale(getChildAt(index + 1), evaluate(fraction, 1, 0.9));// 缩小
                } else if (index == getChildCount() - 1) {
                    scale(getChildAt(index), evaluate(fraction, 0.9, 1));// 放大
                    scale(getChildAt(index - 1), evaluate(fraction, 1, 0.9));// 缩小
                } else {
                    scale(getChildAt(index), evaluate(fraction, 0.9, 1));// 放大
                    scale(getChildAt(index - 1), evaluate(fraction, 1, 0.9));// 缩小
                    scale(getChildAt(index + 1), evaluate(fraction, 1, 0.9));// 缩小
                }
            }
        });

        mFlingAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (onSelectListener != null) {
                    onSelectListener.selectIndex(index);
                }
            }
        });

        mFlingAnimator.setDuration(scaleAnimDuration);
        mFlingAnimator.setInterpolator(new DecelerateInterpolator());
        mFlingAnimator.start();
    }

    /**
     * view缩放
     */
    protected void scale(View view, float scaleValue) {
        view.setScaleX(scaleValue);// X方向上缩放
        view.setScaleY(scaleValue);// Y方向上缩放
    }

    /**
     * 变动后值的实时计算
     *
     * @param fraction 百分占比
     * @param startValue 变动起始值
     * @param endValue 变动终值
     * @return 变动后自身的值
     */
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        //noinspection UnnecessaryBoxing
        return Float.valueOf(startFloat + fraction * (endValue.floatValue() - startFloat));
    }

    public interface OnSelectListener {
        void selectIndex(int index);
    }

    private OnSelectListener onSelectListener;

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }
}
