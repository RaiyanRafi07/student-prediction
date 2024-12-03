import java.util.*;

public class FeedbackAnalyzer {
    private Map<String, Double> passingAverages;
    private Map<String, Double> failingAverages;

    // List of features under the student's control
    private final List<Integer> controllableFeatures = Arrays.asList(
        0, // Hours Studied
        1, // Attendance
        5, // Sleep Hours
        6, // Previous Scores
        7, // Motivation Level
        4, // Extracurricular Activities
        9, // Tutoring Sessions
        14 // Physical Activity
    );

    private final String[] featureNames = {
        "Hours Studied", "Attendance", "Parental Involvement", "Access to Resources", "Extracurricular Activities",
        "Sleep Hours", "Previous Scores", "Motivation Level", "Internet Access", "Tutoring Sessions",
        "Family Income", "Teacher Quality", "School Type", "Peer Influence", "Physical Activity",
        "Learning Disabilities", "Parental Education Level", "Distance from Home", "Gender"
    };

    private final Map<Integer, String[]> categoricalMappings;

    public FeedbackAnalyzer(List<DataPoint> data) {
        // Initialize categoricalMappings
        categoricalMappings = new HashMap<>();
        categoricalMappings.put(2, new String[]{"Low", "Medium", "High"}); // Parental Involvement
        categoricalMappings.put(3, new String[]{"Low", "Medium", "High"}); // Access to Resources
        categoricalMappings.put(4, new String[]{"No", "Yes"}); // Extracurricular Activities
        categoricalMappings.put(7, new String[]{"Low", "Medium", "High"}); // Motivation Level
        categoricalMappings.put(8, new String[]{"No", "Yes"}); // Internet Access
        categoricalMappings.put(10, new String[]{"Low", "Medium", "High"}); // Family Income
        categoricalMappings.put(11, new String[]{"Low", "Medium", "High"}); // Teacher Quality
        categoricalMappings.put(12, new String[]{"Public", "Private"}); // School Type
        categoricalMappings.put(13, new String[]{"Negative", "Neutral", "Positive"}); // Peer Influence
        categoricalMappings.put(15, new String[]{"No", "Yes"}); // Learning Disabilities
        categoricalMappings.put(16, new String[]{"High School", "College", "Postgraduate"}); // Parental Education Level
        categoricalMappings.put(17, new String[]{"Near", "Moderate", "Far"}); // Distance from Home
        categoricalMappings.put(18, new String[]{"Male", "Female"}); // Gender

        calculateAverages(data);
    }

    private void calculateAverages(List<DataPoint> data) {
        Map<String, Double> passSums = new HashMap<>();
        Map<String, Double> failSums = new HashMap<>();
        int passCount = 0, failCount = 0;

        // Initialize sums
        for (int i = 0; i < 20; i++) {
            passSums.put("feature" + i, 0.0);
            failSums.put("feature" + i, 0.0);
        }

        // Calculate sums for passing and failing students
        for (DataPoint dp : data) {
            if (dp.label == 1) { // Passing
                passCount++;
                for (int i = 0; i < dp.features.length; i++) {
                    passSums.put("feature" + i, passSums.get("feature" + i) + dp.features[i]);
                }
            } else { // Failing
                failCount++;
                for (int i = 0; i < dp.features.length; i++) {
                    failSums.put("feature" + i, failSums.get("feature" + i) + dp.features[i]);
                }
            }
        }

        // Calculate averages
        passingAverages = new HashMap<>();
        failingAverages = new HashMap<>();

        for (int i = 0; i < 20; i++) {
            passingAverages.put("feature" + i, passCount > 0 ? passSums.get("feature" + i) / passCount : 0);
            failingAverages.put("feature" + i, failCount > 0 ? failSums.get("feature" + i) / failCount : 0);
        }
    }

    public List<String> getSuggestions(double[] userInput) {
        List<String> suggestions = new ArrayList<>();

        for (int i : controllableFeatures) {
            double userValue = userInput[i];
            double passingAverage = passingAverages.get("feature" + i);

            // Handle categorical features
            if (categoricalMappings.containsKey(i)) {
                String userCategory = categoricalMappings.get(i)[(int) userValue];
                String passingCategory = categoricalMappings.get(i)[(int) Math.round(passingAverage)];
                if (!userCategory.equals(passingCategory)) {
                    suggestions.add("Consider aligning your " + featureNames[i] + " to '" + passingCategory + "'. Your input: '" + userCategory + "'.");
                }
            }
            // Handle numerical features
            else {
                int roundedUserValue = (int) Math.round(userValue);
                int roundedPassingAverage = (int) Math.round(passingAverage);

                if (Math.abs(roundedUserValue - roundedPassingAverage) > 2) { // Significant difference threshold
                    if (roundedUserValue < roundedPassingAverage) {
                        suggestions.add("Consider improving your " + featureNames[i] + ". Average for passing students: " + roundedPassingAverage);
                    } else {
                        suggestions.add("Your " + featureNames[i] + " is already better than the average for passing students!");
                    }
                }
            }
        }

        return suggestions;
    }

    public Map<String, Double> getPassingAverages() {
        return passingAverages;
    }

    public Map<String, Double> getFailingAverages() {
        return failingAverages;
    }
}
