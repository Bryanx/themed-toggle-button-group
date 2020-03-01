package nl.bryanderidder.themedtogglebuttongroup

import android.content.*
import android.util.*
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.view_themedbutton.view.*


/**
 * Group of buttons, only allowed to select one at a time.
 * @author Bryan de Ridder
 */
class ThemedButtonGroup(ctx: Context, attrs: AttributeSet) : LinearLayout(ctx, attrs) {

    private var buttons = listOf<ThemedButton>()
    private var defaultHighLightTextColor: Int = ctx.color(android.R.color.white)
    private var highlightBgColor: Int = ctx.color(R.color.colorPrimary)

    private fun addListener(btn: ThemedButton) {
        btn.cbCardView.setOnTouchListener { _, event ->
            if (!btn.isSelected) btn.animateBg(highlightBgColor, event.x, event.y)
            styleSelected(btn)
            buttons.filter { it != btn }.forEach { styleDeselected(it) }
            if (event.action == MotionEvent.ACTION_DOWN) btn.performClick()
            event.action == MotionEvent.ACTION_DOWN
        }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        super.addView(child, params)
        if (child != null && child is ThemedButton) {
            child.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT,
                1.0f
            )
            child.paddingHorizontal = 4.dp
            buttons += child
            addListener(child)
        }
    }

    private fun styleDeselected(btn: ThemedButton) {
        btn.isSelected = false
        btn.textColor = btn.defaultTextColor
        btn.iconColor = btn.defaultTextColor
        btn.btnBackgroundColor = btn.defaultBackgroundColor
        btn.cbCardViewHighlight.visibility = GONE
    }

    private fun styleSelected(btn: ThemedButton) {
        btn.isSelected = true
//        btn.textColor = defaultHighLightTextColor
//        btn.iconColor = defaultHighLightTextColor
//        btn.btnBackgroundColor = highlightBgColor
        btn.cbTextHighlight.setTextColor(defaultHighLightTextColor)
        btn.cbIconHighlight.setTintColor(defaultHighLightTextColor)
        btn.cbCardViewHighlight.setCardBackgroundColor(highlightBgColor)
    }

    fun styleButtons() = buttons.filter { it.isSelected }.forEach { styleSelected(it) }
}