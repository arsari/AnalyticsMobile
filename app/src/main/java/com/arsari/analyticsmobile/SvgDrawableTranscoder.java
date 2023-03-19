package com.arsari.analyticsmobile;

import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.SimpleResource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.caverock.androidsvg.SVG;

/**
 * It converts an SVG into a `PictureDrawable` which can be used as a `Drawable` in Android
 */
public class SvgDrawableTranscoder implements ResourceTranscoder<SVG, PictureDrawable> {

	/**
	 * It takes an SVG resource and returns a PictureDrawable resource
	 *
	 * @param toTranscode The resource to transcode.
	 * @param options The options object passed to the load method.
	 * @return A resource of type PictureDrawable.
	 */
	@Override
	public Resource<PictureDrawable> transcode(@NonNull Resource<SVG> toTranscode, @NonNull Options options) {
		SVG svg = toTranscode.get();
		Picture picture = svg.renderToPicture();
		PictureDrawable drawable = new PictureDrawable(picture);
		return new SimpleResource<>(drawable);
	}
}
