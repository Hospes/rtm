package ua.hospes.rtm.core

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import ua.hospes.rtm.databinding.ActivityWebviewBinding
import ua.hospes.rtm.domain.preferences.PreferencesManager
import ua.hospes.rtm.utils.extentions.doOnApplyWindowInsets
import javax.inject.Inject

abstract class WebViewActivity : AppCompatActivity() {
    @Inject lateinit var prefs: PreferencesManager
    private val binding by lazy { ActivityWebviewBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding.root.doOnApplyWindowInsets { view, insets, padding, _ ->
            view.updatePadding(bottom = padding.bottom + insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom)
        }
        binding.toolbar.doOnApplyWindowInsets { view, insets, padding, _ ->
            view.updatePadding(top = padding.top + insets.getInsets(WindowInsetsCompat.Type.systemBars()).top)
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(showBackIcon())
        supportActionBar?.setTitle(title())

        binding.accept.setOnClickListener {
            accept()
            setResult(Activity.RESULT_OK)
            finish()
        }
        binding.decline.setOnClickListener { onBackPressed() }
        binding.bottomBar.visibility = when (showAcceptButtons()) {
            true -> View.VISIBLE
            else -> View.GONE
        }

        binding.webview.loadUrl(Uri.decode("file:///android_asset${this.localUrl()}"))
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