package com.loneyang.someviewgroup.veiw;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import static android.content.ContentValues.TAG;

/**
 * Created by user on 2017/5/4.
 */

public class MyGroup extends ViewGroup {
    public MyGroup(Context context) {
        this(context, null);
    }

    public MyGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //计算所有孩子的宽高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        int childCount = getChildCount();

        int childWidth = 0;
        int childHeight = 0;
        MarginLayoutParams childParms;

        //左边两个控件的高度
        int leftHeight = 0;
        //右边两个控件的高度
        int rightHeight = 0;

        //上边两个控件的宽度
        int topWidth = 0;
        //下面两个控件的宽度
        int bottmoWidth = 0;

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childWidth = childView.getMeasuredWidth();
            childHeight = childView.getMeasuredHeight();
            childParms = (MarginLayoutParams) childView.getLayoutParams();
            if (0 == i || 1 == i) {//上面两个控件
                topWidth += childWidth + childParms.leftMargin + childParms.rightMargin;
            }
            if (2 == i || 3 == i) {//下面两个控件
                bottmoWidth += childWidth + childParms.leftMargin + childParms.rightMargin;
            }
            if (0 == i || 2 == i) {//左面两个控件
                leftHeight += childHeight + childParms.topMargin + childParms.bottomMargin;
            }
            if (1 == i || 3 == i) {//右面两个控件
                rightHeight += childHeight + childParms.topMargin + childParms.bottomMargin;
            }
        }
        width = Math.max(topWidth, bottmoWidth);
        height = Math.max(leftHeight, rightHeight);

        //如果是EXACTLY模式，就直接设置为量测的值，否则就是计算的值
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? widthSize : width,
                (heightMode == MeasureSpec.EXACTLY) ? heightSize : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int childWidth = 0;
        int childHeight = 0;
        MarginLayoutParams childParams;

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childWidth = childView.getMeasuredWidth();
            childHeight = childView.getMeasuredHeight();
            childParams = (MarginLayoutParams) childView.getLayoutParams();

            int childLeft = 0, childRight = 0, childTop = 0, childBottom = 0;

            switch (i) {
                case 0:
                    childLeft = childParams.leftMargin;
                    childTop = childParams.topMargin;
                    break;
                case 1:
                    childLeft = getWidth() - childWidth - childParams.leftMargin - childParams.rightMargin;
                    childTop = childParams.topMargin;
                    break;
                case 2:
                    childLeft = childParams.leftMargin;
                    childTop = getHeight() - childHeight - childParams.topMargin - childParams.bottomMargin;
                    break;
                case 3:
                    childLeft = getWidth() - childWidth - childParams.leftMargin - childParams.rightMargin;
                    childTop = getHeight() - childHeight - childParams.topMargin - childParams.bottomMargin;
                    break;
            }
            childRight = childLeft + childWidth;
            childBottom = childTop + childHeight;
            childView.layout(childLeft, childTop, childRight, childBottom);

        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        Log.e("MSL", "generateDefaultLayoutParams");
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(
            ViewGroup.LayoutParams p) {
        Log.e("MSL", "generateLayoutParams p");
        return new MarginLayoutParams(p);
    }
}
