package cr.quarks.dondevotarcr.util;

import android.content.Context;

import com.suigeneris.android.core.data.CoreDBHelper;

/**
 * Created by altfuns on 2/3/16.
 */
public class DBHelper extends CoreDBHelper {

    protected final static int DATABASE_VERSION = 1;

    public final static String DATABASE_NAME = "padron.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, DATABASE_VERSION);
    }
}
