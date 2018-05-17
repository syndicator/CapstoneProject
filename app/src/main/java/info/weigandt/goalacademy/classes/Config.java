package info.weigandt.goalacademy.classes;

public class Config {
    public static final int NUMBER_FOR_BRONZE = 5;
    public static final int NUMBER_FOR_SILVER = 21;
    public static final int NUMBER_FOR_GOLD = 66;
    public static final String DIALOG_MESSAGE = "DIALOG_MESSAGE";
    public static final int PAGE_COUNT = 3;
    public static final String ShareText = "I completed a goal on Goal Academy :)"; // Version 2: Replace with Remote config
    public static final String LIMIT_QUOTES_STRING = "RANDOM() LIMIT 1";
    public static final String AUTHOR_FALLBACK_STRING = "Unknown Author";     // TODO move to strings.xml;
    public static final String CRITICAL_GOAL_WIDGET_TEXT = "urgent!";
    public static final String NORMAL_GOAL_WIDGET_TEXT = "Just  do it.";

    public Config() {
        // ShareText = Resources.getSystem().getString(android.R.string.share_text);
        // ShareText = "I won a trophy for a completing a goal on Goal Academy!";
    }
}
