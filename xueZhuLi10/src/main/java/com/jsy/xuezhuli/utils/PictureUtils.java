package com.jsy.xuezhuli.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.jsy_jiaobao.main.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PictureUtils {
	private final static String TAG = "PictureUtils";

	/**
	 * bitmap保存为图片
	 */
	public static File saveBitmapFile(Bitmap bitmap, String filePath) {
		File file = new File(filePath);// 将要保存图片的路径
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 删除图片
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
			e.printStackTrace();
		}
	}

	/**
	 * 打开文件
	 */
	public static void openFile(Context mContext, String filePath) {
		Log.d(TAG, mContext.toString());
		File currentPath = new File(filePath);
		if (currentPath.isFile()) {
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
	 * @param path 照片路径
	 * @return 角度 获取从相册中选中图片的角度
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
			e.printStackTrace();
		}
		return degree;
	}

	public static Bitmap getbitmapFromURL(String gallPath) {
		Bitmap bitmap;
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
		Bitmap bitmap;
		try {
			FileInputStream fs = new FileInputStream(file);
			Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			int m = (int) (file.length() / 1024 / 1024);
			options.inSampleSize = m == 0 ? 1 : m;
			bitmap = BitmapFactory.decodeStream(fs, null, options);
		} catch (OutOfMemoryError e) {
			bitmap = null;
			System.gc();
		} catch (FileNotFoundException e) {
			bitmap = null;
			System.gc();
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 压缩图片 像素压缩
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