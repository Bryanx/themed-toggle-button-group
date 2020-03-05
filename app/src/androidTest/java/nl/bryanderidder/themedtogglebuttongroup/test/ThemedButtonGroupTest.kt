package nl.bryanderidder.themedtogglebuttongroup.test

import android.view.View
import androidx.annotation.LayoutRes
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.FlakyTest
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import nl.bryanderidder.themedtogglebuttongroup.ThemedButtonGroup
import org.junit.Assert
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
        val layout = createLayout(R.layout.activity_simple)
        Assert.assertNotNull(layout)
    }

    @Throws(Throwable::class)
    private fun createLayout(
        @LayoutRes activityLayoutResId: Int,
        configuration: Configuration = Configuration.EMPTY
    ): ThemedButtonGroup {
        val activity = activityRule.activity
        activityRule.runOnUiThread {
            activity.setContentView(activityLayoutResId)
            val flexboxLayout = activity.findViewById<ThemedButtonGroup>(R.id.themedButtonGroup)
            configuration.apply(flexboxLayout)
        }
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        return activity.findViewById<View>(R.id.themedButtonGroup) as ThemedButtonGroup
    }

    private interface Configuration {

        fun apply(themedButtonGroup: ThemedButtonGroup)

        companion object {
            val EMPTY: Configuration = object :
                Configuration {
                override fun apply(themedButtonGroup: ThemedButtonGroup) = Unit
            }
        }

    }
}