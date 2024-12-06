import java.util.Collections;
import java.util.List;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.FileWriter;
import java.io.IOException;

// This is the controller. It loads data, trains model, evaluates accuracy.
// It also connects the view and model.
public class Main {
    public static void main(String[] args) {
        // Load data
        DataLoader.LoadResult loadRes = DataLoader.loadData("src\\main\\java\\student_data.csv");
        List<Node> data = loadRes.data;

        // Print how many lines were skipped
        System.out.println("Lines skipped: " + loadRes.linesSkipped);

        // Shuffle and split data into train and test
        Collections.shuffle(data);
        int split = (int)(data.size() * 0.8);
        List<Node> trainData = data.subList(0, split);
        List<Node> testData = data.subList(split, data.size());

        // Train model
        int nf = trainData.get(0).getNumFeatures();
        int mf = (int)Math.sqrt(nf);
        RandomForest rf = new RandomForest(100, mf, nf);
        rf.train(trainData);

        // Evaluate model accuracy
        double accuracy = rf.evaluate(testData);
        System.out.println("Model accuracy: " + (accuracy * 100) + "%");

        // Setup feedback
        FeedbackAnalyzer fa = new FeedbackAnalyzer(data);

        // Setup View
        MainView view = new MainView();

        // Controller action for proceed
        view.addProceedButtonListener(e -> {
            String user = view.getNameInput();
            if (user.isEmpty()) {
                view.setWelcomeMessage("Please enter your name.", Color.RED);
            } else {
                view.setWelcomeMessage("Welcome, " + user + "!", Color.BLUE);
                view.showCard("Prediction");
            }
        });

        // Controller action for predict
        view.addPredictButtonListener(e -> {
            try {
                double[] input = view.getPredictionInput();
                int pred = rf.predict(input);
                String res = pred == 1 ? "Pass" : "Fail";
                view.setPredictionResult(res);
                List<String> sugs = fa.getSuggestions(input, res);
                StringBuilder sb = new StringBuilder();
                for (String s : sugs) sb.append(s).append("\n");
                view.setFeedbackText(sb.toString());
                view.showCard("Feedback");
            } catch(NumberFormatException ex) {
                view.setFeedbackText("Please enter valid numeric inputs.");
                view.showCard("Feedback");
            }
        });

        // Controller action for back button
        view.addBackButtonListener(e -> view.showCard("Prediction"));

        // Controller action for export button
        view.addExportButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a file chooser dialog
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showSaveDialog(view.frame); // Accessing frame directly

                if(option == JFileChooser.APPROVE_OPTION){
                    try {
                        // Get the selected file path
                        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                        // Get the feedback text
                        String feedback = view.feedbackArea.getText();
                        // Write to the file
                        FileWriter writer = new FileWriter(filePath);
                        writer.write(feedback);
                        writer.close();
                        // Inform the user
                        JOptionPane.showMessageDialog(view.frame, "Results exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch(IOException ioException){
                        JOptionPane.showMessageDialog(view.frame, "Error exporting results.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private static RandomForest trainModel(List<Node> data) {
        // This method is now integrated into the main method for simplicity
        return null; // Placeholder since training is handled above
    }
}
