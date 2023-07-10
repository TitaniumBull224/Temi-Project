package com.ibsystem.temiassistant

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ibsystem.temiassistant.databinding.ActivityMapBinding
import com.robotemi.sdk.Robot
import com.robotemi.sdk.map.MapDataModel
import kotlinx.coroutines.delay
import java.lang.Thread.sleep
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.roundToInt


class MapActivity : AppCompatActivity() {
    @Volatile
    private var bitmap: Bitmap? = null

    @Volatile
    private var mapDataModel: MapDataModel? = null

    private val singleThreadExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private lateinit var binding: ActivityMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ibBack.setOnClickListener { finish() }
        binding.textViewMapElements.setOnClickListener { refreshMap() }
        binding.textViewMapElements.movementMethod = ScrollingMovementMethod()
        refreshMap()
    }

    private fun refreshMap() {
        binding.progressBar.visibility = View.VISIBLE
        this.singleThreadExecutor.execute {
            mapDataModel = Robot.getInstance().getMapData() ?: return@execute
            val mapImage = mapDataModel!!.mapImage
            Log.i("Map-mapImage", mapDataModel!!.mapImage.typeId)

            bitmap = Bitmap.createBitmap(
                mapImage.data.map { Color.argb((it * 2.55).roundToInt(), 0, 0, 0) }.toIntArray(),
                mapImage.cols,
                mapImage.rows,
                Bitmap.Config.ARGB_8888
            )
            runOnUiThread {
                binding.progressBar.visibility = View.GONE
                binding.textViewMapElements.text = ""
                Log.i("Map-mapId", mapDataModel!!.mapId)
                binding.textViewMapElements.append("[map_id]: ${mapDataModel!!.mapId} \n")
                Log.i("Map-mapInfo", mapDataModel!!.mapInfo.toString())
                binding.textViewMapElements.append("[map_info]: ${mapDataModel!!.mapInfo} \n")
                Log.i("Map-greenPaths", mapDataModel!!.greenPaths.toString())
                binding.textViewMapElements.append("[map_green_path]: ${mapDataModel!!.greenPaths} \n")
                Log.i("Map-virtualWalls", mapDataModel!!.virtualWalls.toString())
                binding.textViewMapElements.append("[map_virtual_walls]: ${mapDataModel!!.virtualWalls} \n")
                Log.i("Map-locations", mapDataModel!!.locations.toString())
                binding.textViewMapElements.append("[map_locations]: ${mapDataModel!!.locations} \n")
                binding.textViewMapElements.append("[map_name]: ${mapDataModel!!.mapName} \n")
                binding.imageViewMap.setImageBitmap(bitmap)
            }
        }
    }

    override fun onDestroy() {
        bitmap?.recycle()
        singleThreadExecutor.shutdownNow()
        super.onDestroy()
    }
}