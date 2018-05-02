package info.weigandt.goalacademy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.weigandt.goalacademy.R;

import static info.weigandt.goalacademy.activities.MainActivity.sGoalList;

public class GoalListAdapter extends RecyclerView.Adapter<GoalListAdapter.TrackViewHolder> {

    // region Variables
    private Context mContext;
    // List is accessible as static var in MainActivity
    // endregion Variables

    // region Constructor
    public GoalListAdapter(Context context) {
        super();    //  TODO needed?
        this.mContext = context;
    }
    // endregion Constructor

    // region Overrides
    /**
     * This is called for each new ViewHolder
     */
    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item_goal;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrackViewHolder(view);
    }

    /**
     * Returns the number of items to display
     */
    @Override
    public int getItemCount() {
        return sGoalList.size();
    }

    /**
     * Updates the contents of the ViewHolder
     *
     * @param holder   The ViewHolder to be updated
     * @param position The position of the item
     */
    @Override
    public void onBindViewHolder(TrackViewHolder holder, int position)



























    {
        holder.bind(position);
    }

    // endregion Overrides

    // region Inner Class
    /**
     * Inner Class - ViewHolder (Holding Views in ViewHolders reduces find_by_id calls).
     * Provide a reference to the views for each data item.
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder
     */
    public class TrackViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_goal_name)
        TextView mTextViewGoalName;
        @BindView(R.id.tv_streak)
        TextView mTextViewStreak;
        @BindView(R.id.tv_completed)
        TextView mTextViewCompleted;
        @BindView(R.id.tv_next_level)
        TextView mTextViewNextLevel;

        public TrackViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * This method takes an integer as input and
         * uses that integer to display the appropriate content within a list item
         *
         * @param listIndex Position of the item in the list
         */
        void bind(int listIndex) {
            mTextViewGoalName.setText(sGoalList.get(listIndex).getName());
            String streak = String.valueOf(1);  // TODO implement method to get Streak number (from DB / calculate it)
            // TODO just store a List / Array of integers to count the completed numbers. special care for 1st week!
            String percentage = "70%";  // TODO implement method
            String nextLevel = "Gold";  // TODO implement method
            mTextViewStreak.setText(streak);
            mTextViewCompleted.setText(percentage);
            mTextViewNextLevel.setText(nextLevel);
        }

        /*  TODO not needed anymore!? after change from codeproject
        /**
         * Called when a user clicks on an item in the list
         *
         * @param v The View that was clicked

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            // mOnClickListener.onListItemClick(clickedPosition);   // TODO fix
        }
        */
    }
    // endregion Inner Class




}