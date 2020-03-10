package nl.bryanderidder.themedtogglebuttongroup

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView

class SelectDeselectButton(ctx: Context) : CardView(ctx) {
    var circularCornerRadius: Boolean = false

    var cbText: TextView = TextView(ctx)
    var cbIcon: ImageView = ImageView(ctx)

    var text: String
        get() = cbText.string
        set(text) {
            cbText.text = text
        }

    var textColor: Int
        get() = cbText.currentTextColor
        set(textColor) = cbText.setTextColor(textColor)

    var textSize: Float
        get() = cbText.textSize
        set(size) {
            cbText.textSize = size.dp.toFloat()
        }

    var btnHeight: Int
        get() = height
        set(height) {
            layoutParams.height = height.dp
        }

    var btnWidth: Int
        get() = width
        set(btnWidth) {
            layoutParams.width = btnWidth.dp
        }

    var padding: Float
        get() = 0F
        set(padding) = setContentPadding(
            padding.toInt(),
            padding.toInt(),
            padding.toInt(),
            padding.toInt()
        )

    var textPaddingHorizontal: Float
        get() = cbText.paddingHorizontal
        set(padding) {
            cbText.paddingHorizontal = padding
        }

    var textPaddingVertical: Float
        get() = cbText.paddingVertical
        set(padding) {
            cbText.paddingVertical = padding
        }

    var paddingHorizontal: Float
        get() = contentPaddingHorizontal
        set(padding) {
            contentPaddingHorizontal = padding
        }

    var paddingVertical: Float
        get() = contentPaddingVertical
        set(padding) {
            contentPaddingVertical = padding
        }

    var btnBackgroundColor: Int
        get() = cardBackgroundColor.defaultColor
        set(btnBackgroundColor) = setCardBackgroundColor(btnBackgroundColor)

    var icon: Drawable
        get() = cbIcon.background
        set(icon) = cbIcon.setImageDrawable(icon)

    var iconPadding: Float
        get() = cbIcon.paddingTop.toFloat()
        set(padding) = cbIcon.setPadding(padding.dp, padding.dp, padding.dp, padding.dp)

    var iconGravity: Int
        get() = (cbIcon.layoutParams as LayoutParams).gravity
        set(position) {
            cbIcon.layoutParams = LayoutParams(
                cbIcon.layoutParams.width,
                cbIcon.layoutParams.height,
                position
            )
        }

    var textGravity: Int
        get() = (cbText.layoutParams as LayoutParams).gravity
        set(position) {
            cbText.layoutParams = LayoutParams(
                cbText.layoutParams.width,
                cbText.layoutParams.height,
                position
            )
        }

    var textAlign: Int
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        get() = cbText.textAlignment
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        set(alignment) {
            cbText.textAlignment = alignment
        }

    var iconColor: Int
        get() = cbIcon.solidColor
        set(iconColor) = cbIcon.setTintColor(iconColor)

    init {
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        cbText.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        cbIcon.layoutParams = LayoutParams(80.px, 80.px)
        isClickable = true
        isFocusable = true
        cardElevation = 0F
        preventCornerOverlap = false
        useCompatPadding = false
        cbIcon.adjustViewBounds = true
        cbIcon.scaleType = ImageView.ScaleType.FIT_XY
    }
}