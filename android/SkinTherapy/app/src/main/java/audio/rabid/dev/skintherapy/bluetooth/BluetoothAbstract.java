package audio.rabid.dev.skintherapy.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by Charles on 7/22/2015.
 */
public abstract class BluetoothAbstract implements Runnable {

    private static final int REQUEST_ENABLE_BT = 0x33;

    private ArrayList<DataListener> dataListeners = new ArrayList<>();
    private ArrayList<StringListener> stringListeners = new ArrayList<>();

    private Thread readerThread = new Thread(this);



    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    protected BluetoothAbstract() throws BluetoothUnavailableException {
        if(mBluetoothAdapter==null){
            throw new BluetoothUnavailableException();
        }

    }

    //http://developer.android.com/guide/topics/connectivity/bluetooth.html

    public void enable(Activity a){
        if(!mBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            a.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public boolean enable(){
        if(!mBluetoothAdapter.isEnabled()){
            return mBluetoothAdapter.enable();
        }else {
            return true;
        }
    }

    public void addDataListener(DataListener callback){

    }

    public void addStringListener(StringListener callback){

    }

    public abstract void send(Object client, byte[] data);

    public void send(Object client, String message){
        send(client, message.getBytes());
    }

    public static class BluetoothUnavailableException extends Exception {}

    interface DataListener {
        void onData(Object client, byte[] data);
    }

    interface StringListener {
        void onString(Object client, byte[] data);
    }
}
