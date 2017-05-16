package com.loneyang.someviewgroup.veiw;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.loneyang.someviewgroup.R;

import static android.content.ContentValues.TAG;

/**
 * Created by vone on 2017/5/16.
 */

public class ArcMenu extends ViewGroup implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        mButton = findViewById(R.id.id_button);
        if (mButton == null) {
            mButton = getChildAt(0);
        }
        rotateView(mButton, 0f, 270f, 500);
        toggleMenu(300);
    }

    /*private void toogleMenu(int durationMillis) {
        for (int i = 0; i < getChildCount() - 1; i++) {
            View childView = getChildAt(i + 1);

            int xFlag = 1;
            int yFlag = 1;
            if (mPosition == Position.LEFT_TOP || mPosition == Position.LEFT_BOTTOM) {
                xFlag = -1;
            }
            if (mPosition == Position.LEFT_TOP || mPosition == Position.RIGHT_TOP) {
                yFlag = -1;
            }
            //childView 的left 和 top
            int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (getChildCount() - 2) * i));
            int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (getChildCount() - 2) * i));
        }
    }*/

    /**
     * 设置菜单的显示位置，定义四个角
     */
    private enum Position {
        LEFT_TOP, RIGHT_TOP, LEFT_BOTTOM, RIGHT_BOTTOM;
    }

    private enum Status {
        OPEN, CLOSE;
    }

    private Position mPosition = Position.LEFT_TOP;

    private int mRadius = 150; //菜单显示的半径，默认150

    private View mButton;//用户点击的按钮

    private Status mCurrentStatus = Status.CLOSE;

    /**
     * 按钮点击事件的接口
     */
    public interface OnMenuItemClickListener {
        void onClick(View view, int position);
    }

    private OnMenuItemClickListener onMenuItemClickListener;

    public void setOnMenuItemClickListener(
            OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mRadius, getResources().getDisplayMetrics());

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu, 0, defStyleAttr);

        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ArcMenu_position:
                    int val = typedArray.getInt(attr, 0);
                    switch (val) {
                        case 0:
                            mPosition = Position.LEFT_TOP;
                            break;
                        case 1:
                            mPosition = Position.RIGHT_TOP;
                            break;
                        case 2:
                            mPosition = Position.LEFT_BOTTOM;
                            break;
                        case 3:
                            mPosition = Position.RIGHT_BOTTOM;
                            break;
                    }
                    break;
                case R.styleable.ArcMenu_radius:
                    mRadius = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_PX, 100f, getResources().getDisplayMetrics()
                    ));
                    break;
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

//        if (!changed) return;
        layoutButton();

        int count = getChildCount();
        for (int i = 0; i < count - 1; i++) {
            View child = getChildAt(i + 1);
            child.setVisibility(GONE);
            int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
            int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));

            int cWidth = child.getMeasuredWidth();
            int cHeight = child.getMeasuredHeight();

            //bottom
            if (mPosition == Position.LEFT_BOTTOM || mPosition == Position.RIGHT_BOTTOM) {

                ct = getMeasuredHeight() - cHeight - ct;
            }
            //right
            if (mPosition == Position.RIGHT_TOP || mPosition == Position.RIGHT_BOTTOM) {
                cl = getMeasuredWidth() - cl - cWidth;
            }
            child.layout(l, t, cl + cWidth, ct + cHeight);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void layoutButton() {
        View cButton = getChildAt(0);

        cButton.setOnClickListener(this);

        int l = 0, t = 0;

        int width = cButton.getMeasuredWidth();
        int height = cButton.getMeasuredHeight();

        switch (mPosition) {
            case LEFT_TOP:
                l = 0;
                t = 0;
                break;
            case LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                t = 0;
                break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
                break;
        }
        cButton.layout(l, t, l + width, t + height);

    }

    /**
     * 按钮动画
     */
    private void rotateView(View view, float fromDegress, float toDegress, int durationMillis) {
        RotateAnimation animation = new RotateAnimation(fromDegress, toDegress, Animation.RELATIVE_TO_SELF, .5f,
                Animation.RELATIVE_TO_SELF, .5f);
        animation.setDuration(durationMillis);
        animation.setFillAfter(true);
        view.setAnimation(animation);
    }

    public void toggleMenu(int durationMillis) {
        int count = getChildCount();
        for (int i = 0; i < count - 1; i++) {
            final View childView = getChildAt(i + 1);
            childView.setVisibility(View.VISIBLE);

            int xflag = 1;
            int yflag = 1;

            if (mPosition == Position.LEFT_TOP
                    || mPosition == Position.LEFT_BOTTOM)
                xflag = -1;
            if (mPosition == Position.LEFT_TOP
                    || mPosition == Position.RIGHT_TOP)
                yflag = -1;

            // child left
            int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
            // child top
            int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));

            AnimationSet animset = new AnimationSet(true);
            Animation animation = null;
            if (mCurrentStatus == Status.CLOSE) {// to open
                animset.setInterpolator(new OvershootInterpolator(2F));
                animation = new TranslateAnimation(xflag * cl, 0, yflag * ct, 0);
                childView.setClickable(true);
                childView.setFocusable(true);
            } else {// to close
                animation = new TranslateAnimation(0f, xflag * cl, 0f, yflag
                        * ct);
                childView.setClickable(false);
                childView.setFocusable(false);
            }
            animation.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    if (mCurrentStatus == Status.CLOSE)
                        childView.setVisibility(View.GONE);

                }
            });

            animation.setFillAfter(true);
            animation.setDuration(durationMillis);
            // 为动画设置一个开始延迟时间，纯属好看，可以不设
            animation.setStartOffset((i * 100) / (count - 1));
            RotateAnimation rotate = new RotateAnimation(0, 720,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(durationMillis);
            rotate.setFillAfter(true);
            animset.addAnimation(rotate);
            animset.addAnimation(animation);
            childView.startAnimation(animset);
            final int index = i + 1;
            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMenuItemClickListener != null)
                        onMenuItemClickListener.onClick(childView, index - 1);
                    menuItemAnin(index - 1);
                    changeStatus();

                }
            });

        }
        changeStatus();
        Log.e(TAG, mCurrentStatus.name() + "");
    }

    /**
     * 开始菜单动画，点击的MenuItem放大消失，其他的缩小消失
     *
     * @param item
     */
    private void menuItemAnin(int item) {
        for (int i = 0; i < getChildCount() - 1; i++) {
            View childView = getChildAt(i + 1);
            if (i == item) {
                childView.startAnimation(scaleBigAnim(300));
            } else {
                childView.startAnimation(scaleSmallAnim(300));
            }
            childView.setClickable(false);
            childView.setFocusable(false);

        }

    }

    /**
     * 缩小消失
     *
     * @param durationMillis
     * @return
     */
    private Animation scaleSmallAnim(int durationMillis) {
        Animation anim = new ScaleAnimation(1.0f, 0f, 1.0f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        anim.setDuration(durationMillis);
        anim.setFillAfter(true);
        return anim;
    }

    /**
     * 放大，透明度降低
     *
     * @param durationMillis
     * @return
     */
    private Animation scaleBigAnim(int durationMillis) {
        AnimationSet animationset = new AnimationSet(true);

        Animation anim = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        Animation alphaAnimation = new AlphaAnimation(1, 0);
        animationset.addAnimation(anim);
        animationset.addAnimation(alphaAnimation);
        animationset.setDuration(durationMillis);
        animationset.setFillAfter(true);
        return animationset;
    }

    private void changeStatus() {
        mCurrentStatus = (mCurrentStatus == Status.CLOSE ? Status.OPEN
                : Status.CLOSE);
    }

}
