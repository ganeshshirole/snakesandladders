package com.snakes_ladder.dice.models

import java.util.ArrayList

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GridInfo {

    @SerializedName("position")
    @Expose
    var position: Int = 0
    @SerializedName("isSnake")
    @Expose
    var isIsSnake: Boolean = false
    @SerializedName("isLadder")
    @Expose
    var isIsLadder: Boolean = false
    @SerializedName("goToPosition")
    @Expose
    var goToPosition: Int = 0

}