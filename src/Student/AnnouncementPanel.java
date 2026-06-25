
package Student;

import javax.swing.*;
import java.awt.*;
import java.sql.*;


public class AnnouncementPanel extends javax.swing.JFrame {
 public AnnouncementPanel() {
        setTitle("Announcements");
        setSize(900, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(230, 240, 250));
        mainPanel.setBounds(0, 0, 900, 620);

        // TOP BAR
        JPanel topBar = new JPanel(null);
        topBar.setBackground(new Color(0, 51, 102));
        topBar.setBounds(0, 0, 900, 70);

        JLabel title = new JLabel("  Student Announcements");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBounds(20, 15, 500, 40);
        topBar.add(title);

        // ANNOUNCEMENT CONTAINER
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(230, 240, 250));
        loadAnnouncements(container);

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBounds(30, 90, 830, 460);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JButton closeBtn = new JButton("Close");
        closeBtn.setBounds(760, 565, 110, 35);
        closeBtn.setBackground(new Color(0, 51, 102));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());

        mainPanel.add(topBar);
        mainPanel.add(scrollPane);
        mainPanel.add(closeBtn);
        add(mainPanel);
        setVisible(true);
    }

    private void loadAnnouncements(JPanel container) {
        container.removeAll();
        try {
            Connection con = Smart_attendance_system.logic.File_Manager.getConnection();
            String sql = "SELECT title, body, post_date, teacher_name FROM announcements ORDER BY post_date DESC";
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                String title   = rs.getString("title");
                String body    = rs.getString("body");
                String date    = rs.getString("post_date");
                String teacher = rs.getString("teacher_name");
                container.add(createCard(title, body, date, teacher));
                container.add(Box.createRigidArea(new Dimension(0, 10)));
            }
            if (!found) {
                JLabel empty = new JLabel("  No announcements yet.");
                empty.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                empty.setForeground(Color.GRAY);
                container.add(empty);
            }
            rs.close(); pst.close(); con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JLabel err = new JLabel("  Error loading announcements: " + e.getMessage());
            err.setForeground(Color.RED);
            container.add(err);
        }
        container.revalidate();
        container.repaint();
    }

    private JPanel createCard(String heading, String details, String date, String teacher) {
        JPanel box = new JPanel(null);
        box.setBackground(Color.WHITE);
        box.setMaximumSize(new Dimension(820, 130));
        box.setPreferredSize(new Dimension(820, 130));
        box.setBorder(BorderFactory.createLineBorder(new Color(0, 51, 102), 2));

        JLabel lblHeading = new JLabel(heading != null ? heading : "Untitled");
        lblHeading.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeading.setForeground(new Color(0, 51, 102));
        lblHeading.setBounds(15, 8, 600, 24);

        JLabel lblTeacher = new JLabel("By: " + (teacher != null ? teacher : "Unknown"));
        lblTeacher.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTeacher.setForeground(new Color(100, 100, 100));
        lblTeacher.setBounds(15, 32, 300, 18);

        JLabel lblDate = new JLabel("Posted: " + (date != null ? date : "—"));
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDate.setForeground(Color.GRAY);
        lblDate.setBounds(320, 32, 350, 18);

        JTextArea txtDetails = new JTextArea(details != null ? details : "");
        txtDetails.setBounds(15, 52, 790, 68);
        txtDetails.setEditable(false);
        txtDetails.setLineWrap(true);
        txtDetails.setWrapStyleWord(true);
        txtDetails.setBackground(Color.WHITE);
        txtDetails.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        box.add(lblHeading);
        box.add(lblTeacher);
        box.add(lblDate);
        box.add(txtDetails);
        return box;
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

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
