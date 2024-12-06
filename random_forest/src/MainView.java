import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

// This class builds the GUI and provides methods to show results and get user inputs.
// It's the VIEW in MVC.
public class MainView {
    private JFrame frame;
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
    private JTextArea feedbackArea;
    private JButton backButton;

    public MainView() {
        try {
            for (UIManager.LookAndFeelInfo info:UIManager.getInstalledLookAndFeels()) {
                if("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch(Exception e){}
        frame=new JFrame("Student Prediction System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,900);

        cardLayout=new CardLayout();
        mainPanel=new JPanel(cardLayout);

        createWelcomePanel();
        createPredictionPanel();
        createFeedbackPanel();

        mainPanel.add(welcomePanel,"Welcome");
        mainPanel.add(predictionPanel,"Prediction");
        mainPanel.add(feedbackPanel,"Feedback");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void createWelcomePanel() {
        welcomePanel=new JPanel(new BorderLayout(10,10));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel mission=new JLabel("<html><h2>Mission</h2>We predict pass/fail. Enter your name to proceed.</html>");
        mission.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel inputPanel=new JPanel(new GridLayout(3,1,10,10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        JLabel nameLabel=new JLabel("Your Name:");
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameField=new JTextField();
        proceedButton=new JButton("Proceed");

        JLabel welcomeMsg=new JLabel("");
        welcomeMsg.setHorizontalAlignment(SwingConstants.CENTER);

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(proceedButton);

        welcomePanel.add(mission,BorderLayout.CENTER);
        welcomePanel.add(inputPanel,BorderLayout.SOUTH);
        welcomePanel.add(welcomeMsg,BorderLayout.NORTH);

        proceedButton.addActionListener(e->{
            String user=nameField.getText().trim();
            if (user.isEmpty()) {
                welcomeMsg.setText("Please enter your name.");
                welcomeMsg.setForeground(Color.RED);
            } else {
                welcomeMsg.setText("Welcome, "+user+"!");
                welcomeMsg.setForeground(Color.BLUE);
                showCard("Prediction");
            }
        });
    }

    public void setWelcomeMessage(String msg,Color c) {
        for (Component comp:welcomePanel.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel lbl=(JLabel)comp;
                if(!lbl.getText().contains("Mission")) {
                    lbl.setText(msg);
                    lbl.setForeground(c);
                    break;
                }
            }
        }
    }

    private void createPredictionPanel() {
        predictionPanel=new JPanel(new GridLayout(22,2,10,10));
        predictionPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        hoursStudied=new JTextField();
        attendance=new JTextField();
        sleepHours=new JTextField();
        previousScores=new JTextField();
        tutoringSessions=new JTextField();
        physicalActivity=new JTextField();

        parentalInvolvement=new JComboBox<>(new String[]{"High","Medium","Low"});
        accessToResources=new JComboBox<>(new String[]{"High","Medium","Low"});
        extracurricularActivities=new JComboBox<>(new String[]{"Yes","No"});
        motivationLevel=new JComboBox<>(new String[]{"High","Medium","Low"});
        internetAccess=new JComboBox<>(new String[]{"Yes","No"});
        familyIncome=new JComboBox<>(new String[]{"High","Medium","Low"});
        teacherQuality=new JComboBox<>(new String[]{"High","Medium","Low"});
        schoolType=new JComboBox<>(new String[]{"Public","Private"});
        peerInfluence=new JComboBox<>(new String[]{"Positive","Neutral","Negative"});
        learningDisabilities=new JComboBox<>(new String[]{"Yes","No"});
        parentalEducation=new JComboBox<>(new String[]{"High School","College","Postgraduate"});
        distanceFromHome=new JComboBox<>(new String[]{"Near","Moderate","Far"});
        gender=new JComboBox<>(new String[]{"Male","Female"});

        predictButton=new JButton("Predict");
        predictionResultLabel=new JLabel("Result: ");

        predictionPanel.add(new JLabel("Hours Studied:"));predictionPanel.add(hoursStudied);
        predictionPanel.add(new JLabel("Attendance:"));predictionPanel.add(attendance);
        predictionPanel.add(new JLabel("Parental Involvement:"));predictionPanel.add(parentalInvolvement);
        predictionPanel.add(new JLabel("Access to Resources:"));predictionPanel.add(accessToResources);
        predictionPanel.add(new JLabel("Extracurricular Activities:"));predictionPanel.add(extracurricularActivities);
        predictionPanel.add(new JLabel("Sleep Hours:"));predictionPanel.add(sleepHours);
        predictionPanel.add(new JLabel("Previous Scores:"));predictionPanel.add(previousScores);
        predictionPanel.add(new JLabel("Motivation Level:"));predictionPanel.add(motivationLevel);
        predictionPanel.add(new JLabel("Internet Access:"));predictionPanel.add(internetAccess);
        predictionPanel.add(new JLabel("Tutoring Sessions:"));predictionPanel.add(tutoringSessions);
        predictionPanel.add(new JLabel("Family Income:"));predictionPanel.add(familyIncome);
        predictionPanel.add(new JLabel("Teacher Quality:"));predictionPanel.add(teacherQuality);
        predictionPanel.add(new JLabel("School Type:"));predictionPanel.add(schoolType);
        predictionPanel.add(new JLabel("Peer Influence:"));predictionPanel.add(peerInfluence);
        predictionPanel.add(new JLabel("Physical Activity:"));predictionPanel.add(physicalActivity);
        predictionPanel.add(new JLabel("Learning Disabilities:"));predictionPanel.add(learningDisabilities);
        predictionPanel.add(new JLabel("Parental Education:"));predictionPanel.add(parentalEducation);
        predictionPanel.add(new JLabel("Distance from Home:"));predictionPanel.add(distanceFromHome);
        predictionPanel.add(new JLabel("Gender:"));predictionPanel.add(gender);

        predictionPanel.add(predictButton);predictionPanel.add(predictionResultLabel);
    }

    private void createFeedbackPanel() {
        feedbackPanel=new JPanel(new BorderLayout(10,10));
        feedbackPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        feedbackArea=new JTextArea();
        feedbackArea.setEditable(false);
        backButton=new JButton("Back");

        feedbackPanel.add(new JScrollPane(feedbackArea),BorderLayout.CENTER);
        feedbackPanel.add(backButton,BorderLayout.SOUTH);
    }

    public void showCard(String cardName) {
        cardLayout.show(mainPanel,cardName);
    }

    public String getNameInput() {
        return nameField.getText().trim();
    }

    public void addProceedButtonListener(ActionListener l) {
        proceedButton.addActionListener(l);
    }

    public void addPredictButtonListener(ActionListener l) {
        predictButton.addActionListener(l);
    }

    public void addBackButtonListener(ActionListener l) {
        backButton.addActionListener(l);
    }

    public double[] getPredictionInput() throws NumberFormatException {
        double[] in=new double[20];
        in[0]=Double.parseDouble(hoursStudied.getText());
        in[1]=Double.parseDouble(attendance.getText());
        in[2]=parentalInvolvement.getSelectedIndex();
        in[3]=accessToResources.getSelectedIndex();
        in[4]=extracurricularActivities.getSelectedIndex();
        in[5]=Double.parseDouble(sleepHours.getText());
        in[6]=Double.parseDouble(previousScores.getText());
        in[7]=motivationLevel.getSelectedIndex();
        in[8]=internetAccess.getSelectedIndex();
        in[9]=Double.parseDouble(tutoringSessions.getText());
        in[10]=familyIncome.getSelectedIndex();
        in[11]=teacherQuality.getSelectedIndex();
        in[12]=schoolType.getSelectedIndex();
        in[13]=peerInfluence.getSelectedIndex();
        in[14]=Double.parseDouble(physicalActivity.getText());
        in[15]=learningDisabilities.getSelectedIndex();
        in[16]=parentalEducation.getSelectedIndex();
        in[17]=distanceFromHome.getSelectedIndex();
        in[18]=gender.getSelectedIndex();
        // Note: We had 19 features initially described, but actually indexing from 0...18 is 19 features total.
        return in;
    }

    public void setPredictionResult(String r) {
        predictionResultLabel.setText("Result: "+r);
    }

    public void setFeedbackText(String f) {
        feedbackArea.setText(f);
    }
}
