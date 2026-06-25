/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Smart_attendance_system.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
public class Teacher extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Teacher.class.getName());

  
    private String teacherId;
    private String teacherName;
    private JPanel pn_utama;

    public Teacher(String teacherId, String teacherName) {
        this.teacherId   = teacherId;
        this.teacherName = teacherName;
        initUI();
    }

    public Teacher() {
        this(null, "Teacher");
    }

    private void initUI() {
        setTitle("Teacher Portal — " + teacherName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // ── TOP BAR ───────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(0, 51, 102));
        topBar.setPreferredSize(new Dimension(1100, 55));

        JLabel title = new JLabel("  Teacher Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        topBar.add(title, BorderLayout.WEST);

        JLabel lblUser = new JLabel("Logged in: " + teacherName + "  ");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setForeground(Color.WHITE);
        topBar.add(lblUser, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // ── SIDEBAR ───────────────────────────────────────────────
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(11, 1, 4, 4));
        sidebar.setBackground(new Color(0, 51, 102));
        sidebar.setPreferredSize(new Dimension(220, 665));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));

        JLabel portalLabel = new JLabel("Teacher Portal", SwingConstants.CENTER);
        portalLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        portalLabel.setForeground(new Color(204, 229, 255));
        sidebar.add(portalLabel);

        JButton btnDash       = sideBtn("Dashboard");
        JButton btnAttendance = sideBtn("Mark Attendance");
        JButton btnLeave      = sideBtn("Approve Leaves");
        JButton btnQuery      = sideBtn("Student Queries");
        JButton btnAnnounce   = sideBtn("Announcements");
        JButton btnAssign     = sideBtn("Assignments");
        JButton btnNotes      = sideBtn("Lecture Notes");
        JButton btnOutline    = sideBtn("Course Outline");
        JButton btnGuide      = sideBtn("Guidelines");
        JButton btnLogout     = sideBtn("Logout");
        btnLogout.setBackground(new Color(153, 0, 0));

        sidebar.add(btnDash);
        sidebar.add(btnAttendance);
        sidebar.add(btnLeave);
        sidebar.add(btnQuery);
        sidebar.add(btnAnnounce);
        sidebar.add(btnAssign);
        sidebar.add(btnNotes);
        sidebar.add(btnOutline);
        sidebar.add(btnGuide);
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // ── MAIN PANEL ────────────────────────────────────────────
        pn_utama = new JPanel(new BorderLayout());
        pn_utama.setBackground(Color.WHITE);
        pn_utama.setBorder(BorderFactory.createLineBorder(new Color(0, 51, 153)));
        add(pn_utama, BorderLayout.CENTER);

        // Default view: dashboard
        showPanel(new menuDashboard(teacherId, teacherName));

        // ── BUTTON ACTIONS ────────────────────────────────────────
        btnDash.addActionListener(e -> showPanel(new menuDashboard(teacherId, teacherName)));

        btnAttendance.addActionListener(e -> showPanel(new Attendence(teacherName, teacherId)));

        btnLeave.addActionListener(e -> {
            if (checkId()) showPanel(new TeacherLeaveApproval(teacherName, teacherId));
        });

        btnQuery.addActionListener(e -> {
            if (checkId()) {
                JFrame qf = new JFrame("Student Queries — " + teacherName);
                qf.setSize(950, 680);
                qf.setLocationRelativeTo(this);
                qf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                qf.setLayout(new BorderLayout());
                qf.add(new TeacherQueryReply(teacherId), BorderLayout.CENTER);
                qf.setVisible(true);
            }
        });

        // FIXED: now uses TeacherAnnouncement (functional panel, not static image)
        btnAnnounce.addActionListener(e -> {
            if (checkId()) showPanel(new TeacherAnnouncement(teacherId, teacherName));
        });

        btnAssign.addActionListener(e -> showPanel(new Assignments()));
        btnNotes.addActionListener(e -> showPanel(new LectureNotes()));
        btnOutline.addActionListener(e -> showPanel(new CourseOutline()));
        btnGuide.addActionListener(e -> showPanel(new Guidelines()));

        btnLogout.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                dispose();
                new Smart_attendance_system.ui.Login().setVisible(true);
            }
        });

        pack();
        setSize(1100, 720);
        setVisible(true);
    }

    private JButton sideBtn(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0, 102, 204));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 8));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(new Color(51, 153, 255)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(new Color(0, 102, 204)); }
        });
        return btn;
    }

    private void showPanel(JPanel panel) {
        pn_utama.removeAll();
        pn_utama.add(panel, BorderLayout.CENTER);
        pn_utama.revalidate();
        pn_utama.repaint();
    }

    private boolean checkId() {
        if (teacherId == null || teacherId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Teacher ID not found. Please re-login.");
            return false;
        }
        return true;}
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Teacher("EMP-001", "Demo Teacher").setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
