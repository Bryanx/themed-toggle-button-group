package nl.bryanderidder.themedtogglebuttongroup

import android.animation.*
import android.content.*
import android.os.*
import android.util.*
import android.view.*
import android.view.animation.*
import androidx.core.animation.*
import com.google.android.flexbox.*
import kotlinx.android.synthetic.main.view_themedbutton.view.*


/**
 * Group of buttons, only allowed to select one at a time.
 * @author Bryan de Ridder
 */
class ThemedButtonGroup(ctx: Context, attrs: AttributeSet) : FlexboxLayout(ctx, attrs) {

//    private var declaredHeight: Float
//    private var declaredWidth: Float
    private var buttons = listOf<ThemedButton>()
    var animator: Animator = AnimatorSet()

    init {
        styleSelectedBtns()
        styleDeSelectedBtns()
//        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.ThemedButtonGroup)
//        this.declaredHeight = styledAttrs.getDimension(R.styleable.ThemedButtonGroup_android_layout_height, 22F)
//        this.declaredWidth = styledAttrs.getDimension(R.styleable.ThemedButtonGroup_android_layout_width, 22F)
//        styledAttrs.recycle()
    }

    private fun addListener(btn: ThemedButton) {
        btn.cbCardView.setOnTouchListener { _, event ->
            if (animator.isRunning) return@setOnTouchListener false
            var animators = listOf<Animator>()
            if (!btn.isSelected) {
                animators += selectButton(btn, event.x, event.y, true)
                styleSelected(btn)
            }
            buttons.filter { it.isSelected && it != btn }.forEach {
                animators += selectButton(it, (it.width/2).toFloat(), (it.height/2).toFloat(), false)
            }
            if (animators.size > 1) animators[1].duration = animators[0].duration / 2
            val set = AnimatorSet()
            set.playTogether(animators)
            set.start()
            if (event.action == MotionEvent.ACTION_UP) btn.performClick()
            event.action == MotionEvent.ACTION_UP
        }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        super.addView(child, params)
        if (child != null && child is ThemedButton) {
            buttons += child
            addListener(child)
        }
    }

    fun selectButton(btn: ThemedButton, x: Float, y: Float, selected: Boolean): Animator {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val size = (btn.btnWidth.coerceAtLeast(btn.btnHeight)*1.2).toFloat()
                animator = ViewAnimationUtils.createCircularReveal(
                    btn.cbCardViewHighlight,
                    x.toInt(),
                    y.toInt(),
                    if (selected) 0F else size,
                    if (selected) size else 0F
                )
                animator.interpolator = AccelerateDecelerateInterpolator()
                if (selected) animator.doOnStart { btn.cbCardViewHighlight.visibility = VISIBLE }
                if (!selected) {
                    animator.doOnStart { styleDeselected(btn) }
                    animator.doOnEnd { btn.cbCardViewHighlight.visibility = GONE }
                }
                btn.isSelected = selected
                animator.duration = 400
            }
            else -> cbCardView.setCardBackgroundColor(btn.highlightBgColor)
        }
        return animator
    }

    private fun styleDeselected(btn: ThemedButton) {
        btn.textColor = btn.defaultTextColor
        btn.iconColor = btn.defaultTextColor
        btn.btnBackgroundColor = btn.defaultBgColor
    }

    private fun styleSelected(btn: ThemedButton) {
        btn.cbTextHighlight.setTextColor(btn.highLightTextColor)
        btn.cbIconHighlight.setTintColor(btn.highLightTextColor)
        btn.cbCardViewHighlight.setCardBackgroundColor(btn.highlightBgColor)
    }

    fun styleSelectedBtns() = buttons.filter { it.isSelected }.forEach { styleSelected(it) }
    fun styleDeSelectedBtns() = buttons.filter { !it.isSelected }.forEach { styleDeselected(it) }
}