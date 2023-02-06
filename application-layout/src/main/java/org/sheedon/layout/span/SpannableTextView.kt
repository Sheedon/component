package org.sheedon.layout.span

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView


/**
 * @author Milap Tank
 *         Email milaptank@gmail.com
 *         Project SpannableTextView - Android
 *         desc SpannableTextView.java  is for extra power to #TextView
 * @since 23/11/17  3:48 PM
 */
class SpannableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var sliceList: MutableList<Slice> = ArrayList()

    init {
        Slice.DEFAULT_ABSOLUTE_TEXT_SIZE = textSize.toInt()
    }

    /**
     * Use this method to add a [Slice] to a SuperTextView.
     * Each [Slice] is added sequentially, so the
     * order you call this method matters.
     *
     * @param slice the Slice
     */
    fun addSlice(slice: Slice) {
        sliceList.add(slice)
    }

    /**
     * Adds a Slice at this specific location. The underlying data structure is a
     * [java.util.List], so expect the same type of behaviour.
     *
     * @param slice    the Slice to add.
     * @param location the index at which to add.
     */
    fun addSlice(slice: Slice, location: Int) {
        sliceList.add(location, slice)
    }

    /**
     * Replaces the Slice at the specified location with this new Slice. The underlying data
     * structure is a [java.util.List], so expect the same type of behaviour.
     *
     * @param newSlice the Slice to insert.
     * @param location the index at which to insert.
     */
    fun replaceSliceAt(location: Int, newSlice: Slice) {
        sliceList[location] = newSlice
    }

    /**
     * Removes the Slice at this specified location. The underlying data structure is a
     * [java.util.List], so expect the same type of behaviour.
     *
     * @param location the index of the Slice to remove
     */
    fun removeSlice(location: Int) {
        sliceList.removeAt(location)
    }

    /**
     * Get a specific [Slice] in position index.
     *
     * @param location position of Piece (0 based)
     * @return Slice o null if invalid index
     */
    fun getSlice(location: Int): Slice? {
        return if (location >= 0 && location < sliceList.size) {
            sliceList[location]
        } else null
    }

    /**
     * Call this method when you're done adding [Slice]s
     * and want this TextView to display the final, styled version of it's String contents.
     *
     *
     * You MUST also call this method whenever you make a modification to the text of a Slice that
     * has already been displayed.
     */
    fun display() {

        // generate the final string based on the pieces
        val builder = StringBuilder()
        for (slice in sliceList) {
            builder.append(slice.getText())
        }

        // apply spans
        var cursor = 0
        val finalString = SpannableString(builder.toString())
        for (slice in sliceList) {
            applySpannableTo(slice, finalString, cursor, cursor + slice.getText().length)
            cursor += slice.getText().length
        }

        // set the styled text
        text = finalString
    }

    private fun applySpannableTo(slice: Slice, finalString: SpannableString, start: Int, end: Int) {
        if (slice.isSubscript()) {
            finalString.setSpan(
                SubscriptSpan(), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        if (slice.isSuperscript()) {
            finalString.setSpan(
                SuperscriptSpan(), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        if (slice.isStrike()) {
            finalString.setSpan(
                StrikethroughSpan(), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        if (slice.isUnderline()) {
            finalString.setSpan(
                UnderlineSpan(), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // set click on text
        if (slice.getOnTextClick() != null) {
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    ds.color = Color.TRANSPARENT
                    ds.isUnderlineText = slice.isUnderline()
                }

                override fun onClick(view: View) {
                    view.invalidate()
                    slice.getOnTextClick()?.onTextClick(view, slice)
                }
            }
            finalString.setSpan(
                clickableSpan, start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            movementMethod = LinkMovementMethod.getInstance()
        }


        if (slice.isRounded()) finalString.setSpan(
            RoundedBackgroundSpan(
                slice.getBackgroundColor().toFloat(), slice.getTextColor(),
                slice.getCornerRadius()
            ), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        if (slice.isCircle()) finalString.setSpan(
            CircleBackgroundSpan(
                slice.getBackgroundColor().toFloat(), slice.getTextColor(),
                slice.getCircleRadius()
            ), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        //https://stackoverflow.com/a/33336650/1293313 for rounded corner
        //url span

        /* if (slice.isUrl())
            finalString.setSpan(new URLSpan(finalString.toString()), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
*/
        // style
        finalString.setSpan(
            StyleSpan(slice.getStyle()), start, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // absolute text size
        finalString.setSpan(
            AbsoluteSizeSpan(slice.getTextSize()), start, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // relative text size
        finalString.setSpan(
            RelativeSizeSpan(slice.getTextSizeRelative()), start, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // text color
        finalString.setSpan(
            ForegroundColorSpan(slice.getTextColor()), start, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // background color
        if (slice.getBackgroundColor() != -1) {
            finalString.setSpan(
                BackgroundColorSpan(slice.getBackgroundColor()), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    fun removeAllSlice() {
        if (sliceList.size > 0) for (i in 0..sliceList.size) {
            removeSlice(0)
        }
    }

    /**
     * Resets the styling of this view and sets it's content to an empty String.
     */
    fun reset() {
        sliceList = ArrayList()
        text = ""
    }

    /**
     * Change text color of all pieces of textview.
     *
     * @param textColor
     */
    fun changeTextColor(textColor: Int) {
        for (slice in sliceList) {
            slice.setTextColor(textColor)
        }
        display()
    }
}