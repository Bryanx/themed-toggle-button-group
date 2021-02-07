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

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import nl.bryanderidder.themedtogglebuttongroup.R.styleable.*

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
class ThemedButton : RelativeLayout {

    val fontCache: MutableMap<String, Typeface> = mutableMapOf()

    /** Default background [RoundedCornerLayout] when the button is not selected. */
    val cvCard: RoundedCornerLayout = RoundedCornerLayout(context)

    /** Default [TextView] when the button is not selected. */
    val tvText: TextView = TextView(context)

    /** Default icon ([ImageView]) when the button is not selected. */
    val ivIcon: ImageView = ImageView(context)

    /** When the button is selected this [RoundedCornerLayout] is shown. */
    val cvSelectedCard: RoundedCornerLayout = RoundedCornerLayout(context)

    /** When the button is selected this [TextView] is shown. */
    val tvSelectedText: TextView = TextView(context)

    /** When the button is selected this ic on ([ImageView]) is shown. */
    val ivSelectedIcon: ImageView = ImageView(context)

    /** Background color when the button is not selected, default is [lightGray] */
    var bgColor: Int
        get() = (cvCard.background as ColorDrawable).color
        set(value) = cvCard.setBackgroundColor(value)

    /** Background color when the button is selected, default is [denim] */
    var selectedBgColor: Int
        get() = (cvSelectedCard.background as ColorDrawable).color
        set(value) = cvSelectedCard.setBackgroundColor(value)

    /**
     * Color of the text when the button is not selected, default is [darkGray]
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
        set(height) = applyToCards { it.layoutParams.height = height }

    var btnWidth: Int
        get() = cvCard.width
        set(width) = applyToCards { it.layoutParams.width = width }

    var icon: Drawable
        get() = ivIcon.background
        set(icon) {
            ivIcon.setImageDrawable(icon)
            ivIcon.layoutParams = LayoutParams(80.px,80.px)
        }

    var selectedIcon: Drawable
        get() = ivIcon.background
        set(icon) {
            ivSelectedIcon.setImageDrawable(icon)
            ivSelectedIcon.layoutParams = LayoutParams(80.px,80.px)
        }

    var borderWidth: Float
        get() = cvCard.borderWidth
        set(value) { cvCard.borderWidth = value }

    var selectedBorderWidth: Float
        get() = cvSelectedCard.borderWidth
        set(value) { cvSelectedCard.borderWidth = value }

    var borderColor: Int
        get() = cvCard.borderColor
        set(value) { cvCard.borderColor = value }

    var selectedBorderColor: Int
        get() = cvSelectedCard.borderColor
        set(value) { cvSelectedCard.borderColor = value }

    /**
     * The font of the text
     * At the moment the font is the same for selected/deselected button.
     * Put your font file in /src/main/assets and enter the the rest of the path here.
     * For example if your font is in: /src/main/assets/fonts/arial.ttf
     * Enter this in your layout: app:toggle_fontFamily="fonts/arial.ttf"
     *
     * <code>
     *   app:toggle_fontFamily="fonts/arial.ttf"
     * </code>
     *
     * Or to update it programmatically:
     *
     * <code>
     *   button.fontFamily = "fonts/arial.ttf"
     * </code>
     */
    var fontFamily: String = "Roboto"
        set(value) {
            field = value
            applyToTexts { it.typeface = getTypeFace(value) }
        }

    /**
     * Constructor for programmatically creating buttons.
     */
    constructor(ctx: Context) : super(ctx) {
        initialiseViews()
        this.bgColor = lightGray
        this.selectedBgColor = denim
        this.selectedTextColor = context.color(android.R.color.white)
        applyToCards { it.cornerRadius = 155f }
        applyToTexts { it.setPadding(50, 35, 50, 35) }
    }

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
        initialiseViews()
        getStyledAttributes(attrs)
    }

    private fun initialiseViews() {
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
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
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        cvCard.performClick()
    }

    private fun getStyledAttributes(attributeSet: AttributeSet) {
        val attrs = context.obtainStyledAttributes(attributeSet, ThemedButton)
        this.text = attrs.getString(ThemedButton_toggle_text) ?: ""
        this.text = attrs.getString(ThemedButton_android_text) ?: this.text
        this.selectedText = attrs.getString(ThemedButton_toggle_selectedText) ?: this.text
        attrs.getColor(ThemedButton_toggle_backgroundColor, lightGray).also { this.bgColor = it }
        attrs.getColor(ThemedButton_toggle_selectedBackgroundColor, denim).also { this.selectedBgColor = it }
        attrs.getColor(ThemedButton_toggle_textColor, darkGray).also { this.textColor = it }
        attrs.getColor(ThemedButton_toggle_selectedTextColor, context.color(android.R.color.white)).also { this.selectedTextColor = it }
        attrs.getBoolean(ThemedButton_toggle_circularCornerRadius, false).also { this.circularCornerRadius = it }
        attrs.getDimension(ThemedButton_toggle_borderWidth, 0F).also { this.borderWidth = it; this.selectedBorderWidth = it }
        attrs.getDimension(ThemedButton_toggle_selectedBorderWidth, this.borderWidth).also { this.selectedBorderWidth = it }
        attrs.getColor(ThemedButton_toggle_borderColor, cvCard.borderColor).also { borderColor = it; selectedBorderColor = it }
        attrs.getColor(ThemedButton_toggle_selectedBorderColor, this.borderColor).also { this.selectedBorderColor = it }
        attrs.getDimension(ThemedButton_toggle_btnCornerRadius, 21F.px).also { applyToCards { c -> c.cornerRadius = it } }
        attrs.getDimension(ThemedButton_toggle_padding, -1F).also { applyToCards { c -> c.setViewPadding(all=it) } }
        attrs.getDimension(ThemedButton_toggle_paddingHorizontal, -1F).also { applyToCards { c -> c.setViewPadding(horizontal=it) } }
        attrs.getDimension(ThemedButton_toggle_paddingVertical, -1F).also { applyToCards { c -> c.setViewPadding(vertical=it) } }
        attrs.getDimension(ThemedButton_toggle_paddingRight, -1F).also { applyToCards { c -> c.setViewPadding(right=it) } }
        attrs.getDimension(ThemedButton_toggle_paddingTop, -1F).also { applyToCards { c -> c.setViewPadding(top=it) } }
        attrs.getDimension(ThemedButton_toggle_paddingLeft, -1F).also { applyToCards { c -> c.setViewPadding(left=it) } }
        attrs.getDimension(ThemedButton_toggle_paddingBottom, -1F).also { applyToCards { c -> c.setViewPadding(bottom=it) } }
        attrs.getDimension(ThemedButton_toggle_textPadding, -1F).also { applyToTexts { c -> c.setViewPadding(all=it) } }
        attrs.getDimension(ThemedButton_toggle_textPaddingHorizontal, 14.pxf).also { applyToTexts { t -> t.setViewPadding(horizontal=it) } }
        attrs.getDimension(ThemedButton_toggle_textPaddingVertical, -1F).also { applyToTexts { t -> t.setViewPadding(vertical=it) } }
        attrs.getDimension(ThemedButton_toggle_textPaddingRight, -1F).also { applyToTexts { t -> t.setViewPadding(right=it) } }
        attrs.getDimension(ThemedButton_toggle_textPaddingTop, -1F).also { applyToTexts { t -> t.setViewPadding(top=it) } }
        attrs.getDimension(ThemedButton_toggle_textPaddingLeft, -1F).also { applyToTexts { t -> t.setViewPadding(left=it) } }
        attrs.getDimension(ThemedButton_toggle_textPaddingBottom, -1F).also { applyToTexts { t -> t.setViewPadding(bottom=it) } }
        attrs.getDimension(ThemedButton_toggle_iconPadding, -1F).also { applyToIcons { c -> c.setViewPadding(all=it) } }
        attrs.getDimension(ThemedButton_toggle_iconPaddingHorizontal, -1F).also { applyToIcons { i -> i.setViewPadding(horizontal=it) } }
        attrs.getDimension(ThemedButton_toggle_iconPaddingVertical, -1F).also { applyToIcons { i -> i.setViewPadding(vertical=it) } }
        attrs.getDimension(ThemedButton_toggle_iconPaddingRight, -1F).also { applyToIcons { i -> i.setViewPadding(right=it) } }
        attrs.getDimension(ThemedButton_toggle_iconPaddingTop, -1F).also { applyToIcons { i -> i.setViewPadding(top=it) } }
        attrs.getDimension(ThemedButton_toggle_iconPaddingLeft, -1F).also { applyToIcons { i -> i.setViewPadding(left=it) } }
        attrs.getDimension(ThemedButton_toggle_iconPaddingBottom, -1F).also { applyToIcons { i -> i.setViewPadding(bottom=it) } }
        attrs.getDrawable(ThemedButton_toggle_icon)?.let { this.icon = it; this.selectedIcon = it.constantState?.newDrawable()!! }
        attrs.getDrawable(ThemedButton_toggle_selectedIcon)?.let { this.selectedIcon = it }
        attrs.getColor(ThemedButton_toggle_iconColor, textColor).also { ivIcon.setTintColor(it) }
        attrs.getInt(ThemedButton_toggle_iconGravity, Gravity.CENTER).also { applyToIcons { i -> i.layoutGravity = it } }
        attrs.getDimension(ThemedButton_toggle_textSize, 15F.px).also { applyToTexts { t -> t.textSize = it.dp.toFloat() } }
        attrs.getInt(ThemedButton_toggle_textGravity, Gravity.CENTER).also { applyToTexts { i -> i.layoutGravity = it } }
        attrs.getInt(ThemedButton_toggle_textAlignment, 4).also { applyToTexts { t -> if (Build.VERSION.SDK_INT >= 17) t.textAlignment = it } }
        (attrs.getString(ThemedButton_toggle_fontFamily) ?: fontFamily).also { fontFamily = it }
        attrs.recycle()
    }

    private fun getTypeFace(assetPath: String): Typeface? {
        if (assetPath.isEmpty() || assetPath == "Roboto") return Typeface.DEFAULT
        var path = assetPath
        if (path.startsWith("/")) path = path.replaceFirst("/","")
        val typeface = fontCache[path] ?: Typeface.createFromAsset(context.assets, path)
        fontCache.addIfAbsent(path, typeface)
        return typeface
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        btnHeight = heightMeasureSpec
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (circularCornerRadius) {
            applyToCards { it.cornerRadius = (btnHeight.coerceAtMost(btnWidth) / 2.2).toFloat() }
        }
    }

    /**
     * Apply a function to [cvCard] and [cvSelectedCard]
     * This is a convenience method for styling regardless of whether the button is selected.
     */
    fun applyToCards(func: (RoundedCornerLayout) -> Unit) =
        listOf(cvCard, cvSelectedCard).forEach(func::invoke)

    /**
     * Apply a function to [tvText] and [tvSelectedText]
     * This is a convenience method for styling regardless of whether the button is selected.
     */
    fun applyToTexts(func: (TextView) -> Unit) =
        listOf(tvText, tvSelectedText).forEach(func::invoke)

    /**
     * Apply a function to [ivIcon] and [ivSelectedIcon]
     * This is a convenience method for styling regardless of whether the button is selected.
     */
    fun applyToIcons(func: (ImageView) -> Unit) =
        listOf(ivIcon, ivSelectedIcon).forEach(func::invoke)
}