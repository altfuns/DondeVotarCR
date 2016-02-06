package cr.quarks.dondevotarcr;

import android.support.multidex.MultiDexApplication;

import com.suigeneris.android.core.CoreApp;

import cr.quarks.dondevotarcr.util.DBHelper;

/**
 * Created by altfuns on 2/3/16.
 */
public class App extends MultiDexApplication {
    private static App instance;
    private DBHelper helper;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        this.helper = new DBHelper(this);

        CoreApp.setHelper(this.helper);
    }
}
