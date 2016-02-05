package cr.quarks.dondevotarcr;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public static void processFile(Context context, String path){
        BufferedReader reader = null;
        SQLiteDatabase db = null;
        DatabaseUtils.InsertHelper insertHelper = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(path), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            Person person = null;
            long init = new Date().getTime();
            db = CoreApp.getHelper().openDataBase(SQLiteDatabase.OPEN_READWRITE);
            insertHelper = new DatabaseUtils.InsertHelper(db, Person.getTableName(Person.class));
            db.beginTransaction();
            while ((mLine = reader.readLine()) != null) {
                //process line
                insertHelper.prepareForReplace();
                person = Person.createPerson(mLine);
                person.bind(insertHelper);
                insertHelper.execute();
                //person.save();
                //mLine.split(",");
                //LogIt.d(Util.class, String.format("Eache line time: %s", (new Date().getTime() - init) /1000));
            }
            db.setTransactionSuccessful();
            LogIt.d(Util.class, String.format("Load Time: %s", (new Date().getTime() - init)/1000));
            LogIt.d(Util.class, String.format("Save Time: %s", (new Date().getTime() - init)/1000));
        } catch (IOException e) {
            LogIt.e(Util.class, e, e.getMessage());
        } finally {
            if(db != null) {
                db.endTransaction();
            }
            if(insertHelper != null) {
                insertHelper.close();
            }
            CoreApp.getHelper().close();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

    }
}
