package com.snakes_ladder.dice.adapters;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.snakes_ladder.dice.R;
import com.snakes_ladder.dice.models.GridInfo;
import com.snakes_ladder.dice.util.Util;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private final int gridItemSize;
    private final RelativeLayout.LayoutParams layoutParams1;
    private final RelativeLayout.LayoutParams layoutParams2;
    private List<GridInfo> values;
    private int player1Position = 1;
    private int player2Position = 1;
    private boolean isPlayer1 = true;
    StatusListener statusListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView player1Icon;
        public ImageView player2Icon;

        public ViewHolder(View v) {
            super(v);
            player1Icon = v.findViewById(R.id.player1);
            player2Icon = v.findViewById(R.id.player2);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GridAdapter(Context mContext, List<GridInfo> myDataset, int gridItemSize) {
        values = myDataset;
        this.gridItemSize = gridItemSize;

        layoutParams1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );

        layoutParams2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );

        try {
            this.statusListener = ((StatusListener) mContext);
            statusListener.status(isPlayer1);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement StatusListener.");
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View iv = inflater.inflate(R.layout.row_layout, parent, false);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(gridItemSize, gridItemSize);
        iv.setLayoutParams(parms);

        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(iv);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final GridInfo gridInfo = values.get(position);

        if (player1Position == player2Position) {
            layoutParams1.setMargins(0, 0, Util.convertDpToPixel(30), Util.convertDpToPixel(10));
            holder.player1Icon.setLayoutParams(layoutParams1);
            layoutParams2.setMargins(Util.convertDpToPixel(30), Util.convertDpToPixel(10), 0, 0);
            holder.player2Icon.setLayoutParams(layoutParams2);
        } else {
            layoutParams1.setMargins(0, 0, 0, 0);
            holder.player1Icon.setLayoutParams(layoutParams1);
            holder.player2Icon.setLayoutParams(layoutParams1);
        }

        if (gridInfo.getPosition() != player1Position)
            holder.player1Icon.setImageDrawable(null);
        else
            holder.player1Icon.setImageResource(R.drawable.ic_strategy_1);

        if (gridInfo.getPosition() != player2Position)
            holder.player2Icon.setImageDrawable(null);
        else
            holder.player2Icon.setImageResource(R.drawable.ic_strategy_2);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

    public void setPlayerPosition(int newPosition, int moveCount, ImageView dice_picture) {

        if (isPlayer1) {

            if (player1Position == 1 && moveCount == 1)
                movePlayer(newPosition, moveCount, dice_picture);
            else if (player1Position > 1) {
                movePlayer(newPosition, moveCount, dice_picture);
            } else {
                isPlayer1 = false;
                statusListener.status(false);
            }

        } else {

            if (player2Position == 1 && moveCount == 1)
                movePlayer(newPosition, moveCount, dice_picture);
            else if (player2Position > 1) {
                movePlayer(newPosition, moveCount, dice_picture);
            } else {
                isPlayer1 = true;
                statusListener.status(true);
            }
        }
    }

    private void movePlayer(final int newPosition, int moveCount, final ImageView dice_picture) {
        dice_picture.setEnabled(false);
        new CountDownTimer((moveCount * 500) + 500, 500) {
            public void onTick(long millisUntilFinished) {
                if (isPlayer1)
                    player1Position += 1;
                else
                    player2Position += 1;
                notifyDataSetChanged();
            }

            public void onFinish() {
                if (isPlayer1)
                    player1Position = newPosition;
                else player2Position = newPosition;

                notifyDataSetChanged();

                if (newPosition == 25)
                    statusListener.winner(isPlayer1);
                else
                    isPlayer1 = !isPlayer1;

                statusListener.status(isPlayer1);
                dice_picture.setEnabled(true);
            }
        }.start();
    }

    public int getPlayerPosition() {
        if (isPlayer1)
            return player1Position;
        else return player2Position;
    }

    public interface StatusListener {
        void winner(boolean isPlayer1);
        void loser(boolean isPlayer1);
        void status(boolean isPlayer1);
    }
}