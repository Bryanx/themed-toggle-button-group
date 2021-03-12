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

import android.content.res.Resources
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout

/**
 * Contains all extension methods.
 * @author Bryan de Ridder
 */

internal fun ThemedButton.setMargin(leftMargin: Int? = null, topMargin: Int? = null,
                   rightMargin: Int? = null, bottomMargin: Int? = null) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(
        leftMargin ?: params.leftMargin,
        topMargin ?: params.topMargin,
        rightMargin ?: params.rightMargin,
        bottomMargin ?: params.bottomMargin)
    layoutParams = params
}

/**
 * Makes the button scale down slightly.
 */
fun ThemedButton.bounceDown() {
    val animScaleDown = ScaleAnimation(1f, 0.9f, 1f, 0.9f, (width/2).toFloat(), (height/2).toFloat())
    animScaleDown.duration = 200
    animScaleDown.fillAfter = true
    animScaleDown.interpolator = DecelerateInterpolator()
    startAnimation(animScaleDown)
}

/**
 * Adds a touch listener that also triggers a cancel event if
 * action up is triggered outside the touched view
 */
internal fun View.setOnBoundedTouchListener(
    callback: (
        isActionDown: Boolean,
        isActionUp: Boolean,
        isActionCancel: Boolean,
        event: MotionEvent?
    ) -> Unit
) {
    var rect: Rect? = null
    var cancelled = false
    setOnTouchListener { v: View?, event: MotionEvent? ->
        performClick()
        val viewLeft = v?.left ?: 0
        val viewTop = v?.top ?: 0
        val viewright = v?.right ?: 0
        val viewBottom = v?.bottom ?: 0
        val eventX = event?.x?.toInt() ?: 0
        val eventY = event?.y?.toInt() ?: 0
        val action = event?.action
        when {
            action == MotionEvent.ACTION_DOWN -> {
                cancelled = false
                callback.invoke(true, false, false, event)
                rect = Rect(viewLeft, viewTop, viewright, viewBottom)
            }
            action == MotionEvent.ACTION_UP && !cancelled -> {
                if (rect?.contains(viewLeft + eventX, viewTop + eventY) == false) {
                    cancelled = true
                    callback.invoke(false, false, true, event)
                } else {
                    callback.invoke(false, true, false, event)
                }
            }
            action == MotionEvent.ACTION_MOVE -> {
                if (rect?.contains(viewLeft + eventX, viewTop + eventY) == false && !cancelled) {
                    cancelled = true
                    callback.invoke(false, false, true, event)
                }
            }
            action == MotionEvent.ACTION_CANCEL && !cancelled -> {
                cancelled = true
                callback.invoke(false, false, true, event)
            }
        }
        true
    }
}

/**
 * Makes the button scale up slightly (with overshoot).
 */
fun ThemedButton.bounceUp() {
    val animScaleUp = ScaleAnimation(0.9f, 1f, 0.9f, 1f, (width/2).toFloat(), (height/2).toFloat())
    animScaleUp.duration = 200
    animScaleUp.startOffset = 100
    animScaleUp.interpolator = OvershootInterpolator(3f)
    startAnimation(animScaleUp)
}

/** Utility function for setting the padding */
internal fun View.setViewPadding(
    left: Float? = null, top: Float? = null,
    right: Float? = null, bottom: Float? = null,
    horizontal: Float? = null, vertical: Float? = null,
    all: Float? = null
) {
    if (listOfNotNull(left, top, right, bottom, horizontal, vertical, all).any { it < 0f }) return
    all?.let { setPadding(it.toInt(), it.toInt(), it.toInt(), it.toInt()) }
    horizontal?.let { setPadding(it.toInt(), paddingTop, it.toInt(), paddingBottom) }
    vertical?.let { setPadding(paddingLeft, it.toInt(), paddingRight, it.toInt()) }
    setPadding(
        left?.toInt() ?: paddingLeft,
        top?.toInt() ?: paddingTop,
        right?.toInt() ?: paddingRight,
        bottom?.toInt() ?: paddingBottom
    )
}

internal var View.layoutGravity
    get() = (layoutParams as FrameLayout.LayoutParams).gravity
    set(value) {
        layoutParams = FrameLayout.LayoutParams(
            layoutParams.width,
            layoutParams.height,
            value
        )
    }

internal fun <K, V> MutableMap<K, V>.addIfAbsent(key: K, value: V): V? {
    var v: V? = get(key)
    if (v == null) v = put(key, value)
    return v
}

internal fun <T> MutableList<T>.enqueue(item: T) = if (!this.contains(item)) this.add(this.count(), item) else null

internal fun <T> MutableList<T>.dequeue(): T? = if (this.count() > 0) this.removeAt(0) else null

internal val View.name: String get() =
    if (this.id == -0x1) "no-id"
    else resources.getResourceEntryName(id) ?: "error-getting-name"

internal val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

internal val Int.pxf: Float get() = (this * Resources.getSystem().displayMetrics.density)

internal val Float.px: Float get() = this * Resources.getSystem().displayMetrics.density

internal val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

internal val Float.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

internal val ThemedButton.centerX: Float get() = (width / 2).toFloat()

internal val ThemedButton.centerY: Float get() = (height / 2).toFloat()
