package com.android.customization.model.font.data.repository

import com.android.customization.model.CustomizationManager
import com.android.customization.model.font.FontManager
import com.android.customization.model.font.FontOption
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FontRepository(private val fontManager: FontManager) {

    val isAvailable: Boolean
        get() = fontManager.isAvailable

    suspend fun getOptions(): List<FontOption> = suspendCancellableCoroutine { continuation ->
        fontManager.fetchOptions(object : CustomizationManager.OptionsFetchedListener<FontOption> {
            override fun onOptionsLoaded(options: List<FontOption>?) {
                continuation.resume(options ?: emptyList())
            }

            override fun onError(throwable: Throwable?) {
                continuation.resume(emptyList())
            }
        }, true)
    }

    suspend fun applyOption(option: FontOption): Boolean = suspendCancellableCoroutine { continuation ->
        fontManager.apply(option, object : CustomizationManager.Callback {
            override fun onSuccess() {
                continuation.resume(true)
            }

            override fun onError(throwable: Throwable?) {
                continuation.resume(false)
            }
        })
    }

    fun getActiveOption(options: List<FontOption>): FontOption? {
        return options.find { fontManager.isActive(it) }
    }
}