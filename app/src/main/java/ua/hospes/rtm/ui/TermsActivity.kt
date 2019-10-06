package ua.hospes.rtm.ui

import ua.hospes.rtm.R
import ua.hospes.rtm.core.WebViewActivity

class TermsActivity : WebViewActivity() {
    override fun title(): Int = R.string.terms_title

    override fun localUrl(): String = "/terms.html"

    override fun showBackIcon(): Boolean = prefs.isTermsAccepted

    override fun showAcceptButtons(): Boolean = !prefs.isTermsAccepted

    override fun accept() = with(prefs) { isTermsAccepted = true }
}