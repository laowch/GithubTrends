package com.laowch.githubtrends.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.laowch.githubtrends.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by lao on 3/4/14.
 */
public class AsyncImageView extends ImageView {


    String url;

    ImageLoadingListener imageLoadingListener;

    public AsyncImageView(final Context context) {
        super(context);
        init();
    }

    public AsyncImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AsyncImageView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setImageLoadingListener(ImageLoadingListener listener) {
        this.imageLoadingListener = listener;
    }

    protected void init() {

    }

    public void loadImage(final String imageUrl) {
        if (imageUrl != null && url != null) {
            if (Uri.parse(imageUrl).getPath().equals(Uri.parse(url).getPath())) {
                return;
            }
        }
        url = imageUrl;
        executeLoadImage();
    }


    protected void executeLoadImage() {

        if (TextUtils.isEmpty(url)) {
            setImageResource(R.drawable.image_loading_resource);
        } else {

            DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.image_loading_resource)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .displayer(new SimpleBitmapDisplayer());

            DisplayImageOptions options = builder.build();
            ImageLoader.getInstance().displayImage(url, this, options, imageLoadingListener);
        }
    }
}
