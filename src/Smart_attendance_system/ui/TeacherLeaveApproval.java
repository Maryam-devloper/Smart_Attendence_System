
package Smart_attendance_system.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.logging.*;
public class TeacherLeaveApproval extends javax.swing.JPanel {
 private JButton btnApprove, btnReject;
    private JLabel  jLabel1;
    private JScrollPane jScrollPane1;
    private JTable  table;
    private DefaultTableModel tableModel;
    private static final Logger logger = Logger.getLogger(TeacherLeaveApproval.class.getName());

    private String teacherName;
    private String teacherId;

    public TeacherLeaveApproval(String teacherName, String teacherId) {
        this.teacherName = teacherName;
        this.teacherId   = teacherId;
        buildUI();
        loadPendingLeaves();
    }

    public TeacherLeaveApproval(String teacherName) {
        this.teacherName = teacherName;
        this.teacherId   = null;
        buildUI();
        fetchTeacherId();
        loadPendingLeaves();
    }

    public TeacherLeaveApproval() {
        buildUI();
        loadPendingLeaves();
    }

    private void fetchTeacherId() {
        if (teacherName == null || teacherName.trim().isEmpty()) return;
        try (Connection con = Smart_attendance_system.logic.File_Manager.getConnection()) {
            PreparedStatement pst = con.prepareStatement(
                "SELECT id FROM users WHERE name = ? AND role = 'Teacher'");
            pst.setString(1, teacherName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) teacherId = rs.getString("id");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching teacher ID", e);
        }
    }

    private void buildUI() {
        table = new JTable();
        jScrollPane1 = new JScrollPane();
        btnApprove = new JButton();
        btnReject  = new JButton();
        jLabel1    = new JLabel();

        tableModel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Student ID", "Student Name", "Type", "Subject", "Details", "Status"}
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table.setModel(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setBackground(new Color(0, 51, 102));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        jScrollPane1.setViewportView(table);

        btnApprove.setBackground(new Color(0, 102, 51));
        btnApprove.setForeground(Color.WHITE);
        btnApprove.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnApprove.setText(" Approve");
        btnApprove.setFocusPainted(false);
        btnApprove.addActionListener(evt -> updateStatus("Approved"));

        btnReject.setBackground(new Color(153, 0, 0));
        btnReject.setForeground(Color.WHITE);
        btnReject.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnReject.setText(" Reject");
        btnReject.setFocusPainted(false);
        btnReject.addActionListener(evt -> updateStatus("Rejected"));

        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jLabel1.setText("Pending Leave Requests — " +
            (teacherName != null ? teacherName : "All"));

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(jLabel1);

        JPanel notePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        notePanel.setBackground(new Color(255, 255, 220));
        notePanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        JLabel noteLabel = new JLabel(
            "  ℹ Approving a leave updates that student's attendance to 'Leave' (counts as Present in percentage)  ");
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        notePanel.add(noteLabel);

        JPanel northBlock = new JPanel(new BorderLayout(5, 5));
        northBlock.setBackground(Color.WHITE);
        northBlock.add(topPanel,  BorderLayout.NORTH);
        northBlock.add(notePanel, BorderLayout.SOUTH);

        JPanel botPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botPanel.setBackground(Color.WHITE);
        botPanel.add(btnApprove);
        botPanel.add(btnReject);

        add(northBlock,    BorderLayout.NORTH);
        add(jScrollPane1,  BorderLayout.CENTER);
        add(botPanel,      BorderLayout.SOUTH);
    }

    private void loadPendingLeaves() {
        tableModel.setRowCount(0);
        if (teacherId == null) {
            jLabel1.setText("Error: Teacher ID not found. Re-login.");
            return;
        }
        try (Connection con = Smart_attendance_system.logic.File_Manager.getConnection()) {
            String sql =
                "SELECT lr.request_id, lr.student_id, lr.student_name, " +
                "       lr.leave_type, lr.subject, lr.body, lr.status " +
                "FROM leave_requests lr " +
                "WHERE lr.status = 'Pending' AND lr.teacher_id = ? " +
                "ORDER BY lr.apply_date DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, teacherId);
            ResultSet rs = pst.executeQuery();
            int count = 0;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("request_id"),
                    rs.getString("student_id"),
                    rs.getString("student_name"),
                    rs.getString("leave_type"),
                    rs.getString("subject"),
                    rs.getString("body"),
                    rs.getString("status")
                });
                count++;
            }
            jLabel1.setText(count == 0
                ? "No Pending Leave Requests"
                : "Pending Leave Requests (" + count + ")");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading leaves", e);
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    /**
     * KEY FIX:
     * When "Approved" — insert/update attendance row to "Leave" for that
     * student + subject + date of leave (use today if no date stored).
     * Leave is treated as Present in the attendance % calculation.
     */
    private void updateStatus(String newStatus) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a leave request!");
            return;
        }

        int    requestId   = (int)    tableModel.getValueAt(row, 0);
        String studentId   = (String) tableModel.getValueAt(row, 1);
        String studentName = (String) tableModel.getValueAt(row, 2);
        String subject     = (String) tableModel.getValueAt(row, 4);

        int confirm = JOptionPane.showConfirmDialog(this,
            newStatus + " leave for " + studentName + " (" + subject + ")?",
            "Confirm " + newStatus, JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = Smart_attendance_system.logic.File_Manager.getConnection()) {

            // 1) Update leave_requests status
            PreparedStatement upd = con.prepareStatement(
                "UPDATE leave_requests SET status = ? WHERE request_id = ?");
            upd.setString(1, newStatus);
            upd.setInt(2, requestId);
            upd.executeUpdate();

            // 2) If APPROVED → upsert attendance as "Leave"
            //    (Leave counts as Present in percentage)
            if (newStatus.equals("Approved")) {
                // Check if attendance row already exists for today
                PreparedStatement chk = con.prepareStatement(
                    "SELECT attendance_id FROM attendance " +
                    "WHERE student_id = ? AND subject = ? AND date = CURDATE()");
                chk.setString(1, studentId);
                chk.setString(2, subject);
                ResultSet cr = chk.executeQuery();

                if (cr.next()) {
                    // Update existing record
                    int attId = cr.getInt("attendance_id");
                    PreparedStatement upAtt = con.prepareStatement(
                        "UPDATE attendance SET status = 'Leave' WHERE attendance_id = ?");
                    upAtt.setInt(1, attId);
                    upAtt.executeUpdate();
                } else {
                    // Insert new Leave record
                    PreparedStatement insAtt = con.prepareStatement(
                        "INSERT INTO attendance " +
                        "(student_id, student_name, subject, status, date, marked_by) " +
                        "VALUES (?, ?, ?, 'Leave', CURDATE(), ?)");
                    insAtt.setString(1, studentId);
                    insAtt.setString(2, studentName);
                    insAtt.setString(3, subject);
                    insAtt.setString(4, teacherName != null ? teacherName : "Teacher");
                    insAtt.executeUpdate();
                }

                JOptionPane.showMessageDialog(this,
                    "Leave Approved!\nAttendance marked as 'Leave' (counts as Present).");
            } else {
                JOptionPane.showMessageDialog(this, "Leave Rejected.");
            }

            loadPendingLeaves();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating status", e);
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
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
