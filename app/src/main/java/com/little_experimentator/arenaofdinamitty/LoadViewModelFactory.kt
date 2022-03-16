package com.little_experimentator.arenaofdinamitty

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.little_experimentator.arenaofdinamitty.usecases.Load

class LoadViewModelFactory(activity:Activity):ViewModelProvider.Factory {
    var activity=activity

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoadViewModel(load= Load(activity=activity)) as T
    }
}