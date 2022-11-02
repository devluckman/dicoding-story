package com.man.story.core.utils.popup

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.PopupWindowCompat
import com.google.android.gms.maps.GoogleMap
import com.man.story.R

/**
 *
 * Created by Lukmanul Hakim on  11/10/22
 * devs.lukman@gmail.com
 */
object PopUpBuilder {

    fun explorer(context: Context, anchor: View, callback: ExplorerCallback) {
        val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.pop_up_window_options, null)

        val buttonMaps = popupView.findViewById<LinearLayoutCompat>(R.id.btn_maps)
        val buttonSettings = popupView.findViewById<LinearLayoutCompat>(R.id.btn_settings)
        val buttonLogout = popupView.findViewById<LinearLayoutCompat>(R.id.btn_logout)

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true

        val popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.elevation = 10f

        buttonMaps.setOnClickListener { callback.onMaps() }

        buttonSettings.setOnClickListener { callback.onLanguage() }

        buttonLogout.setOnClickListener { callback.onLogout() }

        PopupWindowCompat.showAsDropDown(popupWindow, anchor, 0, 0, Gravity.CENTER)
        PopupWindowCompat.setWindowLayoutType(
            popupWindow,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
    }

    interface ExplorerCallback {
        fun onMaps()
        fun onLanguage()
        fun onLogout()
    }

    fun styleMaps(context: Context, anchor: View, style: (Int) -> Unit) {
        val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.pop_up_window_style_maps, null)

        val buttonNormal = popupView.findViewById<LinearLayoutCompat>(R.id.btn_normal_type)
        val buttonSatellite = popupView.findViewById<LinearLayoutCompat>(R.id.btn_satellite_type)
        val buttonTerrain = popupView.findViewById<LinearLayoutCompat>(R.id.btn_terrain_type)
        val buttonHybrid = popupView.findViewById<LinearLayoutCompat>(R.id.btn_hybrid_type)

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true

        val popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.elevation = 10f

        buttonNormal.setOnClickListener {
            style.invoke(GoogleMap.MAP_TYPE_NORMAL)
            popupWindow.dismiss()
        }

        buttonTerrain.setOnClickListener {
            style.invoke(GoogleMap.MAP_TYPE_TERRAIN)
            popupWindow.dismiss()
        }

        buttonHybrid.setOnClickListener {
            style.invoke(GoogleMap.MAP_TYPE_HYBRID)
            popupWindow.dismiss()
        }

        buttonSatellite.setOnClickListener {
            style.invoke(GoogleMap.MAP_TYPE_SATELLITE)
            popupWindow.dismiss()
        }

        PopupWindowCompat.showAsDropDown(popupWindow, anchor, 0, 0, Gravity.CENTER)
        PopupWindowCompat.setWindowLayoutType(
            popupWindow,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
    }
}