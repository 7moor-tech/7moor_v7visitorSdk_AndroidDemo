package com.moor.imkf.demo.multichat;

import android.content.Context;
import android.text.TextUtils;

import com.moor.imkf.demo.view.imageviewer.MoorImagePreview;
import com.moor.imkf.moorhttp.MoorUrlManager;
import com.moor.imkf.moorsdk.bean.MoorImageInfoBean;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.constants.MoorChatMsgType;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/02/04
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorStartImagePreview {

    public static void startPreview(Context context, MoorMsgBean item, List<MoorMsgBean> items) {
        List<MoorImageInfoBean> pathList = new ArrayList<>();

        for (MoorMsgBean bean : items) {
            if (bean.getContentType().equals(MoorChatMsgType.MSG_TYPE_IMAGE)) {
                String url;
                if (TextUtils.isEmpty(bean.getFileLocalPath())) {
                    url = bean.getContent().startsWith("http") ? bean.getContent() : MoorUrlManager.BASE_IM_URL + bean.getContent();

                } else {
                    url = bean.getFileLocalPath();
                }
                MoorImageInfoBean imageInfoBean = new MoorImageInfoBean();
                imageInfoBean.setId(bean.getMessageId())
                        .setFrom(bean.getFrom())
                        .setPath(url);
                pathList.add(imageInfoBean);
            }
        }
        int index = 0;
        if (pathList.size() > 0) {
            for (int i = 0; i < pathList.size(); i++) {
                MoorImageInfoBean imageInfoBean = pathList.get(i);
                if (item.getMessageId().equals(imageInfoBean.getId())) {
                    index = i;
                }
            }
        }

        MoorImagePreview.getInstance()
                .setContext(context)
                .setIndex(index)
                .setImageList(pathList)
                .start();
    }
}
