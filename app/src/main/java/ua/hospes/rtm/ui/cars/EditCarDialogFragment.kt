package ua.hospes.rtm.ui.cars

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ua.hospes.rtm.R
import ua.hospes.rtm.databinding.DialogAddCarBinding
import ua.hospes.rtm.domain.cars.Car

@AndroidEntryPoint
class EditCarDialogFragment : DialogFragment(R.layout.dialog_add_car) {
    private val viewModel: EditCarViewModel by viewModels()
    private val qualities = arrayOf(Car.Quality.LOW, Car.Quality.NORMAL, Car.Quality.HIGH)


    companion object {
        fun newInstance(car: Car?) = EditCarDialogFragment().apply { arguments = bundleOf("car" to car) }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DialogAddCarBinding.bind(view)

        binding.spQuality.adapter = CarQualityAdapter(requireContext(), *qualities)

        binding.btnSave.setOnClickListener { clickSave(binding) }
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnDelete.setOnClickListener { clickDelete() }

        viewModel.car.observe(this) { onCar(binding, it) }
    }


    private fun onCar(binding: DialogAddCarBinding, car: Car?) = with(binding) {
        btnDelete.isEnabled = car != null
        if (car == null) spQuality.setSelection(1)
        else selectQuality(binding, car.quality)
        car ?: return
        val number = car.number.toString()
        etNumber.setText(number)
        etNumber.setSelection(number.length)
        cbBroken.isChecked = car.broken
    }

    private fun selectQuality(binding: DialogAddCarBinding, quality: Car.Quality) = qualities.forEachIndexed { i, q ->
        if (q == quality) {
            binding.spQuality.setSelection(i)
            return@forEachIndexed
        }
    }


    private fun clickSave(binding: DialogAddCarBinding) = lifecycleScope.launch {
        val number = binding.etNumber.text.toString().toIntOrNull() ?: throw IllegalArgumentException("Number is null")
        viewModel.save(number, binding.spQuality.selectedItem as Car.Quality, binding.cbBroken.isChecked)
        dismiss()
    }.let { }

    private fun clickDelete() = lifecycleScope.launch {
        viewModel.delete()
        dismiss()
    }.let { }
}