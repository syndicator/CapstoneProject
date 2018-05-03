package info.weigandt.goalacademy.classes;

import org.threeten.bp.LocalDateTime;

import info.weigandt.goalacademy.enums.AwardEnum;

/**
 * This class is not a POJO and the data will be derived from the Goal class
 */
public class Trophy {
    private String goalName;
    private LocalDateTime completionDate;
    private Enum<AwardEnum> award;

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public Enum<AwardEnum> getAward() {
        return award;
    }

    public void setAward(Enum<AwardEnum> award) {
        this.award = award;
    }

}
