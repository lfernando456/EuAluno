package com.andreoid.EuAluno;

/**
 * Created by André on 20/05/2016.
 */
import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
public class CircleTransform extends BitmapTransformation {
    Context context;
    public CircleTransform(Context context) {

        super(context);
        this.context = context;
    }
    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
        //return ImageUtils.getCircularBitmapImage(source);
        return ImageUtils.getCircularBitmapImage(source);
    }
    @Override
    public String getId() {
        return getId();
    }
}