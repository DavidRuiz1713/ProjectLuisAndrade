package com.example.lucho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val extra = intent.getStringExtra("EXTRA_ARGS")

        extra?.let{
            findViewById<TextView>(R.id.textView_extra).text = it
        }
    }
}