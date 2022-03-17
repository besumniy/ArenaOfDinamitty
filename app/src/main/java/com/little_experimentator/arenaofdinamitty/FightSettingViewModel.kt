package com.little_experimentator.arenaofdinamitty

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FightSettingViewModel:ViewModel() {
    val choosenWarriorLive= MutableLiveData<String>()
}