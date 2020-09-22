package ua.hospes.rtm.ui.cars

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_add_car.*
import kotlinx.coroutines.launch
import ua.hospes.rtm.R
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.utils.extentions.extra

@AndroidEntryPoint
class EditCarDialogFragment : DialogFragment(R.layout.dialog_add_car) {
    private val viewModel: EditCarViewModel by viewModels()
    private val car by extra<Car>(KEY_CAR)
    private val qualities = arrayOf(Car.Quality.LOW, Car.Quality.NORMAL, Car.Quality.HIGH)


    companion object {
        private const val KEY_CAR = "car"

        fun newInstance(car: Car?) = EditCarDialogFragment().apply { arguments = Bundle().apply { putParcelable(KEY_CAR, car) } }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sp_quality.adapter = CarQualityAdapter(requireContext(), *qualities)

        btn_save.setOnClickListener { clickSave() }
        btn_cancel.setOnClickListener { dismiss() }
        btn_delete.setOnClickListener { clickDelete() }

        viewModel.car.observe(this) { onCar(it) }

        if (savedInstanceState == null) {
            viewModel.initCar(car)
        }
    }


    private fun onCar(car: Car?) {
        btn_delete.isEnabled = car != null
        if (car == null) sp_quality.setSelection(1)
        else selectQuality(car.quality)
        car ?: return
        val number = car.number.toString()
        et_number.setText(number)
        et_number.setSelection(number.length)
        cb_broken.isChecked = car.broken
    }

    private fun selectQuality(quality: Car.Quality) = qualities.forEachIndexed { i, q ->
        if (q == quality) {
            sp_quality.setSelection(i)
            return@forEachIndexed
        }
    }


    private fun clickSave() = lifecycleScope.launch {
        val number = et_number.text.toString().toIntOrNull() ?: throw IllegalArgumentException("Number is null")
        viewModel.save(number, sp_quality.selectedItem as Car.Quality, cb_broken.isChecked)
        dismiss()
    }.let { Unit }

    private fun clickDelete() = lifecycleScope.launch {
        viewModel.delete()
        dismiss()
    }.let { Unit }
}