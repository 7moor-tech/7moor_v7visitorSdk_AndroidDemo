package com.moor.imkf.demo.utils;

import android.text.TextUtils;

import com.moor.imkf.demo.R;


/**
 * <pre>
 *     @author : Trial
 *     @time   : 2021/02/18
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorFileFormatUtils {
    private enum FormatEnum {

        //图片
        IMG("img", R.drawable.moor_icon_file_image, ".jpg", ".jpeg", ".gif", ".png", ".bmp", ".tiff"),

        //文档
        WORD("word", R.drawable.moor_icon_file_word, ".docx", ".dotx", ".doc", ".dot", ".pagers", ".word"),

        //excel
        EXCEL("excel", R.drawable.moor_icon_file_xls, ".xls", ".xlsx", ".xlt", ".xltx", ".excel"),

        //ppt
        PPT("ppt", R.drawable.moor_icon_file_ppt, ".ppt", ".pptx"),

        //pdf
        PDF("pdf", R.drawable.moor_icon_file_pdf, ".pdf"),

        //音频
        MP3("mp3", R.drawable.moor_icon_file_music, ".mp3", ".wav", ".wma"),

        //视频
        VIDEO("video", R.drawable.moor_icon_file_video, ".avi", ".flv", ".mpg", ".mpeg", ".mp4", ".3gp", ".mov", ".rmvb", ".mkv"),

        //压缩包
        ZIP("zip", R.drawable.moor_icon_file_default, ".zip", ".jar", ".rar", ".7z"),

        //未知
        UNKNOWN("unknown", R.drawable.moor_icon_file_default);

        public String TYPE;
        public int ICON;
        public String[] FORMATS;

        /**
         * @param type    文件类型
         * @param icon    对应icon
         * @param formats 包含格式
         */
        FormatEnum(String type, int icon, String... formats) {
            this.TYPE = type;
            this.ICON = icon;
            this.FORMATS = formats;
        }

        /**
         * 通过文件类型获取对应枚举
         *
         * @param extension 文件扩展名
         * @return 文件对应的枚举信息，如果没有，返回未知
         */
        public static FormatEnum getFormat(String extension) {
            for (FormatEnum format : FormatEnum.values()) {
                for (String extend : format.FORMATS) {
                    if (extend.equalsIgnoreCase(extension)) {
                        return format;
                    }
                }
            }
            return UNKNOWN;
        }
    }

    /**
     * 获取文件格式名
     */
    public static String getFormatName(String fileName) {
        if (fileName == null) {
            return null;
        }
        fileName = fileName.trim();
        int dot = fileName.lastIndexOf(".");
        if (dot >= 0) {
            return fileName.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }

    /**
     * 获取文件对应icon
     */
    public static int getFileIcon(String fileName) {
        //获取扩展名并且全部转小写
        String extension = getFormatName(fileName).toLowerCase();
        if (TextUtils.isEmpty(extension)) {
            return FormatEnum.UNKNOWN.ICON;
        }
        FormatEnum format = FormatEnum.getFormat(extension);
        return format.ICON;
    }

    /**
     * 获取文件对应类型
     */
    public static String getFileType(String fileName) {
        //获取扩展名并且全部转小写
        String extension = getFormatName(fileName).toLowerCase();
        if (TextUtils.isEmpty(extension)) {
            return FormatEnum.UNKNOWN.TYPE;
        }
        FormatEnum format = FormatEnum.getFormat(extension);
        return format.TYPE;
    }

}
