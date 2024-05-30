package com.example.trueweather.main.addlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.domain.data.objects.WeatherResult
import com.example.domain.data.utils.collectWhenCreated
import com.example.trueweather.databinding.LocationsBottomSheetLayoutBinding
import com.example.trueweather.utils.setGone
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationsBottomSheet: BottomSheetDialogFragment() {

    private val viewModel: LocationsBottomSheetViewModel by viewModels()
    private lateinit var binding: LocationsBottomSheetLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LocationsBottomSheetLayoutBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        collectWhenCreated(viewModel.locationsState) {
            when(it) {
                LocationsBottomSheetViewModel.LocationsState.Error -> showError()
                LocationsBottomSheetViewModel.LocationsState.Loading -> showGlobalLoading()
                is LocationsBottomSheetViewModel.LocationsState.UserLocationsSuccess -> showUserLocations(it.userLocationsResult)
                is LocationsBottomSheetViewModel.LocationsState.SearchLocationsSuccess -> showSearchedLocations(it.searchLocationsSuccess)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showUserLocations(weatherResult: WeatherResult) {
        showLocations(false)
        Toast.makeText(context, weatherResult.resultList.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun showSearchedLocations(weatherResult: WeatherResult) {
        showLocations(true)
    }

    private fun showError() {
        binding.errorView.setGone(false)
        binding.progressBar.setGone(true)
        binding.locationsView.setGone(true)
    }

    private fun hideError() {
        showGlobalLoading()
    }

    private fun showLocationsLoading() {
        binding.progressBar.setGone(false)
        binding.locationsView.setGone(true)
    }

    private fun showLocations(isSearchLocations: Boolean) {
        binding.progressBar.setGone(true)
        binding.locationsView.setGone(false)
        if(isSearchLocations) binding.saveButton.setGone(true) else binding.saveButton.setGone(false)
    }

    private fun showGlobalLoading() {
        binding.progressBar.setGone(false)
        binding.locationsView.setGone(true)
        binding.errorView.setGone(true)
    }
}