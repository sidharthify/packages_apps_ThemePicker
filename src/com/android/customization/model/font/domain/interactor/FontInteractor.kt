package com.android.customization.model.font.domain.interactor

import com.android.customization.model.font.FontOption
import com.android.customization.model.font.data.repository.FontRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FontInteractor(private val repository: FontRepository) {

    val isAvailable: Boolean
        get() = repository.isAvailable

    fun getFontOptions(): Flow<List<FontOption>> = flow {
        emit(repository.getOptions())
    }

    suspend fun applyFont(option: FontOption): Boolean {
        return repository.applyOption(option)
    }

    fun getActiveOption(options: List<FontOption>): FontOption? {
        return repository.getActiveOption(options)
    }
}