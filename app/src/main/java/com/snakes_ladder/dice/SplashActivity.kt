package com.snakes_ladder.dice

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val titleText = findViewById<TextView>(R.id.titleText)
        val font = Typeface.createFromAsset(assets, "waltograph.ttf")
        titleText.typeface = font

        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }

    companion object {
        private val SPLASH_TIME_OUT: Long = 3000
    }
}
