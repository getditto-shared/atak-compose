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

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

/**
 * An alias to workaround issues resolving `viewModelScope`
 */
open class AtakViewModel : ViewModel() {
    /**
     * [CoroutineScope] tied to this [ViewModel].
     * This scope will be canceled when ViewModel will be cleared, i.e [ViewModel.onCleared] is called
     *
     * This scope is bound to
     * [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate]
     */
    val viewModelScope: CoroutineScope = CloseableCoroutineScope(
        context = SupervisorJob() + Dispatchers.Main.immediate
    )

    init {
        this.addCloseable(viewModelScope as Closeable)
    }
}

@Composable
inline fun <reified T : AtakViewModel> atakViewModel(
    key: String? = null,
    crossinline viewModelInstanceCreator: () -> T
): T {
    val vm: T = viewModel(
        modelClass = T::class.java,
        key = key,
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return viewModelInstanceCreator() as T
            }
        }
    )
    val viewModelStore = LocalViewModelStoreOwner.current?.viewModelStore
    val view = LocalView.current

    DisposableEffect(
        key1 = view,
        effect = {
            val listener = object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(p0: View) {
                }

                override fun onViewDetachedFromWindow(p0: View) {
                    viewModelStore?.clear()
                }
            }

            view.addOnAttachStateChangeListener(listener)

            onDispose {
                view.removeOnAttachStateChangeListener(listener)
            }
        }
    )

    return vm
}

internal class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }
}