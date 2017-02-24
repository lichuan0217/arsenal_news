package top.lemonsoda.gunners.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.MenuItem;

/**
 * Created by chuanl on 2/24/17.
 */

public class Utils {

    public static String getImageFromThumbnailUrl(String thumbnail) {
        //http://c2.hoopchina.com.cn/uploads/star/event/images/160613/thumbnail-2b8555950f110a3da73ee28f914a0a184ac0bdfc.png
        String[] array = thumbnail.split("thumbnail-");
        StringBuilder sb = new StringBuilder();
        for (String str : array) {
            sb.append(str);
        }
        if (array == null || array.length == 0)
            return thumbnail;
        else
            return sb.toString();
    }

    public static void tintMenuIcon(Context context, MenuItem item, @ColorRes int color) {
        Drawable normalDrawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(context, color));

        item.setIcon(wrapDrawable);
    }
}
