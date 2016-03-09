package com.maxcloud.renter.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 描    述：根据内容大小，自动调整高度的ListView。
 * 作    者：向晓阳
 * 时    间：2016/1/4
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class WapListView extends ListView {
    public WapListView(Context context) {
        super(context);

    }

    public WapListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//
//        if (isInEditMode()) {
//            return;
//        }
//
//        View localView1 = getChildAt(0);
//        int column = getWidth() / localView1.getWidth();
//        int childCount = getChildCount();
//        Paint localPaint;
//        localPaint = new Paint();
//        localPaint.setStyle(Paint.Style.STROKE);
//        localPaint.setColor(getContext().getResources().getColor(R.color.tab_button_unselected_background));
//        for (int i = 0; i < childCount; i++) {
//            View cellView = getChildAt(i);
//            if ((i + 1) % column == 0) {
//                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
//            } else if ((i + 1) > (childCount - (childCount % column))) {
//                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
//            } else {
//                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
//                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
//            }
//        }
//        if (childCount % column != 0) {
//            for (int j = 0; j < (column - childCount % column); j++) {
//                View lastView = getChildAt(childCount - 1);
//                canvas.drawLine(lastView.getRight() + lastView.getWidth() * j, lastView.getTop(), lastView.getRight() + lastView.getWidth() * j, lastView.getBottom(), localPaint);
//            }
//        }
//    }
}
