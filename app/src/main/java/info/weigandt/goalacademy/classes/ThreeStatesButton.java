package info.weigandt.goalacademy.classes;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

public class ThreeStatesButton extends AppCompatImageButton {

    public enum StatesEnum {
        NEUTRAL, CHECK, FAIL
    }

    public interface ThreeStatesListener {
        void onNeutral();

        void onCheck();

        void onFail();
    }

    private StatesEnum mState;
    private ThreeStatesListener mThreeStatesListener;

    public ThreeStatesButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Sets initial state
        setState(StatesEnum.NEUTRAL);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        int next = ((mState.ordinal() + 1) % StatesEnum.values().length);
        setState(StatesEnum.values()[next]);
        performThreeStatesClick();
        return true;
    }

    private void performThreeStatesClick() {
        if (mThreeStatesListener == null) return;
        switch (mState) {
            case NEUTRAL:
                mThreeStatesListener.onNeutral();
                break;
            case CHECK:
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
            case CHECK:
                setImageResource(R.drawable.ic_check);
                break;
            case FAIL:
                setImageResource(R.drawable.ic_fail);
                break;
        }
    }

    public StatesEnum getState() {
        return mState;
    }

    public void setState(StatesEnum state) {
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
