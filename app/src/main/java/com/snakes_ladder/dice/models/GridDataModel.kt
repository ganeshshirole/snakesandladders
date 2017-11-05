package com.snakes_ladder.dice.models

import java.util.ArrayList

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GridDataModel {

    @SerializedName("gridData")
    @Expose
    var gridData: List<GridInfo> = ArrayList()

}