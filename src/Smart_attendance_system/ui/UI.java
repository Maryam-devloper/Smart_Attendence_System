
package Smart_attendance_system.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.URL;
import Smart_attendance_system.logic.File_Manager;
import javax.swing.border.EmptyBorder;


public class UI extends javax.swing.JPanel {
    private Login parent;
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JComboBox<String> roleBox;
    

    public UI(Login parent) {
        this.parent = parent;
        initCustomLayout();
    }

    public void clearFields() {
        if(txtUser != null) txtUser.setText("");
        if(txtPass != null) txtPass.setText("");
        if(roleBox != null) roleBox.setSelectedIndex(0);
    }

    // Custom Image scaling class to match Signup_UI exactly
    class ImagePanel extends JPanel {
        private Image img;
        public ImagePanel(Image img) { this.img = img; setOpaque(true); }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                int panelW = getWidth();
                int panelH = getHeight();
                int imgW = img.getWidth(null);
                int imgH = img.getHeight(null);
                int newH = (imgH * panelW) / imgW;
                g2d.drawImage(img, 0, (panelH - newH) / 2, panelW, newH, null);
            }
        }
    }

    private void initCustomLayout() {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(800, 550));
        this.setBackground(Color.WHITE);

        // --- Left Side Image ---
        URL sideUrl = getClass().getResource("/Smart_attendance_system/ui/img/side.png");
        if (sideUrl != null) {
            ImagePanel left = new ImagePanel(new ImageIcon(sideUrl).getImage());
            left.setBounds(0, 0, 400, 550);
            left.setBackground(new Color(243, 245, 255));
            this.add(left);
        }

        // --- Right Side Form Container ---
        JPanel right = new JPanel(null);
        right.setBounds(400, 0, 400, 550);
        right.setBackground(Color.WHITE);
        this.add(right);

        // 1. Logo (Matching Signup icon style)
        URL logoUrl = getClass().getResource("/Smart_attendance_system/ui/img/logo.png");
        if (logoUrl != null) {
            JLabel logo = new JLabel(new ImageIcon(new ImageIcon(logoUrl).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
            logo.setBounds(160, 25, 80, 80);
            right.add(logo);
        }

        // 2. Role Selection (Synchronized with Signup order)
        JLabel lblRole = new JLabel("Role");
        lblRole.setBounds(50, 120, 300, 20);
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 12));
        right.add(lblRole);

        roleBox = new JComboBox<>(new String[]{"Student", "Teacher"});
        roleBox.setBounds(50, 140, 300, 35);
        roleBox.setBackground(Color.WHITE);
        right.add(roleBox);

        // 3. ID Input
        JLabel lblEnroll = new JLabel("Enrollment / Employee ID");
        lblEnroll.setBounds(50, 195, 300, 20);
        lblEnroll.setFont(new Font("Segoe UI", Font.BOLD, 12));
        right.add(lblEnroll);

        txtUser = new JTextField();
        txtUser.setBounds(50, 215, 300, 35);
        right.add(txtUser);

        // 4. Password Input
        JLabel lblPass = new JLabel("Password");
        lblPass.setBounds(50, 270, 300, 20);
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        right.add(lblPass);

        txtPass = new JPasswordField();
        txtPass.setBounds(50, 290, 300, 35);
        right.add(txtPass);

        // 5. Login Button
        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(50, 360, 300, 45);
        btnLogin.setBackground(new Color(0, 153, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> performLogin());
        right.add(btnLogin);

        // 6. Footer (Redirect to Signup)
        JLabel lblNoAcc = new JLabel("Don't have an account?");
        lblNoAcc.setBounds(95, 430, 150, 20);
        right.add(lblNoAcc);

        JButton btnSignUp = new JButton("Sign Up");
        btnSignUp.setBounds(230, 430, 80, 20);
        btnSignUp.setForeground(new Color(0, 153, 255));
        btnSignUp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSignUp.setBorderPainted(false);
        btnSignUp.setContentAreaFilled(false);
        btnSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSignUp.addActionListener(e -> parent.switchPage("SIGNUP"));
        right.add(btnSignUp);
    }

    private void performLogin() {
        String id = txtUser.getText().trim();
    String pass = new String(txtPass.getPassword());
    String role = (String) roleBox.getSelectedItem();

    if (id.isEmpty() || pass.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill all fields");
        return;
    }

    try (Connection con = File_Manager.getConnection()) {
        String sql = "SELECT id, name, role FROM users WHERE id=? AND password=? AND role=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, id);
        pst.setString(2, pass);
        pst.setString(3, role);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            String userId   = rs.getString("id");    // the PRIMARY KEY e.g. "EMP-001" or "02-135252-037"
            String userName = rs.getString("name");  // full name e.g. "Sir Ahmed"

            // Pass both to Login which routes to the correct dashboard
            parent.loginToDashboard(role, userId, userName);
            SwingUtilities.getWindowAncestor(this).dispose();

        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid ID, Password or Role!",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
    }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    }
