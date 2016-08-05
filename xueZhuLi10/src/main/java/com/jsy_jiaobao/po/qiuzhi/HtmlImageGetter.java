package com.jsy_jiaobao.po.qiuzhi;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.MD5Encoder;
import com.jsy.xuezhuli.utils.PictureUtils;
import com.jsy.xuezhuli.utils.StringUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.JSYApplication;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html.ImageGetter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

public class HtmlImageGetter implements ImageGetter {
	private TextView _htmlText;
	private String _imgPath;
	private Drawable _defaultDrawable;
	private String TAG = "HtmlImageGetter";

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
			Drawable drawable = new BitmapDrawable(PictureUtils.getbitmapFromFile(imgK));
			int w = (int) (drawable.getIntrinsicWidth());
			int h = (int) (drawable.getIntrinsicHeight());
			
			if (w>0 && h>0) {
				int width = Constant.ScreenWith/3*2;
				int height = Constant.ScreenWith/3*2*h/w;
				drawable.setBounds(0, 0, width,height);
			}else{
				drawable.setBounds(0, 0, w,h);
			}
			return drawable;
		}
//		_htmlText.setText(_htmlText.getText());
		final URLDrawable urlDrawable = new URLDrawable(_defaultDrawable);
//		new AsyncThread(urlDrawable).execute(imgKey, imgUrl);
		new HttpUtils().download(imgUrl, imgKey, new RequestCallBack<File>() {
			
			@Override
			public void onSuccess(ResponseInfo<File> file) {
//				Options options = new BitmapFactory.Options(); 
//				options.inJustDecodeBounds = false; 
//				int m = (int) (file.result.length()/1024/1024);
//				options.inSampleSize = m==0?1:m; 
				
				try {
//					Bitmap bitmap = PictureUtils.getbitmapFromFile(file.result);
//					Bitmap bitmap = PictureUtils.compressImage(BitmapFactory.decodeFile(file.result.getAbsolutePath(), options),file.result.getAbsolutePath());
					urlDrawable.setDrawable(new BitmapDrawable(PictureUtils.getbitmapFromFile(file.result)));
					_htmlText.requestLayout();
				} catch (Exception e) {
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}
		});
		return urlDrawable;
	}

//	private class AsyncThread extends AsyncTask<String, Integer, Drawable> {
//		private String imgKey;
//		private URLDrawable _drawable;
//
//		public AsyncThread(URLDrawable drawable) {
//			_drawable = drawable;
//		}
//
//		@Override
//		protected Drawable doInBackground(String... strings) {
//			imgKey = strings[0];
//			InputStream inps = null;
//			try {
//				inps = NetWork.getInputStream(strings[1]);
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			if (inps == null)
//				return _drawable;
//			
//			File file = new File(imgKey);
//			try {
//				saveBit(inps,file);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			Options options = new BitmapFactory.Options(); 
//			options.inJustDecodeBounds = false; 
//			int m = (int) (file.length()/1024/1024);
//			options.inSampleSize = m==0?1:m; 
//			
//			try {
//				Bitmap bitmap = PictureUtils.compressImage(BitmapFactory.decodeFile(imgKey, options),imgKey);
//				Drawable drawable = new BitmapDrawable(bitmap);
//				return drawable;
//			} catch (Exception e) {
//			}
//			Drawable drawable = Drawable.createFromPath(file.getAbsolutePath());
//			return drawable;
//		}
//
//		public void onProgressUpdate(Integer... value) {
//
//		}
//
//		@Override
//		protected void onPostExecute(Drawable result) {
//			super.onPostExecute(result);
//			if (result != null) {
//				_drawable.setDrawable(result);
//				_htmlText.requestLayout();
//			}
//		}
//	}
//
//	public void saveBit(InputStream inStream, File imageFile) throws IOException {
//		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//		// 创建一个Buffer字符串
//		byte[] buffer = new byte[1024];
//		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
//		int len = 0;
//		// 使用一个输入流从buffer里把数据读取出来
//		while ((len = inStream.read(buffer)) != -1) {
//			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
//			outStream.write(buffer, 0, len);
//		}
//		// 关闭输入流
//		inStream.close();
//		// 把outStream里的数据写入内存
//
//		// 得到图片的二进制数据，以二进制封装得到数据，具有通用性
//		byte[] data = outStream.toByteArray();
//		// 创建输出流
//		FileOutputStream fileOutStream = new FileOutputStream(imageFile);
//		// 写入数据
//		fileOutStream.write(data);
//
//	}
	public class URLDrawable extends BitmapDrawable {

		private Drawable drawable;

		public URLDrawable(Drawable defaultDraw) {
			setDrawable(defaultDraw);
		}

		private void setDrawable(Drawable ndrawable) {
			drawable = ndrawable;
			if (drawable != null) {
				int w = (int) (drawable.getIntrinsicWidth());
				int h = (int) (drawable.getIntrinsicHeight());
				
				if (w>0 && h>0) {
					int width = Constant.ScreenWith/3*2;
					int height = Constant.ScreenWith/3*2*h/w;
					drawable.setBounds(0, 0, width,height);
					setBounds(0, 0, width,height);
				}else{
					drawable.setBounds(0, 0, w,h);
					setBounds(0, 0, w,h);
				}
				
//				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
//				setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
			}
		}

		@Override
		public void draw(Canvas canvas) {
			if (drawable != null) {
				drawable.draw(canvas);
			}
		}
	}
}
