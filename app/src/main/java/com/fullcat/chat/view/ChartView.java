package com.fullcat.chat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by FullCat on 2016/11/12.
 */
public class ChartView extends View {

    private Paint mPaint;
    private Rect textRec;
    private List<Data> dataSource;
    private int originX;
    private int originY;
    private int width;
    private int height;
    private String txtFirst;
    private String txtLast;
    private String suffixY; //Y轴坐标的后缀
    private int ratio; //Y轴能允许的最大值
    private int cellHeight;
    private float cellWidth;
    private static final String TAG = "EarnChartView";
    private Path path;

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        textRec = new Rect();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(30);
        mPaint.setAntiAlias(true);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dataSource == null || dataSource.isEmpty()) {
            return;
        }
        //获取原点
        mPaint.getTextBounds(txtLast, 0, txtLast.length(), textRec);
        originY = height - getPaddingBottom() - textRec.height() - 16;
        int textWidthX = textRec.width();
        mPaint.getTextBounds(ratio * 4 + suffixY, 0, (ratio * 4 + suffixY).length(), textRec);
        originX = getPaddingLeft() + textRec.width() + 16;
        //绘制X轴
        canvas.drawText(txtFirst, originX, height - getPaddingBottom(), mPaint);
        canvas.drawText(txtLast, width - getPaddingRight() - textWidthX,
                height - getPaddingBottom(), mPaint);
        //获取cell宽高
        cellWidth = (width - getPaddingRight() - originX) * 1.0f / (dataSource.size() - 1);
        cellHeight = (originY - getPaddingTop()) / 4;
        for (int i = 0; i < 5; i++) {
            //画横线
            canvas.drawLine(originX, originY - cellHeight * i,
                    width - getPaddingRight(), originY - cellHeight * i, mPaint);
            mPaint.getTextBounds(ratio * i + suffixY, 0, (ratio * i + suffixY).length(), textRec);
            canvas.drawText(ratio * i + suffixY, originX - textRec.width() - 8,
                    originY - cellHeight * i + textRec.height() / 2, mPaint);
        }
        mPaint.setStyle(Paint.Style.FILL);
        //画曲线
        path.moveTo(originX, originY - (originY - getPaddingTop()) * 1.0f / (ratio * 4) * dataSource.get(0).dataY);
        mPaint.setStrokeWidth(4);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        for (int i = 0; i < dataSource.size() - 1; i++) {
            //折线
//            canvas.drawLine(originX + cellWidth * i,originY - (originY - getPaddingTop()) * 1.0f / (ratio * 4) * dataSource.get(i).dataY,
//                     originX + cellWidth * (i + 1),originY - (originY - getPaddingTop()) * 1.0f / (ratio * 4) * dataSource.get(i + 1).dataY,mPaint);
            //曲线
            if (path.isEmpty()) {
                path.moveTo(originX + cellWidth * i, originY - (originY - getPaddingTop()) * 1.0f / (ratio * 4) * dataSource.get(i).dataY);
            }
            float midX = ((originX + cellWidth * i) + (originX + cellWidth * (i + 1))) / 2;
            path.cubicTo(midX, originY - (originY - getPaddingTop()) * 1.0f / (ratio * 4) * dataSource.get(i).dataY,
                    midX, originY - (originY - getPaddingTop()) * 1.0f / (ratio * 4) * dataSource.get(i + 1).dataY,
                    originX + cellWidth * (i + 1), originY - (originY - getPaddingTop()) * 1.0f / (ratio * 4) * dataSource.get(i + 1).dataY);

        }

        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, mPaint);
        mPaint.setStyle((Paint.Style.FILL));
        canvas.drawCircle(originX, originY - (originY - getPaddingTop()) * 1.0f / (ratio * 4) * dataSource.get(0).dataY, 8, mPaint);
        canvas.drawCircle(originX + cellWidth * (dataSource.size() - 1),
                originY - (originY - getPaddingTop()) * 1.0f / (ratio * 4) * dataSource.get(dataSource.size() - 1).dataY, 8, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    public void setDataSource(List<Data> dataSource) {
        this.dataSource = dataSource;
        txtFirst = dataSource.get(0).dataX;
        txtLast = dataSource.get(dataSource.size() - 1).dataX;
        int max = 0;
        for (Data data : dataSource) {
            if (max < data.dataY) {
                max = data.dataY;
            }
        }
        ratio = max / 3;
        invalidate();
    }

    /**
     * 设置Y轴坐标后缀
     *
     * @param suffixY
     */
    public void setSuffixY(String suffixY) {
        this.suffixY = suffixY;
    }

    public static class Data {
        public String dataX;
        public int dataY;
    }
}
