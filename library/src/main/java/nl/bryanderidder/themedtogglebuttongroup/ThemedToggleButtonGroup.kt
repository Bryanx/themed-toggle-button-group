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
import com.google.android.flexbox.FlexboxLayout
import java.lang.UnsupportedOperationException

/**
 * A group of customisable [ThemedButton]'s,
 * The user is limited to select a [selectableAmount] at a time.
 * When the user selects too many items, the first selected Button is deselected
 * by removing it from the [selectedButtons].
 *
 * @author Bryan de Ridder
 */
class ThemedToggleButtonGroup(ctx: Context, attrs: AttributeSet) : FlexboxLayout(ctx, attrs) {

    private var selectListener: ((ThemedButton) -> Unit)? = null

    private var selectAnimator: Animator = AnimatorSet()

    private var deselectAnimator: Animator? = AnimatorSet()

    /**
     * The amount of buttons that are allowed to be selected. Default is 1.
     * Set it equal to the amount of buttons to make it unlimited.
     */
    var selectableAmount: Int = 1

    /** The amount of buttons that are required to be selected. Default is 1. */
    var requiredAmount: Int = 1

    /** All buttons that are currently in the toggle group. */
    var buttons = listOf<ThemedButton>()

    /** All buttons that are currently selected in the toggle group. */
    var selectedButtons = mutableListOf<ThemedButton>()

    init {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.ThemedToggleButtonGroup)
        this.selectableAmount = styledAttrs.getInt(R.styleable.ThemedToggleButtonGroup_toggle_selectableAmount, 1)
        this.requiredAmount = styledAttrs.getInt(R.styleable.ThemedToggleButtonGroup_toggle_requiredAmount, 1)
        if (requiredAmount > selectableAmount) throw UnsupportedOperationException("Required amount must be smaller than or equal to selectable amount.")
        styledAttrs.recycle()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addClickListeners(btn: ThemedButton) {
        buttons.forEach { it.bounceOnClick() }
        btn.cvCard.setOnTouchListener { _, event ->
            selectAnimator.cancel()
            deselectAnimator?.cancel()
            selectButton(btn, event.x, event.y)
            startAnimations()
            if (event.action == MotionEvent.ACTION_UP) btn.performClick()
            event.action == MotionEvent.ACTION_UP
        }
    }

    private fun startAnimations() {
        try {
            deselectAnimator?.duration = selectAnimator.duration / 2
            val set = AnimatorSet()
            if (deselectAnimator != null) set.playTogether(selectAnimator, deselectAnimator)
            else set.play(selectAnimator)
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
        if (btn.isSelected && buttons.count { it.isSelected } <= requiredAmount) return
        btn.isSelected = !btn.isSelected
        if (btn.isSelected) selectedButtons.enqueue(btn)
        else selectedButtons.remove(btn)
        selectAnimator = getSelectionAnimator(btn, x, y, btn.isSelected)
        if (buttons.count { it.isSelected } > selectableAmount) {
            val removedBtn = selectedButtons.dequeue()!!
            buttons.find { it == removedBtn }?.isSelected = false
            deselectAnimator = getSelectionAnimator(removedBtn, removedBtn.centerX, removedBtn.centerY, false)
        } else {
            deselectAnimator = null
        }
        selectListener?.invoke(btn)
    }

    private fun getSelectionAnimator(btn: ThemedButton, x: Float, y: Float, selected: Boolean): Animator {
        var animator: Animator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val size = (btn.btnWidth.coerceAtLeast(btn.btnHeight) * 1.1).toFloat()
            animator = ViewAnimationUtils.createCircularReveal(
                btn.cvSelectedCard,
                x.toInt(),
                y.toInt(),
                if (selected) 0F else size,
                if (selected) size else 0F
            )
            animator.duration = 400
            animator.interpolator = AccelerateDecelerateInterpolator()
        } else {
            animator = ObjectAnimator.ofFloat(btn.cvSelectedCard, "alpha", if (selected) 0f else 1f, if (selected) 1f else 0f)
        }
        if (selected) btn.cvSelectedCard.visibility = VISIBLE
        else animator.doOnEnd { btn.cvSelectedCard.visibility = GONE }
        return animator
    }

    private fun styleDeselected(btn: ThemedButton) {
        btn.tvText.setTextColor(btn.textColor)
        btn.ivIcon.setTintColor(btn.textColor)
        btn.btnBackgroundColor = btn.bgColor
    }

    private fun styleSelected(btn: ThemedButton) {
        btn.tvSelectedText.setTextColor(btn.selectedTextColor)
        btn.ivSelectedIcon.setTintColor(btn.selectedTextColor)
        btn.cvSelectedCard.setCardBackgroundColor(btn.selectedBgColor)
    }

    fun setOnSelectListener(listener: (ThemedButton) -> Unit) {
        this.selectListener = listener
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        buttons.forEach { addClickListeners(it) }
        buttons.forEach { styleSelected(it) }
        buttons.forEach { styleDeselected(it) }
        setInitialSelection()
    }

    private fun setInitialSelection() {
        buttons.filter { it.isSelected }.forEach(this::intiallySelect)
        val stillRequiredAmount = requiredAmount - buttons.filter { it.isSelected }.count()
        (0 until stillRequiredAmount)
            .map { buttons.first { !it.isSelected } }
            .forEach(this::intiallySelect)
    }

    private fun intiallySelect(button: ThemedButton) {
        button.isSelected = true
        selectedButtons.enqueue(button)
        button.cvSelectedCard.visibility = VISIBLE
    }
}