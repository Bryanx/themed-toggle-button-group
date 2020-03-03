package nl.bryanderidder.themedtogglebuttongroup

import android.content.*
import android.graphics.drawable.*
import android.util.*
import android.view.*
import android.widget.*
import androidx.core.view.*
import kotlinx.android.synthetic.main.view_themedbutton.view.*
import java.util.*

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

    var textSize: Float
        get() = cbText.textSize
        set(size) {
            cbText?.textSize = size.dp.toFloat()
            cbTextHighlight?.textSize = size.dp.toFloat()
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

    var padding: Float
        get() = 0F
        set(padding) {
            cbCardView.setContentPadding(padding.toInt(),padding.toInt(),padding.toInt(),padding.toInt())
            cbCardViewHighlight.setContentPadding(padding.toInt(),padding.toInt(),padding.toInt(),padding.toInt())
        }

    var textPaddingHorizontal: Float
        get() = cbText.paddingHorizontal
        set(padding) {
            cbText.paddingHorizontal = padding
            cbTextHighlight.paddingHorizontal = padding
        }

    var textPaddingVertical: Float
        get() = cbText.paddingVertical
        set(padding) {
            cbText.paddingVertical = padding
            cbTextHighlight.paddingVertical = padding
        }

    var paddingHorizontal: Float
        get() = cbCardView.contentPaddingHorizontal
        set(padding) {
            cbCardView.contentPaddingHorizontal = padding
            cbCardViewHighlight.contentPaddingHorizontal = padding
        }

    var paddingVertical: Float
        get() = cbCardView.contentPaddingVertical
        set(padding) {
            cbCardView.contentPaddingVertical = padding
            cbCardViewHighlight.contentPaddingVertical = padding
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

    var iconGravity: Int
        get() = (cbIcon.layoutParams as FrameLayout.LayoutParams).gravity
        set(position) {
            cbIcon.layoutParams = FrameLayout.LayoutParams(
                cbIcon.layoutParams.width,
                cbIcon.layoutParams.height,
                position
            )
            cbIconHighlight.layoutParams = FrameLayout.LayoutParams(
                cbIconHighlight.layoutParams.width,
                cbIconHighlight.layoutParams.height,
                position
            )
        }

    var textGravity: Int
        get() = (cbText.layoutParams as FrameLayout.LayoutParams).gravity
        set(position) {
            cbText.layoutParams = FrameLayout.LayoutParams(
                cbText.layoutParams.width,
                cbText.layoutParams.height,
                position
            )
            cbTextHighlight.layoutParams = FrameLayout.LayoutParams(
                cbTextHighlight.layoutParams.width,
                cbTextHighlight.layoutParams.height,
                position
            )
        }

    var textAlign: Int
        get() = cbText.textAlignment
        set(alignment) {
            cbText.textAlignment = alignment
            cbTextHighlight.textAlignment = alignment
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
        this.defaultBgColor = styledAttrs.getColor(R.styleable.ThemedButton_backgroundColor, defaultBgColor)
        this.highlightBgColor = styledAttrs.getColor(R.styleable.ThemedButton_highlightBackgroundColor, highlightBgColor)
        this.highLightTextColor = styledAttrs.getColor(R.styleable.ThemedButton_highLightTextColor, highLightTextColor)
        this.defaultTextColor = styledAttrs.getColor(R.styleable.ThemedButton_textColor, defaultTextColor)
        this.cornerRadius = styledAttrs.getDimension(R.styleable.ThemedButton_btnCornerRadius, defaultCornerRadius)
        this.paddingHorizontal = styledAttrs.getDimension(R.styleable.ThemedButton_paddingHorizontal, 0F)
        this.paddingVertical = styledAttrs.getDimension(R.styleable.ThemedButton_paddingVertical, 0F)
        this.textPaddingHorizontal = styledAttrs.getDimension(R.styleable.ThemedButton_textPaddingHorizontal, 0F)
        this.textPaddingVertical = styledAttrs.getDimension(R.styleable.ThemedButton_textPaddingVertical, 0F)
        this.iconPadding = styledAttrs.getDimension(R.styleable.ThemedButton_iconPadding, 0F)
        this.padding = styledAttrs.getDimension(R.styleable.ThemedButton_padding, 0F)
        this.iconColor = styledAttrs.getColor(R.styleable.ThemedButton_iconColor, defaultTextColor)
        this.iconGravity = styledAttrs.getInt(R.styleable.ThemedButton_iconGravity, Gravity.CENTER)
        this.text = styledAttrs.getString(R.styleable.ThemedButton_text) ?: ""
        this.textSize = styledAttrs.getDimension(R.styleable.ThemedButton_textSize, 15F.px)
        this.textGravity = styledAttrs.getInt(R.styleable.ThemedButton_textGravity, Gravity.CENTER)
        this.textAlign = styledAttrs.getInt(R.styleable.ThemedButton_textAlignment, View.TEXT_ALIGNMENT_CENTER)
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