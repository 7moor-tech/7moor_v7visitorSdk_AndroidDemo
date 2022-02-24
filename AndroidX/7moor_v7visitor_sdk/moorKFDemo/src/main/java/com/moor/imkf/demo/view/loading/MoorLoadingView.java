package com.moor.imkf.demo.view.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.utils.MoorColorUtils;
import com.moor.imkf.demo.utils.MoorPixelUtil;
import com.moor.imkf.moorsdk.bean.MoorOptions;
import com.moor.imkf.moorsdk.db.MoorOptionsDao;
import com.moor.imkf.moorsdk.utils.MoorUtils;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 1/26/21
 *     @desc   : 默认loading弹框
 *     @version: 1.0
 * </pre>
 */
public class MoorLoadingView extends View {

    /**
     * 颜色数组
     */
    private int[] colors;

    /**
     * 动画所执行的最大偏移量（即中间点和最左边的距离）
     */
    private final Float maxWidth = MoorPixelUtil.dp2pxF(15f);

    /**
     * 圆半径
     */
    private final Float radius = MoorPixelUtil.dp2pxF(10f);

    /**
     * 当前偏移的X坐标
     */
    private Float currentX = 0f;
    private Paint paint;
    private ValueAnimator valueAnimator;


    public MoorLoadingView(Context context) {
        super(context);
        startAnimator();
    }

    public MoorLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        startAnimator();
    }

    public MoorLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        startAnimator();
    }

    /**
     * 位移动画
     */
    private void startAnimator() {
        MoorOptions options = MoorOptionsDao.getInstance().queryOptions();
        if (options != null) {
            String sdkMainThemeColor = options.getSdkMainThemeColor();
            if (TextUtils.isEmpty(sdkMainThemeColor)) {
                colors = new int[]{MoorUtils.getApp().getResources().getColor(R.color.moor_color_4d0081ff)
                        , MoorUtils.getApp().getResources().getColor(R.color.moor_color_0081ff)};

            } else {
                int colorWithAlpha = MoorColorUtils.getColorWithAlpha(1f, sdkMainThemeColor);
                int colorWithAlpha1 = MoorColorUtils.getColorWithAlpha(0.3f, sdkMainThemeColor);
                colors = new int[]{colorWithAlpha1
                        , colorWithAlpha};
            }
        } else {
            colors = new int[]{MoorUtils.getApp().getResources().getColor(R.color.moor_color_4d0081ff)
                    , MoorUtils.getApp().getResources().getColor(R.color.moor_color_0081ff)};
        }

        valueAnimator = ValueAnimator.ofFloat(0f, maxWidth, 0f, -maxWidth, 0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentX = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });

        valueAnimator.setInterpolator(new LinearInterpolator());
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setDuration(1500);
        valueAnimator.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        //左边的圆
        paint.setColor(colors[0]);
        canvas.drawCircle(centerX - currentX, centerY, radius, paint);

        //右边的圆
        paint.setColor(colors[1]);
        canvas.drawCircle(centerX + currentX, centerY, radius, paint);

    }

    /**
     * 停止动画
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        valueAnimator.cancel();
    }
}
