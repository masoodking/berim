package ir.ac.ut.berim;

import android.app.Application;

import ir.ac.ut.network.NetworkManager;
import ir.ac.ut.utils.TypeFaceUtils;

/**
 * Created by Masood on 12/9/2015 AD.
 */
public class BerimApplication extends Application {

    private static BerimApplication singleton;

    public final static boolean DEBUG = true;

    public BerimApplication() {
        super();
        singleton = this;
    }

    public static BerimApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkManager.connect();

        TypeFaceUtils.setDefaultFont(this, "DEFAULT", "font/IRAN Sans Light.ttf");
        TypeFaceUtils.setDefaultFont(this, "MONOSPACE", "font/IRAN Sans Light.ttf");
        TypeFaceUtils.setDefaultFont(this, "SERIF", "font/IRAN Sans Light.ttf");
        TypeFaceUtils.setDefaultFont(this, "SANS_SERIF", "font/IRAN Sans Light.ttf");
    }

}
