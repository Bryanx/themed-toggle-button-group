package nl.bryanderidder.themedtogglebuttongroup

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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

    private var selectListener: ((ThemedButton) -> Unit)? = null
    var buttons = listOf<ThemedButton>()
    var selectAnimator: Animator = AnimatorSet()
    var deselectAnimator: Animator = AnimatorSet()
    var selectableAmount: Int = 1
    var selectedButtons = mutableListOf<ThemedButton>()

    init {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.ThemedButtonGroup)
        styledAttrs.getInt(R.styleable.ThemedButtonGroup_toggle_selectableAmount, -1).also {
            if (it != -1) this.selectableAmount = it
        }
        styledAttrs.recycle()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addClickListeners(btn: ThemedButton) {
        buttons.forEach { it.bounceOnClick() }
        btn.cbCardView.setOnTouchListener { _, event ->
            selectAnimator.cancel()
            deselectAnimator.cancel()
            if (!btn.isSelected) selectButton(btn, event.x, event.y)
            startAnimations()
            if (event.action == MotionEvent.ACTION_UP) btn.performClick()
            event.action == MotionEvent.ACTION_UP
        }
    }

    private fun startAnimations() {
        try {
            deselectAnimator.duration = selectAnimator.duration / 2
            val set = AnimatorSet()
            set.playTogether(selectAnimator, deselectAnimator)
            set.start()
        } catch (e: Exception) { /* catch exceptions caused by unfinished animations */ }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        super.addView(child, params)
        if (child != null && child is ThemedButton) {
            buttons += child
        }
    }

    fun selectButton(btn: ThemedButton, x: Float, y: Float) {
        btn.isSelected = true
        selectedButtons.enqueue(btn)
        selectAnimator = getSelectionAnimator(btn, x, y, true)
        if (buttons.count { it.isSelected } > selectableAmount) {
            val removedBtn = selectedButtons.dequeue()!!
            buttons.find { it == removedBtn }?.isSelected = false
            deselectAnimator = getSelectionAnimator(removedBtn, removedBtn.centerX, removedBtn.centerY, false)
        }
        btn.cbCardViewHighlight.visibility = VISIBLE
        selectListener?.invoke(btn)
    }

    private fun getSelectionAnimator(btn: ThemedButton, x: Float, y: Float, selected: Boolean): Animator {
        var animator: Animator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val size = (btn.btnWidth.coerceAtLeast(btn.btnHeight) * 1.2).toFloat()
            animator = ViewAnimationUtils.createCircularReveal(
                btn.cbCardViewHighlight,
                x.toInt(),
                y.toInt(),
                if (selected) 0F else size,
                if (selected) size else 0F
            )
            animator.duration = 400
            animator.interpolator = AccelerateDecelerateInterpolator()
        } else {
            animator = ObjectAnimator.ofFloat(btn.cbCardViewHighlight, "alpha", if (selected) 0f else 1f, if (selected) 1f else 0f)
        }
        if (selected) animator.doOnStart { btn.cbCardViewHighlight.visibility = VISIBLE }
        else animator.doOnEnd { btn.cbCardViewHighlight.visibility = GONE }
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

    fun setOnSelectListener(listener: (ThemedButton) -> Unit) {
        this.selectListener = listener
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        buttons.forEach { addClickListeners(it) }
        buttons.forEach { styleSelected(it) }
        buttons.forEach { styleDeselected(it) }
        buttons.filter { it.isSelected }.forEach {
            selectedButtons.enqueue(it)
            it.cbCardViewHighlight.visibility = VISIBLE
        }
    }
}