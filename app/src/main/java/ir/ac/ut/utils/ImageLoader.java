package ir.ac.ut.utils;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import java.io.IOException;

import ir.ac.ut.berim.BerimApplication;

/**
 * Created by keyhanasghari on 7/17/14.
 */
public class ImageLoader {

    private static final boolean DEBUG = false;

    private static final boolean CACHE_ENABLED = true;

    private static final boolean sLoadImages = true;

    private static ImageLoader sInstance;

    private Picasso sPicasso;

    private ImageLoader() {
        //          sPicasso = new Picasso.Builder(BazaarApplication.getInstance()).memoryCache(new LruCache(1000000)).build();
        sPicasso = Picasso.with(BerimApplication.getInstance());
        if (DEBUG) {
            sPicasso.setIndicatorsEnabled(true);
            sPicasso.setLoggingEnabled(true);
        }
    }

    public static ImageLoader getInstance() {
        if (sInstance == null) {
            sInstance = new ImageLoader();
        }
        return sInstance;
    }

    public void display(String uri, ImageView imageView) {
        display(uri, imageView, false, null);
    }

    public void display(String uri, ImageView imageView, final int placeHolder) {
        display(uri, imageView, false, placeHolder, null, 0, 0, 0);
    }

    public void display(String uri, ImageView imageView, final Callback callback) {
        display(uri, imageView, false, callback);
    }

    public void display(String uri, ImageView imageView, boolean fade) {
        display(uri, imageView, fade, null);
    }

    public void display(String uri, ImageView imageView, boolean fade, final int placeHolder) {
        display(uri, imageView, fade, placeHolder, null, 0, 0, 0);
    }

    public void display(String uri, ImageView imageView, boolean fade, final Callback callback) {
        display(uri, imageView, fade, 0, callback, 0, 0, 0);
    }

    public void display(String uri, ImageView imageView, boolean fade, final int placeHolder,
            int radius) {
        display(uri, imageView, fade, placeHolder, null, radius, 0, 0);
    }

    public void display(String uri, ImageView imageView, boolean fade, final int placeHolder,
            int radius, int targetWidth, int targetHeight) {
        display(uri, imageView, fade, placeHolder, null, radius, targetWidth, targetHeight);
    }


    public void display(final String uri, ImageView imageView, boolean fade, final int placeHolder,
            final Callback callback, int radius, int targetWidth, int targetHeight) {
        if (!sLoadImages) {
            return;
        }
        if (!TextUtils.isEmpty(uri)) {
            if (DEBUG) {
                RequestCreator creator = sPicasso.load(uri).noFade();
                if (targetWidth != 0 && targetHeight != 0) {
                    creator = creator.resize(targetWidth, targetHeight);
                }
                if (!CACHE_ENABLED) {
                    creator = creator
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);
                }
                if (radius != 0) {
                    creator = creator
                            .transform(new RoundedPicassoTransformation(radius, 0));
                }
                creator.into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        if (callback != null) {
                            callback.onSuccess();
                        }
                    }

                    @Override
                    public void onError() {
                        if (callback != null) {
                            callback.onError();
                        }
                    }
                });
            } else {
                RequestCreator requestCreator = sPicasso.load(uri);
                if (targetWidth != 0 && targetHeight != 0) {
                    requestCreator = requestCreator.resize(targetWidth, targetHeight);
                }
                if (placeHolder != 0) {
                    requestCreator.placeholder(placeHolder);
                }
                if (radius != 0) {
                    requestCreator = requestCreator
                            .transform(new RoundedPicassoTransformation(radius, 0));
                }
                if (!fade) {
                    requestCreator = requestCreator.noFade();
                }
                requestCreator.into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        if (callback != null) {
                            callback.onSuccess();
                        }
                    }

                    @Override
                    public void onError() {
                        if (callback != null) {
                            callback.onError();
                        }
                    }
                });
            }
        }
    }

    public Bitmap displaySync(String uri, int defaultDrawableId, Callback callback) {
        Bitmap mBitmap = BitmapFactory
                .decodeResource(BerimApplication.getInstance().getResources(), defaultDrawableId);
        if (!TextUtils.isEmpty(uri)) {
            try {
                mBitmap = sPicasso.load(uri).placeholder(defaultDrawableId).get();
                callback.onSuccess();
            } catch (IOException e) {
                callback.onError();
                e.printStackTrace();
            }
        }
        return mBitmap;
    }

    public interface Callback {

        void onSuccess();

        void onError();

        class EmptyCallback implements Callback {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
            }
        }
    }
}
