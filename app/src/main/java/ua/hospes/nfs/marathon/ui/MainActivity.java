package ua.hospes.nfs.marathon.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import ua.hospes.nfs.marathon.App;
import ua.hospes.nfs.marathon.R;
import ua.hospes.nfs.marathon.core.StopWatchService;
import ua.hospes.nfs.marathon.core.di.HasComponent;
import ua.hospes.nfs.marathon.core.di.Injector;
import ua.hospes.nfs.marathon.core.di.module.ActivityModule;
import ua.hospes.nfs.marathon.core.di.scope.ActivityScope;
import ua.hospes.nfs.marathon.ui.cars.CarsFragment;
import ua.hospes.nfs.marathon.ui.drivers.DriversFragment;
import ua.hospes.nfs.marathon.ui.race.RaceFragment;
import ua.hospes.nfs.marathon.ui.settings.SettingsFragment;
import ua.hospes.nfs.marathon.ui.teams.TeamsFragment;

@ActivityScope
@AutoComponent(dependencies = App.class, modules = {ActivityModule.class})
@AutoInjector
public class MainActivity extends AppCompatActivity implements HasComponent<MainActivityComponent>, NavigationDrawerFragment.NavigationDrawerCallbacks {
    private MainActivityComponent component;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.getComponent(this, MainActivityComponent.class).inject(this);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        StopWatchService.checkDeath(this);
    }


    @Override
    public MainActivityComponent getComponent() {
        if (component == null) {
            component = DaggerMainActivityComponent.builder()
                    .appComponent(App.get(this).getComponent())
                    .activityModule(new ActivityModule(this))
                    .build();
        }
        return component;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, RaceFragment.newInstance())
                        .commit();
                break;

            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, TeamsFragment.newInstance())
                        .commit();
                break;

            case 2:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, DriversFragment.newInstance())
                        .commit();
                break;

            case 3:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, CarsFragment.newInstance())
                        .commit();
                break;

            case 4:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, SettingsFragment.newInstance())
                        .commit();
                break;

            default:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
                break;
        }
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}