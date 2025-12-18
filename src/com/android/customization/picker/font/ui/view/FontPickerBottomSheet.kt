package com.android.customization.picker.font.ui.view

import android.app.Dialog
import android.content.res.Configuration
import android.graphics.Color as AndroidColor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.android.customization.picker.font.ui.viewmodel.FontPickerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FontPickerBottomSheet(
    private val viewModelFactory: FontPickerViewModel.Factory
) : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.window?.let { window ->
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.navigationBarColor = AndroidColor.TRANSPARENT
        }

        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                    isFitToContents = true 
                }
            }
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
            setContent {
                val context = LocalContext.current

                val uiMode = context.applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                val isSystemDark = uiMode == Configuration.UI_MODE_NIGHT_YES

                val colorScheme = if (isSystemDark) {
                    dynamicDarkColorScheme(context)
                } else {
                    dynamicLightColorScheme(context)
                }

                val bgColor = if (isSystemDark) Color(0xFF1C1C1C) else Color.White

                MaterialTheme(colorScheme = colorScheme) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = bgColor 
                    ) {
                        val viewModel = ViewModelProvider(
                            this@FontPickerBottomSheet,
                            viewModelFactory
                        )[FontPickerViewModel::class.java]

                        LaunchedEffect(Unit) {
                            viewModel.applyEvent.collect {
                                dismiss()
                                requireActivity().recreate()
                            }
                        }

                        FontSectionScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}