package com.banshouweng.bswBase.widget.BswRecyclerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.banshouweng.bswBase.R;

/**
 * @author 半寿翁
 * @date 2018/4/23.
 */

public class BswDecoration extends RecyclerView.ItemDecoration {


    private int mydevider;
    private Paint dividerPaint;
    private int type;

    public BswDecoration(Context context, @LimitAnnotation.DecorationType int type) {
        this.type = type;
        dividerPaint = new Paint();
        //设置分割线颜色
        dividerPaint.setColor(context.getResources().getColor(R.color.divider_line_color));
        //设置分割线宽度
        mydevider = context.getResources().getDimensionPixelSize(R.dimen.line_height);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mydevider;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        switch (type) {
            case LimitAnnotation.BOTTOM_DECORATION:
                int left = parent.getPaddingLeft();
                int right = parent.getWidth() - parent.getPaddingRight();
                for (int i = 0; i < childCount - 1; i++) {
                    View view = parent.getChildAt(i);
                    float bTop = view.getBottom();
                    float bBottom = view.getBottom() + mydevider;
                    c.drawRect(left, bTop, right, bBottom, dividerPaint);
                }
                break;

            case LimitAnnotation.ROUND_DECORATION:
                for (int i = 0; i < childCount; i++) {
                    View view = parent.getChildAt(i);
                    float leftV = view.getLeft();
                    float rightV = view.getRight();
                    float topV = view.getTop();
                    float bottomV = view.getBottom();

                    //noinspection UnnecessaryLocalVariable
                    float tTop = topV;
                    float tBottom = view.getTop() + mydevider;
                    c.drawRect(leftV, tTop, rightV, tBottom, dividerPaint);

                    //noinspection UnnecessaryLocalVariable
                    float bTop = bottomV;
                    float bBottom = view.getBottom() + mydevider;
                    c.drawRect(leftV, bTop, rightV, bBottom, dividerPaint);

                    //noinspection UnnecessaryLocalVariable
                    float lLeft = leftV;
                    float lRight = view.getLeft() + mydevider;
                    c.drawRect(lLeft, topV, lRight, bottomV, dividerPaint);

                    //noinspection UnnecessaryLocalVariable
                    float rLeft = rightV;
                    float rRight = view.getRight() + mydevider;
                    c.drawRect(rLeft, topV, rRight, bottomV, dividerPaint);
                }
                break;
        }
    }
}
