package org.sheedon.resource;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * 一个旨在减少99%的drawable.xml文件，可直接在布局文件中对任意View声明drawable属性。
 *
 * @author : sheedon by hegaojian
 */
public class ProxyDrawable extends StateListDrawable {

    private Drawable originDrawable;

    @Override
    public void addState(int[] stateSet, Drawable drawable) {
        if (stateSet != null && stateSet.length == 1 && stateSet[0] == 0) {
            originDrawable = drawable;
        }
        super.addState(stateSet, drawable);
    }

    Drawable getOriginDrawable() {
        return originDrawable;
    }
}
