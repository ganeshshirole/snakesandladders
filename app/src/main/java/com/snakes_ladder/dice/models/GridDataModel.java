package com.snakes_ladder.dice.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GridDataModel {

    @SerializedName("gridData")
    @Expose
    private List<GridInfo> gridData = new ArrayList<GridInfo>();

    public List<GridInfo> getGridData() {
        return gridData;
    }

    public void setGridData(List<GridInfo> gridData) {
        this.gridData = gridData;
    }

}