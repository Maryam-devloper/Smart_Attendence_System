
package Smart_attendance_system.ui;

import javax.swing.*;
import java.awt.*;
import Smart_attendance_system.logic.File_Manager;
import java.sql.*;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.net.URL;


public class Signup_UI extends javax.swing.JPanel {
    private Login parent; 
    private JTextField txtName, txtID;
    private JPasswordField txtPass, txtConfirmPass;
    private JComboBox<String> cbRole, cbDept, cbSemester, cbSection;
    private JLabel lblID, lblPass, lblConfirm;
    private JButton btnSignUp;
    private JPanel dynamicPanel;
    private ArrayList<JCheckBox> subjectBoxes = new ArrayList<>();

    public Signup_UI(Login parent) {
        this.parent = parent;
        initComponents();     
        initCustomLayout();   
    }

    public void clearAllFields() {
        txtName.setText("");
        txtID.setText("");
        txtPass.setText("");
        txtConfirmPass.setText("");
        cbRole.setSelectedIndex(0);
        cbDept.setSelectedIndex(0);
        for (JCheckBox chk : subjectBoxes) chk.setSelected(false);
    }

    class ImagePanel extends JPanel {
        private Image img;
        public ImagePanel(Image img) { this.img = img; setOpaque(true); }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                int panelW = getWidth(), panelH = getHeight();
                int imgW = img.getWidth(null), imgH = img.getHeight(null);
                int newH = (imgH * panelW) / imgW;
                g2d.drawImage(img, 0, (panelH - newH) / 2, panelW, newH, null);
            }
        }
    }

    private void initCustomLayout() {
        Color actionBlue = new Color(0, 153, 255);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(800, 550)); 
        this.setBackground(Color.WHITE);

        URL sideUrl = getClass().getResource("/Smart_attendance_system/ui/img/side.png");
        if (sideUrl != null) {
            ImagePanel left = new ImagePanel(new ImageIcon(sideUrl).getImage());
            left.setBounds(0, 0, 400, 550);
            left.setBackground(new Color(243, 245, 255));
            this.add(left);
        }

        JPanel right = new JPanel(null);
        right.setBounds(400, 0, 400, 550);
        right.setBackground(Color.WHITE);

        // --- Role ---
        JLabel lblRolePrompt = new JLabel("Role");
        lblRolePrompt.setBounds(50, 10, 300, 20);
        right.add(lblRolePrompt);
        cbRole = new JComboBox<>(new String[]{"Student", "Teacher"});
        cbRole.setBounds(50, 30, 300, 30);
        right.add(cbRole);

        // --- Name ---
        JLabel lblName = new JLabel("Full Name");
        lblName.setBounds(50, 70, 300, 20);
        right.add(lblName);
        txtName = new JTextField();
        txtName.setBounds(50, 90, 300, 30);
        right.add(txtName);

        // --- ID ---
        lblID = new JLabel("Enrollment Number");
        lblID.setBounds(50, 130, 300, 20);
        right.add(lblID);
        txtID = new JTextField();
        txtID.setBounds(50, 150, 300, 30);
        right.add(txtID);

        // --- Department ---
        JLabel lblDeptLabel = new JLabel("Department");
        lblDeptLabel.setBounds(50, 190, 300, 20);
        right.add(lblDeptLabel);
        cbDept = new JComboBox<>(new String[]{"Computer Science", "Software Engineering", "IT", "AI"});
        cbDept.setBounds(50, 210, 300, 30);
        right.add(cbDept);

        // --- Dynamic Section (Semester or Subjects) ---
        dynamicPanel = new JPanel(null);
        dynamicPanel.setBounds(50, 250, 300, 65);
        dynamicPanel.setBackground(Color.WHITE);
        right.add(dynamicPanel);

        // --- Password Fields (Fixed Positions) ---
        lblPass = new JLabel("Password");
        lblPass.setBounds(50, 320, 300, 20);
        right.add(lblPass);
        txtPass = new JPasswordField();
        txtPass.setBounds(50, 340, 300, 30);
        right.add(txtPass);

        lblConfirm = new JLabel("Confirm Password");
        lblConfirm.setBounds(50, 380, 300, 20);
        right.add(lblConfirm);
        txtConfirmPass = new JPasswordField();
        txtConfirmPass.setBounds(50, 400, 300, 30);
        right.add(txtConfirmPass);

        // --- Create Account Button ---
        btnSignUp = new JButton("Create Account");
        btnSignUp.setBounds(50, 450, 300, 35);
        btnSignUp.setBackground(actionBlue);
        btnSignUp.setForeground(Color.WHITE);
        btnSignUp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSignUp.addActionListener(e -> handleSignUp());
        right.add(btnSignUp);

        // --- Already have account (Moved up to 500px to ensure visibility) ---
        JLabel lblAlready = new JLabel("Already have an account?");
        lblAlready.setBounds(90, 500, 160, 20);
        right.add(lblAlready);

        JButton btnBack = new JButton("Login");
        btnBack.setBounds(235, 500, 70, 20);
        btnBack.setForeground(actionBlue);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> parent.switchPage("LOGIN"));
        right.add(btnBack);

        cbRole.addActionListener(e -> updateDynamicFields(cbRole.getSelectedItem().toString()));
        updateDynamicFields("Student");
        this.add(right);
    }

    private void handleSignUp() {
        String name = txtName.getText().trim();
        String id = txtID.getText().trim();
        String role = cbRole.getSelectedItem().toString();
        String dept = cbDept.getSelectedItem().toString();
        String pass = new String(txtPass.getPassword());
        String confirm = new String(txtConfirmPass.getPassword());

        if (name.isEmpty() || id.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }
        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }

        try (Connection con = File_Manager.getConnection()) {
            String sql = "INSERT INTO users (name, id, password, role, department, semester, section, subjects) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, id);
            pst.setString(3, pass);
            pst.setString(4, role);
            pst.setString(5, dept);

            if (role.equals("Student")) {
    pst.setString(6, cbSemester.getSelectedItem().toString());
    pst.setString(7, cbSection.getSelectedItem().toString());
    pst.setNull(8, java.sql.Types.VARCHAR); // NULL instead of "N/A"
} else {
    pst.setString(6, null); // NULL for teachers
    pst.setString(7, null); // NULL for teachers  
    String subs = subjectBoxes.stream()
            .filter(AbstractButton::isSelected)
            .map(AbstractButton::getText)
            .collect(Collectors.joining(", "));
    pst.setString(8, subs.isEmpty() ? null : subs);
}

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Account Created Successfully!");
            clearAllFields();
            parent.switchPage("LOGIN");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateDynamicFields(String role) {
        dynamicPanel.removeAll();
        subjectBoxes.clear();

        if (role.equals("Student")) {
            lblID.setText("Enrollment Number");
            JLabel sL = new JLabel("Semester"); sL.setBounds(0, 0, 145, 20); dynamicPanel.add(sL);
            cbSemester = new JComboBox<>(new String[]{"1","2","3","4","5","6","7","8"});
            cbSemester.setBounds(0, 20, 145, 30); dynamicPanel.add(cbSemester);

            JLabel xL = new JLabel("Section"); xL.setBounds(155, 0, 145, 20); dynamicPanel.add(xL);
            cbSection = new JComboBox<>(new String[]{"A","B","C","D"});
            cbSection.setBounds(155, 20, 145, 30); dynamicPanel.add(cbSection);
        } else {
            lblID.setText("Employee ID");
            JLabel subL = new JLabel("Assigned Subjects"); subL.setBounds(0, 0, 300, 20); dynamicPanel.add(subL);
            JPanel grid = new JPanel(new GridLayout(2, 2));
            grid.setBounds(0, 20, 300, 45); grid.setBackground(Color.WHITE);
            String[] subs = {"Math", "OOP", "DSA", "DBMS"};
            for (String s : subs) {
                JCheckBox chk = new JCheckBox(s); chk.setBackground(Color.WHITE);
                subjectBoxes.add(chk); grid.add(chk);
            }
            dynamicPanel.add(grid);
        }
        dynamicPanel.revalidate();
        dynamicPanel.repaint();
    }
    /* This method is called from within the constructor to initialize the form.
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