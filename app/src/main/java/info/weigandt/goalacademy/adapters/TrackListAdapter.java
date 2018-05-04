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
import info.weigandt.goalacademy.classes.GoalHelper;
import info.weigandt.goalacademy.enums.EventStateEnum;
import info.weigandt.goalacademy.classes.ThreeStatesButton;

import static info.weigandt.goalacademy.activities.MainActivity.sGoalList;
import static info.weigandt.goalacademy.fragments.TrackFragment.sYearWeekString;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.TrackViewHolder> {

    // region Variables
    private Context mContext;
    // List is accessible as static from MainActivity
    public TrackListAdapterListener onClickListener;
    // endregion Variables

    // region Constructor
    public TrackListAdapter(Context context, TrackListAdapterListener listener) {
        super();    //  TODO needed?
        this.mContext = context;
        this.onClickListener = listener;
    }
    // endregion Constructor

    // region Overrides
    /**
     * This is called for each new ViewHolder
     */
    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item_track;
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

    // region Interface TrackListAdapter listener
    public interface TrackListAdapterListener {

        void button_0_OnClick(View v, int position, EventStateEnum state);

        void button_1_OnClick(View v, int position, EventStateEnum state);

        void button_2_OnClick(View v, int position, EventStateEnum state);

        void button_3_OnClick(View v, int position, EventStateEnum state);

        void button_4_OnClick(View v, int position, EventStateEnum state);

        void button_5_OnClick(View v, int position, EventStateEnum state);

        void button_6_OnClick(View v, int position, EventStateEnum state);
    }

    // endregion Interface



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
        @BindView(R.id.tsb_0)
        ThreeStatesButton mThreeStatesButton0;
        @BindView(R.id.tsb_1)
        ThreeStatesButton mThreeStatesButton1;
        @BindView(R.id.tsb_2)
        ThreeStatesButton mThreeStatesButton2;
        @BindView(R.id.tsb_3)
        ThreeStatesButton mThreeStatesButton3;
        @BindView(R.id.tsb_4)
        ThreeStatesButton mThreeStatesButton4;
        @BindView(R.id.tsb_5)
        ThreeStatesButton mThreeStatesButton5;
        @BindView(R.id.tsb_6)
        ThreeStatesButton mThreeStatesButton6;

        public TrackViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


            mThreeStatesButton0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.button_0_OnClick (
                            v, getAdapterPosition(), mThreeStatesButton0.getState());
                }
            });
            mThreeStatesButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.button_1_OnClick (
                            v, getAdapterPosition(), mThreeStatesButton1.getState());
                }
            });
            mThreeStatesButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.button_2_OnClick (
                            v, getAdapterPosition(), mThreeStatesButton2.getState());
                }
            });
            mThreeStatesButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.button_3_OnClick (
                            v, getAdapterPosition(), mThreeStatesButton3.getState());
                }
            });
            mThreeStatesButton4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.button_4_OnClick (
                            v, getAdapterPosition(), mThreeStatesButton4.getState());
                }
            });
            mThreeStatesButton5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.button_5_OnClick (
                            v, getAdapterPosition(), mThreeStatesButton5.getState());
                }
            });
            mThreeStatesButton6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.button_6_OnClick (
                            v, getAdapterPosition(), mThreeStatesButton6.getState());
                }
            });
        }

        /**
         * This method takes an integer as input and
         * uses that integer to display the appropriate content within a list item
         *
         * @param listIndex Position of the item in the list
         */
        void bind(int listIndex) {
            EventStateEnum state;
            mTextViewGoalName.setText(sGoalList.get(listIndex).getName());

            state = GoalHelper.getEventState(sGoalList.get(listIndex), 0, sYearWeekString);
            mThreeStatesButton0.setState(state);
            boolean isBlocked = GoalHelper.isDayBlockedInScheme(0, sGoalList.get(listIndex));
            if (isBlocked) { mThreeStatesButton0.setVisibility(View.INVISIBLE); }

            state = GoalHelper.getEventState(sGoalList.get(listIndex), 1, sYearWeekString);
            mThreeStatesButton1.setState(state);
            isBlocked = GoalHelper.isDayBlockedInScheme(1, sGoalList.get(listIndex));
            if (isBlocked) { mThreeStatesButton1.setVisibility(View.INVISIBLE); }

            state = GoalHelper.getEventState(sGoalList.get(listIndex), 2, sYearWeekString);
            mThreeStatesButton2.setState(state);
            isBlocked = GoalHelper.isDayBlockedInScheme(2, sGoalList.get(listIndex));
            if (isBlocked) { mThreeStatesButton2.setVisibility(View.INVISIBLE); }

            state = GoalHelper.getEventState(sGoalList.get(listIndex), 3, sYearWeekString);
            mThreeStatesButton3.setState(state);
            isBlocked = GoalHelper.isDayBlockedInScheme(3, sGoalList.get(listIndex));
            if (isBlocked) { mThreeStatesButton3.setVisibility(View.INVISIBLE); }

            state = GoalHelper.getEventState(sGoalList.get(listIndex), 4, sYearWeekString);
            mThreeStatesButton4.setState(state);
            isBlocked = GoalHelper.isDayBlockedInScheme(4, sGoalList.get(listIndex));
            if (isBlocked) { mThreeStatesButton4.setVisibility(View.INVISIBLE); }

            state = GoalHelper.getEventState(sGoalList.get(listIndex), 5, sYearWeekString);
            mThreeStatesButton5.setState(state);
            isBlocked = GoalHelper.isDayBlockedInScheme(5, sGoalList.get(listIndex));
            if (isBlocked) { mThreeStatesButton5.setVisibility(View.INVISIBLE); }

            state = GoalHelper.getEventState(sGoalList.get(listIndex), 6, sYearWeekString);
            mThreeStatesButton6.setState(state);
            isBlocked = GoalHelper.isDayBlockedInScheme(6, sGoalList.get(listIndex));
            if (isBlocked) { mThreeStatesButton6.setVisibility(View.INVISIBLE); }
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