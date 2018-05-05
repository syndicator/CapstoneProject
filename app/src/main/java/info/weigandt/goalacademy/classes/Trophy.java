package info.weigandt.goalacademy.classes;

/**
 * This class is a POJO and the data will be derived from the Goal class upon goal completion
 */
public class Trophy {
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
}
