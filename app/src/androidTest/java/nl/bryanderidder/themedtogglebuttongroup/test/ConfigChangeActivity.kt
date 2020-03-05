package nl.bryanderidder.themedtogglebuttongroup.test

import android.app.Activity
import android.content.res.Configuration
import android.util.Log

import com.google.android.flexbox.FlexboxLayout

/**
 * Activity for testing the [FlexboxLayout] that handles configuration changes by itself
 * instead of letting the system take care of those.
 */
class ConfigChangeActivity : Activity() {
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        Log.d(TAG, "onConfigurationChanged: $newConfig")
    }

    companion object {
        private const val TAG = "ConfigChangeActivity"
    }
}
