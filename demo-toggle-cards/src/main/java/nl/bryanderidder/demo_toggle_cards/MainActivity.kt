package nl.bryanderidder.demo_toggle_cards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton
import nl.bryanderidder.themedtogglebuttongroup.ThemedButtonGroup

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val themedButtonGroup = findViewById<ThemedButtonGroup>(R.id.themedButtonGroup)
        val tvText = findViewById<TextView>(R.id.tvText)

        themedButtonGroup.onSelect { button: ThemedButton ->
            tvText.text = button.text
        }
    }
}
