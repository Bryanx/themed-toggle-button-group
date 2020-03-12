package nl.bryanderidder.themedtogglebuttongroup.test

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.FlakyTest
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup
import nl.bryanderidder.themedtogglebuttongroup.name
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
        val button1 = buttonGroup.findViewById<ThemedButton>(R.id.btn1)
        val buttons = buttonGroup.buttons
        assertNotNull(buttonGroup)
        assertThat(buttonGroup, isA(ThemedToggleButtonGroup::class.java))
        assertThat(buttonGroup.childCount, `is`(3))
        assertThat(button1.text, `is`("5:30PM"))
        assertThat(buttons, hasSize(3))
        assertThat(buttonGroup.requiredAmount, `is`(2))
        assertThat(buttonGroup.selectableAmount, `is`(3))
        assertTrue(buttons.all { it.btnBackgroundColor == it.bgColor })
        assertTrue(buttons.all { it.tvText.currentTextColor == it.textColor })
        assertTrue(buttons.all { it.cvCard.visibility == VISIBLE })
        assertThat(buttons.count { it.cvSelectedCard.visibility == GONE }, `is`(1))
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
            buttonGroup.selectButton(buttons[0], 0F, 0F)
            assertThat(buttons.single { it.isSelected }.name, `is`(buttons[0].name))
            //select another button
            buttonGroup.selectButton(buttons[1], 0F, 0F)
            assertThat(buttons.single { it.isSelected }.name, `is`(buttons[1].name))
            //select last button
            buttonGroup.selectButton(buttons[2], 0F, 0F)
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
            buttonGroup.selectButton(buttons[0], 0F, 0F)
            assertThat(buttons.single { it.isSelected }.name, `is`(buttons[0].name))
            //select another button
            buttonGroup.selectButton(buttons[1], 0F, 0F)
            assertThat(buttons.filter { it.isSelected }, hasItems(buttons[0], buttons[1]))
            //select last button, assert that the first button is deselected
            buttonGroup.selectButton(buttons[2], 0F, 0F)
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
            assertThat(buttonGroup.selectedButtons.size, `is`(buttonGroup.requiredAmount))
            assertThat(buttonGroup.buttons.filter { it.isSelected }.size, `is`(buttonGroup.requiredAmount))
            // block deselecting below required amount:
            buttonGroup.selectButton(buttons.first { it.isSelected }, 0F, 0F)
            assertThat(buttonGroup.selectedButtons.size, `is`(buttonGroup.requiredAmount))
            assertThat(buttonGroup.buttons.filter { it.isSelected }.size, `is`(buttonGroup.requiredAmount))
            // allow selecting 3 buttons:
            buttonGroup.selectButton(buttons.first { !it.isSelected }, 0F, 0F)
            assertThat(buttonGroup.selectedButtons.size, `is`(buttonGroup.selectableAmount))
            assertThat(buttonGroup.buttons.filter { it.isSelected }.size, `is`(buttonGroup.selectableAmount))
            // allow deselecting when 3 buttons are selected, requiredAmount = 2
            buttonGroup.selectButton(buttons.first { it.isSelected }, 0F, 0F)
            assertThat(buttonGroup.selectedButtons.size, `is`(buttonGroup.requiredAmount))
            assertThat(buttonGroup.buttons.filter { it.isSelected }.size, `is`(buttonGroup.requiredAmount))
            // don't allow 4 buttons to be selected, selectableAmount = 3
            buttonGroup.selectButton(buttons.first { !it.isSelected }, 0F, 0F)
            buttonGroup.selectButton(buttons.first { !it.isSelected }, 0F, 0F)
            assertThat(buttonGroup.selectedButtons.size, `is`(buttonGroup.selectableAmount))
            assertThat(buttonGroup.buttons.filter { it.isSelected }.size, `is`(buttonGroup.selectableAmount))
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
            buttonGroup.buttons[1].isSelected = true
            assertThat(buttonGroup.selectedButtons.size, `is`(buttonGroup.requiredAmount))
            assertThat(buttonGroup.buttons.filter { it.isSelected }.size, `is`(buttonGroup.requiredAmount))

            // check if the initially selected button is still set
            assertTrue(buttonGroup.buttons[1].isSelected)
        }
    }
}
