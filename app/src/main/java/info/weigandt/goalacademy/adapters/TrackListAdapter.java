package info.weigandt.goalacademy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.classes.ThreeStatesButton;

import static info.weigandt.goalacademy.activities.MainActivity.goalList;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.TrackViewHolder> {

    public TrackListAdapter() { // TODO pass the data here
        super();
    }

    /**
     * This is called for each new ViewHolder
     */
    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.track_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        TrackViewHolder viewHolder = new TrackViewHolder(view);
        return viewHolder;
    }

    /**
     * Inner Class - ViewHolder (Holding Views in ViewHolders reduces find_by_id calls).
     * Provide a reference to the views for each data item.
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder
     */
    public static class TrackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView listItemPosterView;

        public TrackViewHolder(View itemView) {
            super(itemView);
            View threeStatesButton0 = (ThreeStatesButton) itemView.findViewById(R.id.tsb_0);
            threeStatesButton0.setOnClickListener(this);
        }

        /**
         * This method takes an integer as input and
         * uses that integer to display the appropriate content within a list item
         *
         * @param listIndex Position of the item in the list
         */
        void bind(int listIndex) {
            //String url = mPosterList.get(listIndex); TODO take int and update the view correspondingly
            //Picasso.with(mContext).load(url).into(listItemPosterView);
        }

        /**
         * Called when a user clicks on an item in the list
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            // mOnClickListener.onListItemClick(clickedPosition);   // TODO fix
        }
    }

    /**
     * Returns the number of items to display
     */
    @Override
    public int getItemCount() {
        return goalList.size();
    }

    /**
     * Updates the contents of the ViewHolder
     *
     * @param holder   The ViewHolder to be updated
     * @param position The position of the item
     */
    @Override
    public void onBindViewHolder(TrackViewHolder holder, int position) {
        holder.bind(position);
    }

}