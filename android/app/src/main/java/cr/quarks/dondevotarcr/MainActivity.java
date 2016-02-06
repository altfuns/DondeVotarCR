package cr.quarks.dondevotarcr;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.suigeneris.android.core.CoreApp;
import com.suigeneris.android.core.data.DatabaseUtil;

import java.io.IOException;

import cr.quarks.dondevotarcr.model.Person;
import cr.quarks.dondevotarcr.util.DBHelper;
import cr.quarks.dondevotarcr.util.Util;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        createDatabase();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(position == 0) {
            // update the main content by replacing fragments
            fragmentManager.beginTransaction()
                    .replace(R.id.container, VoteFragment.newInstance(position + 1))
                    .commit();
        }else if(position == 1){
            // update the main content by replacing fragments
            fragmentManager.beginTransaction()
                    .replace(R.id.container, DownloadFragment.newInstance(position + 1))
                    .commit();
        }else if(position == 2){
            // update the main content by replacing fragments
            fragmentManager.beginTransaction()
                    .replace(R.id.container, AboutFragment.newInstance(position + 1))
                    .commit();
        }

    }

    private void createDatabase(){
        try {
            if(!CoreApp.getHelper().checkDataBase()){
                CoreApp.getHelper().createDataBase();
                Util.copyDataBase(this, DBHelper.getDatabasePath(), DBHelper.getDataBaseName());
                DatabaseUtil.reCreateTable(Person.class, CoreApp.getHelper().openDataBase());
            }

        }catch (IOException e){
            Log.e("DondeVotarCR", e.getMessage());
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

}
