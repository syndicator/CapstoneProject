package info.weigandt.goalacademy.classes;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.enums.EventStateEnum;

public class ThreeStatesButton extends AppCompatImageButton {

    public interface ThreeStatesListener {
        void onNeutral();

        void onCheck();

        void onFail();
    }

    private EventStateEnum mState;
    private ThreeStatesListener mThreeStatesListener;

    public ThreeStatesButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Sets initial state
        setState(EventStateEnum.NEUTRAL);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        int next = ((mState.ordinal() + 1) % EventStateEnum.values().length);
        setState(EventStateEnum.values()[next]);
        performThreeStatesClick();
        return true;
    }

    private void performThreeStatesClick() {
        if (mThreeStatesListener == null) return;
        switch (mState) {
            case NEUTRAL:
                mThreeStatesListener.onNeutral();
                break;
            case PASS:
                mThreeStatesListener.onCheck();
                break;
            case FAIL:
                mThreeStatesListener.onFail();
                break;
        }
    }

    private void createDrawableState() {
        switch (mState) {
            case NEUTRAL:
                setImageResource(R.drawable.ic_neutral);
                break;
            case PASS:
                setImageResource(R.drawable.ic_check);
                break;
            case FAIL:
                setImageResource(R.drawable.ic_fail);
                break;
        }
    }

    public EventStateEnum getState() {
        return mState;
    }

    public void setState(EventStateEnum state) {
        if (state == null) return;
        this.mState = state;
        createDrawableState();
    }

    public ThreeStatesListener getThreeStatesListener() {
        return mThreeStatesListener;
    }

    public void setThreeStatesListener(ThreeStatesListener threeStatesListener) {
        this.mThreeStatesListener = threeStatesListener;
    }
}
