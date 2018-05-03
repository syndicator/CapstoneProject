package info.weigandt.goalacademy.classes;

import java.util.List;

public class Goal {
    private String pushId;
    private String name;
    private String stringStartDate;
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

    public String getStringStartDate() {
        return stringStartDate;
    }

    public void setStringStartDate(String stringStartDate) {
        this.stringStartDate = stringStartDate;
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

    public static class WeeklyEventCounter {
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
    }
}
