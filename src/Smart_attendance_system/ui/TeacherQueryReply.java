/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Smart_attendance_system.ui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import Smart_attendance_system.logic.File_Manager;
import javax.swing.border.TitledBorder;


/**
 *
 * @author pc
 */
public class TeacherQueryReply extends javax.swing.JPanel {

  

    private JTable queryTable;
    private DefaultTableModel tableModel;
    private String teacherId;
    private String teacherName;
    private JTextArea replyArea;
    private JButton btnSendReply;
    private JLabel headerLabel;

    public TeacherQueryReply(String teacherId) {
        this.teacherId = teacherId;
        fetchTeacherName();
        setupUI();
        loadPendingQueries();
    }

    private void fetchTeacherName() {
        try (Connection con = File_Manager.getConnection()) {
            PreparedStatement pst = con.prepareStatement(
                "SELECT name FROM users WHERE id = ? AND role = 'Teacher'");
            pst.setString(1, teacherId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) teacherName = rs.getString("name");
            else teacherName = teacherId;
        } catch (Exception e) { e.printStackTrace(); teacherName = teacherId; }
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        headerLabel = new JLabel("Student Queries & Doubts");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(new Color(0, 51, 102));

        JLabel teacherLabel = new JLabel("Logged in as: " + teacherName);
        teacherLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        teacherLabel.setForeground(Color.GRAY);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadPendingQueries());
        btnRefresh.setBackground(new Color(51, 153, 255));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerRight.setBackground(Color.WHITE);
        headerRight.add(teacherLabel);
        headerRight.add(btnRefresh);

        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(headerRight, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Student ID", "Student Name", "Subject", "Question"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        queryTable = new JTable(tableModel);
        queryTable.setRowHeight(35);
        queryTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        queryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        queryTable.getTableHeader().setBackground(new Color(0, 51, 102));
        queryTable.getTableHeader().setForeground(Color.WHITE);
        queryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(queryTable), BorderLayout.CENTER);

        // Reply area
        replyArea = new JTextArea(4, 20);
        replyArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        replyArea.setLineWrap(true);
        replyArea.setWrapStyleWord(true);

        queryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) replyArea.setText("");
        });

        btnSendReply = new JButton("Send Reply");
        btnSendReply.setBackground(new Color(0, 102, 51));
        btnSendReply.setForeground(Color.WHITE);
        btnSendReply.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSendReply.setFocusPainted(false);
        btnSendReply.setPreferredSize(new Dimension(140, 40));
        btnSendReply.addActionListener(e -> submitReply());

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new TitledBorder(
            BorderFactory.createLineBorder(new Color(0, 51, 102)),
            "Reply to Selected Query", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnSendReply);

        bottomPanel.add(new JScrollPane(replyArea), BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadPendingQueries() {
        tableModel.setRowCount(0);
        if (teacherId == null || teacherId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Teacher ID not found.");
            return;
        }
        try (Connection con = File_Manager.getConnection()) {
            String sql =
                "SELECT q.query_id, q.student_id, q.student_name, q.subject, q.question " +
                "FROM student_queries q " +
                "WHERE q.teacher_id = ? AND q.status = 'Pending' " +
                "ORDER BY q.ask_date DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, teacherId);
            ResultSet rs = pst.executeQuery();
            int count = 0;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("query_id"),
                    rs.getString("student_id"),
                    rs.getString("student_name") != null ? rs.getString("student_name") : "Unknown",
                    rs.getString("subject")      != null ? rs.getString("subject")      : "General",
                    rs.getString("question")
                });
                count++;
            }
            headerLabel.setText(count == 0
                ? "No Pending Queries"
                : "Pending Queries (" + count + ")");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    private void submitReply() {
        int selectedRow = queryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a query first!");
            return;
        }
        String replyText = replyArea.getText().trim();
        if (replyText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Reply cannot be empty!");
            return;
        }
        int queryId     = (int)    tableModel.getValueAt(selectedRow, 0);
        String studentN = (String) tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Send reply to " + studentN + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = File_Manager.getConnection()) {
            PreparedStatement pst = con.prepareStatement(
                "UPDATE student_queries SET answer = ?, status = 'Replied' WHERE query_id = ?");
            pst.setString(1, replyText);
            pst.setInt(2, queryId);
            if (pst.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Reply sent to " + studentN + "!");
                replyArea.setText("");
                loadPendingQueries();
            }
        } catch (Exception e) {
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
