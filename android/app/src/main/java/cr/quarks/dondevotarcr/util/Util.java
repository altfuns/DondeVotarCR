package cr.quarks.dondevotarcr.util;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.suigeneris.android.core.CoreApp;
import com.suigeneris.android.core.util.LogIt;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;

import cr.quarks.dondevotarcr.model.Person;

/**
 * Created by altfuns on 2/2/16.
 */
public class Util {

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    public static void copyDataBase(Context context, String path, String name) throws IOException {

        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(name);

        // Path to the just created empty db
        String outFileName = path + name;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }
}
