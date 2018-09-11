package com.muzi.paletteimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者: lipeng
 * 时间: 2018/9/11
 * 邮箱: lipeng@moyi365.com
 * 功能:
 */
public class PaletteImage extends View {

    private Paint mPaint;
    private Bitmap mBmp, mShadowBmp;
    private int mDx = 10, mDy = 10;
    private float mRadius = 0;
    private int mShadowColor = 0;

    public PaletteImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            init(context, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PaletteImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        try {
            init(context, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(Context context, AttributeSet attrs) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BitmapShadowView);
        int BitmapID = typedArray.getResourceId(R.styleable.BitmapShadowView_src, 0);
        mDx = typedArray.getInt(R.styleable.BitmapShadowView_shadowDx, 0);
        mDy = typedArray.getInt(R.styleable.BitmapShadowView_shadowDy, 0);
        mRadius = typedArray.getFloat(R.styleable.BitmapShadowView_shadowRadius, 0);
        mShadowColor = typedArray.getColor(R.styleable.BitmapShadowView_shadowColor, 0);
        typedArray.recycle();
        mPaint = new Paint();
        if (BitmapID > 0) {
            mBmp = BitmapFactory.decodeResource(getResources(), BitmapID);
            mShadowBmp = mBmp.extractAlpha();
            Palette.from(mBmp).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@NonNull Palette palette) {
                    mPaint.setColor(palette.getDominantColor(Color.BLACK));
                    invalidate();
                }
            });
        }
    }

    public void setImageResource(@IdRes int idRes) {
        mBmp = BitmapFactory.decodeResource(getResources(), idRes);
        setImageBitmap(mBmp);
    }

    public void setImageBitmap(Bitmap bitmap) {
        mBmp = bitmap;
        if (mBmp == null) return;
        mShadowBmp = mBmp.extractAlpha();
        if (mShadowColor == 0) {
            Palette.from(mBmp).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@NonNull Palette palette) {
                    mPaint.setColor(palette.getDominantColor(Color.BLACK));
                    invalidate();
                }
            });
        } else {
            mPaint.setColor(mShadowColor);
        }
    }

    public void setRadius(int mRadius) {
        this.mRadius = mRadius;
        if (mBmp == null) return;
        invalidate();
    }

    public void setOffsetX(int offsetX) {
        mDx = offsetX;
        if (mBmp == null) return;
        invalidate();
    }

    public void setOffsetY(int offsetY) {
        mDy = offsetY;
        if (mBmp == null) return;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mBmp == null) return;
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = mBmp.getWidth();
        int height = mBmp.getHeight();
        setMeasuredDimension((measureWidthMode == MeasureSpec.EXACTLY) ? measureWidth : width, (measureHeightMode == MeasureSpec.EXACTLY) ? measureHeight : height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBmp == null) return;
        int width = getWidth() - mDx;
        int height = width * mBmp.getHeight() / mBmp.getWidth();

        //绘制阴影
        mPaint.setMaskFilter(new BlurMaskFilter(mRadius, BlurMaskFilter.Blur.NORMAL));
        canvas.drawBitmap(mShadowBmp, null, new Rect(mDx, mDy, width, height), mPaint);

        //绘制原图像
        mPaint.setMaskFilter(null);
        canvas.drawBitmap(mBmp, null, new Rect(0, 0, width, height), mPaint);
    }

}
