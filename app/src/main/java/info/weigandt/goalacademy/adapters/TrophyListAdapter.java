package info.weigandt.goalacademy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.enums.AwardEnum;
import info.weigandt.goalacademy.enums.GoalStatusPseudoEnum;

import static info.weigandt.goalacademy.activities.MainActivity.sTrophyList;

public class TrophyListAdapter extends RecyclerView.Adapter<TrophyListAdapter.TrophyViewHolder> {

    // region Variables
    private Context mContext;
    // List is accessible as static from MainActivity
    // endregion Variables

    // region Constructor
    public TrophyListAdapter(Context context) {
        super();    //  TODO needed?
        this.mContext = context;
    }
    // endregion Constructor

    // region Overrides
    /**
     * This is called for each new ViewHolder
     */
    @Override
    public TrophyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item_trophy;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrophyViewHolder(view);
    }

    /**
     * Returns the number of items to display
     */
    @Override
    public int getItemCount() {
        return sTrophyList.size();
    }

    /**
     * Updates the contents of the ViewHolder
     *
     * @param holder   The ViewHolder to be updated
     * @param position The position of the item
     */
    @Override
    public void onBindViewHolder(TrophyViewHolder holder, int position)



























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
    public class TrophyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_goal_name)
        TextView mTextViewGoalName;
        @BindView(R.id.iv_award)
        ImageView mImageViewAward;
        @BindView(R.id.tv_completion_date)
        TextView mTextViewCompletionDate;
        @BindView(R.id.ib_share)
        ImageButton mImageButtonShare;

        public TrophyViewHolder(View itemView) {
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
            mTextViewGoalName.setText(sTrophyList.get(listIndex).getGoalName());
            if (sTrophyList.get(listIndex).getAward().equals(GoalStatusPseudoEnum.BRONZE_EARNED_STRING))
            {
                mImageViewAward.setImageResource(R.drawable.ic_trophy_bronze);
            }
            else if (sTrophyList.get(listIndex).getAward().equals(GoalStatusPseudoEnum.SILVER_EARNED_STRING))
            {
                mImageViewAward.setImageResource(R.drawable.ic_trophy_silver);
            }
            else if (sTrophyList.get(listIndex).getAward().equals(GoalStatusPseudoEnum.GOLD_EARNED_STRING))
            {
                mImageViewAward.setImageResource(R.drawable.ic_trophy_gold);
            }
            mTextViewCompletionDate.setText(String.valueOf(sTrophyList.get(listIndex).getCompletionDate()));
            // mImageButtonShare... TODO complete this

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