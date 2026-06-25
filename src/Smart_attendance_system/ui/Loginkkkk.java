
package Smart_attendance_system.ui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.*;
import Smart_attendance_system.logic.File_Manager;
  


public class Login extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private UI loginPage;
    private Signup_UI signupPage;
    
    // Track current dashboard to remove it on logout
    private JPanel currentDashboard = null;

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
            // If we are coming back from a dashboard (Logout)
            if (currentDashboard != null) {
                mainPanel.remove(currentDashboard);
                currentDashboard = null;
            }
            loginPage.clearFields();
        } else if (pageName.equals("SIGNUP")) {
            signupPage.clearAllFields();
        }
        cardLayout.show(mainPanel, pageName);
    }

    public void loginToDashboard(String role, String userName) {
        // Clean up any existing dashboard before creating a new one
        if (currentDashboard != null) {
            mainPanel.remove(currentDashboard);
        }

        if (role.equalsIgnoreCase("Teacher")) {
            currentDashboard = new Teacher(this, userName);
            mainPanel.add(currentDashboard, "TEACHER_DASH");
            cardLayout.show(mainPanel, "TEACHER_DASH");
        } else if (role.equalsIgnoreCase("Student")) {
            currentDashboard = new Student(this, userName);
            mainPanel.add(currentDashboard, "STUDENT_DASH");
            cardLayout.show(mainPanel, "STUDENT_DASH");
        }
        
        // Refresh UI components
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
