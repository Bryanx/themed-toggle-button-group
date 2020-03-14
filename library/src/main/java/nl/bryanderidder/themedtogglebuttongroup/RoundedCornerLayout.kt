package nl.bryanderidder.themedtogglebuttongroup

import android.content.Context
import android.graphics.*
import android.view.View
import android.widget.FrameLayout


/**
 * FrameLayout with rounded corners.
 * @author Bryan de Ridder
 */
class RoundedCornerLayout(context: Context) : FrameLayout(context) {

    var cornerRadius = 0f

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        setBackgroundColor(context.color(android.R.color.black));
    }

    override fun draw(canvas: Canvas) {
        val count = canvas.save()
        val path = Path()
        path.addRoundRect(
            RectF(0f, 0f, width.toFloat(), height.toFloat()),
            cornerRadius,
            cornerRadius,
            Path.Direction.CW
        )
        canvas.clipPath(path)
        super.draw(canvas)
        canvas.restoreToCount(count)
    }
}