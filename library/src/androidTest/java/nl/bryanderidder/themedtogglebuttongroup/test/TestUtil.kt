package nl.bryanderidder.themedtogglebuttongroup.test

import android.view.View
import androidx.annotation.LayoutRes
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import nl.bryanderidder.themedtogglebuttongroup.ThemedButtonGroup


@Throws(Throwable::class)
fun createLayout(@LayoutRes activityLayoutResId: Int, activityRule: ActivityTestRule<TestActivity>, configuration: Configuration = Configuration.EMPTY): ThemedButtonGroup {
    val activity = activityRule.activity
    activityRule.runOnUiThread {
        activity.setContentView(activityLayoutResId)
        val flexboxLayout = activity.findViewById<ThemedButtonGroup>(R.id.themedButtonGroup)
        configuration.apply(flexboxLayout)
    }
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    return activity.findViewById<View>(R.id.themedButtonGroup) as ThemedButtonGroup
}

interface Configuration {

    fun apply(themedButtonGroup: ThemedButtonGroup)

    companion object {
        val EMPTY: Configuration = object :
            Configuration {
            override fun apply(themedButtonGroup: ThemedButtonGroup) = Unit
        }
    }

}