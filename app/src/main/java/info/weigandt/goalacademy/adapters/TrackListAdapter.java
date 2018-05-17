package info.weigandt.goalacademy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.classes.GoalHelper;
import info.weigandt.goalacademy.classes.ThreeStatesButton;
import info.weigandt.goalacademy.enums.EventStateEnum;
import timber.log.Timber;

import static info.weigandt.goalacademy.activities.MainActivity.sGoalList;
import static info.weigandt.goalacademy.fragments.TrackFragment.sYearWeekString;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.TrackViewHolder> {

    // region Variables
    private Context mContext;
    // List is accessible as static from MainActivity
    public TrackListAdapterListener onClickListener;
    private long mLastClickTime;
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


        TrackViewHolder trackViewHolder = new TrackViewHolder(view);
        // trackViewHolder.enableAllButtons();
        return trackViewHolder;
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
        holder.bind(position, holder);
    }

    // endregion Overrides

    // region Interface TrackListAdapter listener
    public interface TrackListAdapterListener {

        void button_0_OnClick(ThreeStatesButton threeStatesButton, int position);

        void button_1_OnClick(ThreeStatesButton threeStatesButton, int position);

        void button_2_OnClick(ThreeStatesButton threeStatesButton, int position);

        void button_3_OnClick(ThreeStatesButton threeStatesButton, int position);

        void button_4_OnClick(ThreeStatesButton threeStatesButton, int position);

        void button_5_OnClick(ThreeStatesButton threeStatesButton, int position);

        void button_6_OnClick(ThreeStatesButton threeStatesButton, int position);
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
        private ArrayList<ThreeStatesButton> mButtonList;

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
            mButtonList = new ArrayList<>();

            mThreeStatesButton0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.button_0_OnClick(mThreeStatesButton0, getAdapterPosition());
                }
            });
            mButtonList.add(mThreeStatesButton0);

            mThreeStatesButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.button_1_OnClick(mThreeStatesButton1, getAdapterPosition());

                }
            });
            mButtonList.add(mThreeStatesButton1);

            mThreeStatesButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.button_2_OnClick(mThreeStatesButton2, getAdapterPosition());

                }
            });
            mButtonList.add(mThreeStatesButton2);

            mThreeStatesButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.button_3_OnClick(mThreeStatesButton3, getAdapterPosition());
                }
            });
            mButtonList.add(mThreeStatesButton3);

            mThreeStatesButton4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.button_4_OnClick(mThreeStatesButton4, getAdapterPosition());
                }
            });
            mButtonList.add(mThreeStatesButton4);

            mThreeStatesButton5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.button_5_OnClick(mThreeStatesButton5, getAdapterPosition());
                }
            });
            mButtonList.add(mThreeStatesButton5);

            mThreeStatesButton6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if (!isSafeClick()) return;
                    onClickListener.button_6_OnClick(mThreeStatesButton6, getAdapterPosition());
                }
            });
            mButtonList.add(mThreeStatesButton6);
            disableAllButtons();
        }

        public void disableAllButtons() {
            for (ThreeStatesButton button : mButtonList)
            {
                button.setEnabled(false);
            }
        }
        public void enableAllButtons() {
            for (ThreeStatesButton button : mButtonList)
            {
                button.setEnabled(true);
            }
        }

        /**
         * This method takes an integer as input and
         * uses that integer to display the appropriate content within a list item
         *
         * @param listIndex Position of the item in the list
         * @param holder
         */
        void bind(int listIndex, TrackViewHolder holder) {
            EventStateEnum state;
            mTextViewGoalName.setText(sGoalList.get(listIndex).getName());

            state = GoalHelper.getEventState(sGoalList.get(listIndex), 0, sYearWeekString);
            mThreeStatesButton0.setState(state);
            mThreeStatesButton0 = setContentDescriptionBasedOnState(mThreeStatesButton0);

            boolean isBlocked = GoalHelper.isDayBlockedInScheme(0, sGoalList.get(listIndex));
            if (isBlocked) {
                mThreeStatesButton0.setVisibility(View.INVISIBLE);
            } else {
                mThreeStatesButton0.setVisibility(View.VISIBLE);
            }

            state = GoalHelper.getEventState(sGoalList.get(listIndex), 1, sYearWeekString);
            mThreeStatesButton1.setState(state);
            mThreeStatesButton1 = setContentDescriptionBasedOnState(mThreeStatesButton1);
            isBlocked = GoalHelper.isDayBlockedInScheme(1, sGoalList.get(listIndex));
            if (isBlocked) {
                mThreeStatesButton1.setVisibility(View.INVISIBLE);
            } else {
                mThreeStatesButton1.setVisibility(View.VISIBLE);
            }

            state = GoalHelper.getEventState(sGoalList.get(listIndex), 2, sYearWeekString);
            mThreeStatesButton2.setState(state);
            mThreeStatesButton2 = setContentDescriptionBasedOnState(mThreeStatesButton2);
            isBlocked = GoalHelper.isDayBlockedInScheme(2, sGoalList.get(listIndex));
            if (isBlocked) {
                mThreeStatesButton2.setVisibility(View.INVISIBLE);
            }
            else {
                mThreeStatesButton2.setVisibility(View.VISIBLE);
            }

            state = GoalHelper.getEventState(sGoalList.get(listIndex), 3, sYearWeekString);
            mThreeStatesButton3.setState(state);
            mThreeStatesButton3 = setContentDescriptionBasedOnState(mThreeStatesButton3);
            isBlocked = GoalHelper.isDayBlockedInScheme(3, sGoalList.get(listIndex));
            if (isBlocked) {
                mThreeStatesButton3.setVisibility(View.INVISIBLE);
            } else {
                mThreeStatesButton3.setVisibility(View.VISIBLE);
            }


            state = GoalHelper.getEventState(sGoalList.get(listIndex), 4, sYearWeekString);
            mThreeStatesButton4.setState(state);
            mThreeStatesButton4 = setContentDescriptionBasedOnState(mThreeStatesButton4);
            isBlocked = GoalHelper.isDayBlockedInScheme(4, sGoalList.get(listIndex));
            if (isBlocked) {
                mThreeStatesButton4.setVisibility(View.INVISIBLE);
            }
            else {
                mThreeStatesButton4.setVisibility(View.VISIBLE);
            }

            state = GoalHelper.getEventState(sGoalList.get(listIndex), 5, sYearWeekString);
            mThreeStatesButton5.setState(state);
            mThreeStatesButton5 = setContentDescriptionBasedOnState(mThreeStatesButton5);
            isBlocked = GoalHelper.isDayBlockedInScheme(5, sGoalList.get(listIndex));
            if (isBlocked) {
                mThreeStatesButton5.setVisibility(View.INVISIBLE);
            }
            else {
                mThreeStatesButton5.setVisibility(View.VISIBLE);
            }

            state = GoalHelper.getEventState(sGoalList.get(listIndex), 6, sYearWeekString);
            mThreeStatesButton6.setState(state);
            mThreeStatesButton6 = setContentDescriptionBasedOnState(mThreeStatesButton6);
            isBlocked = GoalHelper.isDayBlockedInScheme(6, sGoalList.get(listIndex));
            if (isBlocked) {
                mThreeStatesButton6.setVisibility(View.INVISIBLE);
            }
            else {
                mThreeStatesButton6.setVisibility(View.VISIBLE);
            }

            holder.enableAllButtons();
        }

        private ThreeStatesButton setContentDescriptionBasedOnState(ThreeStatesButton threeStatesButton) {
            if (threeStatesButton.getState().equals(EventStateEnum.PASS))
            {
                String description =  mContext.getResources().getString(R.string.description_three_states_button_pass);
                threeStatesButton.setContentDescription(description);
            } else if (threeStatesButton.getState().equals(EventStateEnum.FAIL)) {
                String description =  mContext.getResources().getString(R.string.description_three_states_button_fail);
                threeStatesButton.setContentDescription(description);
            }
            else {
                String description =  mContext.getResources().getString(R.string.description_three_states_button_neutral);
                threeStatesButton.setContentDescription(description);
            }
            return threeStatesButton;
        }
    }
    // endregion Inner Class
}