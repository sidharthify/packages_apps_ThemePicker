package com.android.customization.model.font

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelStoreOwner
import com.android.customization.picker.font.FontSectionView
import com.android.customization.picker.font.ui.view.FontPickerBottomSheet
import com.android.customization.picker.font.ui.viewmodel.FontPickerViewModel
import com.android.themepicker.R
import com.android.wallpaper.model.CustomizationSectionController
import com.google.android.flexbox.FlexboxLayout

class FontSectionController(
    private val viewModelFactory: FontPickerViewModel.Factory,
    private val lifecycleOwner: ViewModelStoreOwner
) : CustomizationSectionController<FontSectionView> {

    override fun isAvailable(context: Context): Boolean = true

    override fun createView(context: Context): FontSectionView {
        val view = LayoutInflater.from(context).inflate(
            R.layout.font_section_view,
            null
        ) as FontSectionView

        val lp = FlexboxLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        lp.flexBasisPercent = 1.0f
        view.layoutParams = lp

        val descriptionView = view.findViewById<TextView>(R.id.font_section_description)
        val viewModel = viewModelFactory.create(FontPickerViewModel::class.java)
        
        val currentOptions = viewModel.fontOptions.value
        if (currentOptions.isNotEmpty()) {
             val active = viewModel.selectedOption.value ?: currentOptions[0]
             descriptionView.text = active.title
        }

        view.setOnClickListener {
            if (context is FragmentActivity) {
                val sheet = FontPickerBottomSheet(viewModelFactory)
                sheet.show(context.supportFragmentManager, "FontPicker")
            }
        }

        return view
    }
}