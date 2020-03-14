package nl.bryanderidder.themedtogglebuttongroup

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView


/**
 * A customisable button that can contain an icon and/or text.
 *
 * It has the following structure:
 * [ThemedButton] : [RelativeLayout]
 *  [cvCard] : [RoundedCornerLayout]
 *   [tvText] : [TextView]
 *   [ivIcon] : [ImageView]
 *  [cvSelectedCard] : [RoundedCornerLayout]
 *   [tvSelectedText] : [TextView]
 *   [ivSelectedIcon] : [ImageView]
 *
 * @author Bryan de Ridder
 */
class ThemedButton(ctx: Context, attrs: AttributeSet) : RelativeLayout(ctx, attrs) {

    /** Default background [RoundedCornerLayout] when the button is not selected. */
    val cvCard: RoundedCornerLayout = RoundedCornerLayout(ctx)

    /** Default [TextView] when the button is not selected. */
    val tvText: TextView = TextView(ctx)

    /** Default icon ([ImageView]) when the button is not selected. */
    val ivIcon: ImageView = ImageView(ctx)

    /** When the button is selected this [RoundedCornerLayout] is shown. */
    val cvSelectedCard: RoundedCornerLayout = RoundedCornerLayout(ctx)

    /** When the button is selected this [TextView] is shown. */
    val tvSelectedText: TextView = TextView(ctx)

    /** When the button is selected this ic on ([ImageView]) is shown. */
    val ivSelectedIcon: ImageView = ImageView(ctx)

    /** Background color when the button is not selected, default is [R.color.lightGray] */
    var bgColor: Int
        get() = (cvCard.background as ColorDrawable).color
        set(value) = cvCard.setBackgroundColor(value)

    /** Background color when the button is selected, default is [R.color.denim] */
    var selectedBgColor: Int
        get() = (cvSelectedCard.background as ColorDrawable).color
        set(value) = cvSelectedCard.setBackgroundColor(value)

    /**
     * Color of the text when the button is not selected, default is [R.color.darkGray]
     * If the unselected icon color is not set, it will be the same as this value.
     */
    var textColor: Int
        get() = tvText.currentTextColor
        set(value) {
            tvText.setTextColor(value)
            ivIcon.setTintColor(value)
        }

    /**
     * Color of the text when the button is selected, default is [android.R.color.white]
     * If the selected icon color is not set, it will be the same as this value.
     */
    var selectedTextColor: Int
        get() = tvSelectedText.currentTextColor
        set(value) {
            tvSelectedText.setTextColor(value)
            ivSelectedIcon.setTintColor(value)
        }

    /** If this property is set to true, the corner radius is overridden and made circular, default is false */
    var circularCornerRadius: Boolean = false

    var text: String
        get() = tvText.text.toString()
        set(value) = applyToTexts { it.text = value }

    var selectedText: String
        get() = tvSelectedText.text.toString()
        set(value) = tvSelectedText.setText(value)

    var btnHeight: Int
        get() = cvCard.height
        set(height) = applyToCards { it.layoutParams.height = height.dp }

    var btnWidth: Int
        get() = cvCard.width
        set(width) = applyToCards { it.layoutParams.width = width.dp }

    var icon: Drawable
        get() = ivIcon.background
        set(icon) {
            ivIcon.setImageDrawable(icon)
            ivSelectedIcon.setImageDrawable(icon.constantState?.newDrawable())
            applyToIcons { it.visibility = VISIBLE }
            applyToIcons { it.layoutParams = LayoutParams(80.px,80.px) }
        }

    init {
        layoutParams = LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
        applyToIcons { it.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT) }
        applyToTexts { it.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT) }
        applyToCards { it.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT) }
        applyToIcons { it.adjustViewBounds = true }
        applyToIcons { it.scaleType = ImageView.ScaleType.FIT_CENTER }
        cvSelectedCard.visibility = GONE
        addView(cvCard)
        addView(cvSelectedCard)
        cvCard.addView(ivIcon)
        cvCard.addView(tvText)
        cvSelectedCard.addView(ivSelectedIcon)
        cvSelectedCard.addView(tvSelectedText)
        getStyledAttributes(attrs)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        cvCard.performClick()
    }

    private fun getStyledAttributes(attributeSet: AttributeSet) {
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.ThemedButton)
        this.text = attrs.getString(R.styleable.ThemedButton_toggle_text) ?: ""
        this.text = attrs.getString(R.styleable.ThemedButton_android_text) ?: this.text
        this.selectedText = attrs.getString(R.styleable.ThemedButton_toggle_selectedText) ?: this.text
        this.bgColor = attrs.getColor(R.styleable.ThemedButton_toggle_backgroundColor, context.color(R.color.lightGray))
        this.selectedBgColor = attrs.getColor(R.styleable.ThemedButton_toggle_selectedBackgroundColor, context.color(R.color.denim))
        this.textColor = attrs.getColor(R.styleable.ThemedButton_toggle_textColor, context.color(R.color.darkGray))
        this.selectedTextColor = attrs.getColor(R.styleable.ThemedButton_toggle_selectedTextColor, context.color(android.R.color.white))
        this.circularCornerRadius = attrs.getBoolean(R.styleable.ThemedButton_toggle_circularCornerRadius, false)
        attrs.getDimension(R.styleable.ThemedButton_toggle_btnCornerRadius, 21F.px).also { applyToCards { c -> c.cornerRadius = it } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_padding, -1F).also { applyToCards { c -> c.setViewPadding(all=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_paddingHorizontal, -1F).also { applyToCards { c -> c.setViewPadding(horizontal=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_paddingVertical, -1F).also { applyToCards { c -> c.setViewPadding(vertical=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_paddingRight, -1F).also { applyToCards { c -> c.setViewPadding(right=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_paddingTop, -1F).also { applyToCards { c -> c.setViewPadding(top=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_paddingLeft, -1F).also { applyToCards { c -> c.setViewPadding(left=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_paddingBottom, -1F).also { applyToCards { c -> c.setViewPadding(bottom=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_textPadding, -1F).also { applyToTexts { c -> c.setViewPadding(all=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_textPaddingHorizontal, 14.pxf).also { applyToTexts { t -> t.setViewPadding(horizontal=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_textPaddingVertical, -1F).also { applyToTexts { t -> t.setViewPadding(vertical=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_textPaddingRight, -1F).also { applyToTexts { t -> t.setViewPadding(right=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_textPaddingTop, -1F).also { applyToTexts { t -> t.setViewPadding(top=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_textPaddingLeft, -1F).also { applyToTexts { t -> t.setViewPadding(left=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_textPaddingBottom, -1F).also { applyToTexts { t -> t.setViewPadding(bottom=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_iconPadding, -1F).also { applyToIcons { c -> c.setViewPadding(all=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_iconPaddingHorizontal, -1F).also { applyToIcons { i -> i.setViewPadding(horizontal=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_iconPaddingVertical, -1F).also { applyToIcons { i -> i.setViewPadding(vertical=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_iconPaddingRight, -1F).also { applyToIcons { i -> i.setViewPadding(right=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_iconPaddingTop, -1F).also { applyToIcons { i -> i.setViewPadding(top=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_iconPaddingLeft, -1F).also { applyToIcons { i -> i.setViewPadding(left=it) } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_iconPaddingBottom, -1F).also { applyToIcons { i -> i.setViewPadding(bottom=it) } }
        attrs.getDrawable(R.styleable.ThemedButton_toggle_icon)?.let { this.icon = it }
        attrs.getColor(R.styleable.ThemedButton_toggle_iconColor, textColor).also { ivIcon.setTintColor(it) }
        attrs.getInt(R.styleable.ThemedButton_toggle_iconGravity, Gravity.CENTER).also { applyToIcons { i -> i.layoutGravity = it } }
        attrs.getDimension(R.styleable.ThemedButton_toggle_textSize, 15F.px).also { applyToTexts { t -> t.textSize = it.dp.toFloat() } }
        attrs.getInt(R.styleable.ThemedButton_toggle_textGravity, Gravity.CENTER).also { applyToTexts { i -> i.layoutGravity = it } }
        attrs.getInt(R.styleable.ThemedButton_toggle_textAlignment, 4).also { applyToTexts { t -> if (Build.VERSION.SDK_INT >= 17) t.textAlignment = it } }
        attrs.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        btnHeight = heightMeasureSpec
        btnWidth = widthMeasureSpec
        setMargin(leftMargin = 4.px, rightMargin = 4.px)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (circularCornerRadius) {
            applyToCards { it.cornerRadius = (btnHeight.coerceAtMost(btnWidth) / 2.2).toFloat() }
        }
    }

    private fun applyToCards(func: (RoundedCornerLayout) -> Unit) =
        listOf(cvCard, cvSelectedCard).forEach(func::invoke)

    private fun applyToTexts(func: (TextView) -> Unit) =
        listOf(tvText, tvSelectedText).forEach(func::invoke)

    private fun applyToIcons(func: (ImageView) -> Unit) =
        listOf(ivIcon, ivSelectedIcon).forEach(func::invoke)
}