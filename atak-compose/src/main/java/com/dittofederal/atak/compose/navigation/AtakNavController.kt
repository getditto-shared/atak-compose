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
package com.dittofederal.atak.compose.navigation

import androidx.compose.runtime.Composable
import gov.tak.api.ui.IHostUIService
import com.dittofederal.atak.compose.ComposeContext
import com.dittofederal.atak.compose.pane.ComposePane
import com.dittofederal.atak.compose.pane.showComposePane

interface AtakNavController {
    fun navigate(destination: String)
    fun navigateAndCloseAll(destination: String)
    fun registerDestination(
        destination: String,
        widthRatio: Float,
        heightRatio: Float,
        composable: @Composable () -> Unit
    )

    fun closeAllDestinations()
    fun close(destination: String)
}

class AtakNavControllerImpl(
    private val uiService: IHostUIService,
    private val composeContext: ComposeContext
) : AtakNavController {
    private val composePanes = mutableMapOf<String, ComposePane>()

    override fun navigate(destination: String) {
        val composePane = composePanes[destination] ?: return
        if (uiService.isPaneVisible(composePane.pane)) return

        uiService.showComposePane(composePane)
    }

    override fun navigateAndCloseAll(destination: String) {
        navigate(destination)

        composePanes
            .filter { it.key != destination }
            .values.forEach { composePane ->
                uiService.closePane(composePane.pane)
            }
    }

    override fun registerDestination(
        destination: String,
        widthRatio: Float,
        heightRatio: Float,
        composable: @Composable () -> Unit
    ) {
        if (composePanes.containsKey(destination)) return

        val pane = ComposePane(
            composeContext = composeContext,
            composable = { composable() },
            shouldRetain = true,
            widthRatio = widthRatio,
            heightRatio = heightRatio
        )

        composePanes[destination] = pane
    }

    override fun closeAllDestinations() {
        composePanes.values.forEach { composePane ->
            uiService.closePane(composePane.pane)
        }
    }

    override fun close(destination: String) {
        val composePane = composePanes[destination] ?: return

        uiService.closePane(composePane.pane)
    }
}
