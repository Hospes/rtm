package ua.hospes.rtm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import ua.hospes.rtm.R;
import ua.hospes.rtm.core.StopWatchService;
import ua.hospes.rtm.ui.cars.CarsFragment;
import ua.hospes.rtm.ui.drivers.DriversFragment;
import ua.hospes.rtm.ui.race.RaceFragment;
import ua.hospes.rtm.ui.settings.SettingsFragment;
import ua.hospes.rtm.ui.teams.TeamsFragment;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector, NavigationDrawerFragment.NavigationDrawerCallbacks {
    @Inject DispatchingAndroidInjector<Fragment> fragmentInjector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Fragment managing the behaviors, interactions and presentation of the navigation drawer. */
        NavigationDrawerFragment mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        StopWatchService.checkDeath(this);
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.container, RaceFragment.newInstance())
                        .commit();
                break;

            case 1:
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.container, TeamsFragment.newInstance())
                        .commit();
                break;

            case 2:
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.container, DriversFragment.newInstance())
                        .commit();
                break;

            case 3:
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.container, CarsFragment.newInstance())
                        .commit();
                break;

            case 4:
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.container, SettingsFragment.newInstance())
                        .commit();
                break;

            default:
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
                break;
        }
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }


    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {}

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main, container, false);
        }
    }
}