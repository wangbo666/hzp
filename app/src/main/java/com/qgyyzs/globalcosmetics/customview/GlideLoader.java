package com.qgyyzs.globalcosmetics.customview;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jaiky.imagespickers.ImageLoader;
import com.qgyyzs.globalcosmetics.R;

public class GlideLoader implements ImageLoader {

	private static final long serialVersionUID = 1L;

	@Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .apply(new RequestOptions().placeholder(R.drawable.global_img_default)
                .centerCrop())
                .into(imageView);
    }

}
