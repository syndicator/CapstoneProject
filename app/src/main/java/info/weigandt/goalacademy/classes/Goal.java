package info.weigandt.goalacademy.classes;

import java.util.List;

public class Goal {
    private String name;
    private String stringStartDate;
    private int timesPerWeek;
    private int scheduledWeekdays;
    private List<Integer> counterCompletedEvents;   // Starts in the week of stringStartDate, further weeks are added sequentially
    private int status;

    public List<Integer> getCounterCompletedEvents() {
        return counterCompletedEvents;
    }

    public void setCounterCompletedEvents(List<Integer> counterCompletedEvents) {
        this.counterCompletedEvents = counterCompletedEvents;
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

}
