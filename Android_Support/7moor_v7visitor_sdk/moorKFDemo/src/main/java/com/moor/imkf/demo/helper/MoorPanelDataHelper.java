package com.moor.imkf.demo.helper;

import com.moor.imkf.demo.R;
import com.moor.imkf.moorsdk.bean.MoorPanelBean;
import com.moor.imkf.moorsdk.utils.MoorUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 1/28/21
 *     @desc   : 更多面板数据操作类
 *     @version: 1.0
 * </pre>
 */
public class MoorPanelDataHelper {


    private static List<MoorPanelBean> panelBeanList;

    public static List<MoorPanelBean> getPanelBeanList() {
        return panelBeanList;
    }

    /**
     * 初始化面板数据
     *
     * @return
     */
    public static List<MoorPanelBean> initPanelData() {

        panelBeanList = new ArrayList<>();

        panelBeanList.add(createOrderCardItem());

        panelBeanList.add(createCameraItem());

        panelBeanList.add(createImageItem());

        panelBeanList.add(createFileItem());

//        panelBeanList.add(createQuestionItem());

//        panelBeanList.add(createEvaluateItem());

        return panelBeanList;
    }

    /**
     * 根据类型删除某一个面板功能按钮
     *
     * @param type
     */
    public static void removeItem(MoorPanelBean.TYPE type) {
        if (panelBeanList != null && panelBeanList.size() > 0) {
            Iterator<MoorPanelBean> sListIterator = panelBeanList.iterator();
            while (sListIterator.hasNext()) {
                MoorPanelBean panelBean = sListIterator.next();
                if (panelBean.getType() == type) {
                    sListIterator.remove();
                }
            }
        }

    }

    public static void addItem(MoorPanelBean bean) {
        if (panelBeanList != null) {
            panelBeanList.add(bean);
        }
    }

    public static MoorPanelBean createCameraItem() {
        return new MoorPanelBean(MoorUtils.getApp().getResources().getString(R.string.moor_chat_camera)
                , MoorUtils.getApp().getResources().getDrawable(R.drawable.moor_icon_chat_camera)
                , MoorPanelBean.TYPE.TYPE_CAMERA);
    }

    public static MoorPanelBean createImageItem() {
        return new MoorPanelBean(MoorUtils.getApp().getResources().getString(R.string.moor_chat_img)
                , MoorUtils.getApp().getResources().getDrawable(R.drawable.moor_icon_chat_pic)
                , MoorPanelBean.TYPE.TYPE_IMAGE);
    }

    public static MoorPanelBean createFileItem() {
        return new MoorPanelBean(MoorUtils.getApp().getResources().getString(R.string.moor_chat_file)
                , MoorUtils.getApp().getResources().getDrawable(R.drawable.moor_icon_chat_file)
                , MoorPanelBean.TYPE.TYPE_FILE);
    }

    public static MoorPanelBean createQuestionItem() {
        return new MoorPanelBean(MoorUtils.getApp().getResources().getString(R.string.moor_chat_question)
                , MoorUtils.getApp().getResources().getDrawable(R.drawable.moor_icon_chat_question)
                , MoorPanelBean.TYPE.TYPE_QUESTION);
    }

    public static MoorPanelBean createEvaluateItem() {
        return new MoorPanelBean(MoorUtils.getApp().getResources().getString(R.string.moor_chat_evaluate)
                , MoorUtils.getApp().getResources().getDrawable(R.drawable.moor_icon_chat_investigate)
                , MoorPanelBean.TYPE.TYPE_EVALUATE);
    }

    public static MoorPanelBean createOrderCardItem() {
        return new MoorPanelBean(MoorUtils.getApp().getResources().getString(R.string.moor_chat_send_ordercard)
                , MoorUtils.getApp().getResources().getDrawable(R.drawable.moor_icon_chat_order)
                , MoorPanelBean.TYPE.TYPE_ORDERCARD);
    }

}
