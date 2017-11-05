package com.snakes_ladder.dice.adapters

import android.content.Context
import android.os.CountDownTimer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout

import com.snakes_ladder.dice.R
import com.snakes_ladder.dice.models.GridInfo
import com.snakes_ladder.dice.util.Util

class GridAdapter// Provide a suitable constructor (depends on the kind of dataset)
(mContext: Context?, private val values: List<GridInfo>, private val gridItemSize: Int) : RecyclerView.Adapter<GridAdapter.ViewHolder>() {
    private val layoutParams1: RelativeLayout.LayoutParams
    private val layoutParams2: RelativeLayout.LayoutParams
    private var player1Position = 1
    private var player2Position = 1
    private var isPlayer1 = true
    internal var statusListener: StatusListener

    val playerPosition: Int
        get() = if (isPlayer1)
            player1Position
        else
            player2Position

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var player1Icon: ImageView = v.findViewById(R.id.player1)
        var player2Icon: ImageView = v.findViewById(R.id.player2)

    }

    init {

        layoutParams1 = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        )

        layoutParams2 = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        )

        try {
            this.statusListener = mContext as StatusListener
            statusListener.status(isPlayer1)
        } catch (e: ClassCastException) {
            throw ClassCastException("Activity must implement StatusListener.")
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): GridAdapter.ViewHolder {
        // create a new view
        val inflater = LayoutInflater.from(parent.context)
        val iv = inflater.inflate(R.layout.row_layout, parent, false)
        val parms = LinearLayout.LayoutParams(gridItemSize, gridItemSize)
        iv.layoutParams = parms

        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(iv)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val gridInfo = values[position]

        if (player1Position == player2Position) {
            layoutParams1.setMargins(0, 0, Util.convertDpToPixel(30f), Util.convertDpToPixel(10f))
            holder.player1Icon.layoutParams = layoutParams1
            layoutParams2.setMargins(Util.convertDpToPixel(30f), Util.convertDpToPixel(10f), 0, 0)
            holder.player2Icon.layoutParams = layoutParams2
        } else {
            layoutParams1.setMargins(0, 0, 0, 0)
            holder.player1Icon.layoutParams = layoutParams1
            holder.player2Icon.layoutParams = layoutParams1
        }

        if (gridInfo.position != player1Position)
            holder.player1Icon.setImageDrawable(null)
        else
            holder.player1Icon.setImageResource(R.drawable.ic_strategy_1)

        if (gridInfo.position != player2Position)
            holder.player2Icon.setImageDrawable(null)
        else
            holder.player2Icon.setImageResource(R.drawable.ic_strategy_2)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return values.size
    }

    fun setPlayerPosition(newPosition: Int, moveCount: Int, dice_picture: ImageView) {

        if (isPlayer1) {

            if (player1Position == 1 && moveCount == 1)
                movePlayer(newPosition, moveCount, dice_picture)
            else if (player1Position > 1) {
                movePlayer(newPosition, moveCount, dice_picture)
            } else {
                isPlayer1 = false
                statusListener.status(false)
            }

        } else {

            if (player2Position == 1 && moveCount == 1)
                movePlayer(newPosition, moveCount, dice_picture)
            else if (player2Position > 1) {
                movePlayer(newPosition, moveCount, dice_picture)
            } else {
                isPlayer1 = true
                statusListener.status(true)
            }
        }
    }

    private fun movePlayer(newPosition: Int, moveCount: Int, dice_picture: ImageView) {
        dice_picture.isEnabled = false
        object : CountDownTimer(((moveCount * 400) + 400).toLong(), 400) {
            override fun onTick(millisUntilFinished: Long) {
                if (isPlayer1) {
                    if (newPosition > player1Position)
                        player1Position += 1
                } else {
                    if (newPosition > player2Position)
                        player2Position += 1
                }
                notifyDataSetChanged()
            }

            override fun onFinish() {
                if (isPlayer1)
                    player1Position = newPosition
                else
                    player2Position = newPosition

                notifyDataSetChanged()

                if (newPosition == 25)
                    statusListener.winner(isPlayer1)
                else
                    isPlayer1 = !isPlayer1

                statusListener.status(isPlayer1)
                dice_picture.isEnabled = true
            }
        }.start()
    }

    interface StatusListener {
        fun winner(isPlayer1: Boolean)
        fun loser(isPlayer1: Boolean)
        fun status(isPlayer1: Boolean)
    }
}