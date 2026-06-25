package Smart_attendance_system.ui;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import Student.StudentDashboard;
import Smart_attendance_system.logic.File_Manager;

public class Login extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final UI loginPage;
    private final Signup_UI signupPage;

    public Login() {
        setTitle("Smart Attendance System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 580);
        setLocationRelativeTo(null);
        setResizable(false);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        loginPage = new UI(this);
        signupPage = new Signup_UI(this);
        mainPanel.add(loginPage, "LOGIN");
        mainPanel.add(signupPage, "SIGNUP");
        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void switchPage(String pageName) {
        if (pageName.equals("LOGIN")) {
            loginPage.clearFields();
        } else if (pageName.equals("SIGNUP")) {
            signupPage.clearAllFields();
        }
        cardLayout.show(mainPanel, pageName);
    }

    /**
     * Called after successful login.
     * userId = the PRIMARY KEY id from users table (enrollment no or employee ID)
     * userName = the name column from users table
     * role = "Teacher" or "Student"
     */
    public void loginToDashboard(String role, String userId, String userName) {
        if (role.equalsIgnoreCase("Teacher")) {
            // Pass BOTH the employee ID and name to Teacher
            Teacher teacherWindow = new Teacher(userId, userName);
            teacherWindow.setVisible(true);
            this.dispose();
        } else if (role.equalsIgnoreCase("Student")) {
            // Pass the actual enrollment ID (string) and name
            StudentDashboard dashboard = new StudentDashboard(userId, userName);
            dashboard.setVisible(true);
            this.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}