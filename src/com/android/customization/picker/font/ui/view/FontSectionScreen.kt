package com.android.customization.picker.font.ui.view

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.customization.model.font.FontOption
import com.android.customization.picker.font.ui.viewmodel.FontPickerViewModel

@Composable
fun FontSectionScreen(
    viewModel: FontPickerViewModel,
    isDark: Boolean = isSystemInDarkTheme(),
    modifier: Modifier = Modifier
) {
    val options by viewModel.fontOptions.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()
    val context = LocalContext.current

    val colorScheme = remember(isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        } else {
            if (isDark) darkColorScheme() else lightColorScheme()
        }
    }

    MaterialTheme(colorScheme = colorScheme) {
        val containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        val contentColor = MaterialTheme.colorScheme.onSurface
        val outlineColor = MaterialTheme.colorScheme.outlineVariant

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Font",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 16.dp),
                color = contentColor
            )

            val previewFont = remember(selectedOption) {
                selectedOption?.let {
                    androidx.compose.ui.text.font.FontFamily(
                        androidx.compose.ui.text.font.Typeface(it.headlineFont)
                    )
                }
            }

            // Preview Box
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(28.dp),
                color = containerColor
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ABC • abc • 123",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontFamily = previewFont,
                            fontSize = 32.sp
                        ),
                        textAlign = TextAlign.Center,
                        color = contentColor
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Surface(
                        modifier = Modifier
                            .width(24.dp)
                            .height(2.dp),
                        color = outlineColor
                    ) {}

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Add your favorite fonts\nto every screen",
                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = previewFont),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Options List
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(options) { option ->
                    FontOptionItem(
                        option = option,
                        isSelected = selectedOption?.packageName == option.packageName,
                        onClick = { viewModel.selectFont(option) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FontOptionItem(
    option: FontOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) 
        MaterialTheme.colorScheme.primaryContainer 
    else 
        Color.Transparent

    val contentColor = if (isSelected) 
        MaterialTheme.colorScheme.onPrimaryContainer 
    else 
        MaterialTheme.colorScheme.onSurface

    val borderColor = if (isSelected) 
        MaterialTheme.colorScheme.primary 
    else 
        MaterialTheme.colorScheme.outlineVariant

    val borderWidth = if (isSelected) 2.dp else 1.dp

    val fontFamily = remember(option) {
        androidx.compose.ui.text.font.FontFamily(androidx.compose.ui.text.font.Typeface(option.headlineFont))
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clickable(onClick = onClick)
    ) {
        Surface(
            modifier = Modifier
                .size(80.dp)
                .border(borderWidth, borderColor, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            color = backgroundColor,
            contentColor = contentColor
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "Aa", 
                    style = MaterialTheme.typography.headlineMedium.copy(fontFamily = fontFamily)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = option.title, 
            style = MaterialTheme.typography.labelMedium, 
            color = MaterialTheme.colorScheme.onSurfaceVariant, 
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee()
        )
    }
}