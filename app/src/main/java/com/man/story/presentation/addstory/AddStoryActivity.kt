package com.man.story.presentation.addstory

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.man.story.core.base.BaseActivity
import com.man.story.core.extentions.ifTrue
import com.man.story.core.extentions.observeData
import com.man.story.core.utils.UIText
import com.man.story.core.utils.createCustomTempFile
import com.man.story.core.utils.rotateBitmap
import com.man.story.core.utils.uriToFile
import com.man.story.databinding.ActivityAddStoryBinding
import com.man.story.presentation.stories.StoriesActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

/**
 *
 * Created by Lukmanul Hakim on  04/10/22
 * devs.lukman@gmail.com
 */
@AndroidEntryPoint
class AddStoryActivity : BaseActivity<ActivityAddStoryBinding>(ActivityAddStoryBinding::inflate) {

    val viewModel: AddStoryViewModel by viewModels()
    private lateinit var filePhoto: File
    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    override fun ActivityAddStoryBinding.initialBinding() {

        lifecycleScope.launch {
            viewModel.isSubmitEnabled.collect {
                buttonAdd.isEnabled = it
            }
        }

        observeData(viewModel.isLoading) { state ->
            buttonAdd.isEnabled = !state
        }

        observeData(viewModel.isUploaded) {
            it.ifTrue {
                StoriesActivity::class.java.goWithFinish()
            }
        }
        observeData(viewModel.errorMessage) {
            showMessage(it)
        }

        btnCamera.setOnClickListener { checkPermissionCamera() }

        btnGalery.setOnClickListener { checkPermissionGallery() }

        tvAddLocation.setOnClickListener { getMyLocation() }

        edAddDescription.addTextChangedListener {
            viewModel.setDescription(it.toString())
        }

        buttonAdd.setOnClickListener {
            viewModel.uploadStory()
        }
    }

    // region take photo from camera

    private fun checkPermissionCamera() {
        checkPermission(Manifest.permission.CAMERA) { isGranted ->
            if (isGranted)
                startTakePhoto()
            else
                requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            isGranted.ifTrue {
                startTakePhoto()
            }
        }

    private lateinit var currentPhotoPath: String
    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(this).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                packageName,
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            filePhoto = File(currentPhotoPath)
            val result = rotateBitmap(
                BitmapFactory.decodeFile(filePhoto.path),
            )
            binding.ivPhoto.setImageBitmap(result)
            viewModel.setFile(filePhoto)
        }
    }

    // endregion

    // region take photo from gallery

    private fun checkPermissionGallery() {
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) { isGranted ->
            if (isGranted)
                startGallery()
            else
                requestStoragePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private val requestStoragePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            isGranted.ifTrue {
                startGallery()
            }
        }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            filePhoto = myFile
            binding.ivPhoto.setImageURI(selectedImg)
            viewModel.setFile(myFile)
        }
    }

    // endregion

    // region Location

    private fun getMyLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                viewModel.setLocation(LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0))
                binding.tvAddLocation.text = String.format("Coordinate %s %s", location?.latitude, location?.longitude)
            }
        } else {
            requestLocationPermission.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> getMyLocation()
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> getMyLocation()
            }
        }

    // endregion
}