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
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.widget.FrameLayout


/**
 * FrameLayout with rounded corners and an optional border.
 * @author Bryan de Ridder
 */
class RoundedCornerLayout(context: Context) : FrameLayout(context) {

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    var cornerRadius = 0f
    var borderWidth = 0f
    var borderColor = darkGray

    override fun draw(canvas: Canvas) {
        val halfBorderWidth = borderWidth / 2
        val count = canvas.save()
        val path = Path()
        if (borderWidth > 0f) {
            paint.style = Paint.Style.STROKE
            paint.color = borderColor
            paint.strokeWidth = borderWidth
        } else {
            // this prevents the border from removing anti-aliassing
            paint.color = (background as ColorDrawable).color
        }
        path.addRoundRect(
            RectF(
                halfBorderWidth,
                halfBorderWidth,
                width.toFloat() - halfBorderWidth,
                height.toFloat() - halfBorderWidth
            ),
            cornerRadius,
            cornerRadius,
            Path.Direction.CW
        )
        canvas.drawPath(path, paint)
        canvas.clipPath(path)
        super.draw(canvas)
        canvas.restoreToCount(count)
    }
}