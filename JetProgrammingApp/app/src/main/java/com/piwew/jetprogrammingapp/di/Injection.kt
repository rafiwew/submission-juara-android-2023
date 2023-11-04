package com.piwew.jetprogrammingapp.di

import com.piwew.jetprogrammingapp.data.LanguageRepository

object Injection {
    fun provideRepository(): LanguageRepository {
        return LanguageRepository.getInstance()
    }
}