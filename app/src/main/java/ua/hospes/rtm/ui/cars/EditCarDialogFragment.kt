package ua.hospes.rtm.ui.cars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_add_car.*
import kotlinx.coroutines.launch
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.utils.extentions.extra

private const val KEY_CAR = "car"

@AndroidEntryPoint
class EditCarDialogFragment : DialogFragment() {
    private val viewModel: EditCarViewModel by viewModels()
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

        btn_save.setOnClickListener { clickSave() }
        btn_cancel.setOnClickListener { dismiss() }
        btn_delete.setOnClickListener { clickDelete() }

        viewModel.initCar(car)

        viewModel.init.observe(viewLifecycleOwner) { onInitCar(it) }
        viewModel.delAvailable.observe(viewLifecycleOwner) { onDeleteButtonAvailable(it) }
    }


    private fun onInitCar(car: Car) {
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


    private fun onDeleteButtonAvailable(available: Boolean) = with(btn_delete) { isEnabled = available }

    private fun clickSave() = lifecycleScope.launch {
        viewModel.save(et_number.text, sp_quality.selectedItem as Car.Quality, cb_broken.isChecked)
        dismiss()
    }.let { Unit }

    private fun clickDelete() = lifecycleScope.launch {
        viewModel.delete()
        dismiss()
    }.let { Unit }
}