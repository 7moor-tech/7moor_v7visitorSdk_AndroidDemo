package com.moor.imkf.demo.data;

import com.google.gson.Gson;
import com.moor.imkf.demo.bean.MoorEvaluationBean;
import com.moor.imkf.demo.constans.MoorDemoConstants;
import com.moor.imkf.demo.preloader.interfaces.IMoorGroupedDataLoader;
import com.moor.imkf.moorhttp.MoorHttpParams;
import com.moor.imkf.moorhttp.MoorHttpUtils;
import com.moor.imkf.moorhttp.MoorUrlManager;
import com.moor.imkf.moorsdk.bean.MoorInfoBean;
import com.moor.imkf.moorsdk.constants.MoorConstants;
import com.moor.imkf.moorsdk.db.MoorInfoDao;

import org.json.JSONObject;

import okhttp3.Response;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 5/31/21
 *     @desc   : 满意度配置预加载
 *     @version: 1.0
 * </pre>
 */
public class MoorEvaluationsPreLoader implements IMoorGroupedDataLoader<MoorEvaluationBean> {
    @Override
    public MoorEvaluationBean loadData() {
        MoorEvaluationBean evaluationBean = new MoorEvaluationBean();
        MoorInfoBean infoBean = MoorInfoDao.getInstance().queryInfo();
        try {
            Response response = MoorHttpUtils
                    .post()
                    .url(MoorUrlManager.BASE_URL + MoorUrlManager.QUERY_SATISFACTION)
                    .params(MoorHttpParams.getInstance().querySatisfactionTmpById(infoBean.getSatisfactionId()))
                    .build()
                    .execute();
            String responseData = response.body().string();
            JSONObject object = new JSONObject(responseData);
            if (object.optBoolean(MoorConstants.MOOR_NET_SUCCESS)) {
                JSONObject jsonObject = object.optJSONObject(MoorConstants.MOOR_NET_DATA);
                evaluationBean = new Gson().fromJson(jsonObject.toString(), MoorEvaluationBean.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return evaluationBean;

    }

    @Override
    public String keyInGroup() {
        return MoorDemoConstants.MOOR_SATISFACTION_PRE_LOADER_KEY;
    }
}
