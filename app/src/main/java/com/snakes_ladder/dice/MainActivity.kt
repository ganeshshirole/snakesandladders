package com.snakes_ladder.dice

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.graphics.drawable.AnimationDrawable
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.snakes_ladder.dice.adapters.GridAdapter
import com.snakes_ladder.dice.models.GridDataModel
import com.snakes_ladder.dice.models.GridInfo
import com.snakes_ladder.dice.util.Util
import java.util.*

class MainActivity : AppCompatActivity(), GridAdapter.StatusListener {
    internal lateinit var dice_picture: ImageView    //reference to dice picture
    internal var rng = Random()    //generate random numbers
    internal lateinit var dice_sound: SoundPool       //For dice sound playing
    internal var sound_id: Int = 0                //Used to control sound stream return by SoundPool
    internal lateinit var handler: Handler            //Post message to start roll
    internal var timer = Timer()    //Used to implement feedback to user
    internal var rolling = false        //Is die rolling?
    private var gridView: RecyclerView? = null
    private var mContext: Context? = null
    private val gson = Gson()
    private var gridData: List<GridInfo>? = null
    private var mAdapter: GridAdapter? = null
    private var jsonData: String? = null
    private var builder: AlertDialog.Builder? = null
    private var playText: TextView? = null

    //Receives message from timer to start dice roll
    internal var callback: Handler.Callback = Handler.Callback {
        //Get roll result
        //Remember nextInt returns 0 to 5 for argument of 6
        //hence + 1

        val moveCount = rng.nextInt(6) + 1

        when (moveCount) {
            1 -> dice_picture.setImageResource(R.drawable.one)
            2 -> dice_picture.setImageResource(R.drawable.two)
            3 -> dice_picture.setImageResource(R.drawable.three)
            4 -> dice_picture.setImageResource(R.drawable.four)
            5 -> dice_picture.setImageResource(R.drawable.five)
            6 -> dice_picture.setImageResource(R.drawable.six)
        }

        movePlayerByCount(moveCount)

        rolling = false    //user can press again
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this
        //Our function to initialise sound playing
        InitSound()

        playText = findViewById(R.id.playText)
        val titleText = findViewById<TextView>(R.id.titleText)
        val font = Typeface.createFromAsset(assets, "waltograph.ttf")
        playText!!.typeface = font
        titleText.typeface = font

        //Get a reference to image widget
        dice_picture = findViewById(R.id.imageDice)
        dice_picture.setOnClickListener(HandleClick())
        //link handler to callback
        handler = Handler(callback)

        initGrid()
        initDialog()
    }

    private fun initDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = AlertDialog.Builder(this@MainActivity, R.style.ThemeDialog)
        } else {
            builder = AlertDialog.Builder(this@MainActivity)
        }
        builder!!.setPositiveButton("Thanks...") { dialog, which ->
            // continue with delete
            setAdapter()
            dialog.dismiss()
        }
                .setIcon(R.drawable.ic_thumb_up_24dp).setCancelable(false)
    }

    private fun initGrid() {
        gridView = findViewById(R.id.gridView)
        // Define a layout for RecyclerView
        val mLayoutManager = GridLayoutManager(mContext, 5)
        gridView!!.layoutManager = mLayoutManager

        jsonData = Util.readFromAssets(this@MainActivity, "grid_data.json")

        setAdapter()
    }

    private fun setAdapter() {

        dice_picture.setImageResource(R.drawable.dice3droll)

        val gridDataModel = gson.fromJson<GridDataModel>(jsonData, GridDataModel::class.java)
        gridData = gridDataModel.gridData

        val gridItemSize = Resources.getSystem().displayMetrics.widthPixels / 5

        // Initialize a new instance of RecyclerView Adapter instance
        mAdapter = GridAdapter(mContext, gridData!!, gridItemSize)

        // Set the adapter for RecyclerView
        gridView!!.adapter = mAdapter
    }

    //User pressed dice, lets start
    private inner class HandleClick : View.OnClickListener {
        override fun onClick(arg0: View) {
            if (!rolling) {
                rolling = true
                //Show rolling image
                dice_picture.setImageResource(R.drawable.animated_dice)
                val rocketAnimation = dice_picture.drawable as AnimationDrawable
                rocketAnimation.start()
                //Start rolling sound
                dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f)
                //Pause to allow image to update
                timer.schedule(Roll(), 800)
            }
        }
    }

    //New code to initialise sound playback
    internal fun InitSound() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Use the newer SoundPool.Builder
            //Set the audio attributes, SONIFICATION is for interaction events
            //uses builder pattern
            val aa = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()

            //default max streams is 1
            //also uses builder pattern
            dice_sound = SoundPool.Builder().setAudioAttributes(aa).build()

        } else {
            // Running on device earlier than Lollipop
            //Use the older SoundPool constructor
            dice_sound = PreLollipopSoundPool.NewSoundPool()
        }
        //Load the dice sound
        sound_id = dice_sound.load(this, R.raw.shake_dice, 1)
    }

    //When pause completed message sent to callback
    private inner class Roll : TimerTask() {
        override fun run() {
            handler.sendEmptyMessage(0)
        }
    }

    private fun movePlayerByCount(moveCount: Int) {
        var moveTo = 0
        for (gridInfo in gridData!!) {
            if (gridInfo.position == mAdapter!!.playerPosition) {
                moveTo = gridInfo.position + moveCount
                break
            }
        }

        for (gridInfo in gridData!!) {
            if (gridInfo.position == moveTo) {
                if (gridInfo.isIsLadder || gridInfo.isIsSnake)
                    mAdapter!!.setPlayerPosition(gridInfo.goToPosition, moveCount, dice_picture)
                else
                    mAdapter!!.setPlayerPosition(moveTo, moveCount, dice_picture)
                break
            }
        }
    }


    override fun winner(isPlayer1: Boolean) {
        if (isPlayer1)
            builder!!.setTitle("Player 1 Winner...")
        else
            builder!!.setTitle("Player 2 Winner...")

        builder!!.setMessage("Congrats on your win, congrats on your fearless effort; congrats on your achievements and wish you many congrats for your future. Always have faith in you and have courage to win any challenge that comes your way. Congratulation.")
                .show()
    }

    override fun loser(isPlayer1: Boolean) {

        if (isPlayer1)
            builder!!.setTitle("Player 1 Loser...")
        else
            builder!!.setTitle("Player 2 Loser...")

        builder!!.setMessage("Maybe the truth is, there's a little bit of loser in all of us. Being happy isn't having everything in your life be perfect. Maybe it's about stringing together all the little things.")
                .show()
    }

    override fun status(isPlayer1: Boolean) {
        if (isPlayer1) {
            playText!!.text = "Player 1"
        } else {
            playText!!.text = "Player 2"
        }
    }

    //Clean up
    override fun onPause() {
        super.onPause()
        dice_sound.pause(sound_id)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}
