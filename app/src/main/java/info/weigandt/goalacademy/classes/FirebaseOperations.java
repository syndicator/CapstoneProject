package info.weigandt.goalacademy.classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static info.weigandt.goalacademy.activities.MainActivity.sGoalsDatabaseReference;

public class FirebaseOperations {

    public static void UpdateGoal(Goal goal) {
        String goalId = goal.getPushId();
        DatabaseReference goalReference = sGoalsDatabaseReference.child(goalId);
        goalReference.setValue(goal);
    }

    public static void FirebaseSetup() {

    }

    public static void addGoalToDatabase(Goal goal) {
        sGoalsDatabaseReference.push().setValue(goal);
    }
}
