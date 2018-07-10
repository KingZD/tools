package com.zed.tools;

import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by codeest on 2016/8/4.
 */
public class ToastUtil {

    private static Toast toast;

    public static void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);//如果不为空，则直接改变当前toast的文本
        }
        toast.show();
    }

    public static void showToastCenter(String text) {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);//如果不为空，则直接改变当前toast的文本
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
