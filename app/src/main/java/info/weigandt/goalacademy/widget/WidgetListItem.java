package info.weigandt.goalacademy.widget;

public class WidgetListItem {
    private String goalName;
    private String infoText;

    public WidgetListItem(String goalName, String infoText) {
            this.goalName = goalName;
            this.infoText = infoText;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }
}
