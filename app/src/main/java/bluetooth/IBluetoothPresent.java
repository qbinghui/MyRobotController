package bluetooth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by zhangtingting on 15/7/27.
 */
public interface IBluetoothPresent {

    //蓝牙操作
    boolean initBluetoothService(Context context);
    void startBluetoothService(Activity context);
    void sendData(String cmd);
    void stopBluetoothService();
    void findBlue(Activity context);
    void resolveBlueResult(Intent intent);
    void enableBlue();
    void disconnect();
    void setConnectListener(IBlueConnectListener listener);
}
