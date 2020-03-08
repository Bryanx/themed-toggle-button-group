package nl.bryanderidder.themedtogglebuttongroup.test

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.animation.doOnEnd
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.FlakyTest
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton
import nl.bryanderidder.themedtogglebuttongroup.ThemedButtonGroup
import nl.bryanderidder.themedtogglebuttongroup.name
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class ThemedButtonGroupTest {

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
        assertThat(buttonGroup, isA(ThemedButtonGroup::class.java))
        assertThat(buttonGroup.childCount, `is`(3))
        assertThat(button1.text, `is`("5:30PM"))
        assertThat(buttons, hasSize(3))
        assertTrue(buttons.all { it.btnBackgroundColor == it.defaultBgColor })
        assertTrue(buttons.all { it.textColor == it.defaultTextColor })
        assertTrue(buttons.all { !it.isSelected })
        assertTrue(buttons.all { it.cbCardView.visibility == VISIBLE })
        assertTrue(buttons.all { it.cbCardViewHighlight.visibility == GONE })
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testSingleSelection() {
        val buttonGroup = createLayout(R.layout.activity_single_selection, activityRule)
        val buttons = buttonGroup.buttons

        buttonGroup.onSelect { btn ->
            assertThat(btn.text, `is`(buttons.single { it.isSelected }.text))
        }

        //select a single button
        buttonGroup.selectButton(buttons[0], 0F, 0F, true).doOnEnd {
            assertThat(buttons.single { it.isSelected }.name, `is`(buttons[0].name))
        }
        //select another button
        buttonGroup.selectButton(buttons[1], 0F, 0F, true).doOnEnd {
            assertThat(buttons.single { it.isSelected }.name, `is`(buttons[1].name))
        }
        //select last button
        buttonGroup.selectButton(buttons[2], 0F, 0F, true).doOnEnd {
            assertThat(buttons.single { it.isSelected }.name, `is`(buttons[2].name))
        }
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testMultipleSelection() {
        val buttonGroup = createLayout(R.layout.activity_multiple_selection, activityRule)
        val buttons = buttonGroup.buttons
        //select 1 button
        buttonGroup.selectButton(buttons[0], 0F, 0F, true).doOnEnd {
            assertThat(buttons.single { it.isSelected }.name, `is`(buttons[0].name))
            assertThat(buttons[0].cbCardViewHighlight.visibility, `is`(VISIBLE))
            assertThat(buttons[1].cbCardViewHighlight.visibility, `is`(GONE))
            assertThat(buttons[2].cbCardViewHighlight.visibility, `is`(GONE))
        }
        //select another button
        buttonGroup.selectButton(buttons[1], 0F, 0F, true).doOnEnd {
            assertThat(buttons.filter { it.isSelected }, hasItems(buttons[0], buttons[1]))
            assertThat(buttons[0].cbCardViewHighlight.visibility, `is`(VISIBLE))
            assertThat(buttons[1].cbCardViewHighlight.visibility, `is`(VISIBLE))
            assertThat(buttons[2].cbCardViewHighlight.visibility, `is`(GONE))
        }
        //select last button
        buttonGroup.selectButton(buttons[2], 0F, 0F, true).doOnEnd {
            assertThat(buttons.filter { it.isSelected }, hasItems(buttons[1], buttons[2]))
            assertThat(buttons[0].cbCardViewHighlight.visibility, `is`(GONE))
            assertThat(buttons[1].cbCardViewHighlight.visibility, `is`(VISIBLE))
            assertThat(buttons[2].cbCardViewHighlight.visibility, `is`(VISIBLE))
        }
    }

}