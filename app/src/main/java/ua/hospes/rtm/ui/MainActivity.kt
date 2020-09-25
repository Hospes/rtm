package ua.hospes.rtm.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import ua.hospes.rtm.R
import ua.hospes.rtm.core.StopWatchService
import ua.hospes.rtm.databinding.ActivityMainBinding
import ua.hospes.rtm.domain.preferences.PreferencesManager
import ua.hospes.rtm.ui.cars.CarsFragment
import ua.hospes.rtm.ui.drivers.DriversFragment
import ua.hospes.rtm.ui.race.RaceFragment
import ua.hospes.rtm.ui.settings.SettingsFragment
import ua.hospes.rtm.ui.teams.TeamsFragment
import ua.hospes.rtm.ui.test.ComposeTestFragment
import ua.hospes.rtm.utils.extentions.doOnApplyWindowInsets
import javax.inject.Inject

private const val REQUEST_CODE_PRIVACY = 10
private const val REQUEST_CODE_TERMS = 20

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    @Inject lateinit var prefs: PreferencesManager
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding.toolbar.doOnApplyWindowInsets { view, insets, padding, _ ->
            view.updatePadding(top = padding.top + insets.getInsets(WindowInsetsCompat.Type.systemBars()).top)
        }
        setSupportActionBar(binding.toolbar)

        binding.navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        if (savedInstanceState == null) {
            binding.navView.setCheckedItem(R.id.nav_race)
            navigateTo(RaceFragment())
        }

        if (!prefs.isPrivacyAccepted) startActivityForResult(Intent(this, PrivacyActivity::class.java), REQUEST_CODE_PRIVACY)
        else if (!prefs.isTermsAccepted) startActivityForResult(Intent(this, TermsActivity::class.java), REQUEST_CODE_TERMS)

        StopWatchService.checkDeath(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> binding.drawerLayout.openDrawer(GravityCompat.START).let { true }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        binding.drawerLayout.closeDrawers()

        when (menuItem.itemId) {
            R.id.nav_race -> navigateTo(RaceFragment()).also { menuItem.isChecked = true }
            R.id.nav_teams -> navigateTo(TeamsFragment()).also { menuItem.isChecked = true }
            R.id.nav_drivers -> navigateTo(DriversFragment()).also { menuItem.isChecked = true }
            R.id.nav_cars -> navigateTo(CarsFragment()).also { menuItem.isChecked = true }
            R.id.nav_settings -> navigateTo(SettingsFragment()).also { menuItem.isChecked = true }
            R.id.nav_test -> navigateTo(ComposeTestFragment()).also { menuItem.isChecked = true }

            R.id.nav_privacy -> startActivity(Intent(this, PrivacyActivity::class.java))
            R.id.nav_terms -> startActivity(Intent(this, TermsActivity::class.java))

            else -> Unit
        }
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
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