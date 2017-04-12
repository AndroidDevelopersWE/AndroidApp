package co.dtechsystem.carefer.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The Scaler2 class will create Drawables and also save them in the memory for
 * future use The class will cater the FirstTime use of the application by
 * itself and keep that in the the saved preference xml
 * 
 * @author HuzaifaH
 * 
 */
@SuppressLint("NewApi")
public class Scaler {

	private static final String DT_PREFRENCES = "DTechPrefrences",
			KEY = "imagescopy";
	private Activity mActivity;
	public static int sScreenWidth = 0, sScreenHeight = 0;
	public static float sScreenWidth_DP = 0, sScreenHeight_DP = 0;
	private int mDesignedHeight = 1280, mDesignedWidth = 720;
	private String path;
	private boolean mFirsTime = false, mKeyStored = false;
	private int mDrawableWidth;
	private int mDrawableHeight;
	// private int mInSampleSize = 4;
	public static float sWidthScaleFactor = 0, sHeightScaleFactor = 0;
	public static final short SMALLER_ONLY = 1, LARGER_ONLY = 2;

	/**
	 * Constructor will initialize activity and mActivity of the Application, it
	 * will also be required to set the default size of the screen on which the
	 * screen was designed. E.g. it should be the size of the back ground image
	 * or the splash image.
	 * 
	 * @param activity
	 * @param designedHeight
	 * @param designedWidth
	 */
	public Scaler(Activity activity, int designedWidth, int designedHeight) {
		mDesignedHeight = designedHeight;
		mDesignedWidth = designedWidth;
		this.mActivity = activity;
		if (sScreenWidth == 0) {
			DisplayMetrics displaymetrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);
			sScreenWidth = displaymetrics.widthPixels;
			sScreenHeight = displaymetrics.heightPixels;
			/*
			 * check if the Designed Mode is equivalent to the Current Received
			 * Screen Values if Not then Switch the Values
			 */
			if ((mDesignedWidth > mDesignedHeight && sScreenWidth < sScreenHeight)
					|| (mDesignedWidth < mDesignedHeight && sScreenWidth > sScreenHeight)) {
				/**
				 * then System Provided Incorrect Values height should be width
				 * and width should must be the Height
				 */
				sScreenWidth = displaymetrics.heightPixels;
				sScreenHeight = displaymetrics.widthPixels;
//				Log.v(Utilities.TAG, "Values Suitched");
			}
			//Toast.makeText(activity, "sScreenHeight:"+sScreenHeight, Toast.LENGTH_LONG).show();
			//Toast.makeText(activity, "sScreenWidth:"+sScreenWidth, Toast.LENGTH_LONG).show();
			

			Log.v("mScreenHeight", String.valueOf(sScreenHeight));
			Log.v("mScreenWidth", String.valueOf(sScreenWidth));

			sWidthScaleFactor = (float) sScreenWidth / (float) mDesignedWidth;
			sHeightScaleFactor = (float) sScreenHeight
					/ (float) mDesignedHeight;
			sScreenHeight_DP = sScreenHeight / displaymetrics.density;
			sScreenWidth_DP = sScreenWidth / displaymetrics.density;
			

		}
		SharedPreferences myPrefs = mActivity.getSharedPreferences(
				DT_PREFRENCES, 0);
		if (myPrefs.contains(KEY)) {
			path = myPrefs.getString(KEY, "");
			mFirsTime = false;
			mKeyStored = true;

		} else
			mFirsTime = true;
	}

	/**
	 * makes a new drawable for the Drawable Resource
	 * 
	 * @param drawableResourceID
	 * @return
	 */
	public Drawable getDrawableFromMemory(int drawableResourceID) {
		try {

			if (!mFirsTime) {
				Bitmap b = loadImageFromStorage(path, drawableResourceID);
				if (b == null)
					return null;
				Drawable d = new BitmapDrawable(mActivity.getResources(), b);
				return d;
			} else {
				Bitmap originalBitmap = BitmapFactory.decodeResource(
						mActivity.getResources(), drawableResourceID);
				// calculate new height and width of the drawable image to make
				mDrawableHeight = (int) ((float) originalBitmap.getHeight() * sHeightScaleFactor);
				mDrawableWidth = (int) ((float) originalBitmap.getWidth() * sWidthScaleFactor);
				// create a new scaled bitmap from the original Bitmap
				Bitmap bitmap = Bitmap.createScaledBitmap(originalBitmap, mDrawableWidth, mDrawableHeight,
						false);
				// convert the Bitmap into a new Drawable
				Drawable d = new BitmapDrawable(mActivity.getResources(),
						bitmap);
				// save the Bitmap in the internal storage
				path = saveToInternalSorage(bitmap, drawableResourceID);
				// check if the path was stored in the preference
				if (!mKeyStored) {
					storeFilePathinThePref();
				}
				return d;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * takes the resourceId of the drawable and the resource of the imageView
	 * 
	 * @param drawableResourceID
	 * @param imageViewResourceID
	 */
	public void scaleImageFromMemory(int drawableResourceID,
			int imageViewResourceID) {
		ImageView img = (ImageView) mActivity.findViewById(imageViewResourceID);
		img.setImageDrawable(getDrawableFromMemory(drawableResourceID));
	}

	/**
	 * takes the resourceId of the Drawable and the resource of the TextView
	 * 
	 * @param drawableResourceID
//	 * @param imageViewResourceID
	 */
	@SuppressWarnings("deprecation")
	public void scaleTextViewBackground(int drawableResourceID,
			int textViewResourceID) {
		TextView tv = (TextView) mActivity.findViewById(textViewResourceID);
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			tv.setBackgroundDrawable(getDrawable(drawableResourceID));
		} else {
			tv.setBackground(getDrawable(drawableResourceID));
		}
	}

	/**
	 * takes the resourceId of the drawable and the resource of the Button
	 * 
	 * @param drawableResourceID
//	 * @param imageViewResourceID
	 */
	@SuppressWarnings("deprecation")
	public void scaleButtonBackground(int drawableResourceID,
			int buttonResourceID) {
		Button btn = (Button) mActivity.findViewById(buttonResourceID);
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
			btn.setBackgroundDrawable(getDrawable(drawableResourceID));
		else
			btn.setBackground(getDrawable(drawableResourceID));
	}

	/**
	 * takes the resourceId of the drawable and the resource of the Button with
	 * a single factorial for both height and width
	 * 
	 * @param drawableResourceID
	 * @param buttonResourceID
	 * @param SmallerFactorialORLarger
	 */
	@SuppressWarnings("deprecation")
	public void scaleButtonBackground(int drawableResourceID,
			int buttonResourceID, short SmallerFactorialORLarger) {
		Button btn = (Button) mActivity.findViewById(buttonResourceID);
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
			btn.setBackgroundDrawable(getDrawable(drawableResourceID,
					SmallerFactorialORLarger));
		else
			btn.setBackground(getDrawable(drawableResourceID,
					SmallerFactorialORLarger));
	}

	/**
	 * takes the resourceId of the drawable and the resource of the Button
	 * 
	 * @param drawableResourceID
//	 * @param imageViewResourceID
	 */
	@SuppressWarnings("deprecation")
	public void scaleViewBackground(int drawableResourceID, int buttonResourceID) {
		View btn = (View) mActivity.findViewById(buttonResourceID);
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
			btn.setBackgroundDrawable(getDrawable(drawableResourceID));
		else
			btn.setBackground(getDrawable(drawableResourceID));
	}

	/**
	 * takes the resourceId of the drawable and the resource of the Button with
	 * a single factorial for both height and width
	 * 
	 * @param drawableResourceID
	 * @param buttonResourceID
	 * @param SmallerFactorialORLarger
	 */
	@SuppressWarnings("deprecation")
	public void scaleViewBackground(int drawableResourceID,
			int buttonResourceID, short SmallerFactorialORLarger) {
		View btn = (View) mActivity.findViewById(buttonResourceID);
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
			btn.setBackgroundDrawable(getDrawable(drawableResourceID,
					SmallerFactorialORLarger));
		else
			btn.setBackground(getDrawable(drawableResourceID,
					SmallerFactorialORLarger));
	}

	@SuppressWarnings("deprecation")
	public void scaleViewBackground(Dialog dialog, int drawableResourceID,
			int buttonResourceID) {
		View btn = (View) dialog.findViewById(buttonResourceID);
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
			btn.setBackgroundDrawable(getDrawable(drawableResourceID));
		else
			btn.setBackground(getDrawable(drawableResourceID));
	}

	/**
	 * takes the resourceId of the drawable and the resource of the Button with
	 * a single factorial for both height and width
	 * 
	 * @param drawableResourceID
	 * @param buttonResourceID
	 * @param SmallerFactorialORLarger
	 */
	@SuppressWarnings("deprecation")
	public void scaleViewBackground(Dialog dialog, int drawableResourceID,
			int buttonResourceID, short SmallerFactorialORLarger) {
		View btn = (View) dialog.findViewById(buttonResourceID);
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
			btn.setBackgroundDrawable(getDrawable(drawableResourceID,
					SmallerFactorialORLarger));
		else
			btn.setBackground(getDrawable(drawableResourceID,
					SmallerFactorialORLarger));
	}

	/**
	 * takes the resourceId of the drawable and the resource of the Button
	 * 
	 * @param drawableResourceID
	 * @param buttonView
	 */
	@SuppressWarnings("deprecation")
	public void scaleViewBackground(int drawableResourceID, View buttonView) {
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
			buttonView.setBackgroundDrawable(getDrawable(drawableResourceID));
		else
			buttonView.setBackground(getDrawable(drawableResourceID));
	}

	/**
	 * takes the resourceId of the drawable and the resource of the Button with
	 * a single factorial for both height and width
	 * 
	 * @param drawableResourceID
	 * @param buttonView
	 * @param SmallerFactorialORLarger
	 */
	@SuppressWarnings("deprecation")
	public void scaleViewBackground(int drawableResourceID, View buttonView,
			short SmallerFactorialORLarger) {
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
			buttonView.setBackgroundDrawable(getDrawable(drawableResourceID,
					SmallerFactorialORLarger));
		else
			buttonView.setBackground(getDrawable(drawableResourceID,
					SmallerFactorialORLarger));
	}

	@SuppressWarnings("deprecation")
	public void scaleViewBackground(Dialog dialog, int drawableResourceID,
			View buttonView) {
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
			buttonView.setBackgroundDrawable(getDrawable(drawableResourceID));
		else
			buttonView.setBackground(getDrawable(drawableResourceID));
	}

	/**
	 * takes the resourceId of the drawable and the resource of the Button with
	 * a single factorial for both height and width
	 * 
	 * @param drawableResourceID
	 * @param buttonView
	 * @param SmallerFactorialORLarger
	 */
	@SuppressWarnings("deprecation")
	public void scaleViewBackground(Dialog dialog, int drawableResourceID,
			View buttonView, short SmallerFactorialORLarger) {

		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
			buttonView.setBackgroundDrawable(getDrawable(drawableResourceID,
					SmallerFactorialORLarger));
		else
			buttonView.setBackground(getDrawable(drawableResourceID,
					SmallerFactorialORLarger));
	}

	/**
	 * makes a new drawable for the Drawable Resource
	 * 
	 * @param drawableResourceID
	 * @return
	 */
	public Drawable getDrawable(int drawableResourceID) {
		try {
			/*
			 * Options options = new BitmapFactory.Options(); options.inScaled =
			 * false; options.inDither = false; options.inJustDecodeBounds =
			 * false; // options.inSampleSize=mInSampleSize; //
			 * options.inPreferredConfig = Bitmap.Config.RGB_565; Bitmap
			 * originalBitmap = BitmapFactory.decodeResource(
			 * mActivity.getResources(), drawableResourceID, options);
			 */
			// calculate new height and width of the drawable image to make
			/*
			 * int h = (int) ((float) originalBitmap.getHeight() *
			 * sHeightScaleFactor); int w = (int) ((float)
			 * originalBitmap.getWidth() * sWidthScaleFactor);
			 */
			BitmapDrawable bd = (BitmapDrawable) mActivity.getResources()
					.getDrawable(drawableResourceID);
			mDrawableHeight = (int) ((float) bd.getBitmap().getHeight() * sHeightScaleFactor);
			mDrawableWidth = (int) ((float) bd.getBitmap().getWidth() * sWidthScaleFactor);
			// create a new scaled bitmap from the original Bitmap
			Bitmap bitmap = Bitmap.createScaledBitmap(bd.getBitmap(), mDrawableWidth, mDrawableHeight,
					true);
			// convert the Bitmap into a new Drawable
			Drawable d = new BitmapDrawable(mActivity.getResources(), bitmap);
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * makes a new drawable for the Drawable Resource
	 * 
	 * @param drawable
	 * @return
	 */
	public Drawable getDrawable(BitmapDrawable drawable) {
		try {
			/*
			 * Options options = new BitmapFactory.Options(); options.inScaled =
			 * false; options.inDither = false; options.inJustDecodeBounds =
			 * false; // options.inSampleSize=mInSampleSize; //
			 * options.inPreferredConfig = Bitmap.Config.RGB_565; Bitmap
			 * originalBitmap = BitmapFactory.decodeResource(
			 * mActivity.getResources(), drawableResourceID, options);
			 */
			// calculate new height and width of the drawable image to make
			/*
			 * int h = (int) ((float) originalBitmap.getHeight() *
			 * sHeightScaleFactor); int w = (int) ((float)
			 * originalBitmap.getWidth() * sWidthScaleFactor);
			 */			
			mDrawableHeight = (int) ((float) drawable.getBitmap().getHeight() * sHeightScaleFactor);
			mDrawableWidth = (int) ((float) drawable.getBitmap().getWidth() * sWidthScaleFactor);
			// create a new scaled bitmap from the original Bitmap
			Bitmap bitmap = Bitmap.createScaledBitmap(drawable.getBitmap(), mDrawableWidth, mDrawableHeight,
					true);
			// convert the Bitmap into a new Drawable
			Drawable d = new BitmapDrawable(mActivity.getResources(), bitmap);
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * makes a new drawable for the Drawable Resource
	 * 
	 * @param drawableResourceID
	 * @return
	 */
	public Drawable getDrawable(int drawableResourceID,
			short SmallerFactorialORLarger) {
		try {
			/*
			 * Options options = new BitmapFactory.Options(); options.inScaled =
			 * false; options.inDither = false; options.inJustDecodeBounds =
			 * false; options.inSampleSize = mInSampleSize;
			 * options.inPreferredConfig = Bitmap.Config.RGB_565;
			 */
			/*
			 * Bitmap originalBitmap = BitmapFactory.decodeResource(
			 * mActivity.getResources(), drawableResourceID, options);
			 */// calculate new height and width of the drawable image to make
			float factorial = 0, greater = 0, smaller = 0;
			if (sHeightScaleFactor > sWidthScaleFactor) {
				greater = sHeightScaleFactor;
				smaller = sWidthScaleFactor;
			} else {
				smaller = sHeightScaleFactor;
				greater = sWidthScaleFactor;
			}

			if (SmallerFactorialORLarger == SMALLER_ONLY)
				factorial = smaller;
			else
				factorial = greater;
			BitmapDrawable bd = (BitmapDrawable) mActivity.getResources()
					.getDrawable(drawableResourceID);

			mDrawableHeight = (int) ((float) bd.getBitmap().getHeight() * factorial);
			mDrawableWidth = (int) ((float) bd.getBitmap().getWidth() * factorial);
			/*
			 * options.outHeight = h; options.outWidth = w; options.inSampleSize
			 * = calculateInSampleSize(bd.getBitmap().getWidth(),bd.getBitmap().
			 * getHeight(), w, h); Bitmap originalBitmap =
			 * BitmapFactory.decodeResource( mActivity.getResources(),
			 * drawableResourceID, options);
			 */
			// create a new scaled bitmap from the original Bitmap
			Bitmap bitmap = Bitmap.createScaledBitmap(bd.getBitmap(), mDrawableWidth, mDrawableHeight,
					true);
			// if (originalBitmap != bitmap)
			// originalBitmap.recycle();
			// convert the Bitmap into a new Drawable
			Drawable d = new BitmapDrawable(mActivity.getResources(), bitmap);
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static int calculateInSampleSize(int rawWidth, int rawHeight,
			int reqWidth, int reqHeight) {
		// Raw height and width of image

		int inSampleSize = 1;

		if (rawHeight > reqHeight || rawWidth > reqWidth) {

			final int halfHeight = rawHeight / 2;
			final int halfWidth = rawWidth / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	/**
	 * takes the resourceId of the drawable and the resource of the imageView
	 * 
	 * @param drawableResourceID
	 * @param imageViewResourceID
	 */
	public void scaleImage(int drawableResourceID, int imageViewResourceID) {
		ImageView img = (ImageView) mActivity.findViewById(imageViewResourceID);
		img.setImageDrawable(getDrawable(drawableResourceID));
	}

	/**
	 * takes the resourceId of the drawable and ImageView object
	 * 
	 * @param drawableResourceID
	 * @param im
	 */
	public void scaleImage(int drawableResourceID, ImageView im) {
		im.setImageDrawable(getDrawable(drawableResourceID));
	}

	/**
	 * takes the resourceId of the drawable and ImageView object
	 * 
	 * @param drawableResourceID
	 * @param im
	 */
	public void scaleImage(int drawableResourceID, ImageView im,
			short SmallerFactorialORLarger) {
		im.setImageDrawable(getDrawable(drawableResourceID,
				SmallerFactorialORLarger));
	}

	/**
	 * called from Dialog to scale Image and Assign value to the Image View
	 * 
	 * @param dialog
	 * @param drawableResourceID
	 * @param imageViewResourceID
	 */
	public void scaleImage(Dialog dialog, int drawableResourceID,
			int imageViewResourceID) {
		ImageView img = (ImageView) dialog.findViewById(imageViewResourceID);
		img.setImageDrawable(getDrawable(drawableResourceID));
	}

	/**
	 * takes the resourceId of the drawable and the resource of the imageView
	 * and if chooses to use a single Factorial
	 * 
	 * @param drawableResourceID
	 * @param imageViewResourceID
	 * @param SmallerFactorialORLarger
	 */

	public void scaleImage(int drawableResourceID, int imageViewResourceID,
			short SmallerFactorialORLarger) {
		ImageView img = (ImageView) mActivity.findViewById(imageViewResourceID);
		img.setImageDrawable(getDrawable(drawableResourceID,
				SmallerFactorialORLarger));
	}

	/**
	 * Called from Dialog only
	 * 
	 * @param dialog
	 * @param drawableResourceID
	 * @param imageViewResourceID
	 * @param SmallerFactorialORLarger
	 */
	public void scaleImage(Dialog dialog, int drawableResourceID,
			int imageViewResourceID, short SmallerFactorialORLarger) {
		ImageView img = (ImageView) dialog.findViewById(imageViewResourceID);
		img.setImageDrawable(getDrawable(drawableResourceID,
				SmallerFactorialORLarger));
	}

	/**
	 * returns the pixels of the percentage of Screen Height
	 * 
	 * @param percentage
	 * @return
	 */
	public int getPixelsFromPercentageOfHeight(float percentage) {
		return (int) ((((float) sScreenHeight) * percentage) / 100f);

	}

	/**
	 * returns the pixels of the percentage of Screen Height
	 * 
	 * @param percentage
	 * @return
	 */
	public int getPixelsFromPercentageOfWidth(float percentage) {
		return (int) ((((float) sScreenWidth) * percentage) / 100f);
	}

	/**
	 * returns the pixels of the percentage of Screen Height
	 * 
	 * @param percentage
	 * @return
	 */
	public float getDpFromPercentageOfHeight(float percentage) {
		return (sScreenHeight_DP * percentage) / 100f;

	}

	/**
	 * returns the pixels of the percentage of Screen Height
	 * 
	 * @param percentage
	 * @return
	 */
	public float getDpFromPercentageOfWidth(float percentage) {
		return (sScreenWidth_DP * percentage) / 100f;
	}

	// //////////////////////////////////////////////////////////////////

	// //////////PRIVATE METHODS

	// //////////////////////////////////////////////////////////////////

	/**
	 * will take the Bitmap Image and the Resource id of that image. It will
	 * store that image in the Internal Storage. Returns the complete path of
	 * the storage.
	 * 
//	 * @param bitmapImageBitmap
	 * @param id
	 * @return
	 */
	private String saveToInternalSorage(Bitmap bitmapImage, int id) {
		ContextWrapper cw = new ContextWrapper(mActivity);
		// path to /data/data/yourapp/app_data/imageDir
		@SuppressWarnings("static-access")
		File directory = cw.getDir("imageDir", mActivity.MODE_PRIVATE);
		// Create imageDir
		File mypath = new File(directory, "image" + id);
		FileOutputStream fos = null;
		try {

			fos = new FileOutputStream(mypath);

			// Use the compress method on the BitMap object to write image to
			// the OutputStream
			bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return directory.getAbsolutePath();
	}

	/**
	 * will take path of the internal storage Directory and the id of the image.
	 * Returns the Bitmap image from the internal storage.
	 * 
	 * @param path
	 * @param id
	 * @return
	 */
	private Bitmap loadImageFromStorage(String path, int id) {

		try {
			File f = new File(path, "image" + id);
			// Decode the input stream to create the Bitmap
			Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
			return b;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Store the internal Storage Path in the Preference
	 */
	private void storeFilePathinThePref() {
		SharedPreferences myPrefs = mActivity.getSharedPreferences(
				DT_PREFRENCES, 0);
		SharedPreferences.Editor prefsEditor = myPrefs.edit();
		prefsEditor.putString(KEY, path);
		prefsEditor.commit();
		mKeyStored = true;
	}
	public static final int FLIP_VERTICAL = 1;
	public static final int FLIP_HORIZONTAL = 2;
	 /**
	  * This function will flip the Bitmap vertically or horizontally and then return the new created Bitmap
	  * @param src
	  * @param verticallyOrHorizontally
	  * @return
	  */
	public static Bitmap flip(Bitmap src, int verticallyOrHorizontally) {
	    // create new matrix for transformation
	    Matrix matrix = new Matrix();
	    // if vertical
	    if(verticallyOrHorizontally == FLIP_VERTICAL) {
	        // y = y * -1
	        matrix.preScale(1.0f, -1.0f);
	    }
	    // if horizonal
	    else if(verticallyOrHorizontally == FLIP_HORIZONTAL) {
	        // x = x * -1
	        matrix.preScale(-1.0f, 1.0f);
	    // unknown type
	    } else {
	        return null;
	    }
	 
	    // return transformed image
	    return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
	}

	public Activity getmActivity() {
		return mActivity;
	}

	public void setmActivity(Activity mActivity) {
		this.mActivity = mActivity;
	}

	public int getsScreenWidth() {
		return sScreenWidth;
	}

	public static void setsScreenWidth(int sScreenWidth) {
		Scaler.sScreenWidth = sScreenWidth;
	}

	public int getsScreenHeight() {
		return sScreenHeight;
	}

	public static void setsScreenHeight(int sScreenHeight) {
		Scaler.sScreenHeight = sScreenHeight;
	}

	public static float getsScreenWidth_DP() {
		return sScreenWidth_DP;
	}

	public static void setsScreenWidth_DP(float sScreenWidth_DP) {
		Scaler.sScreenWidth_DP = sScreenWidth_DP;
	}

	public static float getsScreenHeight_DP() {
		return sScreenHeight_DP;
	}

	public static void setsScreenHeight_DP(float sScreenHeight_DP) {
		Scaler.sScreenHeight_DP = sScreenHeight_DP;
	}

	public int getmDesignedHeight() {
		return mDesignedHeight;
	}

	public void setmDesignedHeight(int mDesignedHeight) {
		this.mDesignedHeight = mDesignedHeight;
	}

	public int getmDesignedWidth() {
		return mDesignedWidth;
	}

	public void setmDesignedWidth(int mDesignedWidth) {
		this.mDesignedWidth = mDesignedWidth;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean ismFirsTime() {
		return mFirsTime;
	}

	public void setmFirsTime(boolean mFirsTime) {
		this.mFirsTime = mFirsTime;
	}

	public boolean ismKeyStored() {
		return mKeyStored;
	}

	public void setmKeyStored(boolean mKeyStored) {
		this.mKeyStored = mKeyStored;
	}

	public static float getsWidthScaleFactor() {
		return sWidthScaleFactor;
	}

	public static void setsWidthScaleFactor(float sWidthScaleFactor) {
		Scaler.sWidthScaleFactor = sWidthScaleFactor;
	}

	public static float getsHeightScaleFactor() {
		return sHeightScaleFactor;
	}

	public static void setsHeightScaleFactor(float sHeightScaleFactor) {
		Scaler.sHeightScaleFactor = sHeightScaleFactor;
	}

	public static String getDtPrefrences() {
		return DT_PREFRENCES;
	}

	public static String getKey() {
		return KEY;
	}

	public int getmDrawableWidth() {
		return mDrawableWidth;
	}

	public void setmDrawableWidth(int mDrawableWidth) {
		this.mDrawableWidth = mDrawableWidth;
	}

	public int getmDrawableHeight() {
		return mDrawableHeight;
	}

	public void setmDrawableHeight(int mDrawableHeight) {
		this.mDrawableHeight = mDrawableHeight;
	}
}
