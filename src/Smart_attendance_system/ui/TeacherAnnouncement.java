/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Smart_attendance_system.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import Smart_attendance_system.logic.File_Manager;
public class TeacherAnnouncement extends javax.swing.JPanel {
private String teacherId;
    private String teacherName;
    private JComboBox<String> subjectCombo;
    private JTextField titleField;
    private JTextArea bodyArea;
    private JTable historyTable;
    private DefaultTableModel historyModel;

    public TeacherAnnouncement(String teacherId, String teacherName) {
        this.teacherId   = teacherId;
        this.teacherName = teacherName;
        buildUI();
        loadSubjects();
        loadHistory();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ── HEADER ────────────────────────────────────────────────
        JLabel header = new JLabel("Post Announcement");
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(new Color(0, 51, 102));
        add(header, BorderLayout.NORTH);

        // ── FORM + HISTORY ─────────────────────────────────────────
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setDividerLocation(260);
        split.setBackground(Color.WHITE);

        // FORM
        JPanel form = new JPanel(null);
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 51, 102)),
            "New Announcement", 0, 0,
            new Font("Segoe UI", Font.BOLD, 13)));

        JLabel lblSub = new JLabel("Subject:");
        lblSub.setBounds(20, 30, 100, 25);
        form.add(lblSub);

        subjectCombo = new JComboBox<>();
        subjectCombo.setBounds(130, 30, 200, 28);
        form.add(subjectCombo);

        JLabel lblTitle = new JLabel("Title:");
        lblTitle.setBounds(20, 70, 100, 25);
        form.add(lblTitle);

        titleField = new JTextField();
        titleField.setBounds(130, 70, 450, 28);
        form.add(titleField);

        JLabel lblBody = new JLabel("Message:");
        lblBody.setBounds(20, 110, 100, 25);
        form.add(lblBody);

        bodyArea = new JTextArea();
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        JScrollPane bodyScroll = new JScrollPane(bodyArea);
        bodyScroll.setBounds(130, 110, 450, 90);
        form.add(bodyScroll);

        JButton btnPost = new JButton("Post Announcement");
        btnPost.setBounds(130, 215, 200, 35);
        btnPost.setBackground(new Color(0, 51, 102));
        btnPost.setForeground(Color.WHITE);
        btnPost.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnPost.setFocusPainted(false);
        btnPost.addActionListener(e -> postAnnouncement());
        form.add(btnPost);

        split.setTopComponent(form);

        // HISTORY TABLE
        JPanel histPanel = new JPanel(new BorderLayout());
        histPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 51, 102)),
            "Posted Announcements", 0, 0,
            new Font("Segoe UI", Font.BOLD, 13)));

        historyModel = new DefaultTableModel(
            new String[]{"ID", "Subject", "Title", "Date"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        historyTable = new JTable(historyModel);
        historyTable.setRowHeight(28);
        historyTable.getTableHeader().setBackground(new Color(0, 51, 102));
        historyTable.getTableHeader().setForeground(Color.WHITE);
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.setBackground(new Color(153, 0, 0));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteAnnouncement());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.setBackground(Color.WHITE);
        btnRow.add(btnDelete);

        histPanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        histPanel.add(btnRow, BorderLayout.SOUTH);
        split.setBottomComponent(histPanel);

        add(split, BorderLayout.CENTER);
    }

    private void loadSubjects() {
        try (Connection con = File_Manager.getConnection()) {
            String sql = "SELECT subjects FROM users WHERE id = ? AND role = 'Teacher'";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, teacherId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String subs = rs.getString("subjects");
                if (subs != null && !subs.isEmpty()) {
                    subjectCombo.removeAllItems();
                    for (String s : subs.split(",\\s*")) subjectCombo.addItem(s.trim());
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void postAnnouncement() {
        String subject = (String) subjectCombo.getSelectedItem();
        String title   = titleField.getText().trim();
        String body    = bodyArea.getText().trim();

        if (title.isEmpty() || body.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and message cannot be empty!");
            return;
        }

        try (Connection con = File_Manager.getConnection()) {
            String sql = "INSERT INTO announcements (teacher_id, teacher_name, subject, title, body) VALUES (?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, teacherId);
            pst.setString(2, teacherName);
            pst.setString(3, subject != null ? subject : "General");
            pst.setString(4, title);
            pst.setString(5, body);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Announcement posted successfully!");
            titleField.setText("");
            bodyArea.setText("");
            loadHistory();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void loadHistory() {
        historyModel.setRowCount(0);
        try (Connection con = File_Manager.getConnection()) {
            String sql = "SELECT announcement_id, subject, title, post_date " +
                         "FROM announcements WHERE teacher_id = ? ORDER BY post_date DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, teacherId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                historyModel.addRow(new Object[]{
                    rs.getInt("announcement_id"),
                    rs.getString("subject"),
                    rs.getString("title"),
                    rs.getTimestamp("post_date")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void deleteAnnouncement() {
        int row = historyTable.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a row to delete."); return; }
        int id = (int) historyModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this announcement?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try (Connection con = File_Manager.getConnection()) {
            PreparedStatement pst = con.prepareStatement("DELETE FROM announcements WHERE announcement_id = ?");
            pst.setInt(1, id);
            pst.executeUpdate();
            loadHistory();
        } catch (Exception e) { e.printStackTrace(); }
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
