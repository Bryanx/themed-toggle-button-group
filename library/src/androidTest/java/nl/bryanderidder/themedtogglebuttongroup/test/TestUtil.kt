package nl.bryanderidder.themedtogglebuttongroup.test

import android.view.View
import androidx.annotation.LayoutRes
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup


@Throws(Throwable::class)
fun createLayout(@LayoutRes activityLayoutResId: Int, activityRule: ActivityTestRule<TestActivity>, configuration: Configuration = Configuration.EMPTY): ThemedToggleButtonGroup {
    val activity = activityRule.activity
    activityRule.runOnUiThread {
        activity.setContentView(activityLayoutResId)
        val flexboxLayout = activity.findViewById<ThemedToggleButtonGroup>(R.id.themedButtonGroup)
        configuration.apply(flexboxLayout)
    }
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    return activity.findViewById<View>(R.id.themedButtonGroup) as ThemedToggleButtonGroup
}

interface Configuration {

    fun apply(themedToggleButtonGroup: ThemedToggleButtonGroup)

    companion object {
        val EMPTY: Configuration = object :
            Configuration {
            override fun apply(themedToggleButtonGroup: ThemedToggleButtonGroup) = Unit
        }
    }

}