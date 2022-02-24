package com.moor.imkf.demo.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/03/26
 *     @desc   : 防止快速重复点击
 *     @version: 1.0
 * </pre>
 */
public class MoorAntiShakeUtils {

    private final List<OneClickUtil> utils = new ArrayList<>();

    private MoorAntiShakeUtils() {
    }

    public static MoorAntiShakeUtils getInstance() {
        return MoorAntiShakeUtils.SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final MoorAntiShakeUtils sInstance = new MoorAntiShakeUtils();
    }


    public boolean check() {
        String flag = null;
        flag = Thread.currentThread().getStackTrace()[2].getMethodName();
        for (OneClickUtil util : utils) {
            if (util.getMethodName().equals(flag)) {
                return util.check();
            }
        }
        OneClickUtil clickUtil = new OneClickUtil(flag);
        utils.add(clickUtil);
        return clickUtil.check();
    }


    static class OneClickUtil {
        private final String methodName;
        public static final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime = 0;

        public OneClickUtil(String methodName) {
            this.methodName = methodName;
        }

        public String getMethodName() {
            return methodName;
        }

        public boolean check() {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                return false;
            } else {
                return true;
            }
        }

    }
}
