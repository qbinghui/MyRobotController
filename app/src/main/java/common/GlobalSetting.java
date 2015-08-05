package common;

import app.akexorcist.bluetotohspp.library.BluetoothState;

/**
 * Created by zhangtingting on 15/7/18.
 */
public class GlobalSetting {

    private GlobalSetting(){

    }

    public static class BluetoothSetting{
        public static boolean BLUE_TYPE = BluetoothState.DEVICE_OTHER;
    }
}
