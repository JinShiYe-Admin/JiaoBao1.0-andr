package com.jsy.xuezhuli.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;
import android.widget.TextView;

import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.personalcenter.QiuZhiSuggestShowRecommentActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

public class HtmlImageGetter implements ImageGetter, DestroyInterface {
    private TextView _htmlText;
    private String _imgPath;
    private Drawable _defaultDrawable;

    public HtmlImageGetter(TextView htmlText, String imgPath,
                           Drawable defaultDrawable) {
        _htmlText = htmlText;
        _imgPath = imgPath;
        _defaultDrawable = defaultDrawable;
    }

    @Override
    public Drawable getDrawable(String imgUrl) {
        String imgKey = MD5Encoder.ecode(imgUrl);
        String path = JSYApplication.getInstance().FILE_PATH + _imgPath;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        imgKey = path + "/" + imgKey + ".jpg";
        File imgK = new File(imgKey);
        if (imgK.exists()) {
            Drawable drawable = QiuZhiSuggestShowRecommentActivity.cache.get(imgKey);
            if (drawable == null) {
                Bitmap bitmap = PictureUtils.getbitmapFromFile(imgK);
                QiuZhiSuggestShowRecommentActivity.cache.put(imgKey, null);
                drawable = new BitmapDrawable(_htmlText.getResources(), bitmap);
                int w = drawable.getIntrinsicWidth();
                int h = drawable.getIntrinsicHeight();

                if (w > 0 && h > 0) {
                    int width = Constant.ScreenWith / 3 * 2;
                    int height = Constant.ScreenWith / 3 * 2 * h / w;
                    drawable.setBounds(0, 0, width, height);
                } else {
                    drawable.setBounds(0, 0, w, h);
                }
            }
            return drawable;
        }
        final URLDrawable urldrawable = new URLDrawable(_defaultDrawable);
        new HttpUtils().download(imgUrl, imgKey, true, new RequestCallBack<File>() {

            @Override
            public void onSuccess(ResponseInfo<File> file) {
                try {
                    Drawable drawable = QiuZhiSuggestShowRecommentActivity.cache.get(file.result.getPath());
                    if (drawable == null) {
                        Bitmap bitmap = PictureUtils.getbitmapFromFile(file.result);
                        QiuZhiSuggestShowRecommentActivity.cache.put(file.result.getPath(), null);
                        drawable = new BitmapDrawable(_htmlText.getResources(), bitmap);
                    }
                    urldrawable.setDrawable(drawable);
                    _htmlText.requestLayout();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }
        });
        return urldrawable;
    }

    @Override
    public void Destroy() {
        System.out.println("-----------html getter destroy");
    }
}
