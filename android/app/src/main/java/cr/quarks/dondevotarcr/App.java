package cr.quarks.dondevotarcr;

import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;

import com.suigeneris.android.core.CoreApp;

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
