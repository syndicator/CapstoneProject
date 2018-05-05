package info.weigandt.goalacademy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.classes.Goal;

public class CustomDialogFragment extends DialogFragment {

    private View mView;
    // Instance of the interface to deliver action events to the "parent" class
    public CustomDialogFragmentListener mCustomDialogFragmentListener;
    public int mTimesAweek;
    @BindView(R.id.et_goal_name)
    TextView mEditTextGoalName;
    @BindView(R.id.btn_plus)
    Button mButtonPlus;
    @BindView(R.id.btn_minus)
    Button mButtonMinus;
    @BindView(R.id.et_times_a_week)
    EditText mEditTextTimesAweek;
    @BindView(R.id.checkbox_monday)
    CheckBox mCheckboxMonday;
    @BindView(R.id.checkbox_tuesday)
    CheckBox mCheckboxTuesday;
    @BindView(R.id.checkbox_wednesday)
    CheckBox mCheckboxWednesday;
    @BindView(R.id.checkbox_thursday)
    CheckBox mCheckboxThursday;
    @BindView(R.id.checkbox_friday)
    CheckBox mCheckboxFriday;
    @BindView(R.id.checkbox_saturday)
    CheckBox mCheckboxSaturday;
    @BindView(R.id.checkbox_sunday)
    CheckBox mCheckboxSunday;
    @BindView(R.id.btn_ok)
    Button mButtonOk;
    @BindView(R.id.btn_cancel)
    Button mButtonCancel;

    public CustomDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Use `newInstance` instead as shown below to add arguments
    }

    public static CustomDialogFragment newInstance(String title) {
        CustomDialogFragment customDialogFragment = new CustomDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);   // TODO remove/comment out if not needed
        customDialogFragment.setArguments(args);
        return customDialogFragment;
    }

    public interface CustomDialogFragmentListener {
        void onDialogPositiveClick(Goal goal);
    }

    public void setCustomDialogFragmentListener(CustomDialogFragmentListener customDialogFragmentListener) {
        mCustomDialogFragmentListener = customDialogFragmentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragmentdialog_new_goal, container);
        ButterKnife.bind(this, mView);
        mTimesAweek = 0;    // TODO later check if ok after state is restored
        mButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckAllCheckboxes();
                if (mTimesAweek < 7) {
                    mTimesAweek += 1;
                    mEditTextTimesAweek.setText(String.valueOf(mTimesAweek));
                }
            }
        });
        mButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimesAweek > 0) {
                    mTimesAweek -= 1;
                    mEditTextTimesAweek.setText(String.valueOf(mTimesAweek));
                }
            }
        });
        mCheckboxMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimesAweek > 0) {
                    mTimesAweek = 0;
                    mEditTextTimesAweek.setText(String.valueOf(mTimesAweek));
                }
            }
        });
        mCheckboxTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimesAweek > 0) {
                    mTimesAweek = 0;
                    mEditTextTimesAweek.setText(String.valueOf(mTimesAweek));
                }
            }
        });
        mCheckboxWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimesAweek > 0) {
                    mTimesAweek = 0;
                    mEditTextTimesAweek.setText(String.valueOf(mTimesAweek));
                }
            }
        });
        mCheckboxThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimesAweek > 0) {
                    mTimesAweek = 0;
                    mEditTextTimesAweek.setText(String.valueOf(mTimesAweek));
                }
            }
        });
        mCheckboxFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimesAweek > 0) {
                    mTimesAweek = 0;
                    mEditTextTimesAweek.setText(String.valueOf(mTimesAweek));
                }
            }
        });
        mCheckboxSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimesAweek > 0) {
                    mTimesAweek = 0;
                    mEditTextTimesAweek.setText(String.valueOf(mTimesAweek));
                }
            }
        });
        mCheckboxSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimesAweek > 0) {
                    mTimesAweek = 0;
                    mEditTextTimesAweek.setText(String.valueOf(mTimesAweek));
                }
            }
        });
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mEditTextGoalName.getText().toString().matches("")) {
                    Toast.makeText(getContext(), R.string.error_no_goal_name, Toast.LENGTH_SHORT).show();
                } else if (!((mCheckboxMonday.isChecked() ||
                        mCheckboxTuesday.isChecked() ||
                        mCheckboxWednesday.isChecked() ||
                        mCheckboxThursday.isChecked() ||
                        mCheckboxFriday.isChecked() ||
                        mCheckboxSaturday.isChecked() ||
                        mCheckboxSunday.isChecked()) ||
                        mTimesAweek > 0)) {
                    Toast.makeText(getContext(), R.string.error_no_time_chosen, Toast.LENGTH_SHORT).show();
                } else {
                    Goal goal = new Goal();
                    goal.setName(mEditTextGoalName.getText().toString());

                    if (mTimesAweek != 0)
                    {
                        goal.setTimesPerWeek(mTimesAweek);
                    }
                    else
                    {
                        int number = 0;
                        if (mCheckboxMonday.isChecked())
                        {
                            number +=1;
                        }
                        if (mCheckboxTuesday.isChecked())
                        {
                            number +=2;
                        }
                        if (mCheckboxWednesday.isChecked())
                        {
                            number +=4;
                        }
                        if (mCheckboxThursday.isChecked())
                        {
                            number +=8;
                        }
                        if (mCheckboxFriday.isChecked())
                        {
                            number +=16;
                        }
                        if (mCheckboxSaturday.isChecked())
                        {
                            number +=32;
                        }
                        if (mCheckboxSunday.isChecked())
                        {
                            number +=64;
                        }
                        goal.setScheduledWeekdays(number);
                    }

                    // Now let's trigger the event
                    if (mCustomDialogFragmentListener != null)
                        mCustomDialogFragmentListener.onDialogPositiveClick(goal); // <---- fire listener here
                    dismiss();
                }
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return mView;
    }

    private void uncheckAllCheckboxes() {
        if (mCheckboxMonday.isChecked()) {
            mCheckboxMonday.toggle();
        }
        if (mCheckboxTuesday.isChecked()) {
            mCheckboxTuesday.toggle();
        }
        if (mCheckboxWednesday.isChecked()) {
            mCheckboxWednesday.toggle();
        }
        if (mCheckboxThursday.isChecked()) {
            mCheckboxThursday.toggle();
        }
        if (mCheckboxFriday.isChecked()) {
            mCheckboxFriday.toggle();
        }
        if (mCheckboxSaturday.isChecked()) {
            mCheckboxSaturday.toggle();
        }
        if (mCheckboxSunday.isChecked()) {
            mCheckboxSunday.toggle();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* appearently, onCreateDialog is used.
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Create new goal");
        getDialog().setTitle(title);

        // Show soft keyboard automatically and request focus to field
        mEditTextGoalName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                */
    }

}
