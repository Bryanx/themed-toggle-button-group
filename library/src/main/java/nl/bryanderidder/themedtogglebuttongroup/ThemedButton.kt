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
import androidx.core.view.marginStart

/**
 * A customisable button that can contain an icon and/or text.
 *
 * @author Bryan de Ridder
 */
class ThemedButton(ctx: Context, attrs: AttributeSet) : RelativeLayout(ctx, attrs) {
    var cbCardView: CardView = CardView(ctx)
    var cbText: TextView = TextView(ctx)
    var cbIcon: ImageView = ImageView(ctx)
    var cbCardViewHighlight: CardView = CardView(ctx)
    var cbTextHighlight: TextView = TextView(ctx)
    var cbIconHighlight: ImageView = ImageView(ctx)

    /** Background color when the button is not selected, default is [R.color.lightGray] */
    var bgColor: Int = ctx.color(R.color.lightGray)

    /** Background color when the button is selected, default is [R.color.denim] */
    var selectedBgColor: Int = ctx.color(R.color.denim)

    /** Color of the text when the button is not selected, default is [R.color.darkGray] */
    var textColor: Int = R.color.darkGray

    /** Color of the text when the button is not selected, default is [android.R.color.white] */
    var selectedTextColor: Int = ctx.color(android.R.color.white)

    /** If this property is set to true, the cornerradius is override and made circular, default is false */
    var circularCornerRadius: Boolean = false

    var text: String
        get() = cbText.string
        set(text) = applyToText { it.text = text }

    var textSize: Float
        get() = cbText.textSize
        set(size) = applyToText { it.textSize = size.dp.toFloat() }

    var cornerRadius: Float
        get() = cbCardView.radius
        set(cornerRadius) = applyToCards { it.radius = cornerRadius }

    var btnHeight: Int
        get() = cbCardView.height
        set(height) = applyToCards { it.layoutParams.height = height.dp }

    var btnWidth: Int
        get() = cbCardView.width
        set(width) = applyToCards { it.layoutParams.width = width.dp }

    var padding: Float
        get() = cbCardView.paddingTop.toFloat()
        set(padding) = applyToCards { it.setContentPadding(padding.toInt(),padding.toInt(),padding.toInt(),padding.toInt()) }

    var textPaddingHorizontal: Float
        get() = cbText.paddingHorizontal
        set(padding) = applyToText { it.paddingHorizontal = padding }


    var textPaddingVertical: Float
        get() = cbText.paddingVertical
        set(padding) = applyToText { it.paddingVertical = padding }

    var paddingHorizontal: Float
        get() = cbCardView.contentPaddingHorizontal
        set(padding) = applyToCards { it.contentPaddingHorizontal = padding }

    var paddingVertical: Float
        get() = cbCardView.contentPaddingVertical
        set(padding) = applyToCards { it.contentPaddingVertical = padding }

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
        set(position) = applyToText {
            it.layoutParams = FrameLayout.LayoutParams(
                cbTextHighlight.layoutParams.width,
                cbTextHighlight.layoutParams.height,
                position
            )
        }

    var textAlign: Int
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        get() = cbText.textAlignment
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        set(alignment) = applyToText { it.textAlignment = alignment }

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

        getStyledAttributes(attrs)
        initialiseViews()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        cbCardView.performClick()
    }

    private fun getStyledAttributes(attrs: AttributeSet) {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.ThemedButton)
        this.bgColor = styledAttrs.getColor(R.styleable.ThemedButton_toggle_backgroundColor, bgColor)
        this.selectedBgColor = styledAttrs.getColor(R.styleable.ThemedButton_toggle_highlightBackgroundColor, selectedBgColor)
        this.selectedTextColor = styledAttrs.getColor(R.styleable.ThemedButton_toggle_highLightTextColor, selectedTextColor)
        this.textColor = styledAttrs.getColor(R.styleable.ThemedButton_toggle_textColor, textColor)
        this.cornerRadius = styledAttrs.getDimension(R.styleable.ThemedButton_toggle_btnCornerRadius, 21F.px)
        this.paddingHorizontal = styledAttrs.getDimension(R.styleable.ThemedButton_toggle_paddingHorizontal, 0F)
        this.paddingVertical = styledAttrs.getDimension(R.styleable.ThemedButton_toggle_paddingVertical, 0F)
        this.textPaddingHorizontal = styledAttrs.getDimension(R.styleable.ThemedButton_toggle_textPaddingHorizontal, 14.px.toFloat())
        this.textPaddingVertical = styledAttrs.getDimension(R.styleable.ThemedButton_toggle_textPaddingVertical, 0F)
        this.iconPadding = styledAttrs.getDimension(R.styleable.ThemedButton_toggle_iconPadding, 0F)
        this.padding = styledAttrs.getDimension(R.styleable.ThemedButton_toggle_padding, 0F)
        styledAttrs.getDrawable(R.styleable.ThemedButton_toggle_icon)?.let { this.icon = it }
        this.iconColor = styledAttrs.getColor(R.styleable.ThemedButton_toggle_iconColor, textColor)
        this.iconGravity = styledAttrs.getInt(R.styleable.ThemedButton_toggle_iconGravity, Gravity.CENTER)
        this.text = styledAttrs.getString(R.styleable.ThemedButton_toggle_text) ?: ""
        this.textSize = styledAttrs.getDimension(R.styleable.ThemedButton_toggle_textSize, 15F.px)
        this.textGravity = styledAttrs.getInt(R.styleable.ThemedButton_toggle_textGravity, Gravity.CENTER)
        this.textAlign = styledAttrs.getInt(R.styleable.ThemedButton_toggle_textAlignment, 4)
        this.circularCornerRadius = styledAttrs.getBoolean(R.styleable.ThemedButton_toggle_circularCornerRadius, false)
        styledAttrs.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        btnHeight = heightMeasureSpec
        btnWidth = widthMeasureSpec
        setMargin(leftMargin= 4.px, rightMargin= 4.px)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (circularCornerRadius) {
            cornerRadius = (btnHeight.coerceAtMost(btnWidth) / 2.2).toFloat()
        }
    }

    private fun applyToCards(func: (CardView) -> Unit) =
        listOf(cbCardView, cbCardViewHighlight).forEach { func.invoke(it) }

    private fun applyToText(func: (TextView) -> Unit) =
        listOf(cbText, cbTextHighlight).forEach { func.invoke(it) }

    fun initialiseViews() {
        setPadding(0,0,0,0)
        btnBackgroundColor = bgColor
        cbText.setTextColor(textColor)
    }
}