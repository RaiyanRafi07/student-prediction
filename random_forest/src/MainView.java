import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainView {
    JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private JPanel welcomePanel;
    private JPanel predictionPanel;
    private JPanel feedbackPanel;

    private JTextField nameField;
    private JButton proceedButton;

    private JTextField hoursStudied, attendance, sleepHours, previousScores, tutoringSessions, physicalActivity;
    private JComboBox<String> parentalInvolvement, accessToResources, extracurricularActivities, motivationLevel, internetAccess;
    private JComboBox<String> familyIncome, teacherQuality, schoolType, peerInfluence, learningDisabilities, parentalEducation, distanceFromHome, gender;
    private JButton predictButton;
    private JLabel predictionResultLabel;

    JTextArea feedbackArea;
    private JButton backButton;
    private JButton exportButton;

    private JTextArea updatesArea;

    public MainView() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, use default
        }

        frame = new JFrame("Student Prediction System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 900); // Adjusted size for layout
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // Center the frame on the screen

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
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        // Updates section
        updatesArea = new JTextArea();
        updatesArea.setEditable(false);
        updatesArea.setLineWrap(true);
        updatesArea.setWrapStyleWord(true);
        updatesArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        updatesArea.setText(
            "Mission Statement:\n" +
            "Our model aims to predict whether a student will pass or fail based on various factors.\n\n" +
            "Latest Updates:\n" +
            "- Added GUI-based prediction input.\n" +
            "- Introduced export functionality for results and suggestions."
        );
    
        JScrollPane updatesScroll = new JScrollPane(updatesArea);
        updatesScroll.setPreferredSize(new Dimension(550, 200)); // Larger box for updates
    
        JPanel updatesPanel = new JPanel(new BorderLayout());
        updatesPanel.add(updatesScroll, BorderLayout.CENTER);
        updatesPanel.setBorder(BorderFactory.createTitledBorder("Mission & Updates"));
    
        // Input section
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS)); // Aligns components vertically
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100)); // Adds horizontal centering
    
        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centers the label horizontally
    
        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(300, 40)); // Ensures consistent size
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT); // Centers the text field horizontally
    
        proceedButton = new JButton("Proceed to Prediction");
        proceedButton.setMaximumSize(new Dimension(300, 50)); // Ensures consistent size
        proceedButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Centers the button horizontally
    
        inputPanel.add(Box.createVerticalGlue()); // Adds flexible space at the top
        inputPanel.add(nameLabel);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Adds fixed space between components
        inputPanel.add(nameField);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Adds fixed space
        inputPanel.add(proceedButton);
        inputPanel.add(Box.createVerticalGlue()); // Adds flexible space at the bottom
    
        welcomePanel.add(updatesPanel, BorderLayout.NORTH);
        welcomePanel.add(inputPanel, BorderLayout.CENTER);
    }
    
    

    public void setWelcomeMessage(String message, Color color) {
        JPanel inputPanel = (JPanel) welcomePanel.getComponent(1); // Assuming it's at position 1
        JLabel messageLabel = new JLabel(message);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setForeground(color);
        inputPanel.add(messageLabel); 
     }

    private void createPredictionPanel() {
        predictionPanel = new JPanel(new GridLayout(22, 2, 10, 10));
        predictionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Input fields
        hoursStudied = new JTextField();
        hoursStudied.setPreferredSize(new Dimension(300, 40));
        attendance = new JTextField();
        attendance.setPreferredSize(new Dimension(300, 40));
        sleepHours = new JTextField();
        sleepHours.setPreferredSize(new Dimension(300, 40));
        previousScores = new JTextField();
        previousScores.setPreferredSize(new Dimension(300, 40));
        tutoringSessions = new JTextField();
        tutoringSessions.setPreferredSize(new Dimension(300, 40));
        physicalActivity = new JTextField();
        physicalActivity.setPreferredSize(new Dimension(300, 40));

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

        predictButton = new JButton("Predict");
        predictionResultLabel = new JLabel("Result: ");
        predictionResultLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        predictionPanel.add(new JLabel("Hours Studied per week:"));
        predictionPanel.add(hoursStudied);
        predictionPanel.add(new JLabel("Percentage of class attended:"));
        predictionPanel.add(attendance);
        predictionPanel.add(new JLabel("Parental Involvement:"));
        predictionPanel.add(parentalInvolvement);
        predictionPanel.add(new JLabel("Access to Resources:"));
        predictionPanel.add(accessToResources);
        predictionPanel.add(new JLabel("Extracurricular Activities:"));
        predictionPanel.add(extracurricularActivities);
        predictionPanel.add(new JLabel("Sleep Hours per night:"));
        predictionPanel.add(sleepHours);
        predictionPanel.add(new JLabel("Previous Exam Score:"));
        predictionPanel.add(previousScores);
        predictionPanel.add(new JLabel("Motivation Level:"));
        predictionPanel.add(motivationLevel);
        predictionPanel.add(new JLabel("Internet Access:"));
        predictionPanel.add(internetAccess);
        predictionPanel.add(new JLabel("Tutoring Sessions per month:"));
        predictionPanel.add(tutoringSessions);
        predictionPanel.add(new JLabel("Family Income:"));
        predictionPanel.add(familyIncome);
        predictionPanel.add(new JLabel("Teacher Quality:"));
        predictionPanel.add(teacherQuality);
        predictionPanel.add(new JLabel("School Type:"));
        predictionPanel.add(schoolType);
        predictionPanel.add(new JLabel("Peer Influence:"));
        predictionPanel.add(peerInfluence);
        predictionPanel.add(new JLabel("Hours of Physical Activity Per Week:"));
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
        feedbackPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        feedbackArea = new JTextArea();
        feedbackArea.setEditable(false);
        feedbackArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

        exportButton = new JButton("Export Results");
        backButton = new JButton("Back to Prediction");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(exportButton);
        buttonPanel.add(backButton);

        feedbackPanel.add(new JScrollPane(feedbackArea), BorderLayout.CENTER);
        feedbackPanel.add(buttonPanel, BorderLayout.SOUTH);
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

    public void addExportButtonListener(ActionListener listener) {
        exportButton.addActionListener(listener);
    }

    public double[] getPredictionInput() throws NumberFormatException {
        double[] input = new double[19];
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
