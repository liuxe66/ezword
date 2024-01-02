package com.atom.ezwords.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.atom.ezwords.app.EzWordsApp;

/**
 * author : liuxe
 * date : 2021/8/20 1:40 下午
 * description :
 */
public class ToastUtil {
    private static Toast toast;



    public static Handler mHandler = new Handler(Looper.getMainLooper());



    public static void showToast(String text) {
        showToast(text, Toast.LENGTH_SHORT);

    }



    public static void showToast(final String text, final int duration) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            show(text, duration);

        } else {
            mHandler.post(() -> show(text, duration));

        }

    }



    private static void show(String text, int duration) {
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(EzWordsApp.Companion.getContext(), text, duration);

        toast.show();

    }

}

