import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainView {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private JPanel welcomePanel;
    private JPanel predictionPanel;
    private JPanel feedbackPanel;

    // Components for Welcome Panel
    private JTextField nameField;
    private JButton proceedButton;

    // Components for Prediction Panel
    private JTextField hoursStudied, attendance, sleepHours, previousScores, tutoringSessions, physicalActivity;
    private JComboBox<String> parentalInvolvement, accessToResources, extracurricularActivities, motivationLevel, internetAccess;
    private JComboBox<String> familyIncome, teacherQuality, schoolType, peerInfluence, learningDisabilities, parentalEducation, distanceFromHome, gender;
    private JButton predictButton;
    private JLabel predictionResultLabel;

    // Components for Feedback Panel
    private JTextArea feedbackArea;
    private JButton backButton;

    public MainView() {
        frame = new JFrame("Student Prediction System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 800);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        createWelcomePanel();
        createPredictionPanel();
        createFeedbackPanel();

        mainPanel.add(welcomePanel, "Welcome");
        mainPanel.add(predictionPanel, "Prediction");
        mainPanel.add(feedbackPanel, "Feedback");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void createWelcomePanel() {
        welcomePanel = new JPanel(new BorderLayout(10, 10));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
    
        // Mission Statement
        String missionStatement = "<html><h2>Mission Statement</h2>"
                + "Our model aims to provide accurate predictions to help students assess their "
                + "chances of passing or failing based on various academic and personal factors. "
                + "We are committed to improving user experience and regularly updating the system "
                + "with new features to enhance its functionality and accuracy.<br><br>"
                + "<b>Latest Update:</b> Added GUI-based prediction input!</html>";
    
        JLabel missionLabel = new JLabel(missionStatement);
        missionLabel.setVerticalAlignment(SwingConstants.TOP);
        missionLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
        // Input Panel (Name Input and Proceed Button)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 1, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding for the input section
    
        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
        nameField = new JTextField();
        proceedButton = new JButton("Proceed to Prediction");
    
        // Welcome message (initially empty)
        JLabel welcomeMessage = new JLabel("");
        welcomeMessage.setHorizontalAlignment(SwingConstants.CENTER);
    
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(proceedButton);
    
        // Add components to the welcome panel
        welcomePanel.add(missionLabel, BorderLayout.CENTER);
        welcomePanel.add(inputPanel, BorderLayout.SOUTH);
        welcomePanel.add(welcomeMessage, BorderLayout.NORTH);
    
        // Add action listener for the Proceed Button
        proceedButton.addActionListener(e -> {
            String userName = nameField.getText().trim();
            if (userName.isEmpty()) {
                welcomeMessage.setText("Please enter your name.");
                welcomeMessage.setForeground(Color.RED); // Highlight error message in red
            } else {
                welcomeMessage.setText("Welcome, " + userName + "!");
                welcomeMessage.setForeground(Color.BLUE); // Set a friendly color for the message
                showCard("Prediction"); // Switch to the prediction panel
            }
        });
    }
    
    public void setWelcomeMessage(String message, Color color) {
        Component[] components = welcomePanel.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                if (!label.getText().contains("Mission Statement")) { // Skip the mission statement
                    label.setText(message);
                    label.setForeground(color);
                    break;
                }
            }
        }
    }    

    private void createPredictionPanel() {
        predictionPanel = new JPanel(new GridLayout(22, 2, 10, 10));
        predictionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Input fields
        hoursStudied = new JTextField();
        attendance = new JTextField();
        sleepHours = new JTextField();
        previousScores = new JTextField();
        tutoringSessions = new JTextField();
        physicalActivity = new JTextField();

        parentalInvolvement = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        accessToResources = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        extracurricularActivities = new JComboBox<>(new String[]{"Yes", "No"});
        motivationLevel = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        internetAccess = new JComboBox<>(new String[]{"Yes", "No"});
        familyIncome = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        teacherQuality = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        schoolType = new JComboBox<>(new String[]{"Public", "Private"});
        peerInfluence = new JComboBox<>(new String[]{"Positive", "Neutral", "Negative"});
        learningDisabilities = new JComboBox<>(new String[]{"Yes", "No"});
        parentalEducation = new JComboBox<>(new String[]{"High School", "College", "Postgraduate"});
        distanceFromHome = new JComboBox<>(new String[]{"Near", "Moderate", "Far"});
        gender = new JComboBox<>(new String[]{"Male", "Female"});

        predictionResultLabel = new JLabel("Result: ");
        predictButton = new JButton("Predict");

        // Add components to panel
        predictionPanel.add(new JLabel("Hours Studied (0-44):"));
        predictionPanel.add(hoursStudied);
        predictionPanel.add(new JLabel("Attendance (60-100):"));
        predictionPanel.add(attendance);
        predictionPanel.add(new JLabel("Parental Involvement:"));
        predictionPanel.add(parentalInvolvement);
        predictionPanel.add(new JLabel("Access to Resources:"));
        predictionPanel.add(accessToResources);
        predictionPanel.add(new JLabel("Extracurricular Activities:"));
        predictionPanel.add(extracurricularActivities);
        predictionPanel.add(new JLabel("Sleep Hours (4-10):"));
        predictionPanel.add(sleepHours);
        predictionPanel.add(new JLabel("Previous Scores (50-100):"));
        predictionPanel.add(previousScores);
        predictionPanel.add(new JLabel("Motivation Level:"));
        predictionPanel.add(motivationLevel);
        predictionPanel.add(new JLabel("Internet Access:"));
        predictionPanel.add(internetAccess);
        predictionPanel.add(new JLabel("Tutoring Sessions (0-8):"));
        predictionPanel.add(tutoringSessions);
        predictionPanel.add(new JLabel("Family Income:"));
        predictionPanel.add(familyIncome);
        predictionPanel.add(new JLabel("Teacher Quality:"));
        predictionPanel.add(teacherQuality);
        predictionPanel.add(new JLabel("School Type:"));
        predictionPanel.add(schoolType);
        predictionPanel.add(new JLabel("Peer Influence:"));
        predictionPanel.add(peerInfluence);
        predictionPanel.add(new JLabel("Physical Activity (0-6):"));
        predictionPanel.add(physicalActivity);
        predictionPanel.add(new JLabel("Learning Disabilities:"));
        predictionPanel.add(learningDisabilities);
        predictionPanel.add(new JLabel("Parental Education:"));
        predictionPanel.add(parentalEducation);
        predictionPanel.add(new JLabel("Distance from Home:"));
        predictionPanel.add(distanceFromHome);
        predictionPanel.add(new JLabel("Gender:"));
        predictionPanel.add(gender);

        predictionPanel.add(predictButton);
        predictionPanel.add(predictionResultLabel);
    }

    private void createFeedbackPanel() {
        feedbackPanel = new JPanel(new BorderLayout(10, 10));
        feedbackPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        feedbackArea = new JTextArea();
        feedbackArea.setEditable(false);

        backButton = new JButton("Back to Prediction");

        feedbackPanel.add(new JScrollPane(feedbackArea), BorderLayout.CENTER);
        feedbackPanel.add(backButton, BorderLayout.SOUTH);
    }

    public void showCard(String cardName) {
        cardLayout.show(mainPanel, cardName);
    }

    public String getNameInput() {
        return nameField.getText().trim();
    }

    public void addProceedButtonListener(ActionListener listener) {
        proceedButton.addActionListener(listener);
    }

    public void addPredictButtonListener(ActionListener listener) {
        predictButton.addActionListener(listener);
    }

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public double[] getPredictionInput() throws NumberFormatException {
        double[] input = new double[20];
        input[0] = Double.parseDouble(hoursStudied.getText());
        input[1] = Double.parseDouble(attendance.getText());
        input[2] = parentalInvolvement.getSelectedIndex();
        input[3] = accessToResources.getSelectedIndex();
        input[4] = extracurricularActivities.getSelectedIndex();
        input[5] = Double.parseDouble(sleepHours.getText());
        input[6] = Double.parseDouble(previousScores.getText());
        input[7] = motivationLevel.getSelectedIndex();
        input[8] = internetAccess.getSelectedIndex();
        input[9] = Double.parseDouble(tutoringSessions.getText());
        input[10] = familyIncome.getSelectedIndex();
        input[11] = teacherQuality.getSelectedIndex();
        input[12] = schoolType.getSelectedIndex();
        input[13] = peerInfluence.getSelectedIndex();
        input[14] = Double.parseDouble(physicalActivity.getText());
        input[15] = learningDisabilities.getSelectedIndex();
        input[16] = parentalEducation.getSelectedIndex();
        input[17] = distanceFromHome.getSelectedIndex();
        input[18] = gender.getSelectedIndex();
        return input;
    }

    public void setPredictionResult(String result) {
        predictionResultLabel.setText("Result: " + result);
    }

    public void setFeedbackText(String feedback) {
        feedbackArea.setText(feedback);
    }
}
