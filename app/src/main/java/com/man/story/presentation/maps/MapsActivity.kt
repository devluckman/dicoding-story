package com.man.story.presentation.maps

import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.man.story.R
import com.man.story.core.base.BaseActivity
import com.man.story.core.extentions.ifTrue
import com.man.story.core.extentions.observeData
import com.man.story.core.utils.popup.PopUpBuilder
import com.man.story.databinding.ActivityMapsBinding
import com.man.story.domain.model.story.StoryModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : BaseActivity<ActivityMapsBinding>(ActivityMapsBinding::inflate),
    OnMapReadyCallback {

    private val viewModel: MapsViewModel by viewModels()
    private lateinit var mMap: GoogleMap

    override fun ActivityMapsBinding.initialBinding() {

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@MapsActivity)

        observeData(viewModel.isLoading) { state ->
            binding.progressBarMap.isVisible = state
        }

        observeData(viewModel.resultStories) { data ->
            addStoriesMarkers(data)
        }

        observeData(viewModel.errorMessage) { uiText ->
            showMessage(uiText)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getMyLocation()

        val dicodingSpace = LatLng(-6.8957643, 107.6338462)
        mMap.addMarker(
            MarkerOptions()
                .position(dicodingSpace)
                .title("Dicoding Space")
                .snippet("Batik Kumeli No.50")
        )
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 15f))

        viewModel.getStoriesWithLocation()
        onActionListener()
    }

    private fun getMyLocation() {
        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) { isGranted ->
            if (isGranted)
                mMap.isMyLocationEnabled = true
            else
                requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            isGranted.ifTrue {
                getMyLocation()
            }
        }

    private fun addStoriesMarkers(storiesList: List<StoryModel>?) {
        storiesList?.let {
            for (story in it) {
                val latLng = LatLng(
                    story.latitude ?: continue,
                    story.longitude ?: continue
                )
                val marker = MarkerOptions()
                    .position(latLng)
                    .title(story.name)
                    .snippet(story.description)
                mMap.addMarker(marker)
            }
        }
    }

    private fun onActionListener() {
        binding.actionStyleMaps.setOnClickListener {
            PopUpBuilder.styleMaps(this, it) { style ->
                mMap.mapType = style
            }
        }
    }
}