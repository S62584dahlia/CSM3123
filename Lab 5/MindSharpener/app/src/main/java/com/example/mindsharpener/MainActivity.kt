package com.example.mindsharpener

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val radioGroup: RadioGroup = findViewById(R.id.radio_group)
        val button: Button = findViewById(R.id.button)

        // Get radio group selected item using on checked change listener
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            Toast.makeText(
                applicationContext,
                "On checked change: ${radio.text}",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Get radio group selected status and text using button click event
        button.setOnClickListener {
            // Get the checked radio button id from the radio group
            val id: Int = radioGroup.checkedRadioButtonId
            if (id != -1) { // If any radio button checked from the radio group
                // Get the instance of the radio button using id
                val radio: RadioButton = findViewById(id)
                Toast.makeText(
                    applicationContext,
                    "On button click: ${radio.text}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // If no radio button checked in this radio group
                Toast.makeText(
                    applicationContext,
                    "On button click: nothing selected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Get the selected radio button text using radio button on click listener
    fun radio_button_click(view: View) {
        // Get the clicked radio button instance
        val radioGroup: RadioGroup = findViewById(R.id.radio_group)
        val radio: RadioButton = findViewById(radioGroup.checkedRadioButtonId)
        Toast.makeText(
            applicationContext,
            "On click: ${radio.text}",
            Toast.LENGTH_SHORT
        ).show()
    }
}
