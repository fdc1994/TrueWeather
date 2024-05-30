package com.example.trueweather.main.addlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.trueweather.R
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LocationsBottomSheet: BottomSheetDialogFragment() {

    private val viewModel: LocationsBottomSheetViewModel by viewModels<LocationsBottomSheetViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.locations_bottom_sheet_layout, container, false)
    }
}