package com.example.poketrainer.utils

import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ShowBars(isRequiredToShowBars: Boolean) {

    rememberSystemUiController().apply {
        this.isSystemBarsVisible = isRequiredToShowBars
        this.isStatusBarVisible = isRequiredToShowBars
        this.isNavigationBarVisible = isRequiredToShowBars
    }
}