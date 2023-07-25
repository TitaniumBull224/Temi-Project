package com.ibsystem.temiassistant.presentation.map_ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotemi.sdk.Robot
import com.robotemi.sdk.map.MapDataModel
import com.robotemi.sdk.navigation.model.Position
import kotlinx.coroutines.launch


class MapScreenViewModel: ViewModel() {
    private val mRobot = Robot.getInstance()

    var mapDataModel by mutableStateOf<MapDataModel?>(null)
    private var currentPosition by mutableStateOf<Position?>(null)



    fun loadMapData() {
        viewModelScope.launch {
            mapDataModel = mRobot.getMapData()
        }
    }
    fun setPosition(position: Position) {
        currentPosition = position
    }
    fun getPosition():Position? {
        return currentPosition
    }

}