package com.piwew.jetprogrammingapp.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.piwew.jetprogrammingapp.data.LanguageRepository
import com.piwew.jetprogrammingapp.ui.screen.detail.DetailViewModel
import com.piwew.jetprogrammingapp.ui.screen.favorite.FavoriteViewModel
import com.piwew.jetprogrammingapp.ui.screen.home.HomeViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val repository: LanguageRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}