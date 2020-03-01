package nl.bryanderidder.themedtogglebuttongroup

import android.content.*
import android.graphics.drawable.*
import android.os.*
import android.util.*
import android.view.*
import android.view.animation.*
import android.widget.*
import androidx.annotation.*
import androidx.cardview.widget.*
import androidx.core.animation.*
import kotlinx.android.synthetic.main.view_themedbutton.view.*

/**
 * Custom button with rounded corners.
 * @author Bryan de Ridder
 */
class ThemedButton(ctx: Context, attrs: AttributeSet) : RelativeLayout(ctx, attrs) {
    private val defaultCornerRadius: Float = 22F
    private val defaultText: String = "CLICK"
    private val tvTextHighlight: TextView
    private val cardViewHighlight: CardView
    private val ivIconHighlight: ImageView
    private val tvText: TextView
    private val cardView: CardView
    private val ivIcon: ImageView
    private var circularCornerRadius: Boolean = false

    val defaultTextColor: Int = ctx.color(R.color.darkGray)
    val defaultBackgroundColor: Int = ctx.color(R.color.lightGray)

    var textColor: Int
        get() = tvText.currentTextColor
        set(textColor) = tvText.setTextColor(textColor)

    var text: String
        get() = tvText.string
        set(text) {
            tvText.text = text
            tvTextHighlight.text = text
        }

    var cornerRadius: Float
        get() = cardView.radius
        set(cornerRadius) {
            cardView.radius = cornerRadius
            cardViewHighlight.radius = cornerRadius
        }

    var btnHeight: Int
        get() = cardView.height
        set(btnHeight) {
            if (btnHeight == -2) cardView.layoutParams.height = 150.dp
            cardView.layoutParams.height = btnHeight.dp
        }

    var btnWidth: Int
        get() = cardView.width
        set(btnWidth) {
            cardView.layoutParams.width = btnWidth
            cardViewHighlight.layoutParams.width = btnWidth
        }

    var paddingHorizontal: Int
        get() = cbText.paddingStart
        set(paddingHorizontal) {
            cbText.setPadding(
                paddingHorizontal.px,
                cbText.paddingTop,
                paddingHorizontal.px,
                cbText.paddingBottom
            )
            cbTextHighlight.setPadding(
                paddingHorizontal.px,
                cbText.paddingTop,
                paddingHorizontal.px,
                cbText.paddingBottom
            )
        }

    var btnBackgroundColor: Int
        get() = cardView.cardBackgroundColor.defaultColor
        set(btnBackgroundColor) = cardView.setCardBackgroundColor(btnBackgroundColor)

    var icon: Drawable
        get() = ivIcon.background
        set(icon) {
            ivIcon.setImageDrawable(icon)
            ivIcon.visibility = VISIBLE
            ivIconHighlight.setImageDrawable(icon)
            ivIconHighlight.visibility = VISIBLE
        }

    var iconColor: Int
        get() = ivIcon.solidColor
        set(iconColor) = ivIcon.setTintColor(iconColor)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_themedbutton, this)
        tvText = findViewById(R.id.cbText)
        cardView = findViewById(R.id.cbCardView)
        ivIcon = findViewById(R.id.cbIcon)
        tvTextHighlight = findViewById(R.id.cbTextHighlight)
        cardViewHighlight = findViewById(R.id.cbCardViewHighlight)
        ivIconHighlight = findViewById(R.id.cbIconHighlight)
        handleStyledAttributes(attrs)
    }

    private fun handleStyledAttributes(attrs: AttributeSet) {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.ThemedButton)
        this.btnBackgroundColor = styledAttrs.getInt(R.styleable.ThemedButton_backgroundColor, defaultBackgroundColor)
        this.textColor = styledAttrs.getInt(R.styleable.ThemedButton_textColor, defaultTextColor)
        this.cornerRadius = styledAttrs.getDimension(R.styleable.ThemedButton_btnCornerRadius, defaultCornerRadius)
        this.iconColor = styledAttrs.getInt(R.styleable.ThemedButton_iconColor, defaultTextColor)
        this.text = styledAttrs.getString(R.styleable.ThemedButton_text) ?: defaultText
        this.circularCornerRadius = (styledAttrs.getBoolean(R.styleable.ThemedButton_circularCornerRadius, false))
        styledAttrs.getDrawable(R.styleable.ThemedButton_icon)?.let { this.icon = it }
        styledAttrs.recycle()
    }

    fun animateBg(bgColor: Int, x: Float, y: Float) = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
            val reveal = ViewAnimationUtils.createCircularReveal(
                cardViewHighlight,
                x.toInt(),
                y.toInt(),
                0.toFloat(),
                (cardView.width.coerceAtLeast(cardView.height)).toFloat()
            )
            reveal.interpolator = AccelerateDecelerateInterpolator()
            reveal.duration = 400
            reveal.doOnStart { cardViewHighlight.visibility = View.VISIBLE }
            reveal.start()
        }
        else -> cardView.setCardBackgroundColor(bgColor)
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