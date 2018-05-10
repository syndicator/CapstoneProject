package info.weigandt.goalacademy.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is a POJO and the data will be derived from the Goal class upon goal completion
 */
public class Trophy implements Parcelable {
    private String goalName;
    private String completionDate;
    private String award;
    private String pushId;

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public Trophy() {
    }

    protected Trophy(Parcel in) {
        goalName = in.readString();
        completionDate = in.readString();
        award = in.readString();
        pushId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(goalName);
        dest.writeString(completionDate);
        dest.writeString(award);
        dest.writeString(pushId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trophy> CREATOR = new Parcelable.Creator<Trophy>() {
        @Override
        public Trophy createFromParcel(Parcel in) {
            return new Trophy(in);
        }

        @Override
        public Trophy[] newArray(int size) {
            return new Trophy[size];
        }
    };
}
