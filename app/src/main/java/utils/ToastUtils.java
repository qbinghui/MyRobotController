package utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zhangtingting on 15/7/19.
 */
public class ToastUtils {

    public static final void toastShort(Context context,int resId){
        Toast.makeText(context,resId,Toast.LENGTH_SHORT).show();
    }
}
