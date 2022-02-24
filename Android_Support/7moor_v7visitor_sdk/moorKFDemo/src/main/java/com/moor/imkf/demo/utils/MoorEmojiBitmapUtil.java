package com.moor.imkf.demo.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.moor.imkf.demo.bean.MoorEmojiBean;
import com.moor.imkf.demo.bean.MoorEmojiListBean;
import com.moor.imkf.demo.constans.MoorFilePath;
import com.moor.imkf.moorsdk.bean.MoorEmotion;
import com.moor.imkf.moorsdk.constants.MoorConstants;
import com.moor.imkf.moorsdk.constants.MoorPathConstants;
import com.moor.imkf.moorsdk.db.MoorEmojiDao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 1/25/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorEmojiBitmapUtil {
    public static List<MoorEmotion> moorEmotions = new ArrayList<>();
    private static List<String> codeList = new ArrayList<>();

    public static List<MoorEmotion> cropBitmap(MoorEmojiBean emojiBean) {
        Bitmap resBitmap = decodeBitmapFromFile();
        Bitmap bit = null;
        //总个数
        int allCount = emojiBean.getBookArray().size();
        //列数
        int oneRowCount = emojiBean.getColumnNum();
        //单个宽px
        int countW = emojiBean.getEmojiSize() * 2;
        //单个高px
        int countH = emojiBean.getEmojiSize() * 2;
        //行数
        int column = (allCount % oneRowCount == 0) ? allCount / oneRowCount : (allCount / oneRowCount) + 1;
        codeList.clear();
        for (MoorEmojiListBean listBean : emojiBean.getBookArray()) {
            String str = listBean.getStr();
            codeList.add(str);
        }
        moorEmotions.clear();
        for (int i = 0; i < column; i++) {
            //i代表当前行数
            for (int b = 0; b < oneRowCount; b++) {
                //b代表每行第几个
                int x = b * countW;
                int y = i * countH;

                bit = Bitmap.createBitmap(resBitmap, x, y, countW, countH, null, false);


                File file = MoorFilePath.getDownloadFile(MoorPathConstants.getStoragePath(MoorPathConstants.PATH_NAME_MOOR_DOWNLOAD_FILE)
                        , "emoji", "moor_emoji_" + moorEmotions.size() + ".png");
                saveBitmapFile(bit, file);
                MoorEmotion emotion = new MoorEmotion();
                emotion.setCode(codeList.get(moorEmotions.size()))
                        .setFilePath(file.getAbsolutePath())
                        .setText("moor_emoji_" + moorEmotions.size() + ".png");
                moorEmotions.add(emotion);
                if (moorEmotions.size() == allCount) {
                    break;
                }
            }
        }
        if (bit != null && !bit.isRecycled()) {
            bit.recycle();
            bit = null;
        }

        return moorEmotions;
    }

    public static List<MoorEmotion> getMoorEmotions() {
        if (moorEmotions.size()==0) {
            moorEmotions= MoorEmojiDao.getInstance().queryEmojis();
        }
        return moorEmotions;
    }

    /**
     * 将bitmap保存成File
     *
     * @param bitmap
     * @param file
     */
    private static void saveBitmapFile(Bitmap bitmap, File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ResourceType")
    private static Bitmap decodeBitmapFromFile() {
        File file = MoorFilePath.getDownloadFile(MoorPathConstants.getStoragePath(MoorPathConstants.PATH_NAME_MOOR_DOWNLOAD_FILE)
                , "", MoorConstants.PATH_NAME_MOOR_EMOJI_FILE);
        try {
            InputStream inputStream = new FileInputStream(file);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeStream(inputStream, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
