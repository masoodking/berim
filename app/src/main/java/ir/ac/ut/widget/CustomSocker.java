package ir.ac.ut.widget;

import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;

import android.util.Log;

/**
 * Created by Masood on 1/11/2016 AD.
 */
public class CustomSocker extends Socket {

    public CustomSocker(Manager io, String nsp) {
        super(io, nsp);
    }

    @Override
    public Socket disconnect() {
        Log.wtf("Socket", "disconnected");
        return super.disconnect();
    }

    @Override
    public Socket connect() {
        Log.wtf("Socket", "connected");
        return super.connect();
    }
}
