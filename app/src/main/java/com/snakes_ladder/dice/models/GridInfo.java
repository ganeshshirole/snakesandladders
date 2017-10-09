package com.snakes_ladder.dice.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GridInfo {

    @SerializedName("position")
    @Expose
    private int position;
    @SerializedName("isSnake")
    @Expose
    private boolean isSnake;
    @SerializedName("isLadder")
    @Expose
    private boolean isLadder;
    @SerializedName("goToPosition")
    @Expose
    private int goToPosition;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isIsSnake() {
        return isSnake;
    }

    public void setIsSnake(boolean isSnake) {
        this.isSnake = isSnake;
    }

    public boolean isIsLadder() {
        return isLadder;
    }

    public void setIsLadder(boolean isLadder) {
        this.isLadder = isLadder;
    }

    public int getGoToPosition() {
        return goToPosition;
    }

    public void setGoToPosition(int goToPosition) {
        this.goToPosition = goToPosition;
    }

}