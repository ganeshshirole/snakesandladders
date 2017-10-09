package com.snakes_ladder.dice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.snakes_ladder.dice.adapters.GridAdapter;
import com.snakes_ladder.dice.models.GridDataModel;
import com.snakes_ladder.dice.models.GridInfo;
import com.snakes_ladder.dice.util.Util;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements GridAdapter.StatusListener {
    ImageView dice_picture;    //reference to dice picture
    Random rng = new Random();    //generate random numbers
    SoundPool dice_sound;       //For dice sound playing
    int sound_id;                //Used to control sound stream return by SoundPool
    Handler handler;            //Post message to start roll
    Timer timer = new Timer();    //Used to implement feedback to user
    boolean rolling = false;        //Is die rolling?
    private RecyclerView gridView;
    private Context mContext;
    private Gson gson = new Gson();
    private List<GridInfo> gridData;
    private GridAdapter mAdapter;
    private String jsonData;
    private AlertDialog.Builder builder;
    private TextView playText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        //Our function to initialise sound playing
        InitSound();

        playText = findViewById(R.id.playText);
        TextView titleText = findViewById(R.id.titleText);
        Typeface font = Typeface.createFromAsset(getAssets(), "waltograph.ttf");
        playText.setTypeface(font);
        titleText.setTypeface(font);

        //Get a reference to image widget
        dice_picture = findViewById(R.id.imageDice);
        dice_picture.setOnClickListener(new HandleClick());
        //link handler to callback
        handler = new Handler(callback);

        initGrid();
        initDialog();
    }

    private void initDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(MainActivity.this, R.style.ThemeDialog);
        } else {
            builder = new AlertDialog.Builder(MainActivity.this);
        }
        builder.setPositiveButton("Thanks...", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // continue with delete
                setAdapter();
                dialog.dismiss();
            }
        })
                .setIcon(R.drawable.ic_thumb_up_24dp).setCancelable(false);
    }

    private void initGrid() {
        gridView = findViewById(R.id.gridView);
        // Define a layout for RecyclerView
        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 5);
        gridView.setLayoutManager(mLayoutManager);

        jsonData = Util.readFromAssets(MainActivity.this, "grid_data.json");

        setAdapter();
    }

    private void setAdapter() {

        dice_picture.setImageResource(R.drawable.dice3droll);

        GridDataModel gridDataModel = gson.fromJson(jsonData, GridDataModel.class);
        gridData = gridDataModel.getGridData();

        int gridItemSize = Resources.getSystem().getDisplayMetrics().widthPixels / 5;

        // Initialize a new instance of RecyclerView Adapter instance
        mAdapter = new GridAdapter(mContext, gridData, gridItemSize);

        // Set the adapter for RecyclerView
        gridView.setAdapter(mAdapter);
    }

    //User pressed dice, lets start
    private class HandleClick implements View.OnClickListener {
        public void onClick(View arg0) {
            if (!rolling) {
                rolling = true;
                //Show rolling image
                dice_picture.setImageResource(R.drawable.animated_dice);
                AnimationDrawable rocketAnimation = (AnimationDrawable) dice_picture.getDrawable();
                rocketAnimation.start();
                //Start rolling sound
                dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f);
                //Pause to allow image to update
                timer.schedule(new Roll(), 800);
            }
        }
    }

    //New code to initialise sound playback
    void InitSound() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Use the newer SoundPool.Builder
            //Set the audio attributes, SONIFICATION is for interaction events
            //uses builder pattern
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            //default max streams is 1
            //also uses builder pattern
            dice_sound = new SoundPool.Builder().setAudioAttributes(aa).build();

        } else {
            // Running on device earlier than Lollipop
            //Use the older SoundPool constructor
            dice_sound = PreLollipopSoundPool.NewSoundPool();
        }
        //Load the dice sound
        sound_id = dice_sound.load(this, R.raw.shake_dice, 1);
    }

    //When pause completed message sent to callback
    private class Roll extends TimerTask {
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    //Receives message from timer to start dice roll
    Handler.Callback callback = new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            //Get roll result
            //Remember nextInt returns 0 to 5 for argument of 6
            //hence + 1

            int moveCount = rng.nextInt(6) + 1;

            switch (moveCount) {
                case 1:
                    dice_picture.setImageResource(R.drawable.one);
                    break;
                case 2:
                    dice_picture.setImageResource(R.drawable.two);
                    break;
                case 3:
                    dice_picture.setImageResource(R.drawable.three);
                    break;
                case 4:
                    dice_picture.setImageResource(R.drawable.four);
                    break;
                case 5:
                    dice_picture.setImageResource(R.drawable.five);
                    break;
                case 6:
                    dice_picture.setImageResource(R.drawable.six);
                    break;
            }

            movePlayerByCount(moveCount);

            rolling = false;    //user can press again
            return true;
        }
    };

    private void movePlayerByCount(int moveCount) {
        int moveTo = 0;
        for (GridInfo gridInfo : gridData) {
            if (gridInfo.getPosition() == mAdapter.getPlayerPosition()) {
                moveTo = gridInfo.getPosition() + moveCount;
                break;
            }
        }

        for (GridInfo gridInfo : gridData) {
            if (gridInfo.getPosition() == moveTo) {
                if (gridInfo.isIsLadder() || gridInfo.isIsSnake())
                    mAdapter.setPlayerPosition(gridInfo.getGoToPosition(), moveCount, dice_picture);
                else mAdapter.setPlayerPosition(moveTo, moveCount, dice_picture);
                break;
            }
        }
    }


    @Override
    public void winner(boolean isPlayer1) {
        if (isPlayer1)
            builder.setTitle("Player 1 Winner...");
        else builder.setTitle("Player 2 Winner...");

        builder.setMessage("Congrats on your win, congrats on your fearless effort; congrats on your achievements and wish you many congrats for your future. Always have faith in you and have courage to win any challenge that comes your way. Congratulation.")
                .show();
    }

    @Override
    public void loser(boolean isPlayer1) {

        if (isPlayer1)
            builder.setTitle("Player 1 Loser...");
        else builder.setTitle("Player 2 Loser...");

        builder.setMessage("Maybe the truth is, there's a little bit of loser in all of us. Being happy isn't having everything in your life be perfect. Maybe it's about stringing together all the little things.")
                .show();
    }

    @Override
    public void status(boolean isPlayer1) {
        if (isPlayer1) {
            playText.setText("Player 1");
        } else {
            playText.setText("Player 2");
        }
    }

    //Clean up
    protected void onPause() {
        super.onPause();
        dice_sound.pause(sound_id);
    }

    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
