package com.example.trueweather.main.addlocation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.data.objects.WeatherFetchStatus
import com.example.domain.data.objects.WeatherResult
import com.example.domain.data.objects.WeatherResultWrapper
import com.example.domain.data.utils.collectWhenCreated
import com.example.trueweather.databinding.LocationsBottomSheetLayoutBinding
import com.example.trueweather.main.addlocation.ui.AddLocationsAdapter
import com.example.trueweather.utils.setGone
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationsBottomSheet: BottomSheetDialogFragment(), OnLocationClickListener{

    private val viewModel: LocationsBottomSheetViewModel by viewModels()
    private lateinit var binding: LocationsBottomSheetLayoutBinding
    private var recyclerViewAdapter: AddLocationsAdapter = AddLocationsAdapter(null, this)

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private val debounceDelay: Long = 500
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LocationsBottomSheetLayoutBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        collectWhenCreated(viewModel.locationsState) {
            when(it) {
                LocationsBottomSheetViewModel.LocationsState.Error -> showError()
                is LocationsBottomSheetViewModel.LocationsState.Loading -> {
                    if(it.isFirstLoading) {
                        showGlobalLoading()
                    } else {
                        showSearchLoading()
                    }
                }
                is LocationsBottomSheetViewModel.LocationsState.UserLocationsSuccess -> showUserLocations(it.userLocationsResult)
                is LocationsBottomSheetViewModel.LocationsState.SearchLocationsSuccess -> showSearchedLocations(it.searchLocationsSuccess)
                is LocationsBottomSheetViewModel.LocationsState.IOError -> showErrorToast(it.isAdd, it.locationName)
                is LocationsBottomSheetViewModel.LocationsState.IOSuccess -> showSuccessToast(it.isAdd, it.locationName)
            }
        }
        setupView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = recyclerViewAdapter
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No-op
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No-op
            }

            override fun afterTextChanged(s: Editable?) {
                runnable?.let { handler.removeCallbacks(it) }

                runnable = Runnable {
                    if(s.toString().isNotEmpty() && s.toString().length > 1) viewModel.searchLocations(s.toString().trim())
                    else if(s.toString().isEmpty()) viewModel.loadData()
                }
                handler.postDelayed(runnable!!, debounceDelay)
            }
        })
    }

    private fun showUserLocations(weatherResult: WeatherResult) {
        recyclerViewAdapter.updateWeatherResult(weatherResult)
        showLocations(false)
    }

    private fun showSearchedLocations(weatherResult: WeatherResult) {
        showLocations(true)
        recyclerViewAdapter.updateWeatherResult(weatherResult)
    }

    private fun showError() {
        binding.errorView.setGone(false)
        binding.progressView.setGone(true)
        binding.locationsView.setGone(true)
    }

    private fun hideError() {
        showGlobalLoading()
    }

    private fun showSearchLoading() {
        binding.progressView.setGone(false)
    }

    private fun showLocations(isSearchLocations: Boolean) {
        binding.progressView.setGone(true)
        binding.locationsView.setGone(false)
        if(isSearchLocations) binding.saveButton.setGone(true) else binding.saveButton.setGone(false)
    }

    private fun showGlobalLoading() {
        binding.progressView.setGone(false)
        binding.locationsView.setGone(true)
        binding.errorView.setGone(true)
    }

    override fun onLocationClick(weatherResult: WeatherResultWrapper?) {
        if(weatherResult?.status == WeatherFetchStatus.SUCCESS_FROM_PERSISTENCE) {
            weatherResult.address?.globalIdLocal.let {
                viewModel.removeLocation(weatherResult)
            }
        } else {
            //This means it is a search location that should be added
            viewModel.addLocation(weatherResult)
        }
    }

    private fun showErrorToast(isAdd: Boolean, locationName: String?) {
        binding.progressView.setGone(true)
        if(isAdd) {
            val locationExtra = locationName?.let { "ao adicionar $it ás suas localizações.\nPor favor tente mais tarde" }
            Toast.makeText(context, "Ocorreu um erro $locationExtra", Toast.LENGTH_SHORT).show()
        } else {
            val locationExtra = locationName?.let { "ao remover $it das suas localizações.\nPor favor tente mais tarde" }
            Toast.makeText(context, "Ocorreu um erro $locationExtra", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSuccessToast(isAdd: Boolean, locationName: String?) {
        binding.progressView.setGone(true)
        if(isAdd) {
            Toast.makeText(context, "$locationName foi adicionado às suas localizações", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "$locationName foi removido suas localizações", Toast.LENGTH_SHORT).show()
        }

    }
}


interface OnLocationClickListener {
    fun onLocationClick(weatherResult: WeatherResultWrapper?)
}