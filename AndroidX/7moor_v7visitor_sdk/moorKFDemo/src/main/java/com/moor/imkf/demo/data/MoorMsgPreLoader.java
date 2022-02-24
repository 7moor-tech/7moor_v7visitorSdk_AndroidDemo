package com.moor.imkf.demo.data;

import com.moor.imkf.demo.constans.MoorDemoConstants;
import com.moor.imkf.demo.preloader.interfaces.IMoorGroupedDataLoader;
import com.moor.imkf.moorsdk.bean.MoorInfoBean;
import com.moor.imkf.moorsdk.bean.MoorMsgBean;
import com.moor.imkf.moorsdk.db.MoorInfoDao;
import com.moor.imkf.moorsdk.db.MoorMsgDao;

import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/02/25
 *     @desc   : 消息列表预加载
 *     @version: 1.0
 * </pre>
 */
public class MoorMsgPreLoader implements IMoorGroupedDataLoader<List<MoorMsgBean>> {
    @Override
    public List<MoorMsgBean> loadData() {
        MoorInfoBean bean = MoorInfoDao.getInstance().queryInfo();
        //首次查5条
        return MoorMsgDao.getInstance().queryMsgList(bean.getUserId(), 1,5L);

    }

    @Override
    public String keyInGroup() {
        return MoorDemoConstants.MOOR_MSG_PRE_LOADER_KEY;
    }
}
