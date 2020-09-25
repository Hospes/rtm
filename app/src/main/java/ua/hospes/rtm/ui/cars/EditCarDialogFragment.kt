package ua.hospes.rtm.ui.cars

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ua.hospes.rtm.R
import ua.hospes.rtm.databinding.DialogAddCarBinding
import ua.hospes.rtm.domain.cars.Car
import ua.hospes.rtm.utils.ViewBindingHolder
import ua.hospes.rtm.utils.extentions.extra

@AndroidEntryPoint
class EditCarDialogFragment : DialogFragment(R.layout.dialog_add_car), ViewBindingHolder<DialogAddCarBinding> by ViewBindingHolder.Impl() {
    private val viewModel: EditCarViewModel by viewModels()
    private val car by extra<Car>(KEY_CAR)
    private val qualities = arrayOf(Car.Quality.LOW, Car.Quality.NORMAL, Car.Quality.HIGH)


    companion object {
        private const val KEY_CAR = "car"

        fun newInstance(car: Car?) = EditCarDialogFragment().apply { arguments = Bundle().apply { putParcelable(KEY_CAR, car) } }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(DialogAddCarBinding.bind(view), this)

        binding.spQuality.adapter = CarQualityAdapter(requireContext(), *qualities)

        binding.btnSave.setOnClickListener { clickSave() }
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnDelete.setOnClickListener { clickDelete() }

        viewModel.car.observe(this) { onCar(it) }

        if (savedInstanceState == null) {
            viewModel.initCar(car)
        }
    }


    private fun onCar(car: Car?) = with(binding) {
        btnDelete.isEnabled = car != null
        if (car == null) spQuality.setSelection(1)
        else selectQuality(car.quality)
        car ?: return
        val number = car.number.toString()
        etNumber.setText(number)
        etNumber.setSelection(number.length)
        cbBroken.isChecked = car.broken
    }

    private fun selectQuality(quality: Car.Quality) = qualities.forEachIndexed { i, q ->
        if (q == quality) {
            binding.spQuality.setSelection(i)
            return@forEachIndexed
        }
    }


    private fun clickSave() = lifecycleScope.launch {
        val number = binding.etNumber.text.toString().toIntOrNull() ?: throw IllegalArgumentException("Number is null")
        viewModel.save(number, binding.spQuality.selectedItem as Car.Quality, binding.cbBroken.isChecked)
        dismiss()
    }.let { Unit }

    private fun clickDelete() = lifecycleScope.launch {
        viewModel.delete()
        dismiss()
    }.let { Unit }
}