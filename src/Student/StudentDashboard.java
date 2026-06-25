
package Student;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import Smart_attendance_system.logic.File_Manager;


public class StudentDashboard extends javax.swing.JFrame {
    
     String studentId;
    String studentName;
    JFrame currentFrame;

    private JLabel cardAttValue, cardCoursesValue, cardLeaveValue, cardStatusValue;

    public StudentDashboard(String studentId, String studentName) {
        this.studentId   = studentId;
        this.studentName = studentName;

        setTitle("Student Dashboard — " + studentName);
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        buildUI();
        loadStats();
        setVisible(true);
    }

    public StudentDashboard() { this("GUEST", "Demo Student"); }

    private void buildUI() {
        // ── SIDEBAR ───────────────────────────────────────────────
        JPanel sidebar = new JPanel(new GridLayout(8, 1, 4, 4));
        sidebar.setBackground(new Color(0, 51, 102));
        sidebar.setPreferredSize(new Dimension(220, 700));
        sidebar.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel portalTitle = new JLabel("CMS Portal", SwingConstants.CENTER);
        portalTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        portalTitle.setForeground(new Color(204, 229, 255));
        sidebar.add(portalTitle);

        JButton btnAttendance  = menuBtn("  Attendance");
        JButton btnAnnouncement= menuBtn("  Announcements");
        JButton btnCourses     = menuBtn("  Courses");
        JButton btnLeave       = menuBtn("  Apply Leave");
        JButton btnQuery       = menuBtn("  Ask Query");
        JButton btnLogout      = menuBtn("  Logout");
        btnLogout.setBackground(new Color(153, 0, 0));

        sidebar.add(btnAttendance);
        sidebar.add(btnAnnouncement);
        sidebar.add(btnCourses);
        sidebar.add(btnLeave);
        sidebar.add(btnQuery);
        sidebar.add(btnLogout);
        add(sidebar, BorderLayout.WEST);

        // ── TOP BAR ───────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(51, 153, 255));
        topBar.setPreferredSize(new Dimension(980, 55));

        JLabel welcome = new JLabel("  Welcome, " + studentName);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcome.setForeground(Color.WHITE);
        topBar.add(welcome, BorderLayout.WEST);

        JLabel dateLabel = new JLabel(new java.util.Date().toString() + "  ");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(Color.WHITE);
        topBar.add(dateLabel, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // ── MAIN CONTENT ──────────────────────────────────────────
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(240, 248, 255));

        JLabel heading = new JLabel("Student Dashboard");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(0, 51, 102));
        heading.setBounds(30, 20, 400, 40);
        mainPanel.add(heading);

        JLabel subheading = new JLabel("ID: " + studentId);
        subheading.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subheading.setForeground(Color.GRAY);
        subheading.setBounds(30, 55, 300, 20);
        mainPanel.add(subheading);

        // ── STATS CARDS ────────────────────────────────────────────
        JPanel card1 = buildCard("Overall Attendance", "Loading...", new Color(0, 102, 204));
        JPanel card2 = buildCard("Courses Enrolled",   "Loading...", new Color(0, 153, 76));
        JPanel card3 = buildCard("Pending Leaves",     "Loading...", new Color(204, 102, 0));
        JPanel card4 = buildCard("Attendance Status",  "Loading...", new Color(102, 0, 153));

        card1.setBounds(30,  90,  210, 120);
        card2.setBounds(260, 90,  210, 120);
        card3.setBounds(490, 90,  210, 120);
        card4.setBounds(720, 90,  210, 120);

        mainPanel.add(card1);
        mainPanel.add(card2);
        mainPanel.add(card3);
        mainPanel.add(card4);

        // Store value labels for updating
        cardAttValue     = findValueLabel(card1);
        cardCoursesValue = findValueLabel(card2);
        cardLeaveValue   = findValueLabel(card3);
        cardStatusValue  = findValueLabel(card4);

        // ── INFO BOX ────────────────────────────────────────────────
        JPanel infoBox = new JPanel(new BorderLayout());
        infoBox.setBackground(new Color(255, 255, 220));
        infoBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 200, 0), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        infoBox.setBounds(30, 240, 900, 60);

        JLabel infoText = new JLabel(
            "<html><b>ℹ Note:</b> Leave approved by teacher counts as Present in attendance percentage. " +
            "Attendance below 75% in any subject will trigger a warning.</html>");
        infoText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infoBox.add(infoText, BorderLayout.CENTER);
        mainPanel.add(infoBox);

        add(mainPanel, BorderLayout.CENTER);

        // ── BUTTON ACTIONS ─────────────────────────────────────────
        btnAttendance.addActionListener(e -> open(new AttendancePanel(studentId)));
        btnCourses.addActionListener(e -> open(new CoursesPanel()));
        btnLeave.addActionListener(e -> open(new LeavePanel(studentId)));
        btnQuery.addActionListener(e -> open(new QueryPanel(studentId)));
        btnAnnouncement.addActionListener(e -> open(new AnnouncementPanel()));
        btnLogout.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                dispose();
                new Smart_attendance_system.ui.Login().setVisible(true);
            }
        });
    }

    private JButton menuBtn(String text) {
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

    private JPanel buildCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(color);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblVal = new JLabel(value, SwingConstants.CENTER);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblVal.setForeground(Color.WHITE);
        lblVal.setName("VALUE_LABEL");

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblVal,   BorderLayout.CENTER);
        return card;
    }

    private JLabel findValueLabel(JPanel card) {
        for (Component c : card.getComponents()) {
            if (c instanceof JLabel && "VALUE_LABEL".equals(c.getName())) return (JLabel) c;
        }
        return new JLabel();
    }

    private void loadStats() {
        SwingWorker<Void, Void> w = new SwingWorker<Void, Void>() {
            String att = "0%", courses = "0", pendingLeaves = "0", statusText = "N/A";

            @Override
            protected Void doInBackground() {
                try (Connection con = File_Manager.getConnection()) {
                    // Attendance % — Leave counts as Present
                    PreparedStatement p1 = con.prepareStatement(
                        "SELECT " +
                        "  SUM(CASE WHEN status IN ('Present','Leave') THEN 1 ELSE 0 END) AS eff, " +
                        "  SUM(CASE WHEN status IN ('Present','Absent','Leave') THEN 1 ELSE 0 END) AS tot " +
                        "FROM attendance WHERE student_id = ?");
                    p1.setString(1, studentId);
                    ResultSet r1 = p1.executeQuery();
                    if (r1.next()) {
                        int eff = r1.getInt("eff");
                        int tot = r1.getInt("tot");
                        if (tot > 0) {
                            int pct = (eff * 100) / tot;
                            att = pct + "%";
                            statusText = pct >= 75 ? "✓ Eligible" : "⚠ At Risk";
                        }
                    }

                    // Distinct subjects
                    PreparedStatement p2 = con.prepareStatement(
                        "SELECT COUNT(DISTINCT subject) FROM attendance WHERE student_id = ?");
                    p2.setString(1, studentId);
                    ResultSet r2 = p2.executeQuery();
                    if (r2.next()) courses = String.valueOf(r2.getInt(1));

                    // Pending leaves
                    PreparedStatement p3 = con.prepareStatement(
                        "SELECT COUNT(*) FROM leave_requests WHERE student_id = ? AND status = 'Pending'");
                    p3.setString(1, studentId);
                    ResultSet r3 = p3.executeQuery();
                    if (r3.next()) pendingLeaves = String.valueOf(r3.getInt(1));

                } catch (Exception e) { e.printStackTrace(); }
                return null;
            }

            @Override
            protected void done() {
                cardAttValue.setText(att);
                cardCoursesValue.setText(courses);
                cardLeaveValue.setText(pendingLeaves);
                cardStatusValue.setText(statusText);
            }
        };
        w.execute();
    }

    private void open(JFrame frame) {
        if (currentFrame != null) currentFrame.dispose();
        currentFrame = frame;
        currentFrame.setLocationRelativeTo(this);
        currentFrame.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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

public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDashboard("TEST-001", "Test Student"));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
