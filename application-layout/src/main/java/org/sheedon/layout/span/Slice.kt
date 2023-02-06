package org.sheedon.layout.span

import android.graphics.Color
import android.graphics.Typeface

/**
 * java类作用描述
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/12/13 12:55
 */
class Slice(builder: Builder) {

    private var text = builder.text
    private var textColor = builder.textColor
    private var textSize = builder.textSize
    private var backgroundColor = builder.backgroundColor
    private var textSizeRelative = builder.textSizeRelative
    private var style = builder.style
    private var underline = builder.underline
    private var superscript = builder.superscript
    private var strike = builder.strike
    private var subscript = builder.subscript
    private var sliceId = builder.sliceId
    private var onTextClick: OnTextClick? = builder.onTextClick
    private var isRounded = builder.isRounded
    private var isCircle = builder.isCircle
    private var cornerRadius = builder.cornerRadius
    private var circleRadius = builder.circleRadius

    fun isRounded(): Boolean {
        return isRounded
    }

    fun isCircle(): Boolean {
        return isCircle
    }

    fun getCornerRadius(): Int {
        return cornerRadius
    }

    fun getCircleRadius(): Int {
        return circleRadius
    }


    class Builder(val text: String = "") {
        internal var textSize: Int = DEFAULT_ABSOLUTE_TEXT_SIZE
        internal var textColor: Int = Color.BLACK
        internal var backgroundColor = -1
        internal var textSizeRelative: Float = DEFAULT_RELATIVE_TEXT_SIZE
        internal var style: Int = Typeface.NORMAL
        internal var underline = false
        internal var strike = false
        internal var superscript = false
        internal var subscript = false
        internal var onTextClick: OnTextClick? = null
        internal var sliceId = 0
        internal var isRounded = false
        internal var isCircle = false
        internal var cornerRadius = 0
        internal var circleRadius = 0

        /**
         * Sets the absolute text size.
         *
         * @param sliceId text sliceId
         * @return a Builder
         */
        fun setSliceId(sliceId: Int): Builder {
            this.sliceId = sliceId
            return this
        }

        fun setCornerRadius(cornerRadius: Int): Builder {
            isRounded = true
            this.cornerRadius = cornerRadius
            return this
        }

        /**
         * Sets the absolute text size.
         *
         * @param textSize text size in pixels
         * @return a Builder
         */
        fun textSize(textSize: Int): Builder {
            this.textSize = textSize
            return this
        }

        /**
         * Sets the text color.
         *
         * @param textColor the color
         * @return a Builder
         */
        fun textColor(textColor: Int): Builder {
            this.textColor = textColor
            return this
        }

        /**
         * Sets the background color.
         *
         * @param backgroundColor the color
         * @return a Builder
         */
        fun backgroundColor(backgroundColor: Int): Builder {
            this.backgroundColor = backgroundColor
            return this
        }

        /**
         * Sets the relative text size.
         *
         * @param textSizeRelative relative text size
         * @return a Builder
         */
        fun textSizeRelative(textSizeRelative: Float): Builder {
            this.textSizeRelative = textSizeRelative
            return this
        }

        /**
         * Sets the text click event.
         *
         * @param onTextClick text click
         * @return a Builder
         */
        fun setOnTextClick(onTextClick: OnTextClick?): Builder {
            this.onTextClick = onTextClick
            return this
        }

        /**
         * Sets a style to this Slice.
         *
         * @param style see [android.graphics.Typeface]
         * @return a Builder
         */
        fun style(style: Int): Builder {
            this.style = style
            return this
        }

        /**
         * Underlines this Piece.
         *
         * @return a Builder
         */
        fun underline(): Builder {
            underline = true
            return this
        }

        /**
         * Strikes this Piece.
         *
         * @return a Builder
         */
        fun strike(): Builder {
            strike = true
            return this
        }

        /**
         * Sets this Piece as a superscript.
         *
         * @return a Builder
         */
        fun superscript(): Builder {
            superscript = true
            return this
        }

        /**
         * Sets this Piece as a subscript.
         *
         * @return a Builder
         */
        fun subscript(): Builder {
            subscript = true
            return this
        }

        /**
         * Creates a with the customized
         * parameters.
         *
         * @return a Slice
         */
        fun build(): Slice {
            return Slice(this)
        }

        fun setCircleRadius(circleRadius: Int): Builder {
            this.circleRadius = circleRadius
            isCircle = true
            return this
        }
    }


    /**
     * Sets the text color of this Piece. If you're creating a new Slice, you should do so using it's
     *
     *
     * Use this method if you want to change the text color of an existing Slice that is already
     * displayed. After doing so, you MUST call `display()` for the changes to show up.
     *
     * @param textColor of text (it is NOT android Color resources ID, use getResources().getColor(R.color.colorId) for it)
     */
    fun setTextColor(textColor: Int) {
        this.textColor = textColor
    }

    /**
     * Sets the text of this Slice. If you're creating a new Slice, you should do so using it's
     *
     *
     * Use this method if you want to modify the text of an existing Slice that is already
     * displayed. After doing so, you MUST call `display()` for the changes to show up.
     *
     * @param text the text to display
     */
    fun setText(text: String) {
        this.text = text
    }


    fun getText(): String {
        return text
    }

    fun getTextColor(): Int {
        return textColor
    }

    fun getTextSize(): Int {
        return textSize
    }

    fun getBackgroundColor(): Int {
        return backgroundColor
    }

    fun getTextSizeRelative(): Float {
        return textSizeRelative
    }

    fun getStyle(): Int {
        return style
    }

    fun isUnderline(): Boolean {
        return underline
    }

    fun isSuperscript(): Boolean {
        return superscript
    }

    fun isStrike(): Boolean {
        return strike
    }

    fun isSubscript(): Boolean {
        return subscript
    }

    fun getOnTextClick(): OnTextClick? {
        return onTextClick
    }

    fun getSliceId(): Int {
        return sliceId
    }

    companion object {
        internal var DEFAULT_ABSOLUTE_TEXT_SIZE = 0
        private const val DEFAULT_RELATIVE_TEXT_SIZE = 1f
    }
}