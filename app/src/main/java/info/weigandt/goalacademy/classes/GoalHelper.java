package info.weigandt.goalacademy.classes;

import android.util.EventLog;

import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.WeekFields;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.weigandt.goalacademy.enums.EventStateEnum;

import static info.weigandt.goalacademy.activities.MainActivity.sGoalList;

public class GoalHelper {
    public static Goal ChangeEventEntryInGoal(Goal goal, EventStateEnum newState, int clickedWeekday, LocalDate currentlyDisplayedLocalDate)
    {
        String currentlyDisplayedYearWeekString = convertDateToYearWeekString(currentlyDisplayedLocalDate);

        // #case0   Status is now PASS: this means, before it has been NEUTRAL
        //  -> add the pass to the list
        // check for existing week entry. If not present, create a new one!

        // #case1 Status is now FAIL: -> means was PASS before
        //  -> remove from PASS list first, then add to FAIL list!

        // case2 Status is now NEUTRAL -> means was FAIL before
        //  -> remove from fail list

        if (goal.getWeeklyEventCounterList() == null)
        {
            // create a new counter for this week and add it to goal
            int newCounter = AddEventToCounter(0, clickedWeekday);
            Goal.WeeklyEventCounter weeklyEventCounter = new Goal.WeeklyEventCounter();
            weeklyEventCounter.setYearWeekString(currentlyDisplayedYearWeekString);
            weeklyEventCounter.setWeekPassCounter(newCounter);
            goal.setWeeklyEventCounterList(new ArrayList<Goal.WeeklyEventCounter>());
            goal.getWeeklyEventCounterList().add(weeklyEventCounter);
        }
        else
        {
            List<Goal.WeeklyEventCounter> currentWeeklyEventCounterList = goal.getWeeklyEventCounterList();
            if (newState == EventStateEnum.PASS)
            {
                int updatedCounter;
                boolean found = false;
                for (int i = 0; i < currentWeeklyEventCounterList.size() && !found; i++)
                {
                    // check out which counter we need to change, given by currentlyDisplayedLocalDate
                    Goal.WeeklyEventCounter weeklyEventCounter = currentWeeklyEventCounterList.get(i);
                    String yearsWeekString = weeklyEventCounter.getYearWeekString();
                    if (yearsWeekString.equals(currentlyDisplayedYearWeekString))
                    {
                        int weekPassCounter = weeklyEventCounter.getWeekPassCounter();  // TODO this MUST be set to 0 as default in class, else this will crash!
                        updatedCounter = GoalHelper.AddEventToCounter(weekPassCounter, clickedWeekday);
                        goal.getWeeklyEventCounterList().get(i).setWeekPassCounter(updatedCounter);
                        found = true;
                    }
                }
                if (!found) {
                    // create a new counter for this week and add it to goal
                    updatedCounter = AddEventToCounter(0, clickedWeekday);
                    Goal.WeeklyEventCounter weeklyEventCounter = new Goal.WeeklyEventCounter();
                    weeklyEventCounter.setYearWeekString(currentlyDisplayedYearWeekString);
                    weeklyEventCounter.setWeekPassCounter(updatedCounter);
                    goal.getWeeklyEventCounterList().add(weeklyEventCounter);
                }

            }
            else if (newState == EventStateEnum.FAIL) {
                int updatedCounter;
                boolean found = false;
                for (int i = 0; i < currentWeeklyEventCounterList.size() && !found; i++)
                {
                    // check out which counter we need to change, given by currentlyDisplayedLocalDate
                    Goal.WeeklyEventCounter weeklyEventCounter = currentWeeklyEventCounterList.get(i);
                    String yearsWeekString = weeklyEventCounter.getYearWeekString();
                    if (yearsWeekString.equals(currentlyDisplayedYearWeekString))
                    {
                        // remove from PASS list first
                        int tempPassCounter = goal.getWeeklyEventCounterList().get(i).getWeekPassCounter();
                        int cleanedPassCounter = GoalHelper.RemoveEventFromCounter(tempPassCounter, clickedWeekday);
                        goal.getWeeklyEventCounterList().get(i).setWeekPassCounter(cleanedPassCounter);

                        // add to FAIL list
                        int weekFailCounter = weeklyEventCounter.getWeekFailCounter();  // TODO this MUST be set to 0 as default in class, else this will crash!
                        updatedCounter = GoalHelper.AddEventToCounter(weekFailCounter, clickedWeekday);
                        goal.getWeeklyEventCounterList().get(i).setWeekFailCounter(updatedCounter);
                        found = true;
                    }
                }
            }
            else if (newState == EventStateEnum.NEUTRAL) {
                int updatedCounter;
                boolean found = false;
                for (int i = 0; i < currentWeeklyEventCounterList.size() && !found; i++)
                {
                    // check out which counter we need to change, given by currentlyDisplayedLocalDate
                    Goal.WeeklyEventCounter weeklyEventCounter = currentWeeklyEventCounterList.get(i);
                    String yearsWeekString = weeklyEventCounter.getYearWeekString();
                    if (yearsWeekString.equals(currentlyDisplayedYearWeekString))
                    {
                        // remove from FAIL list
                        int tempPassCounter = goal.getWeeklyEventCounterList().get(i).getWeekFailCounter();
                        int cleanedPassCounter = GoalHelper.RemoveEventFromCounter(tempPassCounter, clickedWeekday);
                        goal.getWeeklyEventCounterList().get(i).setWeekFailCounter(cleanedPassCounter);
                        found = true;
                    }
                }
            }
        }
        return goal;
    }

    public static String convertDateToYearWeekString(LocalDate localDate) {
        String yearWeekString = String.valueOf(localDate.getYear());
        TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNumber = localDate.get(weekOfYear);
        return yearWeekString + "-" + String.valueOf(weekNumber);
    }

    private static int AddEventToCounter(int weekCounter, int clickedWeekday) {
        switch (clickedWeekday) {
            case 0:
                weekCounter += 1;
                break;
            case 1:
                weekCounter += 2;
                break;
            case 2:
                weekCounter += 4;
                break;
            case 3:
                weekCounter += 8;
                break;
            case 4:
                weekCounter += 16;
                break;
            case 5:
                weekCounter += 32;
                break;
            case 6:
                weekCounter += 64;
                break;
        }
        return weekCounter;
    }
    private static int RemoveEventFromCounter(int weekCounter, int clickedWeekday) {
        switch (clickedWeekday) {
            case 0:
                weekCounter -= 1;
                break;
            case 1:
                weekCounter -= 2;
                break;
            case 2:
                weekCounter -= 4;
                break;
            case 3:
                weekCounter -= 8;
                break;
            case 4:
                weekCounter -= 16;
                break;
            case 5:
                weekCounter -= 32;
                break;
            case 6:
                weekCounter -= 64;
                break;
        }
        return weekCounter;
    }
    public static EventStateEnum getEventState(Goal goal, int weekDay, String yearWeekString)
    {
        if (goal.getWeeklyEventCounterList() == null)
        {
            return EventStateEnum.NEUTRAL;
        }
        else
        {
            for (int i = 0; i < goal.getWeeklyEventCounterList().size(); i++)
            {
                Goal.WeeklyEventCounter weeklyEventCounter = goal.getWeeklyEventCounterList().get(i);
                if (weeklyEventCounter.getYearWeekString().equals(yearWeekString))
                {
                    if(checkIfDayIsInCounter(weekDay, goal.getWeeklyEventCounterList().get(i).getWeekPassCounter()))
                    {
                        return EventStateEnum.PASS;
                    }
                    else if (checkIfDayIsInCounter(weekDay, goal.getWeeklyEventCounterList().get(i).getWeekFailCounter()))
                    {
                        return EventStateEnum.FAIL;
                    }
                    else
                    {
                        return EventStateEnum.NEUTRAL;
                    }
                }
            }
            return EventStateEnum.NEUTRAL;
        }

    }
    public static boolean checkIfDayIsInCounter(int weekDay, int weekCounter)
    {
        if (weekCounter>=64)
        {
            if (weekDay == 6) { return true; }
            weekCounter -=64;
        }
        if (weekCounter>=32)
        {
            if (weekDay == 5) { return true; }
            weekCounter -=32;
        }
        if (weekCounter>=16)
        {
            if (weekDay == 4) { return true; }
            weekCounter -=16;
        }
        if (weekCounter>=8)
        {
            if (weekDay == 3) { return true; }
            weekCounter -=8;
        }
        if (weekCounter>=4)
        {
            if (weekDay == 2) { return true; }
            weekCounter -=4;
        }
        if (weekCounter>=2)
        {
            if (weekDay == 1) { return true; }
            weekCounter -=2;
        }
        if (weekCounter==1)
        {
            if (weekDay == 0) { return true; }
        }
        return false;
    }

    public static boolean isDayBlockedInScheme(int weekDay, Goal goal) {
        if ((goal.getTimesPerWeek() != 0))
        {
            return false;
        }
        int scheme = goal.getScheduledWeekdays();
        if (scheme>=64)
        {
            if (weekDay == 6) { return false; }
            scheme -=64;
        }
        if (scheme>=32)
        {
            if (weekDay == 5) { return false; }
            scheme -=32;
        }
        if (scheme>=16)
        {
            if (weekDay == 4) { return false; }
            scheme -=16;
        }
        if (scheme>=8)
        {
            if (weekDay == 3) { return false; }
            scheme -=8;
        }
        if (scheme>=4)
        {
            if (weekDay == 2) { return false; }
            scheme -=4;
        }
        if (scheme>=2)
        {
            if (weekDay == 1) { return false; }
            scheme -=2;
        }
        if (scheme==1)
        {
            if (weekDay == 0) { return false; }
        }
        return true;
    }

    public static int calculateNumberOfEvents(int weekCounter) {
        int total = 0;
        if (weekCounter>=64)
        {
            total += 1;
            weekCounter -=64;
        }
        if (weekCounter>=32)
        {
            total += 1;
            weekCounter -=32;
        }
        if (weekCounter>=16)
        {
            total += 1;
            weekCounter -=16;
        }
        if (weekCounter>=8)
        {
            total += 1;
            weekCounter -=8;
        }
        if (weekCounter>=4)
        {
            total += 1;
            weekCounter -=4;
        }
        if (weekCounter>=2)
        {
            total += 1;
            weekCounter -=2;
        }
        if (weekCounter==1)
        {
            total += 1;
        }
        return total;
    }

    public static int calculateNumberOfPassesGivenWeek(Goal goal, LocalDate displayedWeek) {
        int totalPasses = 0;
        for (Goal.WeeklyEventCounter weeklyEventCounter : goal.getWeeklyEventCounterList()) {
            if (weeklyEventCounter.getYearWeekString().equals(convertDateToYearWeekString(displayedWeek))) {
                totalPasses += calculateNumberOfEvents(weeklyEventCounter.getWeekPassCounter());
            }
        }
        return totalPasses;
    }

    public static int calculateNumberOfTotalPasses(Goal goal) {
        if (goal.getWeeklyEventCounterList() == null)
        {
            return 0;
        }
        else
        {
            int totalPasses = 0;
            for (Goal.WeeklyEventCounter weeklyEventCounter : goal.getWeeklyEventCounterList()) {
                totalPasses += calculateNumberOfEvents(weeklyEventCounter.getWeekPassCounter());
            }
            return totalPasses;
        }
    }

    public static int calculateNumberOfFails(Goal goal, LocalDate displayedWeek) {
        int totalFails = 0;
        for (Goal.WeeklyEventCounter weeklyEventCounter : goal.getWeeklyEventCounterList()) {
            if (weeklyEventCounter.getYearWeekString().equals(convertDateToYearWeekString(displayedWeek))) {
                totalFails += calculateNumberOfEvents(weeklyEventCounter.getWeekFailCounter());
            }
        }
        return totalFails;
    }
}
