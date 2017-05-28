package de.ivleafcloverapps.pillreminder.main;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import de.ivleafcloverapps.pillreminder.R;
import de.ivleafcloverapps.pillreminder.fragments.CalendarFragment;
import de.ivleafcloverapps.pillreminder.fragments.SettingsFragment;
import de.ivleafcloverapps.pillreminder.services.NotificationAlarmManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * id of the active menu
     */
    int activeMenuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize colored top line, named toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialize navigation menu layout and toggle function
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // intialize navigation menu and make sure, the first menu is selected
        activeMenuId = 0;
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(activeMenuId);
        navigationView.setNavigationItemSelectedListener(this);

        // because FragmentLayout has no body, we have to initialize the default fragment
        setFragment(new CalendarFragment());

        // start Background NotificationService
        NotificationAlarmManager.startAlarmManager(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            // when back is double clicked the app should be closed
            finish();
        } else {
            // when back is clicked, show the menu
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // check if this entry is already selected
        if (id == activeMenuId) {
            // nothing to do here, only close the NavigationViewer
        } else {
            activeMenuId = id;
            // change fragments and put them to stack
            if (id == R.id.nav_calendar) {
                setFragment(new CalendarFragment());
            } else if (id == R.id.nav_settings) {
                setFragment(new SettingsFragment());
            }
        }

        // close the menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * sets the fragment in the FrameLayout body<br />
     * if there is an old one it will be replaced
     *
     * @param newFragment
     */
    private void setFragment(Fragment newFragment) {
        FrameLayout fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.commit();
    }
}
