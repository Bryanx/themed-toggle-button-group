package nl.bryanderidder.themedtogglebuttongroup

import android.content.Context
import android.content.res.Resources
import android.graphics.PorterDuff
import android.os.Build
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

/**
 * Contains all extension methods.
 * @author Bryan de Ridder
 */

fun ImageView.setTintColor(color: Int, blendMode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN) {
    this.setColorFilter(color, blendMode)
}

// Returns a color
fun Context.color(id: Int): Int = ContextCompat.getColor(this, id)

fun CardView.addRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        foreground = getDrawable(context, resourceId)
    }
}

fun ThemedButton.setMargin(leftMargin: Int? = null, topMargin: Int? = null,
                   rightMargin: Int? = null, bottomMargin: Int? = null) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(
        leftMargin ?: params.leftMargin,
        topMargin ?: params.topMargin,
        rightMargin ?: params.rightMargin,
        bottomMargin ?: params.bottomMargin)
    layoutParams = params
}

fun ThemedButton.bounceOnClick() {
    val animScaleDown = ScaleAnimation(1f, 0.9f, 1f, 0.9f, (width/2).toFloat(), (height/2).toFloat()).apply {  }
    animScaleDown.duration = 200
    animScaleDown.fillAfter = true
    animScaleDown.interpolator= DecelerateInterpolator()
    val animScaleUp = ScaleAnimation(0.9f, 1f, 0.9f, 1f, (width/2).toFloat(), (height/2).toFloat())
    animScaleUp.duration = 200
    animScaleUp.startOffset = 100
    animScaleUp.interpolator= OvershootInterpolator(3f)
    setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) startAnimation(animScaleDown)
        if (event.action == MotionEvent.ACTION_UP) startAnimation(animScaleUp)
        if (event.action == MotionEvent.ACTION_UP) performClick()
        true
    }
}

fun CardView.setCardPadding(
    left: Float? = null, top: Float? = null,
    right: Float? = null, bottom: Float? = null,
    horizontal: Float? = null, vertical: Float? = null,
    all: Float? = null
) {
    if (listOfNotNull(left, top, right, bottom, horizontal, vertical, all).any { it < 0f }) return
    all?.let { setContentPadding(it.toInt(), it.toInt(), it.toInt(), it.toInt()) }
    horizontal?.let { setContentPadding(it.toInt(), contentPaddingTop, it.toInt(), contentPaddingBottom) }
    vertical?.let { setContentPadding(contentPaddingLeft, it.toInt(), contentPaddingRight, it.toInt()) }
    if (left != null || top != null || right != null || bottom != null)
        setContentPadding(
            left?.toInt() ?: contentPaddingLeft,
            top?.toInt() ?: contentPaddingTop,
            right?.toInt() ?: contentPaddingRight,
            bottom?.toInt() ?: contentPaddingBottom
        )
}

fun View.setViewPadding(
    left: Float? = null, top: Float? = null,
    right: Float? = null, bottom: Float? = null,
    horizontal: Float? = null, vertical: Float? = null,
    all: Float? = null
) {
    if (listOfNotNull(left, top, right, bottom, horizontal, vertical, all).any { it < 0f }) return
    all?.let { setPadding(it.toInt(), it.toInt(), it.toInt(), it.toInt()) }
    horizontal?.let { setPadding(it.toInt(), paddingTop, it.toInt(), paddingBottom) }
    vertical?.let { setPadding(paddingLeft, it.toInt(), paddingRight, it.toInt()) }
    if (left != null || top != null || right != null || bottom != null)
        setPadding(
            left?.toInt() ?: paddingLeft,
            top?.toInt() ?: paddingTop,
            right?.toInt() ?: paddingRight,
            bottom?.toInt() ?: paddingBottom
        )
}

// property extensions

var View.layoutGravity
    get() = (layoutParams as FrameLayout.LayoutParams).gravity
    set(value) {
        layoutParams = FrameLayout.LayoutParams(
            layoutParams.width,
            layoutParams.height,
            value
        )
    }

fun <T> MutableList<T>.enqueue(item: T) = if (!this.contains(item)) this.add(this.count(), item) else null

fun <T> MutableList<T>.dequeue(): T? = if (this.count() > 0) this.removeAt(0) else null

val View.name: String get() =
    if (this.id == -0x1) "no-id"
    else resources.getResourceEntryName(id) ?: "error-getting-name"

val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.pxf: Float get() = (this * Resources.getSystem().displayMetrics.density)

val Float.px: Float get() = this * Resources.getSystem().displayMetrics.density

val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Float.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val ThemedButton.centerX: Float get() = (width / 2).toFloat()

val ThemedButton.centerY: Float get() = (height / 2).toFloat()