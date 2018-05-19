package info.weigandt.goalacademy.classes;

public class Config {
    public static final int NUMBER_FOR_BRONZE = 5;
    public static final int NUMBER_FOR_SILVER = 21;
    public static final int NUMBER_FOR_GOLD = 66;
    public static final int PAGE_COUNT = 3;
    public static final String ShareText = "I completed a goal on Goal Academy :)"; // Version 2: Replace with Remote config
    public static final String LIMIT_QUOTES_STRING = "RANDOM() LIMIT 1";
    public static final String AUTHOR_FALLBACK_STRING = "Unknown Author";
    public static final String CRITICAL_GOAL_WIDGET_TEXT = "Urgent!";
    public static final String NORMAL_GOAL_WIDGET_TEXT = "Just  do it.";
    public static final String GOALS_NODE_NAME = "goals";
    public static final String TROPHIES_NODE_NAME = "trophies";
    public static final String TROPHY_TEXT_A = "\\n Goal: ";
    public static final String TROPHY_TEXT_B = "\\n Trophy gained: ";
    public static final String TROPHY_TEXT_C = "\\n Date of award: ";
    public static final String SQL_ERROR_TEXT = "Failed to insert row into ";
    public static final String URI_ERROR = "Unknown uri: ";
    public static final String ERROR_INSERT = "Error while inserting. Exception:%s";
    public static final String UNKNOWN_URI = "Unknown uri: ";
    public static final String NOT_IMPLEMENTED = "Not yet implemented";
    public static final String QUERY_ERROR = "Error while querying. %s";

    public static final String QUOTES_DB_NAME = "quotesDb.db";
    public static final String MUST_IMPLEMENT_LISTENER = " must implement OnFragmentInteractionListener";
    public static final String QUOTES_ERROR = "Error while getting quote from Content Provider. Message: %s";
    public static final String PROBLEM_WRITING_CONTENT_PROVIDER = "Could not write to Content Provider.";
    public static final String ERROR_READING_SP = "Error while trying to read from Shared Preferences. %s";
    public static final String WIDGET_ERROR = "Intent without SERIALIZED_WIDGET_DATA. Cannot update Widget correctly.";
}
