package com.moor.imkf.demo.view.pagergrid;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 6/7/21
 *     @desc   : Pager配置
 *     @version: 1.0
 * </pre>
 */
public class MoorPagerConfig {
    /**
     * Fling 阀值，滚动速度超过该阀值才会触发滚动
     */
    private static int sFlingThreshold = 1000;
    /**
     * 每一个英寸滚动需要的微秒数，数值越大，速度越慢
     */
    private static float sMillisecondsPreInch = 60f;


    /**
     * 获取当前滚动速度阀值
     *
     * @return 当前滚动速度阀值
     */
    public static int getFlingThreshold() {
        return sFlingThreshold;
    }

    /**
     * 设置当前滚动速度阀值
     *
     * @param flingThreshold 滚动速度阀值
     */
    public static void setFlingThreshold(int flingThreshold) {
        sFlingThreshold = flingThreshold;
    }

    /**
     * 获取滚动速度 英寸/微秒
     *
     * @return 英寸滚动速度
     */
    public static float getMillisecondsPreInch() {
        return sMillisecondsPreInch;
    }

    /**
     * 设置像素滚动速度 英寸/微秒
     *
     * @param millisecondsPreInch 英寸滚动速度
     */
    public static void setMillisecondsPreInch(float millisecondsPreInch) {
        sMillisecondsPreInch = millisecondsPreInch;
    }

}
