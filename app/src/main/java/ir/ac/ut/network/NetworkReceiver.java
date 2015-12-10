package ir.ac.ut.network;

/**
 * Created by Masood on 12/9/2015 AD.
 */
public abstract class NetworkReceiver<T> {

    public abstract void onResponse(T response);

    public abstract void onErrorResponse(BerimNetworkException error);

}