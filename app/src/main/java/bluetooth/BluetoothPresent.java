package bluetooth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.qbinghui.myrobotcontroller.R;

import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import utils.ToastUtils;

/**
 * Created by zhangtingting on 15/7/27.
 */
public class BluetoothPresent implements IBluetoothPresent{

    @Override
    public boolean initBluetoothService(Context context) {
        if(BluetoothManager.INSTANCE.init(context)){
            return true;
        }else{
            ToastUtils.toastShort(context, R.string.blue_not_avaliable);
            return false;
        }
    }

    @Override
    public void startBluetoothService(Activity context) {
        BluetoothManager.INSTANCE.startBlueService(context);
    }

    @Override
    public void sendData(String cmd) {
        BluetoothManager.INSTANCE.sendData(cmd);
    }

    @Override
    public void stopBluetoothService() {
        BluetoothManager.INSTANCE.stopBlueService();
    }


    @Override
    public void findBlue(Activity context) {
        Intent intent = new Intent(context, DeviceList.class);
        context.startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    @Override
    public void resolveBlueResult(Intent intent) {
        BluetoothManager.INSTANCE.connect(intent);
    }

    @Override
    public void enableBlue() {
        BluetoothManager.INSTANCE.enableBlue();
    }

    @Override
    public void disconnect() {
        BluetoothManager.INSTANCE.disconnect();
    }

    @Override
    public void setConnectListener(IBlueConnectListener listener) {
        BluetoothManager.INSTANCE.setConnectListener(listener);
    }

}
