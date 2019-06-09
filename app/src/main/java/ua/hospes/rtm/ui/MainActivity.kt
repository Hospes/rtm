package ua.hospes.rtm.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ua.hospes.rtm.R
import ua.hospes.rtm.core.StopWatchService
import ua.hospes.rtm.ui.cars.CarsFragment
import ua.hospes.rtm.ui.drivers.DriversFragment
import ua.hospes.rtm.ui.race.RaceFragment
import ua.hospes.rtm.ui.settings.SettingsFragment
import ua.hospes.rtm.ui.teams.TeamsFragment

class MainActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        nav_view.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        if (savedInstanceState == null) {
            nav_view.setCheckedItem(R.id.nav_race)
            navigateTo(RaceFragment.newInstance())
        }

        StopWatchService.checkDeath(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> drawer_layout.openDrawer(GravityCompat.START).let { true }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        // set item as selected to persist highlight
        menuItem.isChecked = true
        // close drawer when item is tapped
        drawer_layout.closeDrawers()

        // Add code here to update the UI based on the item selected
        // For example, swap UI fragments here

        when (menuItem.itemId) {
            R.id.nav_race -> navigateTo(RaceFragment.newInstance())
            R.id.nav_teams -> navigateTo(TeamsFragment())
            R.id.nav_drivers -> navigateTo(DriversFragment())
            R.id.nav_cars -> navigateTo(CarsFragment())
            R.id.nav_settings -> navigateTo(SettingsFragment.newInstance())
            else -> navigateTo(PlaceHolderFragment())
        }
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }


    private fun navigateTo(fragment: Fragment) = supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.container, fragment)
            .commit()


    private class PlaceHolderFragment : Fragment(R.layout.fragment_main)
}