package com.snakes_ladder.dice

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playOffline = findViewById<Button>(R.id.playOffline)
        playOffline.setOnClickListener {
            val intent = Intent(this@MainActivity, BoardActivity::class.java)
            startActivity(intent)
        }

        val playOnline = findViewById<Button>(R.id.playOnline)
        playOnline.setOnClickListener {
            Toast.makeText(this@MainActivity, "Coming soon.", Toast.LENGTH_SHORT).show();
        }

    }

}
