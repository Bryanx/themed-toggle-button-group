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

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.RectEvaluator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart

/**
 * List of animation util functions
 * @author Bryan de Ridder
 */

internal object AnimationUtils {

    const val clipBounds = "clipBounds"

    @SuppressLint("NewApi")
    private fun createWindowAnimator(target: View, reveal: Boolean): ObjectAnimator {
        val animator = ObjectAnimator.ofObject(target, clipBounds, RectEvaluator(), 0, 0)
        if (reveal) animator.doOnStart { target.visibility = VISIBLE }
        else animator.doOnEnd { target.visibility = GONE }
        animator.duration = 300
        animator.interpolator = CubicBezierInterpolator.STANDARD_CURVE
        return animator
    }

    fun createHorizontalWindowAnimator(rect: View, target: View, reveal: Boolean): Animator {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
            return createFadeAnimator(target, reveal, 400L)
        val animator = createWindowAnimator(target, reveal)
        val from = Rect(rect.left, rect.top + rect.height / 2, rect.right, rect.bottom - rect.height / 2)
        val to = Rect(rect.left, rect.top, rect.right, rect.bottom)
        if (reveal) animator.setObjectValues(from, to)
        else animator.setObjectValues(to, from)
        return animator
    }

    fun createVerticalWindowAnimator(rect: View, target: View, reveal: Boolean): Animator {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
            return createFadeAnimator(target, reveal, 400L)
        val animator = createWindowAnimator(target, reveal)
        val from = Rect(rect.left + rect.width / 2, rect.top, rect.right - rect.width / 2, rect.bottom)
        val to = Rect(rect.left, rect.top, rect.right, rect.bottom)
        if (reveal) animator.setObjectValues(from, to)
        else animator.setObjectValues(to, from)
        return animator
    }

    fun createVericalSlideAnimator(rect: View, target: View, reveal: Boolean): Animator {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
            return createFadeAnimator(target, reveal, 400L)
        val from = Rect(rect.left, if (reveal) rect.bottom else rect.top, rect.right, rect.bottom)
        val to = Rect(rect.left, rect.top, rect.right, if (reveal) rect.bottom else rect.top)
        val animator = ObjectAnimator.ofObject(target, clipBounds, RectEvaluator(), from, to)
        if (reveal) animator.doOnStart { target.visibility = VISIBLE }
        else animator.doOnEnd { target.visibility = GONE }
        animator.duration = 400
        animator.interpolator = CubicBezierInterpolator.STANDARD_CURVE
        return animator
    }

    fun createHorizontalSlideAnimator(rect: View, target: View, reveal: Boolean): Animator {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
            return createFadeAnimator(target, reveal, 400L)
        val from = Rect(rect.left, rect.top, if (reveal) rect.left else rect.right, rect.bottom)
        val to = Rect(if (reveal) rect.left else rect.right, rect.top, rect.right, rect.bottom)
        val animator = ObjectAnimator.ofObject(target, clipBounds, RectEvaluator(), from, to)
        if (reveal) animator.doOnStart { target.visibility = VISIBLE }
        else animator.doOnEnd { target.visibility = GONE }
        animator.duration = 400
        animator.interpolator = CubicBezierInterpolator.STANDARD_CURVE
        return animator
    }

    fun createFadeAnimator(target: View, reveal: Boolean, duration: Long): Animator {
        val animator = ObjectAnimator.ofFloat(target, "alpha", if (reveal) 0f else 1f, if (reveal) 1f else 0f)
        animator.duration = duration
        if (reveal) animator.doOnStart { target.visibility = VISIBLE }
        else animator.doOnEnd { target.visibility = GONE }
        return animator
    }

    fun createCircularReveal(
        target: View,
        x: Float,
        y: Float,
        reveal: Boolean,
        size: Float
    ): Animator {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return createFadeAnimator(target, reveal, 400L)
        val from = if (reveal) 0F else size
        val to = if (reveal) size else 0F
        val animator = ViewAnimationUtils.createCircularReveal(
            target,
            if (reveal) x.toInt() else (target.width / 2),
            if (reveal) y.toInt() else (target.height / 2),
            from,
            to
        )
        animator.duration = if (reveal) 400 else 200
        animator.interpolator = AccelerateDecelerateInterpolator()
        if (reveal) animator.doOnStart { target.visibility = VISIBLE }
        else animator.doOnEnd { target.visibility = GONE }
        return animator
    }
}
