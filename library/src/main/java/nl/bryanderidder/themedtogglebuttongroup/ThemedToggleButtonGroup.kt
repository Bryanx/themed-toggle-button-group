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
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.google.android.flexbox.FlexboxLayout
import nl.bryanderidder.themedtogglebuttongroup.SelectAnimation.*
import nl.bryanderidder.themedtogglebuttongroup.R.styleable.*


/**
 * A group of customisable [ThemedButton]'s,
 * The user is limited to select a [selectableAmount] at a time.
 * When the user selects too many items, the first selected Button is deselected
 * by removing it from the [selectedButtons].
 *
 * @author Bryan de Ridder
 */
class ThemedToggleButtonGroup : FlexboxLayout {

    private var selectListener: ((ThemedButton) -> Unit)? = null

    private var selectAnimator: Animator = AnimatorSet()

    private var deselectAnimator: Animator? = AnimatorSet()

    private lateinit var animatorSet: AnimatorSet

    /**
     * The amount of space between the [buttons] when they are positioned next to each other.
     * Default is 10dp.
     */
    var horizontalSpacing: Int = 10.px
        set(value) {
            field = value
            if (buttons.isNotEmpty())
                buttons.subList(0, buttons.lastIndex).forEach { it.setMargin(rightMargin = value) }
        }

    /**
     * Sets the animation when selecting the button.
     * Some animations require a certain API, if that requirement is not met
     * the fade animation will be used.
     */
    var selectAnimation: SelectAnimation = CIRCULAR_REVEAL

    /**
     * The amount of buttons that are allowed to be selected. Default is 1.
     * Set it equal to the amount of buttons to make it unlimited.
     */
    var selectableAmount: Int = 1

    /** The amount of buttons that are required to be selected. Default is 1. */
    var requiredAmount: Int = 1

    /** All buttons that are currently in the toggle group. */
    var buttons: List<ThemedButton> = listOf<ThemedButton>()

    /** All buttons that are currently selected in the toggle group. */
    var selectedButtons: MutableList<ThemedButton> = mutableListOf<ThemedButton>()

    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
        getStyledAttributes(attrs)
    }

    private fun getStyledAttributes(attrs: AttributeSet) {
        val styledAttrs = context.obtainStyledAttributes(attrs, ThemedToggleButtonGroup)
        this.selectableAmount = styledAttrs.getInt(ThemedToggleButtonGroup_toggle_selectableAmount, 1)
        this.requiredAmount = styledAttrs.getInt(ThemedToggleButtonGroup_toggle_requiredAmount, 1)
        this.selectAnimation = SelectAnimation.values()[styledAttrs.getInt(ThemedToggleButtonGroup_toggle_selectAnimation, 1)]
        this.horizontalSpacing = styledAttrs.getDimension(ThemedToggleButtonGroup_toggle_horizontalSpacing, 10.pxf).toInt()
        if (requiredAmount > selectableAmount) throw UnsupportedOperationException("Required amount must be smaller than or equal to selectable amount.")
        styledAttrs.recycle()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addClickListeners(btn: ThemedButton) {
        btn.cvCard.setOnBoundedTouchListener { isActionDown: Boolean, isActionUp: Boolean, isActionCancel: Boolean, event: MotionEvent? ->
            if (isActionDown) btn.bounceDown()

            if (isActionUp) {
                selectButtonWithAnimation(btn, event?.x ?: 0f, event?.y ?: 0f)
                btn.performClick()
            }

            if (isActionUp || isActionCancel) btn.bounceUp()
        }
    }

    private fun startAnimations() {
        if (this::animatorSet.isInitialized && animatorSet.isRunning) animatorSet.cancel()
        try {
            animatorSet = AnimatorSet()
            animatorSet.startDelay = 5 //small start delay to make sure onStart always happens before onCancel
            if (deselectAnimator != null) animatorSet.playTogether(selectAnimator, deselectAnimator)
            else animatorSet.play(selectAnimator)
            animatorSet.start()
        } catch (e: Exception) { /* catch exceptions caused by unfinished animations */ }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        super.addView(child, params)
        if (child != null && child is ThemedButton) {
            buttons += child
            horizontalSpacing = horizontalSpacing
            addClickListeners(child)
        }
    }

    /**
     * Selects or deselects the passed [ThemedButton]'s id with animation.
     *
     * @param id Id of the [ThemedButton]'s view.
     */
    fun selectButtonWithAnimation(@IdRes id: Int) = selectButtonWithAnimation(findViewById<ThemedButton>(id))

    /**
     * Selects or deselects the passed [ThemedButton] with animation. This call is blocked if
     * this makes the amount of selected buttons fall below the [requiredAmount] of selected buttons
     * or if the button is not enabled.
     *
     * @param btn The [ThemedButton] to be selected/deselected.
     * @param x Horizontal position of the click.
     * @param y Vertical position of the click.
     */
    fun selectButtonWithAnimation(
        btn: ThemedButton,
        x: Float = (btn.btnWidth / 2).toFloat(),
        y: Float = (btn.btnHeight / 2).toFloat()
    ) {
        if (selectedButtons.isNotEmpty() && selectedButtons.last() != btn) selectAnimator.cancel()
        deselectAnimator?.cancel()
        val changed = selectButton(btn, x, y, true)
        if (changed) startAnimations()
    }

    /**
     * Selects or deselects the passed [ThemedButton]'s id. This call is blocked if this makes the amount of
     * selected buttons fall below the [requiredAmount] of selected buttons or if the button is not enabled.
     *
     * @param id Id of the [ThemedButton]'s view.
     */
    fun selectButton(@IdRes id: Int) = selectButton(findViewById<ThemedButton>(id))

    /**
     * Selects or deselects the passed [ThemedButton]. This call is blocked if this makes the amount of
     * selected buttons fall below the [requiredAmount] of selected buttons or if the button is not enabled.
     *
     * If the button is already selected, firing this method will deselect the button,
     * unless it will make the amount of selected buttons fall below the 'requiredAmount' of selected buttons,
     * in that case nothing happens.
     *
     * @param btn The [ThemedButton] to be selected/deselected.
     * @param x Horizontal position of the click.
     * @param y Vertical position of the click.
     * @param animate Whether the select should be animated.
     * @return Whether something changed.
     */
    fun selectButton(
        btn: ThemedButton,
        x: Float = (btn.btnWidth / 2).toFloat(),
        y: Float = (btn.btnHeight / 2).toFloat(),
        animate: Boolean = false
    ): Boolean {
        if (!btn.isEnabled || btn.isSelected && buttons.count { it.isSelected } <= requiredAmount)
            return false
        btn.isSelected = !btn.isSelected
        if (btn.isSelected) selectedButtons.enqueue(btn)
        else selectedButtons.remove(btn)
        if (animate) selectAnimator = getSelectionAnimator(btn, x, y, btn.isSelected)
        else btn.cvSelectedCard.visibility = if (btn.isSelected) VISIBLE else GONE
        if (buttons.count { it.isSelected } > selectableAmount) {
            val removedBtn = selectedButtons.dequeue()!!
            buttons.find { it == removedBtn }?.isSelected = false
            if (animate) deselectAnimator = getSelectionAnimator(removedBtn, x, y, false)
            else removedBtn.cvSelectedCard.visibility = GONE
        } else {
            if (animate) deselectAnimator = null
        }
        selectListener?.invoke(btn)
        return true
    }

    private fun getSelectionAnimator(btn: ThemedButton, x: Float, y: Float, selected: Boolean): Animator =
        when (selectAnimation) {
            NONE -> AnimationUtils.createFadeAnimator(btn.cvSelectedCard, selected, 0L)
            FADE -> AnimationUtils.createFadeAnimator(btn.cvSelectedCard, selected, 400L)
            VERTICAL_SLIDE -> AnimationUtils.createVericalSlideAnimator(btn.cvCard, btn.cvSelectedCard, selected)
            HORIZONTAL_SLIDE -> AnimationUtils.createHorizontalSlideAnimator(btn.cvCard, btn.cvSelectedCard, selected)
            HORIZONTAL_WINDOW -> AnimationUtils.createHorizontalWindowAnimator(btn.cvCard, btn.cvSelectedCard, selected)
            VERTICAL_WINDOW -> AnimationUtils.createVerticalWindowAnimator(btn.cvCard, btn.cvSelectedCard, selected)
            else -> AnimationUtils.createCircularReveal(btn.cvSelectedCard, x, y, selected, (btn.btnWidth.coerceAtLeast(btn.btnHeight) * 1.1).toFloat())
        }

    /** Listen on selection changes. Alternatively you can add onclick listeners to the buttons. */
    fun setOnSelectListener(listener: (ThemedButton) -> Unit) {
        this.selectListener = listener
    }
}
