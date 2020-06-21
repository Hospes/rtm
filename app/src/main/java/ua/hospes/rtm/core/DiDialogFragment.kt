package ua.hospes.rtm.core

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import ua.hospes.rtm.R
import javax.inject.Inject

abstract class DiDialogFragment : DialogFragment(), HasAndroidInjector {
    @Inject lateinit var injector: DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> = injector


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_RTM_Dialog)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }
}