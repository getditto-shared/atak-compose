/*
    MIT License

    Copyright (c) 2023 Ditto

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
*/
package com.dittofederal.atak.compose.pane

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import gov.tak.api.ui.Pane
import gov.tak.api.ui.PaneBuilder

class ComposePane(
    private val composeContext: Context,
    private val composable: @Composable () -> Unit,
    widthRatio: Float,
    heightRatio: Float,
    shouldRetain: Boolean
) {
    val view: FrameLayout by lazy { FrameLayout(composeContext) }

    val pane: Pane by lazy {
        PaneBuilder(view)
            .setMetaValue(Pane.RELATIVE_LOCATION, Pane.Location.Default)
            .setMetaValue(Pane.PREFERRED_WIDTH_RATIO, widthRatio)
            .setMetaValue(Pane.PREFERRED_HEIGHT_RATIO, heightRatio)
            .setMetaValue(Pane.RETAIN, shouldRetain)
            .build()
    }

    val composeViewFactory: () -> View by lazy {
        {
            ComposeView(context = composeContext).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
                setContent {
                    composable()
                }
            }
        }
    }
}
