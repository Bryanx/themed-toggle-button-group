/**
 *
 * ThemedToggleButtonGroup
 *
 * Created by Bryan de Ridder on 15/3/20.
 * Copyright (c) 2021 Bryan de Ridder (br.deridder@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package nl.bryanderidder.themedtogglebuttongroup.test

import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.marginEnd
import androidx.core.view.marginRight
import androidx.core.view.marginStart
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.FlakyTest
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import nl.bryanderidder.themedtogglebuttongroup.*
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@MediumTest
@RunWith(AndroidJUnit4::class)
class ThemedToggleButtonGroupTest {

    @JvmField
    @Rule
    var activityRule = ActivityTestRule(TestActivity::class.java)

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testLoadFromLayoutXml() {
        val buttonGroup = createLayout(R.layout.activity_simple, activityRule)
        val buttons = buttonGroup.buttons
        assertNotNull(buttonGroup)
        assertThat(buttonGroup, isA(ThemedToggleButtonGroup::class.java))
        assertThat(buttonGroup.childCount, `is`(3))
        assertThat(buttons, hasSize(3))
        assertThat(buttonGroup.requiredAmount, `is`(2))
        assertThat(buttonGroup.selectableAmount, `is`(3))
        assertTrue(buttons.all { it.bgColor == it.context.color(R.color.lightGray) })
        assertTrue(buttons.all { it.selectedBgColor == it.context.color(R.color.denim) })
        assertTrue(buttons.all { it.textColor == it.context.color(R.color.darkGray) })
        assertTrue(buttons.all { it.selectedTextColor == it.context.color(android.R.color.white) })
        assertTrue(buttons.all { it.cvCard.visibility == VISIBLE })
        assertThat(buttons.count { it.cvSelectedCard.visibility == GONE }, `is`(buttons.size))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testLoadTextFromXml() {
        val buttonGroup = createLayout(R.layout.activity_simple, activityRule)
        val button1 = buttonGroup.findViewById<ThemedButton>(R.id.btn1)
        val button2 = buttonGroup.findViewById<ThemedButton>(R.id.btn2)
        val button3 = buttonGroup.findViewById<ThemedButton>(R.id.btn3)
        assertThat(button1.text, `is`("5:30PM"))
        assertThat(button1.selectedText, `is`("selectedText"))
        assertThat(button2.text, `is`("6:00PM"))
        assertThat(button2.selectedText, `is`("6:00PM"))
        assertThat(button3.text, `is`("6:30PM"))
        assertThat(button3.selectedText, `is`("6:30PM"))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testStyleLayoutProgrammatically() {
        val buttonGroup = createLayout(R.layout.activity_simple, activityRule)
        val button1 = buttonGroup.findViewById<ThemedButton>(R.id.btn1)
        activityRule.runOnUiThread {
            button1.textColor = R.color.lavender
            assertThat(button1.tvText.currentTextColor, `is`(R.color.lavender))
            assertThat(button1.textColor, `is`(R.color.lavender))
            button1.selectedTextColor = R.color.lavender
            assertThat(button1.tvSelectedText.currentTextColor, `is`(R.color.lavender))
            assertThat(button1.selectedTextColor, `is`(R.color.lavender))
            button1.bgColor = R.color.lavender
            assertThat((button1.cvCard.background as ColorDrawable).color, `is`(R.color.lavender))
            assertThat(button1.bgColor, `is`(R.color.lavender))
            button1.selectedBgColor = R.color.lavender
            assertThat((button1.cvSelectedCard.background as ColorDrawable).color, `is`(R.color.lavender))
            assertThat(button1.selectedBgColor, `is`(R.color.lavender))
            button1.text = "click"
            assertThat(button1.tvText.text.toString(), `is`("click"))
            assertThat(button1.text, `is`("click"))
            // if only the unselected text is set, assert that the selected text is the same.
            assertThat(button1.tvSelectedText.text.toString(), `is`("click"))
            button1.selectedText = "clicked"
            assertThat(button1.tvSelectedText.text.toString(), `is`("clicked"))
            assertThat(button1.selectedText, `is`("clicked"))
        }
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testSingleSelection() {
        val buttonGroup = createLayout(R.layout.activity_single_selection, activityRule)
        val buttons = buttonGroup.buttons
        activityRule.runOnUiThread {
            buttonGroup.setOnSelectListener { btn ->
                assertThat(btn.text, `is`(buttons.single { it.isSelected }.text))
            }
            //select a single button
            buttonGroup.selectButton(buttons[0], 0F, 0F, false)
            assertThat(buttons.single { it.isSelected }.name, `is`(buttons[0].name))
            //select another button
            buttonGroup.selectButton(buttons[1], 0F, 0F, false)
            assertThat(buttons.single { it.isSelected }.name, `is`(buttons[1].name))
            //select last button
            buttonGroup.selectButton(buttons[2], 0F, 0F, false)
            assertThat(buttons.single { it.isSelected }.name, `is`(buttons[2].name))
        }
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testMultipleSelection() {
        val buttonGroup = createLayout(R.layout.activity_multiple_selection, activityRule)
        val buttons = buttonGroup.buttons
        activityRule.runOnUiThread {
            //select 1 button
            buttonGroup.selectButton(buttons[0], 0F, 0F, false)
            assertThat(buttons.single { it.isSelected }.name, `is`(buttons[0].name))
            //select another button
            buttonGroup.selectButton(buttons[1], 0F, 0F, false)
            assertThat(buttons.filter { it.isSelected }, hasItems(buttons[0], buttons[1]))
            //select last button, assert that the first button is deselected
            buttonGroup.selectButton(buttons[2], 0F, 0F, false)
            assertThat(buttons.filter { it.isSelected }, hasItems(buttons[1], buttons[2]))
        }
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testRequiredAmount() {
        val buttonGroup = createLayout(R.layout.activity_required_selection, activityRule)
        val buttons = buttonGroup.buttons
        // selectableAmount = 3, requiredAmount = 2
        activityRule.runOnUiThread {
            // check if required amount is initially set:
            buttonGroup.selectButton(R.id.btn1)
            buttonGroup.selectButton(R.id.btn2)
            assertThat(buttonGroup.selectedButtons.size, `is`(buttonGroup.requiredAmount))
            assertThat(buttonGroup.buttons.filter { it.isSelected }.size, `is`(buttonGroup.requiredAmount))
            // block deselecting below required amount:
            buttonGroup.selectButton(buttons.first { it.isSelected }, 0F, 0F, false)
            assertThat(buttonGroup.selectedButtons.size, `is`(buttonGroup.requiredAmount))
            assertThat(buttonGroup.buttons.filter { it.isSelected }.size, `is`(buttonGroup.requiredAmount))
            // allow selecting 3 buttons:
            buttonGroup.selectButton(buttons.first { !it.isSelected }, 0F, 0F, false)
            assertThat(buttonGroup.selectedButtons.size, `is`(buttonGroup.selectableAmount))
            assertThat(buttonGroup.buttons.filter { it.isSelected }.size, `is`(buttonGroup.selectableAmount))
            // allow deselecting when 3 buttons are selected, requiredAmount = 2
            buttonGroup.selectButton(buttons.first { it.isSelected }, 0F, 0F, false)
            assertThat(buttonGroup.selectedButtons.size, `is`(buttonGroup.requiredAmount))
            assertThat(buttonGroup.buttons.filter { it.isSelected }.size, `is`(buttonGroup.requiredAmount))
            // don't allow 4 buttons to be selected, selectableAmount = 3
            buttonGroup.selectButton(buttons.first { !it.isSelected }, 0F, 0F, false)
            buttonGroup.selectButton(buttons.first { !it.isSelected }, 0F, 0F, false)
            assertThat(buttonGroup.selectedButtons.size, `is`(buttonGroup.selectableAmount))
            assertThat(buttonGroup.buttons.filter { it.isSelected }.size, `is`(buttonGroup.selectableAmount))
        }
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testDeselection() {
        val buttonGroup = createLayout(R.layout.activity_deselect_button, activityRule)
        activityRule.runOnUiThread {
            //select button
            buttonGroup.selectButton(R.id.btn1)
            assertThat(buttonGroup.selectedButtons.size, `is`(1))
            assertThat(buttonGroup.buttons[0].cvSelectedCard.visibility, `is`(VISIBLE))
            //deselect button
            buttonGroup.selectButton(R.id.btn1)
            assertThat(buttonGroup.selectedButtons.size, `is`(0))
            assertThat(buttonGroup.buttons[0].cvSelectedCard.visibility, `is`(GONE))
            assertThat(buttonGroup.buttons[1].cvSelectedCard.visibility, `is`(GONE))
            assertThat(buttonGroup.buttons[2].cvSelectedCard.visibility, `is`(GONE))
        }
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testRequiredAmountProgrammatically() {
        val buttonGroup = createLayout(R.layout.activity_required_selection, activityRule)
        // selectableAmount = 3, requiredAmount = 2
        activityRule.runOnUiThread {
            // select button programmatically
            buttonGroup.selectButton(buttonGroup.buttons[2])
            buttonGroup.selectButton(buttonGroup.buttons[3])
            assertThat(buttonGroup.selectedButtons.size, `is`(2))
            assertThat(buttonGroup.buttons.filter { it.isSelected }.size, `is`(2))
            assertThat(buttonGroup.buttons[0].cvSelectedCard.visibility, `is`(GONE))
            assertThat(buttonGroup.buttons[1].cvSelectedCard.visibility, `is`(GONE))
            assertThat(buttonGroup.buttons[2].cvSelectedCard.visibility, `is`(VISIBLE))
            assertThat(buttonGroup.buttons[3].cvSelectedCard.visibility, `is`(VISIBLE))

            // deselect buttons and check that requiredAmount is not corrupted
            buttonGroup.selectButton(buttonGroup.buttons[2])
            buttonGroup.selectButton(buttonGroup.buttons[3])
            assertThat(buttonGroup.selectedButtons.size, `is`(2))
            assertThat(buttonGroup.buttons.filter { it.isSelected }.size, `is`(2))
            assertThat(buttonGroup.buttons[0].cvSelectedCard.visibility, `is`(GONE))
            assertThat(buttonGroup.buttons[1].cvSelectedCard.visibility, `is`(GONE))
            assertThat(buttonGroup.buttons[2].cvSelectedCard.visibility, `is`(VISIBLE))
            assertThat(buttonGroup.buttons[3].cvSelectedCard.visibility, `is`(VISIBLE))
        }
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testStyleBordersFromLayout() {
        val buttonGroup = createLayout(R.layout.activity_borders, activityRule)
        val buttons = buttonGroup.buttons
        val ctx = buttonGroup.context
        activityRule.runOnUiThread {
            //Check button borders when none are set:
            assertThat(buttons[0].borderWidth, `is`(0F))
            assertThat(buttons[0].selectedBorderWidth, `is`(0F))
            assertThat(buttons[0].borderColor, `is`(ctx.color(R.color.darkGray)))
            assertThat(buttons[0].selectedBorderColor, `is`(ctx.color(R.color.darkGray)))
            //Check button borders when only unselected borders are set:
            assertThat(buttons[1].borderWidth, `is`(5.pxf))
            assertThat(buttons[1].selectedBorderWidth, `is`(5.pxf))
            assertThat(buttons[1].borderColor, `is`(ctx.color(R.color.lavender)))
            assertThat(buttons[1].selectedBorderColor, `is`(ctx.color(R.color.lavender)))
            //Check button borders when selected and unselected borders are set:
            assertThat(buttons[2].borderWidth, `is`(5.pxf))
            assertThat(buttons[2].selectedBorderWidth, `is`(20.pxf))
            assertThat(buttons[2].borderColor, `is`(ctx.color(R.color.lavender)))
            assertThat(buttons[2].selectedBorderColor, `is`(ctx.color(R.color.sapphire)))
        }
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testStyleBordersProgrammatically() {
        val buttonGroup = createLayout(R.layout.activity_borders, activityRule)
        val ctx = buttonGroup.context
        val button1 = buttonGroup.findViewById<ThemedButton>(R.id.btn1)
        activityRule.runOnUiThread {
            //Check button borders when only unselected borders are set:
            button1.borderColor = ctx.color(R.color.sapphire)
            button1.borderWidth = 20.pxf
            assertThat(button1.cvCard.borderWidth, `is`(20.pxf))
            assertThat(button1.cvSelectedCard.borderWidth, `is`(0.pxf))
            assertThat(button1.cvCard.borderColor, `is`(ctx.color(R.color.sapphire)))
            assertThat(button1.cvSelectedCard.borderColor, `is`(ctx.color(R.color.darkGray)))
            //Check button borders when selected and unselected borders are set:
            button1.selectedBorderColor = ctx.color(R.color.lavender)
            button1.selectedBorderWidth = 10.pxf
            assertThat(button1.cvCard.borderWidth, `is`(20.pxf))
            assertThat(button1.cvSelectedCard.borderWidth, `is`(10.pxf))
            assertThat(button1.cvCard.borderColor, `is`(ctx.color(R.color.sapphire)))
            assertThat(button1.cvSelectedCard.borderColor, `is`(ctx.color(R.color.lavender)))
        }
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testHorizontalSpacing() {
        val buttonGroup = createLayout(R.layout.activity_simple, activityRule)
        val buttons = buttonGroup.buttons
        activityRule.runOnUiThread {
            // Assert that initial spacing is 10dp
            assertThat(buttonGroup.horizontalSpacing, `is`(10.px))
            // Check that front and end don't have margin
            assertThat(buttons.first().marginStart, `is`(0))
            assertThat(buttons.last().marginEnd, `is`(0))
            assertTrue(buttons.subList(0, buttons.lastIndex).all { it.marginEnd == 10.px })
            // Change spacing programmatically to 20dp
            buttonGroup.horizontalSpacing = 20.px
            assertThat(buttonGroup.horizontalSpacing, `is`(20.px))
            assertThat(buttons.first().marginStart, `is`(0))
            assertThat(buttons.last().marginEnd, `is`(0))
            assertTrue(buttons.subList(0, buttons.lastIndex).all { it.marginEnd == 20.px })
        }
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testCustomFont() {
        val buttonGroup = createLayout(R.layout.activity_custom_font, activityRule)
        val buttons = buttonGroup.buttons
        val font = Typeface.createFromAsset(buttonGroup.context.assets, "fonts/ubuntu_mono.ttf")
        activityRule.runOnUiThread {
            buttons[2].fontFamily = "fonts/ubuntu_mono.ttf"
            // Test poperty
            assertThat(buttons[0].fontFamily, `is`("Roboto"))
            assertThat(buttons[1].fontFamily, `is`("fonts/ubuntu_mono.ttf"))
            assertThat(buttons[2].fontFamily, `is`("fonts/ubuntu_mono.ttf"))
            // Assert that actual textviews have the correct typeface
            assertThat(buttons[0].tvText.typeface, `is`(Typeface.DEFAULT))
            assertThat(buttons[0].tvSelectedText.typeface, `is`(Typeface.DEFAULT))
            assertThat(buttons[1].tvText.typeface, `is`(font))
            assertThat(buttons[1].tvSelectedText.typeface, `is`(font))
            assertThat(buttons[2].tvText.typeface, `is`(font))
            assertThat(buttons[2].tvSelectedText.typeface, `is`(font))
        }
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testProgrammaticallyAddingButtonsInitialStyling() {
        val buttonGroup = createLayout(R.layout.activity_empty, activityRule)
        activityRule.runOnUiThread {
            val btn1 = ThemedButton(buttonGroup.context)
            btn1.text = "Button 1"
            buttonGroup.addView(btn1, ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT))
            val btn2 = ThemedButton(buttonGroup.context)
            btn2.text = "Button 2"
            buttonGroup.addView(btn2, ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT))
            val btn3 = ThemedButton(buttonGroup.context)
            btn3.text = "Button 3"
            buttonGroup.addView(btn3, ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT))

            val buttons = buttonGroup.buttons
            assertThat(buttons.size, `is`(3))
            assertThat(buttons[0], `is`(btn1))
            assertThat(buttons[1], `is`(btn2))
            assertThat(buttons[2], `is`(btn3))
            assertThat(buttons[0].selectedTextColor, `is`(btn1.context.color(android.R.color.white)))
            assertThat(buttons[0].selectedBgColor, `is`(denim))
            assertThat(buttons[0].bgColor, `is`(lightGray))
        }
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testClickShouldFailWhenButtonIsDisabled() {
        val buttonGroup = createLayout(R.layout.activity_single_selection, activityRule)
        val buttons = buttonGroup.buttons
        buttons[0].isEnabled = false
        activityRule.runOnUiThread {
            buttonGroup.selectButton(buttons[0], 0F, 0F, false)
            assertTrue(buttons.none { it.isSelected })
        }
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testClickShouldSucceedWhenButtonIsEnabled() {
        val buttonGroup = createLayout(R.layout.activity_single_selection, activityRule)
        val buttons = buttonGroup.buttons
        buttons.forEach { it.isEnabled = true }
        activityRule.runOnUiThread {
            buttonGroup.selectButton(buttons[0], 0F, 0F, false)
            assertThat(buttons.single { it.isSelected }.name, `is`(buttons[0].name))
        }
    }
}
