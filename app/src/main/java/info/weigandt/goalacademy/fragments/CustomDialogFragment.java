package info.weigandt.goalacademy.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.weigandt.goalacademy.R;
import timber.log.Timber;

public class CustomDialogFragment extends DialogFragment {

    private View mView;
    private int mTimesAweek;
    @BindView(R.id.et_goal_name) TextView mEditTextGoalName;
    @BindView(R.id.btn_plus) Button mButtonPlus;
    @BindView(R.id.btn_minus) Button mButtonMinus;
    @BindView(R.id.et_times_a_week) EditText mEditTextTimesAweek;

    public CustomDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static CustomDialogFragment newInstance(String title) {
        CustomDialogFragment customDialogFragment = new CustomDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);   // TODO remove/comment out if not needed
        customDialogFragment.setArguments(args);
        return customDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragmentdialog_new_goal, container);
        ButterKnife.bind(this, mView);
        Timber.e("DOH!");
        mView.findViewById(R.layout.)
        mButtonPlus.setText("DADADA");
        mButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timber.e("onClick executed.");
                if (Integer.getInteger(mEditTextTimesAweek.getText().toString()) < 7)
                {
                    mTimesAweek +=1;
                    mEditTextTimesAweek.setText(String.valueOf(mTimesAweek));
                }
            }
        });
        return mView;
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
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.fragmentdialog_new_goal, null))
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO handle data callback here?
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CustomDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
