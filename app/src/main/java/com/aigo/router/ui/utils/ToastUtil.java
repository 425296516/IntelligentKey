package com.aigo.router.ui.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zhangcirui on 16/4/26.
 */
public class ToastUtil {
    private static Toast mToast;

    public ToastUtil() {
    }

    public static void showToast(Context context, String text) {
        if(mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }

        mToast.show();
    }

    public static void rawToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void cancelToast() {
        if(mToast != null) {
            mToast.cancel();
        }

    }
}
