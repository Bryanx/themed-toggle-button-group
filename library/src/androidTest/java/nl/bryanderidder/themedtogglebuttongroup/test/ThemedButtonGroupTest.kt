package nl.bryanderidder.themedtogglebuttongroup.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.FlakyTest
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import nl.bryanderidder.themedtogglebuttongroup.ThemedButtonGroup
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
        assertNotNull(buttonGroup)
        assertThat(buttonGroup, isA(ThemedButtonGroup::class.java))
        assertThat(buttonGroup.childCount, `is`(3))
        assertThat(buttonGroup.buttons, hasSize(3))
        assertThat(buttonGroup.buttons[0].text, `is`("5:30PM"))
        assertTrue(buttonGroup.buttons.all { it.btnBackgroundColor == it.defaultBgColor })
        assertTrue(buttonGroup.buttons.all { it.textColor == it.defaultTextColor })
        assertTrue(buttonGroup.buttons.all { !it.isSelected })

        buttonGroup.selectButton(buttonGroup.buttons[0], 0F, 0F, true)

        assertTrue(buttonGroup.buttons[0].isSelected)
        assertTrue(!buttonGroup.buttons[1].isSelected)
        assertTrue(!buttonGroup.buttons[2].isSelected)
    }

}