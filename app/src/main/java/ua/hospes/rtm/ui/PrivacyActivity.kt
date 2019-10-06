package ua.hospes.rtm.ui

import ua.hospes.rtm.R
import ua.hospes.rtm.core.WebViewActivity

class PrivacyActivity : WebViewActivity() {
    override fun title(): Int = R.string.privacy_title

    override fun localUrl(): String = "/privacy_policy.html"
}