
package Student;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class LeavePanel extends JFrame {
 JTabbedPane tabs;
    DefaultTableModel model;
    JTable table;
    HashMap<String, String> subjectTeacherMap = new HashMap<>();
    JComboBox<String> subjectCombo;
    String currentStudentId;
    String currentStudentName;

    // FIXED: Constructor now takes student ID
    public LeavePanel(String studentId) {
        this.currentStudentId = studentId;
        fetchStudentName(); // Get student name from DB

        setTitle("Student Leave Portal");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 15));

        setupViewTab();
        setupApplyTab();

        add(tabs);
        loadLeavesFromDB(); // Load this student's leave history
        setVisible(true);
    }
    
    // NEW: Fetch student name from database
    private void fetchStudentName() {
        try (Connection con = Smart_attendance_system.logic.File_Manager.getConnection()) {
            String sql = "SELECT name FROM users WHERE id = ? AND role = 'Student'";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, currentStudentId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                currentStudentName = rs.getString("name");
                System.out.println("DEBUG: Student name = " + currentStudentName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            currentStudentName = "Unknown";
        }
    }

    private void setupViewTab() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        model = new DefaultTableModel(
            new String[]{"S.No", "Apply Date", "Type", "Subject", "Status"}, 0
        );
        table = new JTable(model);
        table.getTableHeader().setBackground(new Color(0, 51, 102));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(30);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tabs.addTab("My Leaves", tablePanel);
    }

    private void setupApplyTab() {
        JPanel form = new JPanel(null);
        form.setBackground(new Color(245, 245, 245));

        JLabel lblSub = new JLabel("Select Subject:");
        lblSub.setBounds(50, 50, 120, 30);
        subjectCombo = new JComboBox<>();
        subjectCombo.setBounds(180, 50, 250, 35);

        loadSubjectsFromTeachers();

        JLabel lblType = new JLabel("Leave Type:");
        lblType.setBounds(50, 110, 120, 30);
        String[] types = {"Sick Leave", "Family Issue", "Other"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        typeCombo.setBounds(180, 110, 250, 35);

        JLabel lblBody = new JLabel("Reason Details:");
        lblBody.setBounds(50, 170, 120, 30);
        JTextArea body = new JTextArea();
        body.setLineWrap(true);
        JScrollPane bodyScroll = new JScrollPane(body);
        bodyScroll.setBounds(180, 170, 450, 100);

        JButton submit = new JButton("Submit Request");
        submit.setBounds(180, 300, 200, 40);
        submit.setBackground(new Color(0, 51, 102));
        submit.setForeground(Color.WHITE);

        // FIXED: Submit action with proper data insertion
        submit.addActionListener(e -> {
            String selectedSub = (String) subjectCombo.getSelectedItem();
            if (selectedSub == null) {
                JOptionPane.showMessageDialog(this, "No subjects available!");
                return;
            }

            // Get teacher ID mapped to this subject
            String teacherId = subjectTeacherMap.get(selectedSub);
            if (teacherId == null) {
                JOptionPane.showMessageDialog(this, 
                    "No teacher found for this subject! Please contact administrator.");
                return;
            }
            
            String leaveType = (String) typeCombo.getSelectedItem();
            String leaveBody = body.getText().trim();
            
            if (leaveBody.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please provide reason details!");
                return;
            }

            try (Connection con = Smart_attendance_system.logic.File_Manager.getConnection()) {
                
                // FIXED: Insert all required fields including student_name
                String sql = "INSERT INTO leave_requests " +
                             "(student_id, student_name, teacher_id, " +
                             "leave_type, subject, body, status) " +
                             "VALUES (?, ?, ?, ?, ?, ?, 'Pending')";
                             
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, currentStudentId);
                pst.setString(2, currentStudentName);
                pst.setString(3, teacherId);
                pst.setString(4, leaveType);
                pst.setString(5, selectedSub);
                pst.setString(6, leaveBody);
                
                int rowsInserted = pst.executeUpdate();
                
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Leave request submitted successfully!");
                    body.setText("");
                    loadLeavesFromDB(); // Refresh the history tab
                    tabs.setSelectedIndex(0); // Switch to view tab
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to submit request!");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error submitting request: " + ex.getMessage());
            }
        });

        form.add(lblSub);
        form.add(subjectCombo);
        form.add(lblType);
        form.add(typeCombo);
        form.add(lblBody);
        form.add(bodyScroll);
        form.add(submit);
        tabs.addTab("Apply Leave", form);
    }

    // FIXED: Load subjects from teachers who have subjects assigned
    private void loadSubjectsFromTeachers() {
        try (Connection con = Smart_attendance_system.logic.File_Manager.getConnection()) {
        // Get this student's department first
        String studentDept = null;
        String deptSql = "SELECT department FROM users WHERE id = ? AND role = 'Student'";
        PreparedStatement dp = con.prepareStatement(deptSql);
        dp.setString(1, currentStudentId);
        ResultSet dr = dp.executeQuery();
        if (dr.next()) studentDept = dr.getString("department");

        // Only load teachers from the same department
        String sql = studentDept != null
            ? "SELECT id, subjects FROM users WHERE role = 'Teacher' " +
              "AND department = ? AND subjects IS NOT NULL AND subjects != ''"
            : "SELECT id, subjects FROM users WHERE role = 'Teacher' " +
              "AND subjects IS NOT NULL AND subjects != ''";

        PreparedStatement pst = con.prepareStatement(sql);
        if (studentDept != null) pst.setString(1, studentDept);
        ResultSet rs = pst.executeQuery();

        subjectCombo.removeAllItems();
        subjectTeacherMap.clear();

        while (rs.next()) {
            String teacherId  = rs.getString("id");
            String subsString = rs.getString("subjects");
            String[] subArray = subsString.split(",\\s*");
            for (String sub : subArray) {
                String trimmed = sub.trim();
                if (!trimmed.isEmpty()) {
                    subjectCombo.addItem(trimmed);
                    subjectTeacherMap.put(trimmed, teacherId);
                }
            }
        }

        if (subjectCombo.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No subjects found for your department.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error loading subjects: " + e.getMessage());
    }
    }

    // FIXED: Load only this student's leave history
    private void loadLeavesFromDB() {
        try (Connection con = Smart_attendance_system.logic.File_Manager.getConnection()) {
            String sql = "SELECT apply_date, leave_type, subject, status " +
                         "FROM leave_requests " +
                         "WHERE student_id = ? " +
                         "ORDER BY apply_date DESC";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, currentStudentId);
            
            ResultSet rs = pst.executeQuery();
            model.setRowCount(0);
            int count = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    count++,
                    rs.getTimestamp("apply_date"),
                    rs.getString("leave_type"),
                    rs.getString("subject"),
                    rs.getString("status")
                });
            }
            
            System.out.println("DEBUG: Loaded " + (count-1) + " leave requests for student " + currentStudentId);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading leave history: " + e.getMessage());
        }
    }


  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new LeavePanel("TEST-ID"));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
