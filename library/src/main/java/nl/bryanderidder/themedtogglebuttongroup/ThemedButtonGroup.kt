package nl.bryanderidder.themedtogglebuttongroup

import android.animation.Animator
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.google.android.flexbox.FlexboxLayout

/**
 * A group of customisable [ThemedButton]'s,
 * The user is limited to select a [selectableAmount] at a time.
 * When the user selects too many items, the first selected Button is deselected
 * by removing it from the [selectedButtons].
 *
 * @author Bryan de Ridder
 */
class ThemedButtonGroup(ctx: Context, attrs: AttributeSet) : FlexboxLayout(ctx, attrs) {

    private lateinit var selectListener: (ThemedButton) -> Unit
    var buttons = listOf<ThemedButton>()
    var animator: Animator = AnimatorSet()
    var selectableAmount: Int = 1
    var selectedButtons = mutableListOf<ThemedButton>()

    init {
        styleSelectedBtns()
        styleDeSelectedBtns()
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.ThemedButtonGroup)
        styledAttrs.getInt(R.styleable.ThemedButtonGroup_toggle_selectableAmount, -1).also {
            if (it != -1) this.selectableAmount = it
        }
        styledAttrs.recycle()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addListener(btn: ThemedButton) {
        btn.cbCardView.setOnTouchListener { _, event ->
            if (animator.isRunning) return@setOnTouchListener false
            var animators = listOf<Animator>()
            if (!btn.isSelected) {
                selectListener(btn)
                animators += selectButton(btn, event.x, event.y, true)
                styleSelected(btn)
                selectedButtons.enqueue(btn)
            }
            if (buttons.count { it.isSelected } > selectableAmount)
            {
                val dequeuedBtn = selectedButtons.dequeue()!!
                animators += selectButton(dequeuedBtn, (dequeuedBtn.width/2).toFloat(), (dequeuedBtn.height/2).toFloat(), false)

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
        val size = (btn.btnWidth.coerceAtLeast(btn.btnHeight) * 1.2).toFloat()
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
        return animator
    }

    private fun styleDeselected(btn: ThemedButton) {
        btn.textColor = btn.defaultTextColor
        btn.iconColor = btn.defaultTextColor
        btn.btnBackgroundColor = btn.defaultBgColor
    }

    private fun styleSelected(btn: ThemedButton) {
        btn.cbTextHighlight.setTextColor(btn.defaultHighLightTextColor)
        btn.cbIconHighlight.setTintColor(btn.defaultHighLightTextColor)
        btn.cbCardViewHighlight.setCardBackgroundColor(btn.highlightBgColor)
    }

    fun styleSelectedBtns() = buttons.filter { it.isSelected }.forEach { styleSelected(it) }
    fun styleDeSelectedBtns() = buttons.filter { !it.isSelected }.forEach { styleDeselected(it) }

    fun onSelect(listener: (ThemedButton) -> Unit) {
        this.selectListener = listener
    }
}