package com.moor.imkf.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.utils.MoorPixelUtil;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/5/21
 *     @desc   : 圆形、圆角图片
 *     @version: 1.0
 * </pre>
 */
public class MoorRoundImageView extends AppCompatImageView {
    /**
     * 图片的类型，圆形or圆角
     */
    private int type;
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;

    /**
     * 圆角的大小
     */
    private float mCornerRadius;


    /**
     * 绘图的Paint
     */
    private Paint mBitmapPaint;
    /**
     * 圆角的半径
     */
    private float mRadius;
    /**
     * 3x3 矩阵，主要用于缩小放大
     */
    private Matrix mMatrix;
    /**
     * 渲染图像，使用图像为绘制图形着色
     */
    private BitmapShader mBitmapShader;
    /**
     * view的宽度
     */
    private int mWidth;

    private Path mRoundPath;
    /**
     * 圆角图片区域
     */
    private RectF mRoundRect;

    public MoorRoundImageView(Context context) {
        this(context, null);
    }

    public MoorRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoorRoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.moor_RoundImageView, defStyleAttr, 0);

        type = a.getInt(R.styleable.moor_RoundImageView_moor_type, TYPE_ROUND);
        mCornerRadius = a.getDimension(R.styleable.moor_RoundImageView_moor_corner_radius, MoorPixelUtil.dp2px(8f));

        a.recycle();

        init();

    }

    private void init() {
        mRoundPath = new Path();
        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
         */
        if (type == TYPE_CIRCLE) {
            mWidth = Math.min(MeasureSpec.getSize(widthMeasureSpec),
                    MeasureSpec.getSize(heightMeasureSpec));
            mRadius = mWidth / 2;
            setMeasuredDimension(mWidth, mWidth);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 圆角图片的范围
        if (type == TYPE_ROUND) {
            mRoundRect = new RectF(0, 0, w, h);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {


        if (getDrawable() == null) {
            return;
        }
        setUpShader();

        if (type == TYPE_ROUND) {
            setRoundPath();
            canvas.drawPath(mRoundPath, mBitmapPaint);
        } else if (type == TYPE_CIRCLE) {
            canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
        }
    }

    private void setRoundPath() {
        mRoundPath.reset();


        mRoundPath.addRoundRect(mRoundRect,
                new float[]{mCornerRadius, mCornerRadius,
                        mCornerRadius, mCornerRadius,
                        mCornerRadius, mCornerRadius,
                        mCornerRadius, mCornerRadius},
                Path.Direction.CW);

    }

    /**
     * 初始化BitmapShader
     */
    private void setUpShader() {

        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        Bitmap bmp = drawableToBitamp(drawable);
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (type == TYPE_CIRCLE) {
            // 拿到bitmap宽或高的小值
            int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
            scale = mWidth * 1.0f / bSize;
            //使缩放后的图片居中
            float dx = (bmp.getWidth() * scale - mWidth) / 2;
            float dy = (bmp.getHeight() * scale - mWidth) / 2;
            mMatrix.setTranslate(-dx, -dy);

        } else if (type == TYPE_ROUND) {

            if (!(bmp.getWidth() == getWidth() && bmp.getHeight() == getHeight())) {
                // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
                scale = Math.max(getWidth() * 1.0f / bmp.getWidth(),
                        getHeight() * 1.0f / bmp.getHeight());
                //使缩放后的图片居中
                float dx = (scale * bmp.getWidth() - getWidth()) / 2;
                float dy = (scale * bmp.getHeight() - getHeight()) / 2;
                mMatrix.setTranslate(-dx, -dy);
            }
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.preScale(scale, scale);

        mBitmapShader.setLocalMatrix(mMatrix);

        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        // 设置shader
        mBitmapPaint.setShader(mBitmapShader);
    }


    /**
     * drawable转bitmap
     */
    private Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * 设置图片类型:
     * imageType=0 圆形图片
     * imageType=1 圆角图片
     * 默认为圆角图片
     */
    public MoorRoundImageView setType(int imageType) {
        if (this.type != imageType) {
            this.type = imageType;
            if (this.type != TYPE_ROUND && this.type != TYPE_CIRCLE && this.type != TYPE_ROUND) {
                this.type = TYPE_ROUND;
            }
            requestLayout();
        }
        return this;
    }


    /**
     * 设置圆角图片的圆角大小
     */
    public MoorRoundImageView setCornerRadius(int cornerRadius) {
        cornerRadius = MoorPixelUtil.dp2px(cornerRadius);
        if (mCornerRadius != cornerRadius) {
            mCornerRadius = cornerRadius;
            invalidate();
        }
        return this;
    }

}
