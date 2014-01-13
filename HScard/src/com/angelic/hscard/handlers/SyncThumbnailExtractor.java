package com.angelic.hscard.handlers;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import com.angelic.hscard.utils.ImageTools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class SyncThumbnailExtractor implements Callback {

	private static final String LOADER_THREAD_NAME = "FileImageLoader";
	private static final int MESSAGE_REQUEST_EXTRACTING = 1;
	private static final int MESSAGE_THUMBNAIL_EXTRACTED = 2;

	private boolean mPaused;
	private boolean mDecodingRequested = false;
	final Handler mMainHandler = new Handler(this);
	ExtractorThread mExtractorThread;
	private Context mContext;
	
	private LruCache<String, Bitmap> mMemoryCache;

	private final ConcurrentHashMap<ImageView, FileInfo> mPendingRequests = new ConcurrentHashMap<ImageView, FileInfo>();
	private final static ConcurrentHashMap<String, ImageHolder> mImageCache = new ConcurrentHashMap<String, ImageHolder>();

	private static abstract class ImageHolder {
		public static final int NEEDED = 0;

		public static final int EXTRACTING = 1;

		public static final int EXTRACTED = 2;

		int state;

		public static ImageHolder create() {
			
				return new BitmapHolder();
			
		};

		public abstract boolean setImageView(ImageView v);

		public abstract boolean isNull();

		public abstract void setImage(Object image);
	}

	private static class BitmapHolder extends ImageHolder {
		SoftReference<Bitmap> bitmapRef;

		@Override
		public boolean setImageView(ImageView v) {
			if (bitmapRef.get() == null){
				return false;
			}
			v.setImageBitmap(bitmapRef.get());
			return true;
		}

		@Override
		public boolean isNull() {
			return bitmapRef == null;
		}

		@Override
		public void setImage(Object image) {
			bitmapRef = image == null ? null : new SoftReference<Bitmap>(
					(Bitmap) image);
		}
	}

	private static class FileInfo {
		public FileInfo(String path) {
			this.path = path;
		}

		public String path;
	}

	public SyncThumbnailExtractor(Context context) {
		mContext = context;
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		// 设置图片缓存大小为程序最大可用内存的1/8
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
	}

	public void clear() {
		mPaused = false;
		mImageCache.clear();
		mPendingRequests.clear();
	}

	// 当前Activity调用OnDestory时，将ExtractorThread退出，并清空缓存
	public void stop() {
		pause();

		if (mExtractorThread != null) {
			mExtractorThread.quit();
			mExtractorThread = null;
		}

		clear();
	}

	public void resume() {
		mPaused = false;
		if (!mPendingRequests.isEmpty()) {
			requestExtracting();
		}
	}

	public boolean decodeThumbnail(ImageView view, String path) {
		boolean extracted = loadCache(view, path);
		if (extracted) {
			mPendingRequests.remove(view);
		} else {
			mPendingRequests.put(view, new FileInfo(path));
			if (!mPaused) {
				// Send a request to start loading thumbnails
				requestExtracting();
			}
		}
		return extracted;
	}

	private boolean loadCache(ImageView view, String path) {
		ImageHolder holder = mImageCache.get(path);

		if (holder == null) {
			holder = ImageHolder.create();
			if (holder == null){
				return false;
			}
			mImageCache.put(path, holder);
		} else if (holder.state == ImageHolder.EXTRACTED) {
			if (holder.isNull()) {
				view.setImageBitmap(null);
				return true;
			}
			// failing to set imageview means that the soft reference was
			// released by the GC, we need to reload the thumbnail.
			if (holder.setImageView(view)) {
				return true;
			}
		}
		view.setImageBitmap(null);
		holder.state = ImageHolder.NEEDED;
		return false;
	}


 

	private void requestExtracting() {
		if (!mDecodingRequested) {
			mDecodingRequested = true;
			mMainHandler.sendEmptyMessage(MESSAGE_REQUEST_EXTRACTING);
		}
	}

	public void pause() {
		mPaused = true;
	}

    /**
    * @Description: handle <span style="background-color:#ffffff;">MESSAGE_REQUEST_EXTRACTING message to create </span><span style="background-color:#ffffff;">ExtractorThread and start</span>    *                to extract thumbnail in mPendingRequests's file
    * @param msg
    * <a href="http://my.oschina.net/u/556800" target="_blank" rel="nofollow">@return</a>
    */
    @Override
    public boolean handleMessage(Message msg) {
        switch(msg.what){
            case MESSAGE_REQUEST_EXTRACTING:
                mDecodingRequested = false;
                if (mExtractorThread == null) {
                    mExtractorThread = new ExtractorThread();
                    mExtractorThread.start();
                }
                mExtractorThread.requestLoading();
                return true;
            case MESSAGE_THUMBNAIL_EXTRACTED:
                if (!mPaused) {
                    processExtractThumbnails();
                }
                return true;
        }
        return false;
         
    }
     
    /**
     * Goes over pending loading requests and displays extracted thumbnails. If some of
     * the thumbnails still haven't been extracted, sends another request for image
     * loading.
     */
    private void processExtractThumbnails() {
        Iterator<ImageView> iterator = mPendingRequests.keySet().iterator();
        while (iterator.hasNext()) {
            ImageView view = iterator.next();
            FileInfo info = mPendingRequests.get(view);
            boolean extracted = loadCache(view, info.path);
            if (extracted) {
                iterator.remove();
            }
        }
 
        if (!mPendingRequests.isEmpty()) {
            requestExtracting();
        }
    }

	private class ExtractorThread extends HandlerThread implements Callback {
		private Handler mExtractorHandler;
		public ExtractorThread() {
			super(LOADER_THREAD_NAME);
			// TODO Auto-generated constructor stub
		}
		public void requestLoading() {
			            if (mExtractorHandler == null) {
			               mExtractorHandler = new Handler(getLooper(), this);
			            }  
			            mExtractorHandler.sendEmptyMessage(0);
			        }  

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
            Iterator<FileInfo> iterator = mPendingRequests.values().iterator();
            while (iterator.hasNext()) {
                FileInfo info = iterator.next();
                 
                ImageHolder holder = mImageCache.get(info.path);
                if (holder != null && holder.state == ImageHolder.NEEDED) {
                    // Assuming atomic behavior
                    holder.state = ImageHolder.EXTRACTING;
                    if(getBitmapFromMemoryCache(info.path)!=null){
                    	holder.setImage(getBitmapFromMemoryCache(info.path));
                    }else{
//                    	holder.setImage(ImageTools.getImageThumbnail(info.path,168,236));
                    	holder.setImage(BitmapFactory.decodeFile(info.path)); 
//                    	addBitmapToMemoryCache(info.path, ImageTools.getImageThumbnail(info.path,168,236));
                    	addBitmapToMemoryCache(info.path, BitmapFactory.decodeFile(info.path));
                    }
                    holder.state = BitmapHolder.EXTRACTED;
                    mImageCache.put(info.path, holder);
                }
                
            }
 
            mMainHandler.sendEmptyMessage(MESSAGE_THUMBNAIL_EXTRACTED);
            return true;
		}

	}
	
	
	/**
	 * 将一张图片存储到LruCache中。
	 * 
	 * @param key
	 *            LruCache的键，这里传入序号。
	 * @param bitmap
	 *            LruCache的键，这里传入缩略图Bitmap对象。
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemoryCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 * 
	 * @param key
	 *            LruCache的键，这里传入序号。
	 * @return 对应传入键的Bitmap对象，或者null。
	 */
	public Bitmap getBitmapFromMemoryCache(String key) {
		return mMemoryCache.get(key);
	}


}
