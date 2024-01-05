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
package com.dittofederal.atak.compose

import android.content.Context
import android.content.ContextWrapper
import androidx.compose.ui.platform.ComposeView

/**
 * This class is a workaround for the fact that [ComposeView] needs the [pluginContext] to load
 * resources, but the [hostContext]'s application context.
 *
 * credits: https://git.tak.gov/samples/plugintemplate-compose/
 */
class ComposeContext(
    private val hostContext: Context,
    private val pluginContext: Context
) : ContextWrapper(pluginContext) {
    override fun getApplicationContext(): Context {
        return hostContext.applicationContext
    }
}
