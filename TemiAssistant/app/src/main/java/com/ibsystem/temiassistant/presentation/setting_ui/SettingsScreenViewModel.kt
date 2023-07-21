package com.ibsystem.temiassistant.presentation.setting_ui

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


object SettingVMObj {
    val SettingVM : SettingsScreenViewModel = SettingsScreenViewModel()
}
class SettingsScreenViewModel : ViewModel() {

    private val _isSpeakerOn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isSpeakerOn = _isSpeakerOn.asStateFlow()

    private val _textPreference: MutableStateFlow<String> = MutableStateFlow("")
    var textPreference = _textPreference.asStateFlow()

    private val _intPreference: MutableStateFlow<Int> = MutableStateFlow(0)
    var intPreference = _intPreference.asStateFlow()


    fun toggleSwitch(){
        _isSpeakerOn.value = _isSpeakerOn.value.not()
        // here is place for permanent storage handling - switch
        Log.i("Toggle",_isSpeakerOn.value.toString())
    }

    fun saveText(finalText: String) {
        _textPreference.value = finalText
        // place to store text
    }

    // just checking, if it is not empty - but you can check anything
    fun checkTextInput(text: String) = text.isNotEmpty()

    companion object {
        const val TAG = "SettingsViewModel"
    }

}