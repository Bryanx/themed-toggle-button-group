package nl.bryanderidder.themedtogglebuttongroup

import android.content.*
import android.graphics.drawable.*
import android.util.*
import android.view.*
import android.widget.*
import androidx.core.view.*
import kotlinx.android.synthetic.main.view_themedbutton.view.*

/**
 * Custom button with rounded corners.
 * @author Bryan de Ridder
 */
class ThemedButton(ctx: Context, attrs: AttributeSet) : RelativeLayout(ctx, attrs) {
    private val defaultCornerRadius: Float = 22F
    private var circularCornerRadius: Boolean = false

    var defaultBgColor: Int = ctx.color(R.color.lightGray)
    var highlightBgColor: Int = ctx.color(R.color.colorPrimary)

    var defaultTextColor: Int = ctx.color(R.color.darkGray)
    var highLightTextColor: Int = ctx.color(android.R.color.white)

    var textColor: Int
        get() = cbText.currentTextColor
        set(textColor) = cbText.setTextColor(textColor)

    var text: String
        get() = cbText.string
        set(text) {
            cbText?.text = text
            cbTextHighlight?.text = text
        }

    var cornerRadius: Float
        get() = cbCardView.radius
        set(cornerRadius) {
            cbCardView.radius = cornerRadius
            cbCardViewHighlight.radius = cornerRadius
        }

    var btnHeight: Int
        get() = cbCardView.height
        set(height) {
            cbCardView.layoutParams.height = height.dp
            cbCardViewHighlight.layoutParams.height = height.dp
        }

    var btnWidth: Int
        get() = cbCardView.width
        set(btnWidth) {
            cbCardView.layoutParams.width = btnWidth
            cbCardViewHighlight.layoutParams.width = btnWidth
        }

    var paddingHorizontal: Float
        get() = cbText.paddingHorizontal
        set(padding) {
            cbText.paddingHorizontal = padding
            cbTextHighlight.paddingHorizontal = padding
        }

    var paddingVertical: Float
        get() = cbText.paddingVertical
        set(padding) {
            cbText.paddingVertical = padding
            cbTextHighlight.paddingVertical = padding
        }

    var btnBackgroundColor: Int
        get() = cbCardView.cardBackgroundColor.defaultColor
        set(btnBackgroundColor) = cbCardView.setCardBackgroundColor(btnBackgroundColor)

    var icon: Drawable
        get() = cbIcon.background
        set(icon) {
            cbIcon.setImageDrawable(icon)
            cbIcon.visibility = VISIBLE
            cbIconHighlight.setImageDrawable(icon.constantState?.newDrawable())
            cbIconHighlight.visibility = VISIBLE
        }

    var iconPadding: Float
        get() = cbIcon.paddingStart.toFloat()
        set(padding) {
            cbIcon.setPadding(padding.dp)
            cbIconHighlight.setPadding(padding.dp)
        }

    var iconColor: Int
        get() = cbIcon.solidColor
        set(iconColor) = cbIcon.setTintColor(iconColor)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_themedbutton, this)
        getStyledAttributes(attrs)
    }

    private fun getStyledAttributes(attrs: AttributeSet) {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.ThemedButton)
        this.defaultBgColor = styledAttrs.getInt(R.styleable.ThemedButton_backgroundColor, defaultBgColor)
        this.highlightBgColor = styledAttrs.getInt(R.styleable.ThemedButton_highlightBackgroundColor, highlightBgColor)
        this.highLightTextColor = styledAttrs.getInt(R.styleable.ThemedButton_highLightTextColor, highLightTextColor)
        this.defaultTextColor = styledAttrs.getInt(R.styleable.ThemedButton_textColor, defaultTextColor)
        this.cornerRadius = styledAttrs.getDimension(R.styleable.ThemedButton_btnCornerRadius, defaultCornerRadius)
        this.paddingHorizontal = styledAttrs.getDimension(R.styleable.ThemedButton_paddingHorizontal, 0F)
        this.paddingVertical = styledAttrs.getDimension(R.styleable.ThemedButton_paddingVertical, 0F)
        this.iconPadding = styledAttrs.getDimension(R.styleable.ThemedButton_iconPadding, 25F)
        this.iconColor = styledAttrs.getInt(R.styleable.ThemedButton_iconColor, defaultTextColor)
        this.text = styledAttrs.getString(R.styleable.ThemedButton_text) ?: ""
        this.circularCornerRadius = styledAttrs.getBoolean(R.styleable.ThemedButton_circularCornerRadius, false)
        styledAttrs.getDrawable(R.styleable.ThemedButton_icon)?.let { this.icon = it }
        styledAttrs.recycle()
        setPadding(0,0,0,0)
        btnBackgroundColor = defaultBgColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        btnHeight = heightMeasureSpec
        btnWidth = widthMeasureSpec
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (circularCornerRadius) {
            cornerRadius = (btnHeight.coerceAtMost(btnWidth) / 2.2).toFloat()
        }
    }
}