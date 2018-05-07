package info.weigandt.goalacademy.classes;

import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static info.weigandt.goalacademy.activities.MainActivity.sGoalList;
import static info.weigandt.goalacademy.activities.MainActivity.sGoalsDatabaseReference;
import static info.weigandt.goalacademy.activities.MainActivity.sTrophiesDatabaseReference;

public class FirebaseOperations {

    public static void UpdateGoal(Goal goal) {
        String goalId = goal.getPushId();
        DatabaseReference goalReference = sGoalsDatabaseReference.child(goalId);
        goalReference.setValue(goal);
    }

    public static void addGoalToDatabase(Goal goal) {
        // This way we get the key first, then store the goal there
        String pushKey = sGoalsDatabaseReference.push().getKey();
        goal.setPushId(pushKey);
        sGoalsDatabaseReference.child(pushKey).setValue(goal);
    }
    public static void addTrophyToDatabase(Trophy trophy) {
        // sTrophiesDatabaseReference.push().setValue(trophy);

        // This way we get the key first, then store the goal there
        String pushKey = sTrophiesDatabaseReference.push().getKey();
        trophy.setPushId(pushKey);
        sTrophiesDatabaseReference.child(pushKey).setValue(trophy);
    }

    public static void removeGoalFromDatabase(Goal goal)
    {
        sGoalsDatabaseReference.child(goal.getPushId()).removeValue();
    }

    public static void deleteTest() {
        // sGoalsDatabaseReference.removeValue();
        //DatabaseReference ref = sTrophiesDatabaseReference.child("-LBlCe2-XaSBU_pQfnGY");
        // DatabaseReference ref = sGoalsDatabaseReference.child("-LBogeSy4cABucuHsUj_");
        //        // ref.removeValue();
        Goal goal = sGoalList.get(0);
        sGoalsDatabaseReference.child(goal.getPushId()).removeValue();
    }
}
