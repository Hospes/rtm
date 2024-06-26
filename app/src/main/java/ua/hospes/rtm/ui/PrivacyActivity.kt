package ua.hospes.rtm.ui

import dagger.hilt.android.AndroidEntryPoint
import ua.hospes.rtm.R
import ua.hospes.rtm.core.WebViewActivity

@AndroidEntryPoint
class PrivacyActivity : WebViewActivity() {
    override fun title(): Int = R.string.privacy_title

    override fun localUrl(): String = "/privacy_policy.html"

    override fun showBackIcon(): Boolean = prefs.isPrivacyAccepted

    override fun showAcceptButtons(): Boolean = !prefs.isPrivacyAccepted

    override fun accept() = with(prefs) { isPrivacyAccepted = true }
}