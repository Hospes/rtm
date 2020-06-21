package ua.hospes.rtm.ui.cars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_add_car.*
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.utils.extentions.extra
import javax.inject.Inject

private const val KEY_CAR = "car"

@AndroidEntryPoint
class EditCarDialogFragment : DialogFragment(), EditCarContract.View {
    @Inject lateinit var presenter: EditCarPresenter
    private val car by extra<Car>(KEY_CAR)
    private val qualities = arrayOf(Car.Quality.LOW, Car.Quality.NORMAL, Car.Quality.HIGH)


    companion object {
        @JvmStatic fun newInstance(car: Car?) = EditCarDialogFragment()
                .apply { arguments = Bundle().apply { putParcelable(KEY_CAR, car) } }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            : View? = inflater.inflate(R.layout.dialog_add_car, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sp_quality.adapter = CarQualityAdapter(requireContext(), *qualities)
        if (car == null) sp_quality.setSelection(1)

        btn_save.setOnClickListener { presenter.save(et_number.text, sp_quality.selectedItem as Car.Quality, cb_broken.isChecked) }
        btn_cancel.setOnClickListener { dismiss() }
        btn_delete.setOnClickListener { presenter.delete() }

        presenter.initCar(car)
        presenter.attachView(this, lifecycle)
    }


    override fun onInitCar(car: Car) {
        val number = car.number.toString()
        et_number.setText(number)
        et_number.setSelection(number.length)
        selectQuality(car.quality)
        cb_broken.isChecked = car.broken
    }

    private fun selectQuality(quality: Car.Quality) = qualities.forEachIndexed { i, q ->
        if (q == quality) {
            sp_quality.setSelection(i)
            return@forEachIndexed
        }
    }


    override fun onDeleteButtonAvailable(available: Boolean) = with(btn_delete) { isEnabled = available }

    override fun onSaved() = dismiss()
    override fun onDeleted() = dismiss()

    override fun onError(throwable: Throwable) = Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
}