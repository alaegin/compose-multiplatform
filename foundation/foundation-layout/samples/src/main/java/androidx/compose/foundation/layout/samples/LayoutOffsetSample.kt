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

package androidx.compose.foundation.layout.samples

import androidx.annotation.Sampled
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.absoluteOffsetPx
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.offsetPx
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.tapGestureFilter
import androidx.compose.ui.unit.dp

@Sampled
@Composable
fun LayoutOffsetModifier() {
    // This text will be offset (10.dp, 20.dp) from the center of the available space. In the
    // right-to-left context, the offset will be (-10.dp, 20.dp).
    Text(
        "Layout offset modifier sample",
        Modifier.fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .offset(10.dp, 20.dp)
    )
}

@Sampled
@Composable
fun LayoutAbsoluteOffsetModifier() {
    // This text will be offset (10.dp, 20.dp) from the center of the available space.
    Text(
        "Layout offset modifier sample",
        Modifier.fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .absoluteOffset(10.dp, 20.dp)
    )
}

@Sampled
@Composable
fun LayoutOffsetPxModifier() {
    // This text will be offset in steps of 10.dp from the top left of the available space in
    // left-to-right context, and from top right in right-to-left context.
    val offset = remember { mutableStateOf(0f) }
    Text(
        "Layout offset modifier sample",
        Modifier
            .tapGestureFilter { offset.value += 10f }
            .offsetPx(offset, offset)
    )
}

@Sampled
@Composable
fun LayoutAbsoluteOffsetPxModifier() {
    // This text will be offset in steps of 10.dp from the top left of the available space.
    val offset = remember { mutableStateOf(0f) }
    Text(
        "Layout offset modifier sample",
        Modifier
            .tapGestureFilter { offset.value += 10f }
            .absoluteOffsetPx(offset, offset)
    )
}
