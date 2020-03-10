package nl.bryanderidder.themedtogglebuttongroup

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.util.TypedValue
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.animation.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import java.lang.Integer.parseInt
import java.util.*


/**
 * Contains all extension methods.
 * @author Bryan de Ridder
 */

// Do something on animation end
fun Animation.onAnimateEnd(args: () -> Unit) {
    this.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(arg0: Animation) {}
        override fun onAnimationEnd(animation: Animation) { args() }
        override fun onAnimationRepeat(animation: Animation) {}
    })
}

// Returns an attribute
fun Context.getAttr(id: Int): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(id, typedValue, true)
    return typedValue.data
}

// Darkens an int color by a certain factor
fun Int.setBrightness(factor: Float): Int {
    val hexWithAlpha = Integer.toHexString(this)
    val hex = hexWithAlpha.substring(2, hexWithAlpha.length)
    val hsl = floatArrayOf(0F,0F,0F)
    ColorUtils.RGBToHSL(parseInt(hex.substring( 0, 2 ), 16),
        parseInt(hex.substring( 2, 4 ), 16),
        parseInt(hex.substring( 4, 6 ), 16),
        hsl)
    hsl[2] = factor
    return ColorUtils.HSLToColor(hsl)
}

// Darkens an int color by a certain factor
fun Int.setAlpha(factor: Float): Int = ColorUtils.setAlphaComponent(this, (factor * 255).toInt())

fun ImageView.setTint(id: Int, blendMode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN) {
    this.setColorFilter(id, blendMode)
}


fun ImageView.setTintColor(color: Int, blendMode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN) {
    this.setColorFilter(color, blendMode)
}

fun Button.setBackgroundTint(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.backgroundTintList = ColorStateList.valueOf(color)
    }
}

// Returns a color
fun Context.color(id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun AlertDialog.Builder.setAnimation(styleId: Int): AlertDialog {
    val dialog = this.create()
    dialog.window?.attributes?.windowAnimations = styleId
    return dialog
}

// get menu item name
fun MenuItem.getId(res: Resources): String =
    if (this.itemId == -0x1) "no-id"
    else res.getResourceEntryName(itemId) ?: "error-getting-id"

fun View.addRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        foreground = getDrawable(context, resourceId)
    }
}

fun ObjectAnimator.onAnimateEnd(args: () -> Unit) {
    this.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) { args() }
        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })
}

fun String.equalsIgnoreCase(string: String): Boolean {
    return this.toLowerCase() == string.toLowerCase()
}

fun String.getExtension(): String {
    return this.substring(this.lastIndexOf(".")+1)
}

fun EditText.setLineColor(color: Int) {
    if (Build.VERSION.SDK_INT >= 21) this.backgroundTintList = ColorStateList.valueOf(color)
}

fun Activity?.hideKeyboard() {
    val inputManager = this?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun EditText.onEnter(action: () -> Boolean) {
    this.setOnKeyListener(object : View.OnKeyListener {
        override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                return action()
            }
            return false
        }
    })
}

fun View.bounceOnClick() {
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

fun EditText.showKeyboard() {
    post {
        requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Any.getAttr(name: String): Any? {
    return this.javaClass.getMethod("get${name.initCaps()}").invoke(this)
}

fun String.initCaps(): String {
    return this[0].toUpperCase() + this.substring(1, this.length)
}

// property extensions

// get viewname
val View.name: String get() =
    if (this.id == -0x1) "no-id"
    else resources.getResourceEntryName(id) ?: "error-getting-name"

val TextView.string: String get() = this.text.toString()
val TextView.long: Long get() = this.text.toString().toLong()

val ImageView.tint: ColorFilter get() = this.colorFilter

val Locale.code: String get() = this.language + "-" + this.country

val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.px: Float get() = this * Resources.getSystem().displayMetrics.density

val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Float.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val ThemedButton.centerX: Float get() = (width / 2).toFloat()
val ThemedButton.centerY: Float get() = (height / 2).toFloat()

var CardView.contentPaddingHorizontal: Float
    get() = this.contentPaddingTop.toFloat()
    set(padding) { this.setContentPadding(padding.dp, this.contentPaddingTop, padding.dp, this.contentPaddingBottom) }

var CardView.contentPaddingVertical: Float
    get() = this.contentPaddingTop.toFloat()
    set(padding) { this.setContentPadding(this.contentPaddingLeft, padding.toInt(), this.contentPaddingRight, padding.toInt()+3) }

var TextView.paddingHorizontal: Float
    get() { return this.paddingStart.toFloat() }
    set(padding) { this.setPadding(padding.toInt(), this.paddingTop, padding.toInt(), this.paddingBottom) }

var TextView.paddingVertical: Float
    get() { return this.paddingStart.toFloat() }
    set(padding) { this.setPadding(this.paddingStart, padding.toInt(), this.paddingEnd, padding.toInt()+3) }

fun <T> MutableList<T>.enqueue(item: T) = if (!this.contains(item)) this.add(this.count(), item) else null
fun <T> MutableList<T>.dequeue(): T? = if (this.count() > 0) this.removeAt(0) else null