package ua.hospes.rtm.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import ua.hospes.rtm.theme.RTMTheme

class ComposeTestFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            ComposeView(context = requireContext()).apply {
                setContent {
                    RTMTheme {
                        ComposeTestScreen()
                    }
                }
            }
}