package com.jsy.xuezhuli.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 更改所生成类型注释的模板为 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
public class StringUtils {
    // system root path
    public static String sysRootPath = "";

    /**
     * 字符串替换
     */
    public static String replaceAll(String regex, String replacement,
                                    String inputString) {
        if (inputString == null)
            throw new RuntimeException(" 'inputString' can not be null! ");
        return inputString.replaceAll(regex, replacement);
    }

    /**
     * 获取当前项目的根目录
     *
     * @return 系统目录名
     * @version 1.1
     */
    public static String getPath() {
        if ("".equals(sysRootPath)) {
            StringUtils o = new StringUtils();
            String projectPath = o.getClass().getProtectionDomain()
                    .getCodeSource().getLocation().getPath();
            String path = projectPath.substring(0,
                    projectPath.lastIndexOf("/") - 1);
            sysRootPath = path.substring(0, path.lastIndexOf("/"));
        }
        return sysRootPath;
    }

    /**
     * 去除影响布局的标签
     * @return xml
     */
    public static String xml2webview(String xml) {
        String content = xml.replaceAll("nowrap", "").replaceAll("\"", "\"")
                .replaceAll("\n", "<br/>").replaceAll("top:-", "top:+")
                .replaceAll("top: -", "top: +")
                .replaceAll("table width=\"100%\"", "table")
                .replaceAll("<pre", "<p").replaceAll("</pre>", "</p>");
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:auto; height:auto;}</style>"
                + "</head>";
        String url = "<html>" + head + "<body>" + content + "</body></html>";
        String TAG = "StringUtils";
        Log.d(TAG + "url", url);
        return url;
    }

    public static List<String> getImgStr1(String htmlStr) {
        String img;
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<>();
        String regEx_img = "<img.*src=(.*?)[^>]*?>"; // 图片链接地址
        // String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            img = m_image.group();
            Matcher m = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); // 匹配src
            // Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }

    public static List<String> getImgStr(String htmlStr) {
        String img;
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<>();
        // String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            img = m_image.group();
            // Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)")
                    .matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }
    /**
     * 返回两个字符串中间的内容
     */
    public static String getMiddleString(String all, String start, String end) {
        int beginIdx = all.indexOf(start) + start.length();
        int endIdx = all.indexOf(end);
        return all.substring(beginIdx, endIdx);
    }
}