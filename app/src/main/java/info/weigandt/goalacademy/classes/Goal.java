package info.weigandt.goalacademy.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Goal implements Parcelable {
    private String pushId;
    private String name;
    private int timesPerWeek;
    private int scheduledWeekdays;
    private int status;
    private List<WeeklyEventCounter> WeeklyEventCounterList;

    public List<WeeklyEventCounter> getWeeklyEventCounterList() {
        return WeeklyEventCounterList;
    }

    public void setWeeklyEventCounterList(List<WeeklyEventCounter> weeklyEventCounterList) {
        WeeklyEventCounterList = weeklyEventCounterList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimesPerWeek() {
        return timesPerWeek;
    }

    public void setTimesPerWeek(int timesPerWeek) {
        this.timesPerWeek = timesPerWeek;
    }

    public int getScheduledWeekdays() {
        return scheduledWeekdays;
    }

    public void setScheduledWeekdays(int scheduledWeekdays) {
        this.scheduledWeekdays = scheduledWeekdays;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public static class WeeklyEventCounter implements Parcelable {
        private String yearWeekString;
        private int weekPassCounter;
        private int weekFailCounter;

        public String getYearWeekString() {
            return yearWeekString;
        }

        public void setYearWeekString(String yearWeekString) {
            this.yearWeekString = yearWeekString;
        }

        public int getWeekPassCounter() {
            return weekPassCounter;
        }

        public void setWeekPassCounter(int weekPassCounter) {
            this.weekPassCounter = weekPassCounter;
        }

        public int getWeekFailCounter() {
            return weekFailCounter;
        }

        public void setWeekFailCounter(int weekFailCounter) {
            this.weekFailCounter = weekFailCounter;
        }

        public WeeklyEventCounter() {
        }

        protected WeeklyEventCounter(Parcel in) {
            yearWeekString = in.readString();
            weekPassCounter = in.readInt();
            weekFailCounter = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(yearWeekString);
            dest.writeInt(weekPassCounter);
            dest.writeInt(weekFailCounter);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<WeeklyEventCounter> CREATOR = new Parcelable.Creator<WeeklyEventCounter>() {
            @Override
            public WeeklyEventCounter createFromParcel(Parcel in) {
                return new WeeklyEventCounter(in);
            }

            @Override
            public WeeklyEventCounter[] newArray(int size) {
                return new WeeklyEventCounter[size];
            }
        };
    }

    public Goal() {
    }

    protected Goal(Parcel in) {
        pushId = in.readString();
        name = in.readString();
        timesPerWeek = in.readInt();
        scheduledWeekdays = in.readInt();
        status = in.readInt();
        if (in.readByte() == 0x01) {
            WeeklyEventCounterList = new ArrayList<WeeklyEventCounter>();
            in.readList(WeeklyEventCounterList, WeeklyEventCounter.class.getClassLoader());
        } else {
            WeeklyEventCounterList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pushId);
        dest.writeString(name);
        dest.writeInt(timesPerWeek);
        dest.writeInt(scheduledWeekdays);
        dest.writeInt(status);
        if (WeeklyEventCounterList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(WeeklyEventCounterList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Goal> CREATOR = new Parcelable.Creator<Goal>() {
        @Override
        public Goal createFromParcel(Parcel in) {
            return new Goal(in);
        }

        @Override
        public Goal[] newArray(int size) {
            return new Goal[size];
        }
    };
}