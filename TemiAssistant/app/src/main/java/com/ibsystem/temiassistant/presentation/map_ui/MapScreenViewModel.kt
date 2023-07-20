package com.ibsystem.temiassistant.presentation.map_ui

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibsystem.temiassistant.R
import com.robotemi.sdk.Robot
import com.robotemi.sdk.map.MapDataModel
import com.robotemi.sdk.map.MapModel
import com.robotemi.sdk.navigation.model.Position
import com.robotemi.sdk.permission.Permission
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MapScreenViewModel(private val mRobot: Robot): ViewModel() {

    var mapDataModel by mutableStateOf<MapDataModel?>(null)

    fun loadMapData() {
        viewModelScope.launch {
            mapDataModel = mRobot.getMapData()
        }
    }


//    private var tag: String = "MapScreenViewModel"
//    @Volatile
//    private var bitmap: Bitmap? = null
//    @Volatile
//    private var mapDataModel: MapDataModel? = null
//
//    var mapList: List<MapModel> = ArrayList()
//    private fun getMapList() {
//        mapList = mRobot.getMapList()
//    }
//
//    @Composable
//    fun loadMap(
//        reposeRequired: Boolean,
//        position: Position?,
//        offline: Boolean = false,
//        withoutUI: Boolean = false
//    ) {
//        if (mapList.isEmpty()) {
//            getMapList()
//        }
//        if (mRobot.checkSelfPermission(Permission.MAP) != Permission.GRANTED) {
//            return
//        }
//        var showDialog by remember { mutableStateOf(true) }
//        if (showDialog) {
//            MapListDialog(
//                mapList = mapList,
//                onMapSelected = { map ->
//                    val requestId =
//                        mRobot.loadMap(
//                            map.id,
//                            reposeRequired,
//                            position,
//                            offline = offline,
//                            withoutUI = withoutUI
//                        )
//                    Log.i(tag, "Loading map: $map, request id $requestId, reposeRequired $reposeRequired, position $position, offline $offline, withoutUI $withoutUI")
//                    showDialog = false
//                },
//                onDismissRequest = { showDialog = false }
//            )
//        }
//    }
//    private fun refreshMap() {
//        mapDataModel = mRobot.getMapData()
//        val mapImage = mapDataModel!!.mapImage
//        Log.i("Map-mapImage", mapDataModel!!.mapImage.typeId)
//
//        bitmap = Bitmap.createBitmap(
//            mapImage.data.map { Color.argb((it * 2.55).roundToInt(), 0, 0, 0) }.toIntArray(),
//            mapImage.cols,
//            mapImage.rows,
//            Bitmap.Config.ARGB_8888
//        )
//    }

}