package ua.hospes.rtm.ui.teams

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_select_drivers.*
import ua.hospes.rtm.R
import ua.hospes.rtm.core.DiDialogFragment
import ua.hospes.rtm.domain.drivers.Driver
import ua.hospes.rtm.utils.extentions.extraNotNull
import javax.inject.Inject

private const val KEY_DRIVERS = "drivers"

class SelectDriversDialogFragment : DiDialogFragment(), SelectDriversContract.View {
    @Inject lateinit var presenter: SelectDriversPresenter
    private val adapter = SelectDriversAdapter()
    private val selected by extraNotNull(KEY_DRIVERS, emptyList<Driver>())


    companion object {
        @JvmStatic fun newInstance(drivers: List<Driver>) = SelectDriversDialogFragment()
                .apply { arguments = Bundle().apply { putParcelableArrayList(KEY_DRIVERS, ArrayList(drivers)) } }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? = inflater.inflate(R.layout.dialog_select_drivers, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.setHasFixedSize(true)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter

        adapter.setSelected(selected)

        btn_cancel.setOnClickListener { dismiss() }
        btn_save.setOnClickListener { presenter.save(adapter.getSelectedIds()) }

        presenter.attachView(this, lifecycle)
    }


    override fun onDrivers(list: List<Driver>) = adapter.submitList(list)

    override fun onSaveSelectedDrivers(list: List<Driver>) {
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, Intent().putExtra("drivers", ArrayList(list)))
        dismiss()
    }

    override fun onError(throwable: Throwable) = Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
}