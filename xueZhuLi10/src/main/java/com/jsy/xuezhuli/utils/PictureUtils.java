package com.jsy.xuezhuli.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

public class PictureUtils {
	private final static String TAG = "PictureUtils";

	// 获得路径
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();
	}

	// 转化为bitmap
	public static Bitmap convertToBitmap(String path, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 设置为ture只获取图片大小
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// 返回为空
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// 缩放
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		opts.inSampleSize = (int) scale;
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
				BitmapFactory.decodeFile(path, opts));
		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
	}

	// 转化为String
	/**
	 * bitmap转为base64
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * base64转为bitmap
	 * 
	 * @param base64Data
	 * @return
	 */
	public static Bitmap base64ToBitmap(String base64Data) {
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	/**
	 * bitmap保存为图片
	 * 
	 * @param bitmap
	 * @param filePath
	 * @return
	 */
	public static File saveBitmapFile(Bitmap bitmap, String filePath) {
		File file = new File(filePath);// 将要保存图片的路径
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();

		}

		// FileOutputStream fout = null;
		// File file = new File(filePath);
		// file.mkdirs();
		// String filename = file.getPath() + fileName;
		// try {
		// fout = new FileOutputStream(filename);
		// bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// fout.flush();
		// fout.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		return file;
	}

	public static InputStream Bitmap2IS(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
		return sbs;
	}

	public static Bitmap compressImage(Bitmap image, String filePath) {
		Bitmap bitmap = null;
		try {
			compressBmpToFile(image, filePath);
			Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = 1;
			bitmap = BitmapFactory.decodeFile(filePath, options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 压缩到300K以下
	 */
	public static File compressBmpToFile(Bitmap bmp, String filePath) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 > 300) {
			baos.reset();
			options -= 10;
			bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		File file = new File(filePath);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			Log.d(TAG, oldfile.length() + "");
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				fs.flush();
				fs.close();
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	public static final String getPathByUri(Context context, Uri uri) {
		if (uri == null)
			return null;

		if ("file".equals(uri.getScheme())) {
			return uri.getPath();
		}
		try {
			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cursor = MediaStore.Images.Media.query(
					context.getContentResolver(), uri, projection, null, null);
			if (cursor == null)
				return null;
			cursor.moveToNext();
			if (cursor.getCount() == 0) {
				cursor.close();
				return null;
			} else {
				String imagePath = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
				cursor.close();
				return imagePath;
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 删除图片
	 * 
	 * @param mContext
	 * @param imgPath
	 */
	public static void DeleteImage(Context mContext, String imgPath) {
		try {
			ContentResolver resolver = mContext.getContentResolver();
			Cursor cursor = MediaStore.Images.Media.query(resolver,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					new String[] { MediaStore.Images.Media._ID },
					MediaStore.Images.Media.DATA + "=?",
					new String[] { imgPath }, null);
			if (cursor.moveToFirst()) {
				long id = cursor.getLong(0);
				Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				Uri uri = ContentUris.withAppendedId(contentUri, id);
				mContext.getContentResolver().delete(uri, null, null);
			} else {
				File file = new File(imgPath);
				file.delete();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 打开文件
	 * 
	 * @param mContext
	 * @param filePath
	 */
	public static void openFile(Context mContext, String filePath) {
		Log.d(TAG, mContext.toString());
		File currentPath = new File(filePath);
		if (currentPath != null && currentPath.isFile()) {
			String fileName = currentPath.toString();
			Intent intent;
			try {
				if (checkEndsWithInStringArray(fileName, mContext
						.getResources().getStringArray(R.array.fileEndingImage))) {
					intent = OpenFiles.getImageFileIntent(currentPath);
					mContext.startActivity(intent);
				} else if (checkEndsWithInStringArray(
						fileName,
						mContext.getResources().getStringArray(
								R.array.fileEndingWebText))) {
					intent = OpenFiles.getHtmlFileIntent(currentPath);
					mContext.startActivity(intent);
				} else if (checkEndsWithInStringArray(
						fileName,
						mContext.getResources().getStringArray(
								R.array.fileEndingPackage))) {
					intent = OpenFiles.getApkFileIntent(currentPath);
					mContext.startActivity(intent);

				} else if (checkEndsWithInStringArray(fileName, mContext
						.getResources().getStringArray(R.array.fileEndingAudio))) {
					intent = OpenFiles.getAudioFileIntent(currentPath);
					mContext.startActivity(intent);
				} else if (checkEndsWithInStringArray(fileName, mContext
						.getResources().getStringArray(R.array.fileEndingVideo))) {
					intent = OpenFiles.getVideoFileIntent(currentPath);
					mContext.startActivity(intent);
				} else if (checkEndsWithInStringArray(fileName, mContext
						.getResources().getStringArray(R.array.fileEndingText))) {
					intent = OpenFiles.getTextFileIntent(currentPath);
					mContext.startActivity(intent);
				} else if (checkEndsWithInStringArray(fileName, mContext
						.getResources().getStringArray(R.array.fileEndingPdf))) {
					intent = OpenFiles.getPdfFileIntent(currentPath);
					mContext.startActivity(intent);
				} else if (checkEndsWithInStringArray(fileName, mContext
						.getResources().getStringArray(R.array.fileEndingWord))) {
					intent = OpenFiles.getWordFileIntent(currentPath);
					mContext.startActivity(intent);
				} else if (checkEndsWithInStringArray(fileName, mContext
						.getResources().getStringArray(R.array.fileEndingExcel))) {
					intent = OpenFiles.getExcelFileIntent(currentPath);
					mContext.startActivity(intent);
				} else if (checkEndsWithInStringArray(fileName, mContext
						.getResources().getStringArray(R.array.fileEndingPPT))) {
					intent = OpenFiles.getPPTFileIntent(currentPath);
					mContext.startActivity(intent);
				} else {
					ToastUtil.showMessage(mContext, "无法打开，请安装相应的软件！");
				}
			} catch (Exception e) {
				// TODO: handle exception
				ToastUtil.showMessage(mContext, "无法打开，请安装相应的软件！");
			}
		} else {
			ToastUtil.showMessage(mContext, "文件错误！");
		}
	}

	private static boolean checkEndsWithInStringArray(String checkItsEnd,
			String[] fileEndings) {
		for (String aEnd : fileEndings) {
			if (checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}

	/**
	 * 读取照片exif信息中的旋转角度
	 * 
	 * @param path
	 *            照片路径
	 * @return角度 获取从相册中选中图片的角度
	 */
	public static int readPictureDegree(String path) {
		if (TextUtils.isEmpty(path)) {
			return 0;
		}
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (Exception e) {
		}
		return degree;
	}

	public static Bitmap getbitmapFromURL(String gallPath) {
		Bitmap bitmap = null;
		try {
			int digree = PictureUtils.readPictureDegree(gallPath);
			if (digree != 0) {
				Options options1 = new BitmapFactory.Options();
				options1.inJustDecodeBounds = false;
				int m1 = (int) (new File(gallPath).length() / 1024 / 1024);
				options1.inSampleSize = m1 == 0 ? 1 : m1;
				// 旋转图片
				Matrix mat = new Matrix();
				mat.postRotate(digree);
				Bitmap b = PictureUtils.compressImage(BitmapFactory
						.decodeFile(gallPath));
				bitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
						b.getHeight(), mat, true);
				PictureUtils.saveBitmapFile(bitmap, gallPath);
			} else {
				File f = new File(gallPath);
				Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = false;
				int m = (int) (f.length() / 1024 / 1024);
				options.inSampleSize = m == 0 ? 1 : m;
				bitmap = BitmapFactory.decodeFile(gallPath, options);
			}
		} catch (Exception e) {
			bitmap = null;
			return bitmap;
		}
		return getImage(gallPath);
	}

	public static Bitmap getbitmapFromFile(File file) {
		Bitmap bitmap = null;
		try {
			FileInputStream fs = new FileInputStream(file);
			Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			int m = (int) (file.length() / 1024 / 1024);
			options.inSampleSize = m == 0 ? 1 : m;
			// bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
			// options);
			bitmap = BitmapFactory.decodeStream(fs, null, options);

		} catch (OutOfMemoryError e) {
			bitmap = null;
			System.gc();
			//
		} catch (FileNotFoundException e) {
			bitmap = null;
			System.gc();
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 压缩图片 像素压缩
	 * 
	 * @param srcPath
	 * @return
	 */
	private static Bitmap getImage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;

		float hh = 1024f;// 这里设置高度为1280f
		float ww = 1024f;// 这里设置宽度为720f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w >= h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		if (bitmap != null) {
			Log.d(TAG, bitmap.getWidth() + "");

		} else {
			return null;
		}
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 压缩图片 质量压缩
	 * 
	 * @param image
	 * @return
	 */

	private static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;

		while (baos.toByteArray().length / 1024 > 100 && options > 10) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static File createImageFile(String photoPath) throws IOException {
		// TODO Auto-generated method stub
		String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
				.format(new Date());
		String imageFileName = timeStamp;
		File storageDir = new File(photoPath);
		if (!storageDir.exists()) {
			storageDir.mkdir();
		}
		File file = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);
		// Save a file: path for use with ACTION_VIEW intents
		photoPath = file.getAbsolutePath();
		return file;
	}

	/**
	 * 
	 * @param activity
	 * @param photopath
	 * @param requestCode
	 */
	public static void dispatchTakePictureIntent(Activity activity,
			File photoFile, int requestCode) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
			// Create the File where the photo should go

			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				activity.startActivityForResult(takePictureIntent, requestCode);
			}
		}
	}
}
