package bluetooth;

/**
 * Created by zhangtingting on 15/7/28.
 */
public interface IBlueConnectListener {
    void onConnected(String name, String address);
    void onConnectFailed();
    void onDisconnected();
    void onRecvMsg(String data);

}
