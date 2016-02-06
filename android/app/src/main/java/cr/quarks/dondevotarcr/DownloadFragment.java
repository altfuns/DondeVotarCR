package cr.quarks.dondevotarcr;

/**
 * Created by luisa on 2/5/16.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.suigeneris.android.core.CoreApp;
import com.suigeneris.android.core.util.LogIt;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import cr.quarks.dondevotarcr.model.Canton;
import cr.quarks.dondevotarcr.model.Person;
import cr.quarks.dondevotarcr.model.Province;

/**
 * A vote fragment containing the search field.
 */
public class DownloadFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ProgressDialog mProgressDialog;

    private Spinner spinner;
    private Spinner spinner2;
    private Button button;

    private ArrayAdapter<Province> provinceAdapter;
    private ArrayAdapter<Canton> cantonAdapter;

    private static final String[] provinces = new String[]{ "sj", "al", "ca", "he", "gu", "pu", "li"};

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DownloadFragment newInstance(int sectionNumber) {
        DownloadFragment fragment = new DownloadFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public DownloadFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_download, container, false);
        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);
        button = (Button) rootView.findViewById(R.id.button);

        mProgressDialog = new ProgressDialog(this.getContext());
        mProgressDialog.setMessage("Instalando padron");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);

        provinceAdapter = new ArrayAdapter<Province>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, Province.findAll(Province.class));
        spinner.setAdapter(provinceAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Province province = (Province) adapterView.getItemAtPosition(i);
                cantonAdapter = new ArrayAdapter<Canton>(DownloadFragment.this.getContext(), android.R.layout.simple_spinner_dropdown_item, Canton.findByProvince(province.getId()));
                spinner2.setAdapter(cantonAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Province province = (Province) spinner.getSelectedItem();
                if(cantonAdapter != null){
                    Canton canton = (Canton) spinner2.getSelectedItem();
                    String url = String.format("http://www.tse.go.cr/zip/padron/%s%s.zip", provinces[(int)province.getId() -1], canton.getName().replace(" ", ""));
                    Toast.makeText(getContext(), String.format("Download %s", url), Toast.LENGTH_LONG).show();

                    download(url);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private void download(String url){
        final DownloadTask downloadTask = new DownloadTask(this.getActivity());
        downloadTask.execute(url);

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
    }


    // usually, subclasses of AsyncTask are declared inside the activity class.
// that way, you can easily modify the UI thread from here
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            String url = sUrl[0];
            Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
            File dir = context.getFilesDir();
            if(isSDPresent){
                dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            }else{
                dir = context.getFilesDir();
            }

            String filename = URLUtil.guessFileName(url, null, null);
            String path = dir.getPath() + "/";
            String result = null;
            try{
                result = downloadFile(url,path, filename);
                if(result == null){
                    if(unpackZip(path, filename)){
                        result = processFile(path, filename.split("\\.")[0]+ ".txt");
                    }
                }
            }catch (Exception e){
                result = e.toString();
            }

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context,"Error: "+result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context,"Padron instalado correctamente", Toast.LENGTH_SHORT).show();
        }

        private String downloadFile(String urlPath, String path, String filename) throws IOException{
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(urlPath);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();

                output = new FileOutputStream(path  + filename);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 25 / fileLength));
                    output.write(data, 0, count);
                }


            } catch (Exception e) {
                throw e;
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        private boolean unpackZip(String path, String zipname)
        {
            InputStream is;
            ZipInputStream zis;
            try
            {
                String filename;
                is = new FileInputStream(path + zipname);
                zis = new ZipInputStream(new BufferedInputStream(is));
                ZipEntry ze;
                byte[] buffer = new byte[1024];
                int count;

                while ((ze = zis.getNextEntry()) != null)
                {
                    // zapis do souboru
                    filename = ze.getName();

                    // Need to create directories if not exists, or
                    // it will generate an Exception...
                    if (ze.isDirectory()) {
                        File fmd = new File(path + filename);
                        fmd.mkdirs();
                        continue;
                    }

                    FileOutputStream fout = new FileOutputStream(path + filename);

                    // cteni zipu a zapis
                    while ((count = zis.read(buffer)) != -1)
                    {
                        fout.write(buffer, 0, count);
                    }

                    fout.close();
                    zis.closeEntry();
                }

                zis.close();

                publishProgress(30);

            }
            catch(IOException e)
            {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        public String processFile(String path, String filename){
            BufferedReader reader = null;
            InputStream is;
            SQLiteDatabase db = null;
            DatabaseUtils.InsertHelper insertHelper = null;
            try {
                File file = new File(path + filename);
                long fileLength = file.length();
                long total = 0;

                is = new FileInputStream(file);
                reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));

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

                    total += (long)mLine.length();

                    publishProgress((int) (30 + (total * 70 / fileLength)));
                }
                db.setTransactionSuccessful();
            } catch (IOException e) {
                LogIt.e(DownloadTask.class, e, e.getMessage());
                return e.toString();
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

            return null;
        }
    }


}
