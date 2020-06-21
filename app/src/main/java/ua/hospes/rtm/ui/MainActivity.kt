package ua.hospes.rtm.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import ua.hospes.rtm.R
import ua.hospes.rtm.core.StopWatchService
import ua.hospes.rtm.domain.preferences.PreferencesManager
import ua.hospes.rtm.ui.cars.CarsFragment
import ua.hospes.rtm.ui.drivers.DriversFragment
import ua.hospes.rtm.ui.race.RaceFragment
import ua.hospes.rtm.ui.settings.SettingsFragment
import ua.hospes.rtm.ui.teams.TeamsFragment
import ua.hospes.rtm.utils.extentions.doOnApplyWindowInsets
import javax.inject.Inject

private const val REQUEST_CODE_PRIVACY = 10
private const val REQUEST_CODE_TERMS = 20

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), NavigationView.OnNavigationItemSelectedListener {
    @Inject lateinit var prefs: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drawer_layout.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        toolbar.doOnApplyWindowInsets { view, insets, padding, _ ->
            view.updatePadding(top = padding.top + insets.systemWindowInsetTop)
        }
        setSupportActionBar(toolbar)

        nav_view.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        if (savedInstanceState == null) {
            nav_view.setCheckedItem(R.id.nav_race)
            navigateTo(RaceFragment())
        }

        if (!prefs.isPrivacyAccepted) startActivityForResult(Intent(this, PrivacyActivity::class.java), REQUEST_CODE_PRIVACY)
        else if (!prefs.isTermsAccepted) startActivityForResult(Intent(this, TermsActivity::class.java), REQUEST_CODE_TERMS)

        StopWatchService.checkDeath(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> drawer_layout.openDrawer(GravityCompat.START).let { true }
        null -> false
        else -> super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        drawer_layout.closeDrawers()

        when (menuItem.itemId) {
            R.id.nav_race -> navigateTo(RaceFragment()).also { menuItem.isChecked = true }
            R.id.nav_teams -> navigateTo(TeamsFragment()).also { menuItem.isChecked = true }
            R.id.nav_drivers -> navigateTo(DriversFragment()).also { menuItem.isChecked = true }
            R.id.nav_cars -> navigateTo(CarsFragment()).also { menuItem.isChecked = true }
            R.id.nav_settings -> navigateTo(SettingsFragment()).also { menuItem.isChecked = true }

            R.id.nav_privacy -> startActivity(Intent(this, PrivacyActivity::class.java))
            R.id.nav_terms -> startActivity(Intent(this, TermsActivity::class.java))

            else -> Unit
        }
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) = when (requestCode) {
        REQUEST_CODE_PRIVACY -> when (resultCode) {
            Activity.RESULT_OK ->
                if (!prefs.isTermsAccepted)
                    startActivityForResult(Intent(this, TermsActivity::class.java), REQUEST_CODE_TERMS)
                else Unit
            else -> finish()
        }
        REQUEST_CODE_TERMS -> when (resultCode) {
            Activity.RESULT_OK -> Unit
            else -> finish()
        }
        else -> super.onActivityResult(requestCode, resultCode, data)
    }

    private fun navigateTo(fragment: Fragment) = supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
}