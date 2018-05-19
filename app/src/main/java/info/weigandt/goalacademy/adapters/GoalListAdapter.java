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
import info.weigandt.goalacademy.classes.Config;
import info.weigandt.goalacademy.classes.Goal;
import info.weigandt.goalacademy.classes.GoalHelper;

import static info.weigandt.goalacademy.activities.MainActivity.sGoalList;

public class GoalListAdapter extends RecyclerView.Adapter<GoalListAdapter.TrackViewHolder> {

    // region Variables
    private Context mContext;
    // List is accessible as static var in MainActivity
    // endregion Variables

    // region Constructor
    public GoalListAdapter(Context context) {
        super();
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
        TextView mGoalNameTextView;
        @BindView(R.id.tv_streak)
        TextView mStreakTextView;
        @BindView(R.id.tv_completed)
        TextView mCompletedTextView;
        @BindView(R.id.tv_next_level)
        TextView mNextLevelTextView;

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
            Goal goal = sGoalList.get(listIndex);
            // Name
            mGoalNameTextView.setText(goal.getName());

            // Streak
            int streakNumber = GoalHelper.calculateNumberOfTotalPasses(goal);
            mStreakTextView.setText(String.valueOf(streakNumber));

            // Percentage & Next stage
            int eventsNeededToReachNextLevel = 1;
            int subtractSmaller = 0;
            String nextLevel = "";
            if (streakNumber < Config.NUMBER_FOR_BRONZE)
            {
                 eventsNeededToReachNextLevel = Config.NUMBER_FOR_BRONZE;
                 nextLevel = mContext.getResources().getString(R.id.BRONZE_EARNED_STRING);
            }
            else if (streakNumber < Config.NUMBER_FOR_SILVER) {
                 eventsNeededToReachNextLevel = Config.NUMBER_FOR_SILVER
                        - Config.NUMBER_FOR_BRONZE;
                nextLevel = mContext.getResources().getString(R.id.SILVER_EARNED_STRING);
                subtractSmaller = Config.NUMBER_FOR_BRONZE;
            }
            else if (streakNumber < Config.NUMBER_FOR_GOLD) {
                 eventsNeededToReachNextLevel = Config.NUMBER_FOR_GOLD
                        - Config.NUMBER_FOR_SILVER;
                nextLevel = mContext.getResources().getString(R.id.GOLD_EARNED_STRING);
                subtractSmaller = Config.NUMBER_FOR_SILVER;
            }
            int percentage = ((streakNumber-subtractSmaller)*100) / eventsNeededToReachNextLevel;
            mCompletedTextView.setText(String.valueOf(percentage)+ "%");
            mNextLevelTextView.setText(nextLevel);
        }
    }
    // endregion Inner Class
}