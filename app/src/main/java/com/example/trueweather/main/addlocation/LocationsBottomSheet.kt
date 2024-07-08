package com.example.trueweather.main.addlocation

import android.content.DialogInterface
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
import com.example.trueweather.utils.NetworkConnectivityManager
import com.example.trueweather.utils.setGone
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationsBottomSheet: BottomSheetDialogFragment(), OnLocationClickListener{

    var onDismissListener: (() -> Unit)? = null

    private val viewModel: LocationsBottomSheetViewModel by viewModels()
    private lateinit var binding: LocationsBottomSheetLayoutBinding
    private var recyclerViewAdapter: AddLocationsAdapter = AddLocationsAdapter(null, this)

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private val debounceDelay: Long = 500

    @Inject
    lateinit var networkConnectivityManager: NetworkConnectivityManager
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
        if(networkConnectivityManager.hasInternetConnection()) {
            binding.searchBar.setGone(false)
            binding.searchBar.isEnabled = true
            binding.offlineDisclaimer.setGone(true)
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
        } else {
            binding.searchBar.isEnabled = false
            binding.searchBar.setText("")
            binding.searchBar.setGone(true)
            binding.offlineDisclaimer.setGone(false)
        }

    }

    override fun onResume() {
        setupView()
        super.onResume()
    }

    private fun showUserLocations(weatherResult: WeatherResult) {
        recyclerViewAdapter.updateWeatherResult(weatherResult)
        showLocations()
    }

    private fun showSearchedLocations(weatherResult: WeatherResult) {
        showLocations()
        recyclerViewAdapter.updateWeatherResult(weatherResult)
    }

    private fun showError() {
        binding.errorView.setGone(false)
        binding.progressView.setGone(true)
        binding.locationsView.setGone(true)
    }

    private fun showSearchLoading() {
        binding.progressView.setGone(false)
    }

    private fun showLocations() {
        binding.progressView.setGone(true)
        binding.locationsView.setGone(false)
    }

    private fun showGlobalLoading() {
        binding.progressView.setGone(false)
        binding.locationsView.setGone(true)
        binding.errorView.setGone(true)
    }

    override fun onLocationClick(weatherResult: WeatherResultWrapper?) {
        when(weatherResult?.status) {
            WeatherFetchStatus.SUCCESS_FROM_PERSISTENCE,
            WeatherFetchStatus.NETWORK_ERROR,
            WeatherFetchStatus.OTHER_ERROR,
            WeatherFetchStatus.NO_INTERNET_ERROR -> {
                weatherResult.address?.globalIdLocal.let {
                    viewModel.removeLocation(weatherResult)
                }
            }
            else -> viewModel.addLocation(weatherResult)

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

    override fun onDismiss(dialog: DialogInterface) {
        if(viewModel.hasChange()) onDismissListener?.invoke()
        super.onDismiss(dialog)
    }
}


interface OnLocationClickListener {
    fun onLocationClick(weatherResult: WeatherResultWrapper?)
}