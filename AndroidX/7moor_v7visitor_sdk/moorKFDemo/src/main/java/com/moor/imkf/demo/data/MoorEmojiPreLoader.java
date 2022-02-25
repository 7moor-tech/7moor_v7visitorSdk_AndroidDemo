package com.moor.imkf.demo.data;

import com.google.gson.Gson;
import com.moor.imkf.demo.bean.MoorEmojiBean;
import com.moor.imkf.demo.constans.MoorDemoConstants;
import com.moor.imkf.demo.preloader.interfaces.IMoorGroupedDataLoader;
import com.moor.imkf.demo.utils.MoorEmojiBitmapUtil;
import com.moor.imkf.demo.utils.MoorFileUtils;
import com.moor.imkf.lib.constants.MoorPathConstants;
import com.moor.imkf.lib.http.donwload.MoorSdkFileUtils;
import com.moor.imkf.lib.utils.sharedpreferences.MoorSPUtils;
import com.moor.imkf.moorhttp.MoorHttpUtils;
import com.moor.imkf.moorhttp.MoorUrlManager;
import com.moor.imkf.moorsdk.bean.MoorEmotion;
import com.moor.imkf.moorsdk.constants.MoorConstants;
import com.moor.imkf.moorsdk.db.MoorEmojiDao;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/02/20
 *     @desc   : 表情图片预加载
 *     @version: 1.0
 * </pre>
 */
public class MoorEmojiPreLoader implements IMoorGroupedDataLoader<List<MoorEmotion>> {

    public MoorEmojiPreLoader() {
    }

    @Override
    public List<MoorEmotion> loadData() {
        if (checkLocal()) {
            return MoorEmojiDao.getInstance().queryEmojis();
        } else {
            MoorEmojiBean emojiBean;
            List<MoorEmotion> emotionList = new ArrayList<>();
            try {
                //获取emoji数据
                Response response = MoorHttpUtils
                        .post()
                        .url(MoorUrlManager.BASE_URL + MoorUrlManager.GET_EMOJI_CONFIG)
                        .build()
                        .execute();
                String responseData = response.body().string();
                JSONObject object = new JSONObject(responseData);
                if (object.optBoolean(MoorConstants.MOOR_NET_SUCCESS) && object.optJSONObject(MoorConstants.MOOR_NET_DATA) != null) {
                    JSONObject jsonObject = object.optJSONObject(MoorConstants.MOOR_NET_DATA);
                    emojiBean = new Gson().fromJson(jsonObject.toString(), MoorEmojiBean.class);
                } else {
                    return emotionList;
                }

                if (emojiBean != null) {
                    //下载图片
                    Response fileResponse = MoorHttpUtils
                            .get()
                            .url(MoorUrlManager.BASE_IM_URL + emojiBean.getElvesFigureUrl())
                            .build()
                            .execute();
                    MoorSdkFileUtils.saveFile(fileResponse
                            , MoorPathConstants.getStoragePath(MoorPathConstants.PATH_NAME_MOOR_DOWNLOAD_FILE)
                            , MoorConstants.PATH_NAME_MOOR_EMOJI_FILE, null);


                    //裁剪图片
                    emotionList = MoorEmojiBitmapUtil.cropBitmap(emojiBean);
                    MoorEmojiDao.getInstance().deleteEmojis();
                    MoorEmojiDao.getInstance().insertEmojis(emotionList);
                    MoorSPUtils.getInstance().put(MoorConstants.MOOR_EMOJI_SIZE, emojiBean.getBookArray().size(), true);
                }
                return emotionList;
            } catch (Exception e) {
                e.printStackTrace();
                return emotionList;
            }


        }

    }

    @Override
    public String keyInGroup() {
        return MoorDemoConstants.MOOR_EMOJI_PRE_LOADER_KEY;
    }

    /**
     * 检查是否需要下载表情图片
     * 本地文件是否存在；图片个数是否正确；表情版本号是否一致
     *
     * @return
     */
    private boolean checkLocal() {
        String oldVersion = MoorSPUtils.getInstance().getString(MoorConstants.MOOR_EMOJI_OLD_VERSION);
        String newVersion = MoorSPUtils.getInstance().getString(MoorConstants.MOOR_EMOJI_NEW_VERSION);
        int emojiSize = MoorSPUtils.getInstance().getInt(MoorConstants.MOOR_EMOJI_SIZE, 0);

        File dir = new File(MoorPathConstants.getStoragePath(MoorPathConstants.PATH_NAME_MOOR_DOWNLOAD_FILE) + File.separator + "emoji");
        if (dir.exists()) {
            long count = MoorFileUtils.getFileCount(dir);
            if (count == emojiSize && oldVersion.equals(newVersion)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }


    }
}
