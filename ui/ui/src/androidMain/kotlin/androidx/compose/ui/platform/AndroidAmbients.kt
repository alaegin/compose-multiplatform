/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.compose.ui.platform

import android.content.Context
import android.content.res.Configuration
import android.view.View
import androidx.compose.animation.core.InternalAnimationApi
import androidx.compose.animation.core.rootAnimationClockFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.AmbientSaveableStateRegistry
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner

/**
 * The Android [Configuration]. The [Configuration] is useful for determining how to organize the
 * UI.
 */
val AmbientConfiguration = ambientOf<Configuration>(
    @OptIn(ExperimentalComposeApi::class)
    neverEqualPolicy()
)

/**
 * Provides a [Context] that can be used by Android applications.
 */
val AmbientContext = staticAmbientOf<Context>()

/**
 * The ambient containing the current [LifecycleOwner].
 */
val AmbientLifecycleOwner = staticAmbientOf<LifecycleOwner>()

/**
 * The ambient containing the current [SavedStateRegistryOwner].
 */
val AmbientSavedStateRegistryOwner = staticAmbientOf<SavedStateRegistryOwner>()

/**
 * The ambient containing the current Compose [View].
 */
val AmbientView = staticAmbientOf<View>()

/**
 * The ambient containing the current [ViewModelStoreOwner].
 */
val AmbientViewModelStoreOwner = staticAmbientOf<ViewModelStoreOwner>()

@Composable
@OptIn(ExperimentalComposeUiApi::class, InternalAnimationApi::class)
internal fun ProvideAndroidAmbients(owner: AndroidComposeView, content: @Composable () -> Unit) {
    val view = owner
    val context = view.context
    val scope = rememberCoroutineScope()
    val rootAnimationClock = remember(scope) {
        rootAnimationClockFactory(scope)
    }

    var configuration by remember {
        mutableStateOf(
            context.resources.configuration,
            @OptIn(ExperimentalComposeApi::class)
            neverEqualPolicy()
        )
    }

    owner.configurationChangeObserver = { configuration = it }

    val uriHandler = remember { AndroidUriHandler(context) }
    val viewTreeOwners = owner.viewTreeOwners ?: throw IllegalStateException(
        "Called when the ViewTreeOwnersAvailability is not yet in Available state"
    )

    val saveableStateRegistry = remember {
        DisposableSaveableStateRegistry(view, viewTreeOwners.savedStateRegistryOwner)
    }
    DisposableEffect(Unit) {
        onDispose {
            saveableStateRegistry.dispose()
        }
    }

    Providers(
        AmbientConfiguration provides configuration,
        AmbientContext provides context,
        AmbientLifecycleOwner provides viewTreeOwners.lifecycleOwner,
        AmbientSavedStateRegistryOwner provides viewTreeOwners.savedStateRegistryOwner,
        AmbientSaveableStateRegistry provides saveableStateRegistry,
        AmbientView provides owner.view,
        AmbientViewModelStoreOwner provides viewTreeOwners.viewModelStoreOwner
    ) {
        ProvideCommonAmbients(
            owner = owner,
            animationClock = rootAnimationClock,
            uriHandler = uriHandler,
            content = content
        )
    }
}
