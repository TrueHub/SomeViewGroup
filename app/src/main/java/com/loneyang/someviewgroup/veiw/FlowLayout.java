package com.loneyang.someviewgroup.veiw;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.loneyang.someviewgroup.R;
import java.util.ArrayList;

/**
 * Created by user on 2017/5/4.
 */

public class FlowLayout extends ViewGroup {

    private String childSelcted;//当前选中的child上的文字
    private int selectedIndex = -1;//当前选中的child坐标

    private OnChildSelectedListener mOnChildSelectedListener;

    public void setOnChildSelectedListener(OnChildSelectedListener onChildSelectedListener) {
        this.mOnChildSelectedListener = onChildSelectedListener;
    }

    public String getChildSelcted() {
        return childSelcted;
    }

    public void setChildSelcted(String childSelcted) {
        this.childSelcted = childSelcted;
    }

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(widthMeasureSpec);

        int width = 0;
        int height = 0;

        int lineWidth = 0;//记录每一行的宽度， width取最大的值
        int lineHeight = 0;//记录每一行的高度，height不断累加

        for (int i = 0; i < getChildCount(); i++) {

            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams childParams = (MarginLayoutParams) childView.getLayoutParams();

            int childWidth = childView.getMeasuredWidth() + childParams.leftMargin + childParams.rightMargin;
            int childHeight = childView.getMeasuredHeight() + childParams.topMargin + childParams.bottomMargin;

            if (lineWidth + childWidth > widthSize) {//如果当前控件和当前这一行的宽度之和大于widthSize，换行
                width = Math.max(lineWidth, childWidth);
                lineWidth = childWidth;//重新记录lineWidth，初始为childWidth
                height += childHeight;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            if (i == getChildCount() - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        setMeasuredDimension((MeasureSpec.EXACTLY == widthMode) ? widthSize : width,
                (MeasureSpec.EXACTLY == heightMode) ? heightSize : height);
    }

    private ArrayList<ArrayList<View>> mLines = new ArrayList<>();//二级List，里面有所有的view
    private ArrayList<Integer> mLineHeight = new ArrayList<>();//记录每一行的行高

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLines.clear();
        mLineHeight.clear();
        int width = getWidth();
        int lineWidth = 0, lineHeight = 0;

        ArrayList<View> lineViews = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            MarginLayoutParams childParams = (MarginLayoutParams) child.getLayoutParams();
            //先处理需要换行的情况
            if (lineWidth + childWidth + childParams.leftMargin + childParams.rightMargin > width) {
                mLineHeight.add(lineHeight);
                mLines.add(lineViews);
                lineWidth = 0;
                lineViews = new ArrayList<>();
            }
            //不用换行时，累加
            lineWidth = childWidth + childParams.leftMargin + childParams.rightMargin + lineWidth;
            lineHeight = Math.max(lineHeight, childParams.topMargin + childParams.bottomMargin + childHeight);
            lineViews.add(child);
            //child的点击事件
            final int finalI = i;
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() == null) v.setTag(true);

                    if (selectedIndex != finalI) {//本次点击view不是上次选中的view
                        if (selectedIndex != -1) {
                            getChildAt(selectedIndex).setBackground(getResources().getDrawable(R.drawable.tv_bg));
                        }
                        v.setBackground(getResources().getDrawable(R.drawable.tv_bg_selcected));//设定选中效果
                        selectedIndex = finalI;
                        TextView tv = (TextView) v;
                        childSelcted = (String) tv.getText();
                    } else {//点击的是已经选中的view，取消选中效果
                        selectedIndex = -1;
                        v.setBackground(getResources().getDrawable(R.drawable.tv_bg));
                        childSelcted = null;
                    }
                        mOnChildSelectedListener.onChildSelected(childSelcted,v);

                }
            });
        }
        //记录最后一行
        mLineHeight.add(lineHeight);
        mLines.add(lineViews);


        int left = 0, top = 0;
        //根据行数和每行的view个数遍历所有view

        for (int i = 0; i < mLines.size(); i++) {
            lineViews = mLines.get(i);
            lineHeight = mLineHeight.get(i);
            //遍历当前行所有的view
            for (int j = 0; j < lineViews.size(); j++) {
                View childView = lineViews.get(j);
                if (childView.getVisibility() == GONE) continue;
                MarginLayoutParams childParams = (MarginLayoutParams) childView.getLayoutParams();
                int childLeft = left + childParams.leftMargin;
                int childTop = top + childParams.topMargin;
                int childBottom = childTop + childView.getMeasuredHeight();
                int childRight = childLeft + childView.getMeasuredWidth();
                childView.layout(childLeft, childTop, childRight, childBottom);
                left += childView.getMeasuredWidth() + childParams.leftMargin + childParams.rightMargin;
            }
            left = 0;
            top += lineHeight;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public interface OnChildSelectedListener{
        void onChildSelected(String content,View child);
    }
}
