/*
 * MIT License
 *
 * Copyright (c) 2021 Bryan de Ridder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package nl.bryanderidder.themedtogglebuttongroup

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.ImageView
import androidx.core.content.ContextCompat


/**
 * Color utility functions/properties
 * @author Bryan de Ridder
 */

internal val lightGray: Int = Color.parseColor("#ebebeb")

internal val darkGray: Int = Color.parseColor("#5e5e5e")

internal val denim: Int = Color.parseColor("#5e6fed")

internal fun ImageView.setTintColor(
    color: Int,
    blendMode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN
) = this.setColorFilter(color, blendMode)

internal fun Context.color(id: Int): Int = ContextCompat.getColor(this, id)