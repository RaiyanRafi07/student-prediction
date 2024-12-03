import java.util.Collections;
import java.util.List;
import java.awt.Color;


public class Main {
    public static void main(String[] args) {
        // Load and train the Random Forest
        List<DataPoint> data = DataLoader.loadData("src/student_data.csv");
        RandomForest randomForest = trainModel(data);

        // Initialize the FeedbackAnalyzer
        FeedbackAnalyzer feedbackAnalyzer = new FeedbackAnalyzer(data);

        // Initialize MainView
        MainView mainView = new MainView();

        // Welcome screen actions
        mainView.addProceedButtonListener(e -> {
            String userName = mainView.getNameInput();
            if (userName.isEmpty()) {
                // Display an error message on the welcome screen
                mainView.setWelcomeMessage("Please enter your name.", Color.RED);
            } else {
                // Display a friendly welcome message and move to the prediction screen
                mainView.setWelcomeMessage("Welcome, " + userName + "!", Color.BLUE);
                mainView.showCard("Prediction");
            }
        });

        // Prediction screen actions
        mainView.addPredictButtonListener(e -> {
            try {
                double[] input = mainView.getPredictionInput();
                int prediction = randomForest.predict(input);
                String result = prediction == 1 ? "Pass" : "Fail";
                mainView.setPredictionResult(result);

                // Generate and display feedback
                List<String> suggestions = feedbackAnalyzer.getSuggestions(input);
                StringBuilder feedbackMessage = new StringBuilder("Suggestions for Improvement:\n");
                for (String suggestion : suggestions) {
                    feedbackMessage.append("- ").append(suggestion).append("\n");
                }
                mainView.setFeedbackText(feedbackMessage.toString());

                // Show feedback panel
                mainView.showCard("Feedback");
            } catch (NumberFormatException ex) {
                mainView.setFeedbackText("Please ensure all numerical inputs are within the valid ranges.");
                mainView.showCard("Feedback");
            }
        });

        // Feedback screen actions
        mainView.addBackButtonListener(e -> mainView.showCard("Prediction"));
    }

    private static RandomForest trainModel(List<DataPoint> data) {
        // Shuffle and split data
        Collections.shuffle(data);
        int trainSize = (int) (data.size() * 0.8);
        List<DataPoint> trainingData = data.subList(0, trainSize);

        // Train the Random Forest
        int numFeatures = trainingData.get(0).features.length;
        int maxFeatures = (int) Math.sqrt(numFeatures);
        RandomForest randomForest = new RandomForest(100, maxFeatures, numFeatures);
        randomForest.train(trainingData);

        System.out.println("Model training completed!");
        return randomForest;
    }
}
