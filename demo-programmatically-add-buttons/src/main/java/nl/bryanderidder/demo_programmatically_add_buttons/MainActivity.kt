package nl.bryanderidder.demo_programmatically_add_buttons

import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.appcompat.app.AppCompatActivity
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.activity_main.*
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create the toggle group and add it to the rootView
        val buttonGroup = ThemedToggleButtonGroup(this)
        buttonGroup.justifyContent = JustifyContent.CENTER // this is optional
        rootView.addView(buttonGroup)

        // Create the 1st button and add it to the toggle group
        val btn1 = ThemedButton(buttonGroup.context)
        btn1.text = "Button 1"
        buttonGroup.addView(btn1, ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT))

        // Create the 2nd button and add it to the toggle group
        val btn2 = ThemedButton(buttonGroup.context)
        btn2.text = "Button 2"
        buttonGroup.addView(btn2, ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT))

        // Create the 3rd button and add it to the toggle group
        val btn3 = ThemedButton(buttonGroup.context)
        btn3.text = "Button 3"
        buttonGroup.addView(btn3, ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT))
    }
}
