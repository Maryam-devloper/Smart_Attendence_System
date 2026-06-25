/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Smart_attendance_system.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import Smart_attendance_system.logic.File_Manager;

public class Attendence extends javax.swing.JPanel {
 private String teacherId;
    private String teacherName;
    private String teacherDept;
    private String[] teacherSubjects;

    private JComboBox<String> subjectCombo;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JButton btnSubmit, btnRefresh;
    private JLabel lblStatus;

    // Row data: studentId, studentName, dept, attendanceStatus
    private ArrayList<String[]> studentData = new ArrayList<>();

    public Attendence(String teacherName, String teacherId) {
        this.teacherName = teacherName;
        this.teacherId   = teacherId;
        buildUI();
        loadTeacherInfo();
    }

    public Attendence(String teacherName) {
        this(teacherName, null);
    }

    public Attendence() {
        this(null, null);
    }

    // ─────────────────────────────────────────────────────────────
    //  UI BUILD
    // ─────────────────────────────────────────────────────────────
    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ── TOP BAR ──────────────────────────────────────────────
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        topBar.setBackground(new Color(0, 51, 102));

        JLabel title = new JLabel("Attendance Marking");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        topBar.add(title);

        JLabel lblSubj = new JLabel("  Subject:");
        lblSubj.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSubj.setForeground(Color.WHITE);
        topBar.add(lblSubj);

        subjectCombo = new JComboBox<>();
        subjectCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subjectCombo.setPreferredSize(new Dimension(160, 30));
        subjectCombo.addActionListener(e -> loadStudents());
        topBar.add(subjectCombo);

        btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(51, 153, 255));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> loadStudents());
        topBar.add(btnRefresh);

        add(topBar, BorderLayout.NORTH);

        // ── TABLE ─────────────────────────────────────────────────
        String[] cols = {"#", "Enrollment No.", "Student Name", "Department", "Attendance"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 4; }
            @Override public Class<?> getColumnClass(int c) {
                return c == 4 ? JComboBox.class : String.class;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(32);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        studentTable.getTableHeader().setBackground(new Color(0, 51, 102));
        studentTable.getTableHeader().setForeground(Color.WHITE);

        // Custom combo editor for Attendance column
        String[] statuses = {"/", "Present", "Absent", "Leave"};
        JComboBox<String> statusEditor = new JComboBox<>(statuses);
        studentTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statusEditor));

        // Custom renderer to show combo-like display in attendance column
        studentTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int r, int c) {
                JComboBox<String> cb = new JComboBox<>(statuses);
                if (val != null) cb.setSelectedItem(val.toString());
                cb.setBackground(sel ? t.getSelectionBackground() : Color.WHITE);
                return cb;
            }
        });

        // Color-code rows: green=Present, red=Absent, yellow=Leave
        studentTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                if (!sel) {
                    String status = "";
                    if (t.getValueAt(r, 4) != null) status = t.getValueAt(r, 4).toString();
                    if (status.equals("Present"))      comp.setBackground(new Color(204, 255, 204));
                    else if (status.equals("Absent"))  comp.setBackground(new Color(255, 204, 204));
                    else if (status.equals("Leave"))   comp.setBackground(new Color(255, 255, 204));
                    else                               comp.setBackground(Color.WHITE);
                }
                return comp;
            }
        });

        studentTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(140);
        studentTable.getColumnModel().getColumn(4).setPreferredWidth(120);

        JScrollPane scroll = new JScrollPane(studentTable);
        add(scroll, BorderLayout.CENTER);

        // ── BOTTOM BAR ────────────────────────────────────────────
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);

        lblStatus = new JLabel("  Select a subject and mark attendance.");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setForeground(Color.GRAY);
        bottom.add(lblStatus, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);

        JButton btnMarkAll = new JButton("Mark All Present");
        btnMarkAll.setBackground(new Color(0, 153, 76));
        btnMarkAll.setForeground(Color.WHITE);
        btnMarkAll.setFocusPainted(false);
        btnMarkAll.addActionListener(e -> markAll("Present"));
        btnPanel.add(btnMarkAll);

        JButton btnMarkAbsent = new JButton("Mark All Absent");
        btnMarkAbsent.setBackground(new Color(204, 0, 0));
        btnMarkAbsent.setForeground(Color.WHITE);
        btnMarkAbsent.setFocusPainted(false);
        btnMarkAbsent.addActionListener(e -> markAll("Absent"));
        btnPanel.add(btnMarkAbsent);

        btnSubmit = new JButton("  SUBMIT ATTENDANCE  ");
        btnSubmit.setBackground(new Color(0, 51, 102));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.setFocusPainted(false);
        btnSubmit.addActionListener(e -> submitAttendance());
        btnPanel.add(btnSubmit);

        bottom.add(btnPanel, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
    }

    // ─────────────────────────────────────────────────────────────
    //  LOAD TEACHER INFO & POPULATE SUBJECT COMBO
    // ─────────────────────────────────────────────────────────────
    private void loadTeacherInfo() {
        if (teacherName == null && teacherId == null) {
            lblStatus.setText("  No teacher logged in.");
            btnSubmit.setEnabled(false);
            return;
        }
        try (Connection con = File_Manager.getConnection()) {
            String sql = teacherId != null
                ? "SELECT name, department, subjects FROM users WHERE id = ? AND role = 'Teacher'"
                : "SELECT id, name, department, subjects FROM users WHERE name = ? AND role = 'Teacher'";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, teacherId != null ? teacherId : teacherName);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                if (teacherId == null) teacherId = rs.getString("id");
                if (teacherName == null) teacherName = rs.getString("name");
                teacherDept = rs.getString("department");
                String subsStr = rs.getString("subjects");

                if (subsStr == null || subsStr.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "No subjects assigned. Contact admin.");
                    btnSubmit.setEnabled(false);
                    return;
                }
                teacherSubjects = subsStr.split(",\\s*");
                subjectCombo.removeAllItems();
                for (String s : teacherSubjects) subjectCombo.addItem(s.trim());
                loadStudents();
            } else {
                lblStatus.setText("  Teacher not found in database.");
                btnSubmit.setEnabled(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB error: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  LOAD STUDENTS FOR SELECTED SUBJECT + TEACHER'S DEPT
    // ─────────────────────────────────────────────────────────────
    private void loadStudents() {
        tableModel.setRowCount(0);
        studentData.clear();

        String subject = (String) subjectCombo.getSelectedItem();
        if (subject == null || teacherDept == null) return;

        try (Connection con = File_Manager.getConnection()) {
            // Students in same department
            String sql = "SELECT id, name, department FROM users " +
                         "WHERE role = 'Student' AND department = ? ORDER BY name";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, teacherDept);
            ResultSet rs = pst.executeQuery();

            int row = 1;
            while (rs.next()) {
                String sid  = rs.getString("id");
                String snam = rs.getString("name");
                String dept = rs.getString("department");
                tableModel.addRow(new Object[]{row++, sid, snam, dept, "/"});
                studentData.add(new String[]{sid, snam, dept});
            }
            lblStatus.setText("  " + (row - 1) + " student(s) loaded for " + subject
                    + " [" + teacherDept + "]");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage());
        }
    }

    private void markAll(String status) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(status, i, 4);
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  SUBMIT ATTENDANCE
    //  NOTE: "Leave" status is stored as "Leave" in DB.
    //  In AttendancePanel (student view), Leave is treated same as
    //  Present when calculating percentage — see the SQL query there.
    // ─────────────────────────────────────────────────────────────
    private void submitAttendance() {
        // Stop any active editing
        if (studentTable.isEditing()) {
            studentTable.getCellEditor().stopCellEditing();
        }

        String subject = (String) subjectCombo.getSelectedItem();
        if (subject == null) { JOptionPane.showMessageDialog(this, "Select a subject first!"); return; }
        if (tableModel.getRowCount() == 0) { JOptionPane.showMessageDialog(this, "No students loaded!"); return; }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Submit attendance for " + subject + " (" + tableModel.getRowCount() + " students)?",
            "Confirm Submit", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = File_Manager.getConnection()) {
            // Check if today's attendance already submitted for this teacher+subject
            String checkSql = "SELECT COUNT(*) FROM attendance WHERE subject = ? AND marked_by = ? AND date = CURDATE()";
            PreparedStatement chk = con.prepareStatement(checkSql);
            chk.setString(1, subject);
            chk.setString(2, teacherName);
            ResultSet cr = chk.executeQuery();
            if (cr.next() && cr.getInt(1) > 0) {
                int overwrite = JOptionPane.showConfirmDialog(this,
                    "Attendance for " + subject + " already marked today. Overwrite?",
                    "Duplicate", JOptionPane.YES_NO_OPTION);
                if (overwrite != JOptionPane.YES_OPTION) return;
                // Delete existing records
                PreparedStatement del = con.prepareStatement(
                    "DELETE FROM attendance WHERE subject = ? AND marked_by = ? AND date = CURDATE()");
                del.setString(1, subject);
                del.setString(2, teacherName);
                del.executeUpdate();
            }

            String sql = "INSERT INTO attendance (student_id, student_name, department, subject, status, date, marked_by) " +
                         "VALUES (?, ?, ?, ?, ?, CURDATE(), ?)";
            PreparedStatement pst = con.prepareStatement(sql);

            int saved = 0;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String sid    = tableModel.getValueAt(i, 1).toString();
                String sname  = tableModel.getValueAt(i, 2).toString();
                String dept   = tableModel.getValueAt(i, 3).toString();
                String status = tableModel.getValueAt(i, 4).toString();

                if (sid.isEmpty()) continue;

                pst.setString(1, sid);
                pst.setString(2, sname);
                pst.setString(3, dept);
                pst.setString(4, subject);
                pst.setString(5, status);
                pst.setString(6, teacherName != null ? teacherName : "Unknown");
                pst.addBatch();
                saved++;
            }
            pst.executeBatch();

            JOptionPane.showMessageDialog(this,
                "Attendance saved for " + saved + " students!\n\n" +
                "Note: Leave requests that are approved count as Present.");
            lblStatus.setText("  Attendance submitted for " + subject + " — " + saved + " records saved.");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
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
