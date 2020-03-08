package nl.bryanderidder.themedtogglebuttongroup

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView

/**
 * A customisable button that can contain an icon and/or text.
 *
 * @author Bryan de Ridder
 */
class ThemedButton(ctx: Context, attrs: AttributeSet) : RelativeLayout(ctx, attrs) {
    var circularCornerRadius: Boolean = false
    var cbCardView: CardView = CardView(ctx)
    var cbText: TextView = TextView(ctx)
    var cbIcon: ImageView = ImageView(ctx)
    var cbCardViewHighlight: CardView = CardView(ctx)
    var cbTextHighlight: TextView = TextView(ctx)
    var cbIconHighlight: ImageView = ImageView(ctx)

    var defaultBgColor: Int = ctx.color(R.color.lightGray)
    var highlightBgColor: Int = ctx.color(R.color.colorPrimary)

    var defaultTextColor: Int = R.color.darkGray
    var defaultHighLightTextColor: Int = ctx.color(android.R.color.white)

    var textColor: Int
        get() = cbText.currentTextColor
        set(textColor) = cbText.setTextColor(textColor)

    var highLightTextColor: Int
        get() = cbTextHighlight.currentTextColor
        set(textColor) = cbTextHighlight.setTextColor(textColor)

    var text: String
        get() = cbText.string
        set(text) {
            cbText.text = text
            cbTextHighlight.text = text
        }

    var textSize: Float
        get() = cbText.textSize
        set(size) {
            cbText.textSize = size.dp.toFloat()
            cbTextHighlight.textSize = size.dp.toFloat()
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
            cbIcon.layoutParams = LayoutParams(80.px,80.px)
            cbIconHighlight.layoutParams = LayoutParams(80.px,80.px)
        }

    var iconPadding: Float
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        get() = cbIcon.paddingStart.toFloat()
        set(padding) {
            cbIcon.setPadding(padding.dp, padding.dp, padding.dp, padding.dp)
            cbIconHighlight.setPadding(padding.dp, padding.dp, padding.dp, padding.dp)
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
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        get() = cbText.textAlignment
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        set(alignment) {
            cbText.textAlignment = alignment
            cbTextHighlight.textAlignment = alignment
        }

    var iconColor: Int
        get() = cbIcon.solidColor
        set(iconColor) = cbIcon.setTintColor(iconColor)

    init {
        layoutParams = LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
        cbCardView.layoutParams = LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
        cbCardViewHighlight.layoutParams = LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
        cbIcon.layoutParams = LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
        cbIconHighlight.layoutParams = LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
        cbText.layoutParams = LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
        cbTextHighlight.layoutParams = LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
        getStyledAttributes(attrs)
        initialiseViews()
        cbCardView.cardElevation = 0F
        cbCardView.preventCornerOverlap = false
        cbCardView.useCompatPadding = false
        cbCardViewHighlight.cardElevation = 0F
        cbCardViewHighlight.preventCornerOverlap = false
        cbCardViewHighlight.useCompatPadding = false
        cbCardViewHighlight.visibility = GONE

        cbIcon.adjustViewBounds = true
        cbIcon.scaleType = ImageView.ScaleType.FIT_XY
        cbIconHighlight.adjustViewBounds = true
        cbIconHighlight.scaleType = ImageView.ScaleType.FIT_XY
        cbCardView.addRipple()

        addView(cbCardView)
        addView(cbCardViewHighlight)

        cbCardView.addView(cbIcon)
        cbCardView.addView(cbText)

        cbCardViewHighlight.addView(cbIconHighlight)
        cbCardViewHighlight.addView(cbTextHighlight)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        cbCardView.performClick()
    }

    private fun getStyledAttributes(attrs: AttributeSet) {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.ThemedButton)
        this.defaultBgColor = styledAttrs.getColor(R.styleable.ThemedButton_toggle_backgroundColor, defaultBgColor)
        this.highlightBgColor = styledAttrs.getColor(R.styleable.ThemedButton_toggle_highlightBackgroundColor, highlightBgColor)
        this.defaultHighLightTextColor = styledAttrs.getColor(R.styleable.ThemedButton_toggle_highLightTextColor, defaultHighLightTextColor)
        this.defaultTextColor = styledAttrs.getColor(R.styleable.ThemedButton_toggle_textColor, defaultTextColor)
        this.cornerRadius = styledAttrs.getDimension(R.styleable.ThemedButton_toggle_btnCornerRadius, 15F.px)
        this.paddingHorizontal = styledAttrs.getDimension(R.styleable.ThemedButton_toggle_paddingHorizontal, 0F)
        this.paddingVertical = styledAttrs.getDimension(R.styleable.ThemedButton_toggle_paddingVertical, 0F)
        this.textPaddingHorizontal = styledAttrs.getDimension(R.styleable.ThemedButton_toggle_textPaddingHorizontal, 0F)
        this.textPaddingVertical = styledAttrs.getDimension(R.styleable.ThemedButton_toggle_textPaddingVertical, 0F)
        this.iconPadding = styledAttrs.getDimension(R.styleable.ThemedButton_toggle_iconPadding, 0F)
        this.padding = styledAttrs.getDimension(R.styleable.ThemedButton_toggle_padding, 0F)
        styledAttrs.getDrawable(R.styleable.ThemedButton_toggle_icon)?.let { this.icon = it }
        this.iconColor = styledAttrs.getColor(R.styleable.ThemedButton_toggle_iconColor, defaultTextColor)
        this.iconGravity = styledAttrs.getInt(R.styleable.ThemedButton_toggle_iconGravity, Gravity.CENTER)
        this.text = styledAttrs.getString(R.styleable.ThemedButton_toggle_text) ?: ""
        this.textSize = styledAttrs.getDimension(R.styleable.ThemedButton_toggle_textSize, 15F.px)
        this.textGravity = styledAttrs.getInt(R.styleable.ThemedButton_toggle_textGravity, Gravity.CENTER)
        this.textAlign = styledAttrs.getInt(R.styleable.ThemedButton_toggle_textAlignment, View.TEXT_ALIGNMENT_CENTER)
        this.circularCornerRadius = styledAttrs.getBoolean(R.styleable.ThemedButton_toggle_circularCornerRadius, false)
        styledAttrs.recycle()
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

    fun initialiseViews() {
        setPadding(0,0,0,0)
        btnBackgroundColor = defaultBgColor
        textColor = defaultTextColor
    }
}