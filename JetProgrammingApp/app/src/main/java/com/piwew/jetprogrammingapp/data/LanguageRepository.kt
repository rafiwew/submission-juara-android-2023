package com.piwew.jetprogrammingapp.data

import com.piwew.jetprogrammingapp.model.LanguageItem
import com.piwew.jetprogrammingapp.model.LanguagesData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LanguageRepository {

    private val languageItem = mutableListOf<LanguageItem>()
    private val favoriteLanguages = mutableListOf<String>()

    init {
        if (languageItem.isEmpty()) {
            LanguagesData.language.forEach {
                languageItem.add(LanguageItem(it, 0))
            }
        }
    }

    fun getSortedAndGroupedLanguage(): Flow<Map<Char, List<LanguageItem>>> {
        return flow {
            val sortedLanguages = languageItem.sortedBy { it.item.name }
            val groupedLanguages = sortedLanguages.groupBy { it.item.name[0] }
            emit(groupedLanguages)
        }
    }

    fun getLanguageItemById(languageId: String): LanguageItem {
        return languageItem.first {
            it.item.id == languageId
        }
    }

    fun searchLanguages(query: String): Flow<List<LanguageItem>> {
        return flow {
            val filteredLanguages = languageItem.filter {
                it.item.name.contains(query, ignoreCase = true)
            }
            emit(filteredLanguages)
        }
    }

    fun getFavoriteLanguages(): Flow<List<LanguageItem>> {
        return flow {
            val favoriteLanguageItems = languageItem.filter { it.item.id in favoriteLanguages }
            emit(favoriteLanguageItems)
        }
    }

    fun addToFavorites(languageId: String) {
        if (!favoriteLanguages.contains(languageId)) {
            favoriteLanguages.add(languageId)
        }
    }

    fun removeFromFavorites(languageId: String) {
        favoriteLanguages.remove(languageId)
    }

    fun isFavorite(languageId: String): Boolean {
        return favoriteLanguages.contains(languageId)
    }

    companion object {
        @Volatile
        private var instance: LanguageRepository? = null

        fun getInstance(): LanguageRepository = instance ?: synchronized(this) {
            LanguageRepository().apply {
                instance = this
            }
        }
    }

}