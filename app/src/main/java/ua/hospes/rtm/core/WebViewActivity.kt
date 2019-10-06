package ua.hospes.rtm.core

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_webview.*
import ua.hospes.rtm.R

abstract class WebViewActivity : AppCompatActivity(R.layout.activity_webview) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(title())

        webview.loadUrl(Uri.decode("file:///android_asset${this.localUrl()}"))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> onBackPressed().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    @StringRes
    abstract fun title(): Int

    abstract fun localUrl(): String
}