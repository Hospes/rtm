package ua.hospes.rtm.core

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_webview.*
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.preferences.PreferencesManager
import ua.hospes.rtm.utils.extentions.doOnApplyWindowInsets
import javax.inject.Inject

abstract class WebViewActivity : AppCompatActivity(R.layout.activity_webview) {
    @Inject lateinit var prefs: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        root.doOnApplyWindowInsets { view, insets, padding, _ ->
            view.updatePadding(bottom = padding.bottom + insets.systemWindowInsetBottom)
        }
        toolbar.doOnApplyWindowInsets { view, insets, padding, _ ->
            view.updatePadding(top = padding.top + insets.systemWindowInsetTop)
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(showBackIcon())
        supportActionBar?.setTitle(title())

        accept.setOnClickListener {
            accept()
            setResult(Activity.RESULT_OK)
            finish()
        }
        decline.setOnClickListener { onBackPressed() }
        bottom_bar.visibility = when (showAcceptButtons()) {
            true -> View.VISIBLE
            else -> View.GONE
        }

        webview.loadUrl(Uri.decode("file:///android_asset${this.localUrl()}"))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> onBackPressed().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    @StringRes
    abstract fun title(): Int

    abstract fun localUrl(): String

    abstract fun showBackIcon(): Boolean
    abstract fun showAcceptButtons(): Boolean

    abstract fun accept()
}