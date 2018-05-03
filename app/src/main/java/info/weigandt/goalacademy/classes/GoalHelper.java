package info.weigandt.goalacademy.classes;

import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.WeekFields;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.weigandt.goalacademy.enums.EventStateEnum;

public class GoalHelper {
    public static Goal ChangeEvent(Goal goal, EventStateEnum newState, int clickedWeekday, LocalDate currentlyDisplayedLocalDate)
    {

        // region prepare String format of current week
        String currentlyDisplayedYearWeekString = String.valueOf(currentlyDisplayedLocalDate.getYear());
        TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNumber = currentlyDisplayedLocalDate.get(weekOfYear);
        currentlyDisplayedYearWeekString = currentlyDisplayedYearWeekString + "-" + String.valueOf(weekNumber);
        // endregion

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
}
