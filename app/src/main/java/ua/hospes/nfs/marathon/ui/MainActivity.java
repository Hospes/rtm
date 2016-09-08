package ua.hospes.nfs.marathon.ui;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
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
import ua.hospes.nfs.marathon.ui.drivers.AddDriverDialogFragment;
import ua.hospes.nfs.marathon.ui.drivers.DriversFragment;
import ua.hospes.nfs.marathon.ui.race.RaceFragment;
import ua.hospes.nfs.marathon.ui.teams.AddTeamDialogFragment;
import ua.hospes.nfs.marathon.ui.teams.TeamsFragment;

@ActivityScope
@AutoComponent(dependencies = App.class, modules = {ActivityModule.class})
@AutoInjector
public class MainActivity extends AppCompatActivity implements HasComponent<MainActivityComponent> {
    private MainActivityComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.getComponent(this, MainActivityComponent.class).inject(this);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setup spinner
        AppCompatSpinner spinner = (AppCompatSpinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Section 1",
                        "Teams",
                        "Drivers",
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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

                    default:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                                .commit();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_start:
                StopWatchService.start(this);
                return true;

            case R.id.action_stop:
                StopWatchService.stop(this);
                return true;

            case R.id.action_add_driver:
                AddDriverDialogFragment.newInstance(null).show(getSupportFragmentManager(), "add_driver");
                return true;

            case R.id.action_add_team:
                AddTeamDialogFragment.newInstance(null).show(getSupportFragmentManager(), "add_team");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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


    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}